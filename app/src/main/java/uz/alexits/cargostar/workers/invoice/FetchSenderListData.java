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
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchSenderListData extends Worker {
    private final int perPage;
    private String login;
    private String password;
    private final String token;

    public FetchSenderListData(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
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
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<List<Customer>> response = RetrofitClient.getInstance(getApplicationContext()).getAllCustomers(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllCustomers(): response=" + response.body());
                    final List<Customer> senderList = response.body();

                    if (senderList == null) {
                        Log.e(TAG, "fetchAllCustomers(): sender is NULL");
                        return Result.failure();
                    }

                    final long[] rowsInserted = LocalCache.getInstance(getApplicationContext()).actorDao().insertSenderList(senderList);

                    if (rowsInserted.length <= 0) {
                        Log.e(TAG, "fetchAllCustomers(): couldn't insert entries");
                        return Result.failure();
                    }
                    Log.i(TAG, "fetchAllCustomers(): successfully inserted entries");
                    return Result.success(new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .build());
                }
            }
            else {
                Log.e(TAG, "fetchAllCustomers(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchAllCustomers(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchSenderListData.class.toString();
}
