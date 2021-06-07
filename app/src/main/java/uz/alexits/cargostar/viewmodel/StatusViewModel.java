package uz.alexits.cargostar.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uz.alexits.cargostar.entities.location.City;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.entities.transportation.Route;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;
import uz.alexits.cargostar.repository.InvoiceRepository;
import uz.alexits.cargostar.repository.TransportationRepository;
import uz.alexits.cargostar.repository.TransportationStatusRepository;

public class StatusViewModel extends HeaderViewModel {
    private final TransportationRepository transportationRepository;
    private final TransportationStatusRepository transportationStatusRepository;
    private final InvoiceRepository invoiceRepository;

    private final MutableLiveData<Long> partialId;

    private final MutableLiveData<Long> currentTransportationId;
    private final MutableLiveData<Transportation> currentTransportation;
    private final MutableLiveData<List<Transportation>> currentTransportList;

    private final MutableLiveData<Long> requestId;
    private long invoiceId;

    private long currentPointId;
    private final MutableLiveData<Long> currentTransitPointId;

    private final TransportationStatus inTransitStatus;
    private final TransportationStatus onTheWayStatus;
    private final TransportationStatus deliveredStatus;
    private final TransportationStatus leavingUzbekistanStatus;

    private final MutableLiveData<Route> currentPath;
    private Route nextPath;

    private final MutableLiveData<TransportationStatus> currentStatus;
    private final MediatorLiveData<TransportationStatus> nextStatus;
    private final MediatorLiveData<Long> destinationCountryId;

    private final MutableLiveData<Long> nextStatusId;
    private final MutableLiveData<Long> nextPointId;

    private final MutableLiveData<Integer> offset;

    private final MutableLiveData<UUID> fetchTransportationDataUUID;
    private final MutableLiveData<UUID> fetchInvoiceDataFromCacheUUID;
    private final MutableLiveData<UUID> fetchRouteUUID;
    private final MutableLiveData<UUID> updateStatusUUID;

    private Request requestData;

    public StatusViewModel(final Context context) {
        super(context);

        this.transportationRepository = new TransportationRepository(context);
        this.transportationStatusRepository = new TransportationStatusRepository(context);
        this.invoiceRepository = new InvoiceRepository(context);

        this.currentPointId = 0;
        this.invoiceId = 0;

        this.nextPath = null;
        this.requestData = null;

        this.currentTransportationId = new MutableLiveData<>();
        this.currentTransportation = new MutableLiveData<>();
        this.currentTransportList = new MutableLiveData<>();

        this.currentPath = new MutableLiveData<>();

        this.currentTransitPointId = new MutableLiveData<>();
        this.nextPointId = new MutableLiveData<>();

        this.currentStatus = new MutableLiveData<>();
        this.nextStatusId = new MutableLiveData<>();

        this.partialId = new MutableLiveData<>();

        this.fetchTransportationDataUUID = new MutableLiveData<>();
        this.fetchRouteUUID = new MutableLiveData<>();
        this.updateStatusUUID = new MutableLiveData<>();
        this.fetchInvoiceDataFromCacheUUID = new MutableLiveData<>();

        this.offset = new MutableLiveData<>();

        this.inTransitStatus = new TransportationStatus(3, "В транзитной точке", "");
        this.onTheWayStatus = new TransportationStatus(4, "В пути", "");
        this.deliveredStatus = new TransportationStatus(6, "Доставлена", "");
        this.leavingUzbekistanStatus = new TransportationStatus(8, "Покидает Узбекистан", "");

        this.requestId = new MutableLiveData<>();
        this.destinationCountryId = new MediatorLiveData<>();
        this.nextStatus = new MediatorLiveData<>();

        this.nextStatus.addSource(destinationCountryId, countryId -> {
            if (currentStatus.getValue() == null) {
                return;
            }
            if (currentStatus.getValue().getId() == inTransitStatus.getId()) {
                if (nextPath != null) {
                    nextStatus.setValue(onTheWayStatus);
                }
                else {
                    if (countryId == 191) {
                        nextStatus.setValue(deliveredStatus);
                    }
                    else {
                        nextStatus.setValue(leavingUzbekistanStatus);
                    }
                }
                return;
            }
            if (currentStatus.getValue().getId() == onTheWayStatus.getId()) {
                nextStatus.setValue(inTransitStatus);
                return;
            }
            if (currentStatus.getValue().getId() == deliveredStatus.getId() || currentStatus.getValue().getId() == leavingUzbekistanStatus.getId()) {
                nextStatus.setValue(null);
            }
        });
        this.nextStatus.addSource(currentStatus, status -> {
            if (destinationCountryId.getValue() == null) {
                return;
            }
            if (status.getId() == inTransitStatus.getId()) {
                if (nextPath != null) {
                    nextStatus.setValue(onTheWayStatus);
                }
                else {
                    if (destinationCountryId.getValue() == 191) {
                        nextStatus.setValue(deliveredStatus);
                    }
                    else {
                        nextStatus.setValue(leavingUzbekistanStatus);
                    }
                }
                return;
            }
            if (status.getId() == onTheWayStatus.getId()) {
                nextStatus.setValue(inTransitStatus);
                return;
            }
            if (status.getId() == deliveredStatus.getId() || status.getId() == leavingUzbekistanStatus.getId()) {
                nextStatus.setValue(null);
            }
        });
    }

    public void fetchTransportationData(final long transportationId) {
        fetchTransportationDataUUID.setValue(transportationStatusRepository.fetchTransportationData(transportationId));
    }

    public void fetchRoute(final long transportationId) {
        fetchRouteUUID.setValue(transportationStatusRepository.fetchTransportationRoute(transportationId));
    }

