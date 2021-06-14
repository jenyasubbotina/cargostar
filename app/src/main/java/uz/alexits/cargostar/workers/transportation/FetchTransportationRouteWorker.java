package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Route;
import uz.alexits.cargostar.utils.Constants;

public class FetchTransportationRouteWorker extends Worker {
    private final long transportationId;
    private final boolean result;

    public FetchTransportationRouteWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.transportationId = getInputData().getLong(Constants.KEY_TRANSPORTATION_ID, 0L);
        this.result = getInputData().getBoolean(Constants.ADDRESSEE_IS_ACCEPTED, false);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (transportationId <= 0L) {
            Log.e(TAG, "fetchTransportationRoute(): empty transportation id");
            return Result.failure();
        }
        if (!result) {
            return Result.success();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<List<Route>> response = RetrofitClient.getInstance(getApplicationContext()).getTransportationRoute(transportationId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchTransportationRoute(): response=" + response.body());
                    final List<Route> transportationRoute = response.body();

                    LocalCache.getInstance(getApplicationContext()).transportationStatusDao().insertTransportationRoute(transportationRoute);
                    return Result.success();
                }
            }
            else {
                Log.e(TAG, "fetchTransportationRoute(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchTransportationRoute(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchTransportationRouteWorker.class.toString();
}
