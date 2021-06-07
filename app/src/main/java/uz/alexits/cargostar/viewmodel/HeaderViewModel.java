package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.actor.Courier;
import uz.alexits.cargostar.entities.location.Branche;
import uz.alexits.cargostar.repository.CourierRepository;
import uz.alexits.cargostar.repository.LocationRepository;
import uz.alexits.cargostar.repository.NotificationRepository;
import uz.alexits.cargostar.repository.RequestRepository;
import uz.alexits.cargostar.utils.Constants;

public class HeaderViewModel extends ViewModel {
    protected final CourierRepository courierRepository;
    protected final LocationRepository locationRepository;
    protected final NotificationRepository notificationRepository;
    protected final RequestRepository requestRepository;

    protected final MutableLiveData<UUID> searchRequestByIdUUID;

    public HeaderViewModel(final Context context) {
        this.courierRepository = new CourierRepository(context);
        this.locationRepository = new LocationRepository(context);
        this.notificationRepository = new NotificationRepository(context);
        this.requestRepository = new RequestRepository(context);

        this.searchRequestByIdUUID = new MutableLiveData<>();
    }

    /* header data */
    public LiveData<Courier> getCourierData(final Context context) {
        return courierRepository.selectCourierByLogin(SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN));
    }

    public LiveData<Branche> getBrancheData(final Context context) {
        return locationRepository.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID));
    }

    public LiveData<Integer> selectNewNotificationsCount() {
        return notificationRepository.selectNewNotificationsCount();
    }

    public void searchRequest(final long requestId) {
        searchRequestByIdUUID.setValue(requestRepository.searchRequest(requestId));
    }

    public LiveData<WorkInfo> getSearchRequestResult(final Context context) {
        return Transformations.switchMap(searchRequestByIdUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }
}
