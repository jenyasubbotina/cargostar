package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchInvoiceListWorker extends Worker {
    private final int perPage;
    private String login;
    private String password;

    public FetchInvoiceListWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(SharedPrefs.PASSWORD_HASH);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<List<Invoice>> response = RetrofitClient.getInstance(getApplicationContext()).getInvoiceList(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllInvoices(): response=" + response.body());
                    final List<Invoice> invoiceList = response.body();

                    if (invoiceList == null) {
                        Log.e(TAG, "fetchAllInvoices(): invoiceList is null");
                        return Result.failure();
                    }

                    for (final Invoice invoice : invoiceList) {
                        LocalCache.getInstance(getApplicationContext()).invoiceDao().insertInvoice(invoice);
                    }

                    return ListenableWorker.Result.success(new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password).build());
                }
            }
            else {
                Log.e(TAG, "doWork(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchInvoiceListWorker.class.toString();
}
