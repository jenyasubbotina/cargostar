package uz.alexits.cargostar.workers.actor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.entities.params.CreateClientParams;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Serializer;

public class CreateClientWorker extends Worker {
    private final CreateClientParams params;

    public CreateClientWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.params = new CreateClientParams(
                getInputData().getString(Constants.KEY_EMAIL),
                getInputData().getString(Constants.KEY_PASSWORD),
                getInputData().getString(Constants.KEY_EMAIL),
                getInputData().getString(Constants.KEY_CARGOSTAR),
                getInputData().getString(Constants.KEY_TNT),
                getInputData().getString(Constants.KEY_FEDEX),
                getInputData().getString(Constants.KEY_FIRST_NAME),
                getInputData().getString(Constants.KEY_MIDDLE_NAME),
                getInputData().getString(Constants.KEY_LAST_NAME),
                getInputData().getString(Constants.KEY_PHONE),
                getInputData().getString(Constants.KEY_COUNTRY),
                getInputData().getString(Constants.KEY_CITY_NAME),
                getInputData().getString(Constants.KEY_ADDRESS),
                getInputData().getString(Constants.KEY_GEOLOCATION),
                getInputData().getString(Constants.KEY_ZIP),
                getInputData().getDouble(Constants.KEY_DISCOUNT,0),
                getInputData().getInt(Constants.KEY_USER_TYPE, 0),
                getInputData().getString(Constants.KEY_PASSPORT_SERIAL),
                getInputData().getString(Constants.KEY_INN),
                getInputData().getString(Constants.KEY_COMPANY),
                getInputData().getString(Constants.KEY_PAYER_CONTRACT_NUMBER),
                getInputData().getString(Constants.KEY_SIGNATURE));
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));

            String signatureBytesStr = null;

            if (!TextUtils.isEmpty(params.getSignatureUrl())) {
                signatureBytesStr = Serializer.fileToBase64(params.getSignatureUrl());
            }

            final Response<Client> response = RetrofitClient.getInstance(getApplicationContext()).createClient(params);
            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final Client newClient = response.body();

                    if (newClient == null) {
                        Log.e(TAG, "createUser(): customer is NULL");
                        return Result.failure();
                    }
                    newClient.setEmail(params.getEmail());
                    newClient.setSignatureUrl(params.getSignatureUrl());

                    Log.i(TAG, "createUser(): " + newClient);

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).clientDao().createClient(newClient);

                    if (rowInserted == -1) {
                        Log.e(TAG, "createUser(): couldn't insert customer " + newClient);
                        return Result.failure();
                    }
                    Log.i(TAG, "createUser(): successfully inserted customer " + newClient);
                    return Result.success();
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "createUser(): ", e);
            return Result.failure();
        }
        Log.e(TAG, "createUser(): unknown error");
        return Result.failure();
    }

    private static final String TAG = CreateClientWorker.class.toString();
}
