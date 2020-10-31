package uz.alexits.cargostar.workers.location;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import retrofit2.Response;

public class FetchLocationDataWorker extends Worker {
    public FetchLocationDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final Gson gson = new Gson();

        InputStream countriesIs = null;
        InputStream regionsIs = null;
        InputStream citiesIs = null;

        try {
            countriesIs = getApplicationContext().getAssets().open("location/countries.json");
            regionsIs = getApplicationContext().getAssets().open("location/regions.json");
            citiesIs = getApplicationContext().getAssets().open("location/cities.json");

            Log.i(TAG, "IS: " + countriesIs);

            final Type countryType = new TypeToken<List<Country>>(){}.getType();
            final List<Country> countryList = gson.fromJson(new JsonReader(new InputStreamReader(countriesIs)), countryType);

            final Type regionType = new TypeToken<List<Region>>(){}.getType();
            final List<Region> regionList = gson.fromJson(new JsonReader(new InputStreamReader(regionsIs)), regionType);

            final Type cityType = new TypeToken<List<City>>(){}.getType();
            final List<City> cityList = gson.fromJson(new JsonReader(new InputStreamReader(citiesIs)), cityType);

            Log.i(TAG, "countryList=" + countryList.size());
            Log.i(TAG, "regionList=" + regionList.size());
            Log.i(TAG, "cityList=" + cityList.size());

            LocalCache.getInstance(getApplicationContext()).locationDao().insertCountries(countryList);
            LocalCache.getInstance(getApplicationContext()).locationDao().insertRegions(regionList);
            LocalCache.getInstance(getApplicationContext()).locationDao().insertCities(cityList);
            return Result.success();
        }
        catch (IOException e) {
            Log.e(TAG, "populateLocationData(): ", e);
            return Result.failure();
        }
        finally {
            try {
                if (countriesIs != null) {
                    countriesIs.close();
                }
                if (regionsIs != null) {
                    regionsIs.close();
                }
                if (citiesIs != null) {
                    citiesIs.close();
                }
            }
            catch (IOException e) {
                Log.e(TAG, "populateLocationData(): ", e);
            }
        }
    }

    private static final String TAG = FetchLocationDataWorker.class.toString();
}
