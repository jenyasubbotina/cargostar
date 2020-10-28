package uz.alexits.cargostar.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import uz.alexits.cargostar.workers.location.FetchBranchesWorker;
import uz.alexits.cargostar.workers.location.FetchCitiesWorker;
import uz.alexits.cargostar.workers.location.FetchCountriesWorker;
import uz.alexits.cargostar.workers.location.FetchRegionsWorker;
import uz.alexits.cargostar.workers.packaging.FetchPackagingTypesWorker;
import uz.alexits.cargostar.workers.packaging.FetchPackagingWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestsWorker;

import java.util.concurrent.TimeUnit;

public class SyncWorkRequest {
//    public static void fetchCountryData(@NonNull final Context context, final int perPage) {
//        final Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresCharging(false)
//                .setRequiresStorageNotLow(false)
//                .setRequiresDeviceIdle(false)
//                .build();
//
//        final Data inputData = new Data.Builder()
//                .putInt(KEY_PER_PAGE, perPage)
//                .build();
//
//        final OneTimeWorkRequest fetchCountriesRequest = new OneTimeWorkRequest.Builder(FetchCountriesWorker.class)
//                .setConstraints(constraints)
//                .setInputData(inputData)
//                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
//                .build();
//        WorkManager.getInstance(context).enqueue(fetchCountriesRequest);
//    }

    public static void fetchLocationData(@NonNull final Context context, final int perPage) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, perPage)
                .build();

        final OneTimeWorkRequest fetchCountriesRequest = new OneTimeWorkRequest.Builder(FetchCountriesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchRegionsRequest = new OneTimeWorkRequest.Builder(FetchRegionsWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchCitiesRequest = new OneTimeWorkRequest.Builder(FetchCitiesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchBranchesRequest = new OneTimeWorkRequest.Builder(FetchBranchesWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(fetchCountriesRequest)
                .then(fetchRegionsRequest)
                .then(fetchCitiesRequest)
                .then(fetchBranchesRequest)
                .enqueue();
    }

    public static void fetchPackagingData(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final OneTimeWorkRequest fetchPackagingRequest = new OneTimeWorkRequest.Builder(FetchPackagingWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchPackagingTypesRequest = new OneTimeWorkRequest.Builder(FetchPackagingTypesWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(fetchPackagingRequest)
                .then(fetchPackagingTypesRequest)
                .enqueue();
    }

    public static void fetchRequestData(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final OneTimeWorkRequest fetchRequestsRequest = new OneTimeWorkRequest.Builder(FetchRequestsWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(fetchRequestsRequest);
    }

    public static final String KEY_PER_PAGE = "per-page";
    private static final int DEFAULT_DELAY = 60000;
}
