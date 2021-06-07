package uz.alexits.cargostar.workers.login;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.entities.params.SignInParams;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.actor.Courier;
import uz.alexits.cargostar.utils.Constants;

public class SignInWorker extends Worker {
    private final String login;
    private final String password;
    private final SignInParams params;

    public SignInWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.login = getInputData().getString(Constants.KEY_LOGIN);
        this.password = getInputData().getString(Constants.KEY_PASSWORD);
        this.params = new SignInParams(getInputData().getString(Constants.KEY_TOKEN));
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<Courier> response = RetrofitClient.getInstance(getApplicationContext()).signIn(params);

            if (response.code() == 200 && response.isSuccessful()) {
                final Courier courier = response.body();

                if (courier == null) {
                    Log.e(TAG, "signIn(): courier is NULL");
                    return Result.failure();
                }
                courier.setPassword(password);

                Log.i(TAG, "signIn(): " + courier);

                final long rowInserted = LocalCache.getInstance(getApplicationContext()).courierDao().createCourier(courier);

                if (rowInserted == -1) {
                    Log.e(TAG, "signIn(): couldn't insert courier " + courier);
                    return Result.failure();
                }

                final Data outputData = new Data.Builder()
                        .putLong(SharedPrefs.ID, courier.getId())
                        .putLong(SharedPrefs.BRANCH_ID, courier.getBrancheId())
                        .build();

                return ListenableWorker.Result.success(outputData);
            }
            final ResponseBody error = response.errorBody();

            if (error != null) {
                Log.e(TAG, "signIn(): " + error.string());
                return Result.failure();
            }
            Log.e(TAG, "signIn(): unknown error");
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "signIn(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = SignInWorker.class.toString();
}
