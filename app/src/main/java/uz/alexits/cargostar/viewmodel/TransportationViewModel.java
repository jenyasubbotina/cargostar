package uz.alexits.cargostar.viewmodel;

import android.content.Context;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import uz.alexits.cargostar.entities.location.TransitPoint;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;
import uz.alexits.cargostar.repository.TransportationRepository;

import java.util.List;
import java.util.UUID;

public class TransportationViewModel extends HeaderViewModel {
    private final TransportationRepository transportationRepository;

    private final MutableLiveData<Long> selectedTransitPointId;
    private final MutableLiveData<Long> selectedTransportationStatusId;
    private final MutableLiveData<Pair<Long, Long>> selectedTransitPointIdAndStatusId;

    private final MutableLiveData<UUID> searchTransportationByQrUUID;
    private final MutableLiveData<UUID> fetchTransportationsUUID;
    private final MutableLiveData<UUID> fetchTransitPointsUUID;
    private final MutableLiveData<UUID> fetchTransportationStatusesUUID;

    public TransportationViewModel(final Context context) {
        super(context);

        this.transportationRepository = new TransportationRepository(context);

        this.selectedTransitPointId = new MutableLiveData<>();
        this.selectedTransportationStatusId = new MutableLiveData<>();
        this.selectedTransitPointIdAndStatusId = new MutableLiveData<>();

        this.searchTransportationByQrUUID = new MutableLiveData<>();
        this.fetchTransportationsUUID = new MutableLiveData<>();
        this.fetchTransitPointsUUID = new MutableLiveData<>();
        this.fetchTransportationStatusesUUID = new MutableLiveData<>();
    }

    /* Transportation */

    public LiveData<List<Transportation>> getTransportationList() {
        return Transformations.switchMap(selectedTransitPointIdAndStatusId, input -> {
            return transportationRepository.selectTransportationListByTransitPointAndStatusId(input.first, input.second);
        });
    }

    public LiveData<List<TransitPoint>> getTransitPoints() {
        return transportationRepository.selectTransitPoints();
    }

    public LiveData<List<TransportationStatus>> getTransportationStatuses() {
        return transportationRepository.selectTransportationStatuses();
    }

    public void searchTransportationByQr(final String qr) {
        this.searchTransportationByQrUUID.setValue(transportationRepository.searchTransportationByQr(qr));
    }

    public void fetchTransportationStatuses() {
        fetchTransportationsUUID.setValue(transportationRepository.fetchTransportationList());
    }

    public void fetchTransitPoints() {
        fetchTransitPointsUUID.setValue(transportationRepository.fetchTransitPoints());
    }

    public void fetchTransportationList() {
        fetchTransportationStatusesUUID.setValue(transportationRepository.fetchTransportationStatuses());
    }

    public LiveData<WorkInfo> getSearchTransportationByQrResult(final Context context) {
        return Transformations.switchMap(searchTransportationByQrUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getFetchTransportationsResult(final Context context) {
        return Transformations.switchMap(fetchTransportationsUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getFetchTransitPointsResult(final Context context) {
        return Transformations.switchMap(fetchTransitPointsUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getFetchTransportationStatusesResult(final Context context) {
        return Transformations.switchMap(fetchTransportationStatusesUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public void setSelectedTransitPoint(final TransitPoint transitPoint) {
        this.selectedTransitPointId.setValue(transitPoint.getId());
        this.selectedTransitPointIdAndStatusId.setValue(new Pair<>(transitPoint.getId(),
                selectedTransportationStatusId.getValue() != null ? selectedTransportationStatusId.getValue() : 0));
    }

    public void setSelectedTransportationStatus(final TransportationStatus transportationStatus) {
        this.selectedTransportationStatusId.setValue(transportationStatus.getId());
        this.selectedTransitPointIdAndStatusId.setValue(new Pair<>(
                selectedTransitPointId.getValue() != null ? selectedTransitPointId.getValue() : 0, transportationStatus.getId()));
    }

    private static final String TAG = TransportationViewModel.class.toString();
}
