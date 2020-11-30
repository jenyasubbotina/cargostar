package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

public class TransportationStatusViewModel extends AndroidViewModel {
    private final Repository repository;

    private final MutableLiveData<Long> transportationId;
    private final MutableLiveData<Long> currentTransitPointId;
    private final MutableLiveData<Long> currentCityId;

    private final LiveData<TransportationStatus> inTransitStatus;
    private final LiveData<TransportationStatus> onItsWayStatus;
    private final LiveData<TransportationStatus> deliveredStatus;
    private final LiveData<TransportationStatus> leftCountryStatus;

    private final MutableLiveData<TransportationStatus> nextTransportationStatus;
    private final MutableLiveData<Long> nextTransitPointId;

    private final MutableLiveData<Long> requestId;
    private final MutableLiveData<Long> partialId;

    public TransportationStatusViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);

        this.requestId = new MutableLiveData<>();
        this.partialId = new MutableLiveData<>();
        this.transportationId = new MutableLiveData<>();
        this.currentTransitPointId = new MutableLiveData<>();
        this.currentCityId = new MutableLiveData<>();
        this.nextTransportationStatus = new MutableLiveData<>();
        this.nextTransitPointId = new MutableLiveData<>();

        this.inTransitStatus = repository.selectTransportationStatusById(3L);
        this.onItsWayStatus = repository.selectTransportationStatusById(4L);
        this.deliveredStatus = repository.selectTransportationStatusById(6L);
        this.leftCountryStatus = repository.selectTransportationStatusById(8L);
    }

    /* Request */
    public void setRequestId(final Long requestId) {
        this.requestId.setValue(requestId);
    }

    public LiveData<Request> getCurrentRequest() {
        return Transformations.switchMap(requestId, repository::selectRequest);
    }

    /* Transportation */
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

    public void setPartialId(final Long partialId) {
        if (partialId == null) {
            return;
        }
        this.partialId.setValue(partialId);
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

    public LiveData<TransportationStatus> getLeftCountryStatus() {
        return leftCountryStatus;
    }

    public LiveData<Country> getDestinationCountry() {
        return Transformations.switchMap(requestId, repository::selectDestinationCountryByRequestId);
    }

    public LiveData<List<Transportation>> getPartialTransportationList() {
        return Transformations.switchMap(partialId, repository::selectTransportationsByPartialId);
    }
}
