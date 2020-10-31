package uz.alexits.cargostar.workers.actor;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.utils.Constants;

public class UpdateCourierWorker extends Worker {
    private final long courierId;
    private final String login;
    private final String email;
    private final String password;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String phone;
    private final String photo;

    public UpdateCourierWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1);
        this.login = getInputData().getString(Constants.KEY_LOGIN);
        this.email = getInputData().getString(Constants.KEY_EMAIL);
        this.password = getInputData().getString(Constants.KEY_PASSWORD);
        this.firstName = getInputData().getString(Constants.KEY_FIRST_NAME);
        this.middleName = getInputData().getString(Constants.KEY_MIDDLE_NAME);
        this.lastName = getInputData().getString(Constants.KEY_LAST_NAME);
        this.phone = getInputData().getString(Constants.KEY_PHONE);
        this.photo = getInputData().getString(Constants.KEY_PHOTO);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (courierId <= 0) {
            Log.e(TAG, "updateCourierData(): invalid courierId");
            return Result.failure();
        }

        try {
            final Response<Courier> response = RetrofitClient.getInstance(getApplicationContext(),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH))
                    .updateCourierData(courierId, login, email, password, firstName, middleName, lastName, phone, photo);
            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final Courier updatedCourier = response.body();

                    if (updatedCourier == null) {
                        Log.e(TAG, "updateCourierData(): customer is NULL");
                        return Result.failure();
                    }

                    Log.i(TAG, "updateCourierData(): " + updatedCourier);

                    LocalCache.getInstance(getApplicationContext()).actorDao()
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
