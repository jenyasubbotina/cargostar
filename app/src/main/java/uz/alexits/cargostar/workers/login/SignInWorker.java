package uz.alexits.cargostar.workers.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.api.params.SignInParams;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.SignInActivity;

public class SignInWorker extends Worker {
    private final String login;
    private final String password;
    private final String token;

    public SignInWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.login = getInputData().getString(Constants.KEY_LOGIN);
        this.password = getInputData().getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(SharedPrefs.TOKEN);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<Courier> response = RetrofitClient.getInstance(getApplicationContext()).signIn(token);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final Courier courier = response.body();

                    if (courier == null) {
                        Log.e(TAG, "signIn(): courier is NULL");
                        return Result.failure();
                    }
                    courier.setPassword(password);
                    //todo: get local path for photo / signature
//                    courier.setPhotoUrl(null);

                    Log.i(TAG, "signIn(): " + courier);

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).actorDao().createCourier(courier);

                    if (rowInserted == -1) {
                        Log.e(TAG, "signIn(): couldn't insert courier " + courier);
                        return Result.failure();
                    }

                    final Data outputData = new Data.Builder()
                            .putLong(SharedPrefs.ID, courier.getId())
                            .putLong(SharedPrefs.BRANCH_ID, courier.getBrancheId())
                            .build();

                    Log.i(TAG, "signIn(): successfully inserted courier " + courier);
                    return Result.success(outputData);
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "signIn(): ", e);
            return Result.failure();
        }
        Log.e(TAG, "signIn(): unknown error");
        return Result.failure();
    }

    private static final String TAG = SignInWorker.class.toString();
}
