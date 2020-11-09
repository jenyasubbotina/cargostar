package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.utils.Constants;

public class FetchTransportationRouteWorker extends Worker {
    private final Long transportationId;

    public FetchTransportationRouteWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.transportationId = getInputData().getLong(Constants.KEY_TRANSPORTATION_ID, -1L);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (transportationId == -1L) {
            Log.e(TAG, "fetchTransportationRoute(): empty transportation id");
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<List<Route>> response = RetrofitClient.getInstance(getApplicationContext()).getTransportationRoute(transportationId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchTransportationRoute(): response=" + response.body());
                    final List<Route> transportationRoute = response.body();
                    LocalCache.getInstance(getApplicationContext()).transportationDao().insertTransportationRoute(transportationRoute);
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
