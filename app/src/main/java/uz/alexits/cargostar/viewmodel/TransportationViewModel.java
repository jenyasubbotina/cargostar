package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.database.dao.TransportationDao;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

import java.util.List;

public class TransportationViewModel extends AndroidViewModel {
    private final Repository repository;

    private final MutableLiveData<Long> currentTransitPointId;
//    private final MutableLiveData<Long> currentStatusId;

    private final LiveData<List<Transportation>> currentTransportationList;


    public TransportationViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);

        this.currentTransitPointId = new MutableLiveData<>();

        this.currentTransportationList = repository.selectCurrentTransportations();
    }

    /* Transportation */
    public void setCurrentTransitPointId(final Long currentTransitPointId) {
        this.currentTransitPointId.setValue(currentTransitPointId);
    }

    public LiveData<List<Transportation>> getCurrentTransportationList() {
        return currentTransportationList;
    }

//    public LiveData<List<Transportation>> getTransportationListByTransitPointId() {
//
//    }

    /*Transit points*/
    public LiveData<List<TransitPoint>> selectAllTransitPoints() {
        return repository.selectAllTransitPoints();
    }

    public LiveData<TransitPoint> selectTransitPointByBranch(final long branchId) {
        return repository.selectTransitPointByBranch(branchId);
    }

    public LiveData<List<TransitPoint>> selectTransitPointsByCity(final long cityId) {
        return repository.selectAllTransitPointsByCity(cityId);
    }

    public LiveData<List<TransitPoint>> selectTransitPointsByCountry(final long countryId) {
        return repository.selectAllTransitPointsByCountry(countryId);
    }
}