    public void updateStatus() {
        if (currentTransportation.getValue() == null) {
            Log.e(TAG, "updateStatus(): currentTransportation is NULL");
            return;
        }
        if (nextStatus.getValue() == null) {
            Log.e(TAG, "updateStatus(): nextStatus is NULL");
            return;
        }
        long pointId = currentPointId;

        if (nextPointId.getValue() != null) {
            pointId = nextPointId.getValue();
        }
        updateStatusUUID.setValue(transportationStatusRepository.updateTransportationStatus(
                currentTransportation.getValue().getId(), nextStatus.getValue().getId(), pointId));
    }

    public void sendRecipientSignatureAndUpdateStatus(final long invoiceId,
                                                      final String fullName,
                                                      final String phone,
                                                      final String address,
                                                      final String company,
                                                      final String recipientSignature,
                                                      final String recipientSignatureDate,
                                                      final String comment,
                                                      final boolean addresseeResult) {
        if (currentTransportation.getValue() == null) {
            Log.e(TAG, "updateStatus(): currentTransportation is NULL");
            return;
        }
        if (nextStatus.getValue() == null) {
            Log.e(TAG, "updateStatus(): nextStatus is NULL");
            return;
        }
        long pointId = currentPointId;

        if (nextPointId.getValue() != null) {
            pointId = nextPointId.getValue();
        }
        updateStatusUUID.setValue(transportationStatusRepository.sendRecipientSignatureAndUpdateStatusDelivered(
                invoiceId, fullName, phone, address, company, recipientSignature, recipientSignatureDate, comment, addresseeResult,
                currentTransportation.getValue().getId(), nextStatus.getValue().getId(), pointId));
    }

    public LiveData<List<Transportation>> getPartialList() {
        return Transformations.switchMap(partialId, input -> {
            if (input > 0) {
                return transportationRepository.selectTransportationsByPartialId(input);
            }
            return currentTransportList;
        });
    }

    public LiveData<WorkInfo> getUpdateStatusResult(final Context context) {
        return Transformations.switchMap(updateStatusUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getFetchTransportationDataResult(final Context context) {
        return Transformations.switchMap(fetchTransportationDataUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getFetchRouteResult(final Context context) {
        return Transformations.switchMap(fetchRouteUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<City> getCurrentCity() {
        return Transformations.switchMap(currentTransitPointId, locationRepository::selectCityByTransitPointId);
    }

    public void setCurrentTransitPointId(final long transitPointId) {
        this.currentTransitPointId.setValue(transitPointId);
        this.currentPointId = transitPointId;
    }

    public void setCurrentStatus(final TransportationStatus status) {
        this.currentStatus.setValue(status);
    }

    public void setCurrentTransportation(final Transportation transportation) {
        this.currentTransportationId.setValue(transportation.getId());
        this.currentTransportation.setValue(transportation);
        this.requestId.setValue(transportation.getRequestId());
        this.invoiceId = transportation.getInvoiceId();
        this.partialId.setValue(transportation.getPartialId());

        final List<Transportation> singleList = new ArrayList<>();
        singleList.add(transportation);

        this.currentTransportList.setValue(singleList);
    }

    public void setNextPointId(final long pointId) {
        this.nextPointId.setValue(pointId);
    }

    public void setNextStatus(final TransportationStatus status) {
        this.nextStatusId.setValue(status.getId());
        this.nextStatus.setValue(status);
    }

    public void setCurrentPath(final Route path) {
        this.currentPath.setValue(path);
    }

    public void setNextPath(final Route path) {
        this.nextPath = path;
    }

    public LiveData<Transportation> getCurrentTransportation() {
        return currentTransportation;
    }

    public LiveData<TransportationStatus> getNextStatus() {
        return nextStatus;
    }

    public LiveData<TransportationStatus> getCurrentStatus() {
        return currentStatus;
    }

    public LiveData<List<Route>> getRoute() {
        return Transformations.switchMap(currentTransportationId, transportationStatusRepository::selectRoute);
    }

    public void setProgressOffset(final int progress) {
        this.offset.setValue(progress);
    }

    public LiveData<Integer> getProgressOffset() {
        return offset;
    }

    public long getCurrentTransitPointId() {
        return currentPointId;
    }

    public TransportationStatus getInTransitStatus() {
        return inTransitStatus;
    }

    public TransportationStatus getOnTheWayStatus() {
        return onTheWayStatus;
    }

    public TransportationStatus getDeliveredStatus() {
        return deliveredStatus;
    }

    public TransportationStatus getLeavingUzbekistanStatus() {
        return leavingUzbekistanStatus;
    }

    private static final String TAG = StatusViewModel.class.toString();

    public LiveData<Integer> getPathOffset() {
        return offset;
    }

    public Route getNextPath() {
        return nextPath;
    }

    public LiveData<Long> getDestinationCountryId() {
        return Transformations.switchMap(requestId, locationRepository::selectDestinationCountryIdByRequestId);
    }

    public void setDestinationCountryId(final long countryId) {
        destinationCountryId.setValue(countryId);
    }

    public void fetchRequestDataFromCache(final long requestId) {
        fetchInvoiceDataFromCacheUUID.setValue(requestRepository.fetchRequestDataFromCache(requestId));
    }

    public LiveData<WorkInfo> getFetchRequestDataFromCacheResult(final Context context) {
        return Transformations.switchMap(fetchInvoiceDataFromCacheUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<Request> getRequestDataFromCache() {
        return Transformations.switchMap(requestId, requestRepository::selectRequest);
    }

    public void setRequestData(final Request requestData) {
        this.requestData = requestData;
    }

    public Request getRequestData() {
        return requestData;
    }

    public long getInvoiceId() {
        return invoiceId;
    }
}
