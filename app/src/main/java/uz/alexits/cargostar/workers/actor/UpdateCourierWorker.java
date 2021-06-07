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
import uz.alexits.cargostar.entities.params.UpdateCourierParams;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.actor.Courier;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Serializer;

public class UpdateCourierWorker extends Worker {
    private final long courierId;
    private final String login;
    private final String email;
    private final UpdateCourierParams params;

    public UpdateCourierWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, 0);
        this.login = getInputData().getString(Constants.KEY_LOGIN);
        this.email = getInputData().getString(Constants.KEY_EMAIL);
        this.params = new UpdateCourierParams(getInputData().getString(Constants.KEY_PASSWORD),
                getInputData().getString(Constants.KEY_FIRST_NAME),
                getInputData().getString(Constants.KEY_MIDDLE_NAME),
                getInputData().getString(Constants.KEY_LAST_NAME),
                getInputData().getString(Constants.KEY_PHONE),
                getInputData().getString(Constants.KEY_PHOTO));
    }

    @NonNull
    @Override
    public Result doWork() {
        if (courierId <= 0) {
            Log.e(TAG, "updateCourierData(): invalid courierId");
            return Result.failure();
        }

        String serializedPhoto = null;

        try {
            if (!TextUtils.isEmpty(params.getPhoto())) {
                serializedPhoto = Serializer.bitmapToBase64(getApplicationContext(), params.getPhoto());
                params.setPhoto(serializedPhoto);
            }

            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Courier> response = RetrofitClient.getInstance(getApplicationContext())
                    .updateCourierData(courierId, params);
            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final Courier updatedCourier = response.body();

                    if (updatedCourier == null) {
                        Log.e(TAG, "updateCourierData(): customer is NULL");
                        return Result.failure();
                    }
                    updatedCourier.setLogin(login);
                    updatedCourier.setEmail(email);
                    updatedCourier.setPhotoUrl(params.getPhoto());
                    LocalCache.getInstance(getApplicationContext()).courierDao()
                            .updateCourier(updatedCourier.getId(),
                                    updatedCourier.getPassword(),
                                    updatedCourier.getFirstName(),
                                    updatedCourier.getMiddleName(),
                                    updatedCourier.getLastName(),
                                    updatedCourier.getPhone());

                    Log.i(TAG, "updateCourierData(): successfully updated courier " + updatedCourier);
                    return Result.success();
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "updateCourierData(): ", e);
            return Result.failure();
        }
        Log.e(TAG, "updateCourierData(): unknown error");
        return Result.failure();
    }

    private static final String TAG = UpdateCourierWorker.class.toString();
}
