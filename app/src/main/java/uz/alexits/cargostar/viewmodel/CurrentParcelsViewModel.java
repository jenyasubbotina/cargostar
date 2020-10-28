package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.model.TransportationStatus;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.shipping.Parcel;

import java.util.List;

public class CurrentParcelsViewModel extends AndroidViewModel {
    private final Repository repository;

    public CurrentParcelsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus transportationStatus) {
        return repository.selectParcelsByStatus(courierId, transportationStatus);
    }

    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus[] statusArray) {
        return repository.selectParcelsByStatus(courierId, statusArray);
    }

    public LiveData<List<Parcel>> selectParcelsByLocation(final long courierId, final long locationId) {
        return repository.selectParcelsByLocation(courierId, TransportationStatus.IN_TRANSIT, locationId);
    }

    public LiveData<List<Parcel>> selectParcelsByLocationAndStatus(final long courierId, final TransportationStatus[] statusList, final long locationId) {
        return repository.selectParcelsByLocationAndStatus(courierId, statusList, locationId);
    }

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
