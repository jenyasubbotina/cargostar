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
import uz.alexits.cargostar.model.transportation.TransportationStatus;

public class TransportationStatusViewModel extends AndroidViewModel {
    private final Repository repository;

    private final LiveData<TransportationStatus> inTransitStatus;
    private final LiveData<TransportationStatus> onItsWayStatus;
    private final LiveData<TransportationStatus> deliveredStatus;
    private final LiveData<TransportationStatus> leftCountryStatus;

    private final MutableLiveData<Long> transportationId;
    private final MutableLiveData<Long> requestId;
    private final MutableLiveData<Long> partialId;

    private final MutableLiveData<Long> currentPointId;
    private final MutableLiveData<Long> currentCityId;

    private final MutableLiveData<TransportationStatus> nextStatus;
    private final MutableLiveData<Long> nextPoint;


    public TransportationStatusViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);

        this.requestId = new MutableLiveData<>();
        this.partialId = new MutableLiveData<>();
        this.transportationId = new MutableLiveData<>();
        this.currentPointId = new MutableLiveData<>();
        this.currentCityId = new MutableLiveData<>();
        this.nextStatus = new MutableLiveData<>();
        this.nextPoint = new MutableLiveData<>();

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
    public void setCurrentPointId(final Long currentPointId) {
        this.currentPointId.setValue(currentPointId);
    }

    public void setTransportationId(final Long transportationId) {
        this.transportationId.setValue(transportationId);
    }

    public void setCurrentCityId(final Long cityId) {
        this.currentCityId.setValue(cityId);
    }

    public void setNextStatus(final TransportationStatus transportationStatus) {
        this.nextStatus.setValue(transportationStatus);
    }

    public void setNextPoint(final Long nextPoint) {
        this.nextPoint.setValue(nextPoint);
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

    public LiveData<TransitPoint> getCurrentTransitPoint() {
        return Transformations.switchMap(currentPointId, repository::selectTransitPointById);
    }

    public LiveData<City> getCurrentCity() {
        return Transformations.switchMap(currentCityId, repository::selectCityById);
    }

    public LiveData<TransportationStatus> getNextStatus() {
        return nextStatus;
    }

    public LiveData<Long> getNextTransitPoint() { return nextPoint; }

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

    public LiveData<Transportation> getCurrentTransportation() {
        return Transformations.switchMap(transportationId, repository::selectTransportationById);
    }
}
