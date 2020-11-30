package uz.alexits.cargostar.workers.actor;

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
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchAddressBookWorker extends Worker {
    private final int perPage;
    private String login;
    private String password;
    private final String token;

    public FetchAddressBookWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(SharedPrefs.PASSWORD_HASH);
        this.token = getInputData().getString(Constants.KEY_TOKEN);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            Log.i(TAG, "per-page: " + perPage);
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<List<AddressBook>> response = RetrofitClient.getInstance(getApplicationContext()).getAddressBook(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAddressBook(): response=" + response.body());
                    final List<AddressBook> addressBook = response.body();

                    if (addressBook == null) {
                        Log.e(TAG, "fetchAddressBook(): sender is NULL");
                        return Result.failure();
                    }

                    final long[] rowsInserted = LocalCache.getInstance(getApplicationContext()).invoiceDao().insertAddressBookEntries(addressBook);

                    if (rowsInserted.length <= 0) {
                        Log.e(TAG, "fetchAddressBook(): couldn't insert entries");
                        return Result.failure();
                    }
                    Log.i(TAG, "fetchAddressBook(): successfully inserted entries");
                    return Result.success(new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .build());
                }
            }
            else {
                Log.e(TAG, "fetchAddressBook(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchAddressBook(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchAddressBookWorker.class.toString();
}
