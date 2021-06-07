package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.database.dao.CourierDao;
import uz.alexits.cargostar.entities.actor.Courier;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.actor.UpdateCourierWorker;

public class CourierRepository {
    private final Context context;
    private final CourierDao courierDao;

    public CourierRepository(final Context context) {
        this.context = context;
        this.courierDao = LocalCache.getInstance(context).courierDao();
    }

    public  UUID editProfileData(final String login,
                                 final String email,
                                 final String password,
                                 final String firstName,
                                 final String middleName,
                                 final String lastName,
                                 final String phone,
                                 final String photoUrl) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_COURIER_ID, SharedPrefs.getInstance(context).getLong(SharedPrefs.ID))
                .putString(Constants.KEY_LOGIN, login)
                .putString(Constants.KEY_EMAIL, email)
                .putString(Constants.KEY_PASSWORD, password)
                .putString(Constants.KEY_FIRST_NAME, firstName)
                .putString(Constants.KEY_MIDDLE_NAME, middleName)
                .putString(Constants.KEY_LAST_NAME, lastName)
                .putString(Constants.KEY_PHONE, phone)
                .putString(Constants.KEY_PHOTO, photoUrl)
                .build();
        final OneTimeWorkRequest updateCourierRequest = new OneTimeWorkRequest.Builder(UpdateCourierWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(updateCourierRequest);
        return updateCourierRequest.getId();
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return courierDao.selectCourierByLogin(login);
    }

    public void dropCouriers() {
        courierDao.dropCouriers();
    }
}
