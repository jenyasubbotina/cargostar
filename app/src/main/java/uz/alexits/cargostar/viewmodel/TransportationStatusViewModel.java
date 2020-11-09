package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

public class TransportationStatusViewModel extends AndroidViewModel {
    private final Repository repository;

    private final MutableLiveData<Transportation> currentTransportation;

    private final MutableLiveData<Long> transportationId;
    private final MutableLiveData<Long> currentTransitPointId;
    private final MutableLiveData<Long> currentCityId;

    private final LiveData<TransportationStatus> inTransitStatus;
    private final LiveData<TransportationStatus> onItsWayStatus;
    private final LiveData<TransportationStatus> deliveredStatus;

    private final MutableLiveData<TransportationStatus> nextTransportationStatus;
    private final MutableLiveData<Long> nextTransitPointId;

    public TransportationStatusViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);

        this.currentTransportation = new MutableLiveData<>();

        this.transportationId = new MutableLiveData<>();
        this.currentTransitPointId = new MutableLiveData<>();
        this.currentCityId = new MutableLiveData<>();

        this.inTransitStatus = repository.selectTransportationStatusByName(application.getApplicationContext().getString(R.string.in_transit));
        this.onItsWayStatus = repository.selectTransportationStatusByName(application.getApplicationContext().getString(R.string.on_the_way));
        this.deliveredStatus = repository.selectTransportationStatusByName(application.getApplicationContext().getString(R.string.delivered));

        this.nextTransportationStatus = new MutableLiveData<>();
        this.nextTransitPointId = new MutableLiveData<>();
    }

    /* Transportation */
    public void setCurrentTransportation(final Transportation transportation) {
        this.currentTransportation.setValue(transportation);
    }

    public void setCurrentTransitPointId(final Long currentTransitPointId) {
        this.currentTransitPointId.setValue(currentTransitPointId);
    }

    public void setTransportationId(final Long transportationId) {
        this.transportationId.setValue(transportationId);
    }

    public void setCurrentCityId(final Long cityId) {
        this.currentCityId.setValue(cityId);
    }

    public void setNextTransportationStatus(final TransportationStatus transportationStatus) {
        this.nextTransportationStatus.setValue(transportationStatus);
    }

    public void setNextTransitPointId(final Long nextTransitPointId) {
        this.nextTransitPointId.setValue(nextTransitPointId);
    }

    public LiveData<Transportation> getCurrentTransportation() {
        return currentTransportation;
    }

    public LiveData<List<Route>> getRoute() {
        return Transformations.switchMap(transportationId, repository::selectRouteByTransportationId);
    }

    public LiveData<List<TransportationData>> getTransportationData() {
        return Transformations.switchMap(transportationId, repository::selectTransportationDataByTransportationId);
    }

    public LiveData<TransitPoint> getCurrentTransitPoint() {
        return Transformations.switchMap(currentTransitPointId, repository::selectTransitPointById);
    }

    public LiveData<City> getCurrentCity() {
        return Transformations.switchMap(currentCityId, repository::selectCityById);
    }

    public LiveData<TransportationStatus> getNextStatus() {
        return nextTransportationStatus;
    }

    public LiveData<Long> getNextTransitPoint() { return nextTransitPointId; }

    public LiveData<TransportationStatus> getInTransitStatus() {
        return inTransitStatus;
    }

    public LiveData<TransportationStatus> getOnItsWayStatus() {
        return onItsWayStatus;
    }

    public LiveData<TransportationStatus> getDeliveredStatus() {
        return deliveredStatus;
    }

//    public LiveData<TransitPoint> getCurrentCity() {
//        return Transformations.switchMap(transportationId, repository::selectCityById);
//    }
}
