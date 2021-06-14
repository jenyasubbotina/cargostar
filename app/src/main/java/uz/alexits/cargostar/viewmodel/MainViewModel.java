package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

import uz.alexits.cargostar.repository.AddressBookRepository;
import uz.alexits.cargostar.repository.ClientRepository;
import uz.alexits.cargostar.repository.TransportationRepository;

public class MainViewModel extends HeaderViewModel {
    private final ClientRepository clientRepository;
    private final AddressBookRepository addressBookRepository;
    private final TransportationRepository transportationRepository;

    private MutableLiveData<UUID> scanQrUUID;
    private final MutableLiveData<UUID> fetchLocationDataUUID;
    private final MutableLiveData<UUID> fetchClientListUUID;
    private final MutableLiveData<UUID> fetchAddressBookUUID;

    public MainViewModel(final Context context) {
        super(context);

        this.clientRepository = new ClientRepository(context);
        this.addressBookRepository = new AddressBookRepository(context);
        this.transportationRepository = new TransportationRepository(context);

        this.fetchLocationDataUUID = new MutableLiveData<>();
        this.fetchClientListUUID = new MutableLiveData<>();
        this.fetchAddressBookUUID = new MutableLiveData<>();
        this.scanQrUUID = new MutableLiveData<>();
    }

    public void fetchLocationData() {
        fetchLocationDataUUID.setValue(locationRepository.fetchLocationData());
    }

    public void fetchClientList() {
        fetchClientListUUID.setValue(clientRepository.fetchClientList());
    }

    public void fetchAddressBook() {
        fetchAddressBookUUID.setValue(addressBookRepository.fetchAddressBook());
    }

    public LiveData<WorkInfo> getFetchLocationDataResult(final Context context) {
        return Transformations.switchMap(fetchLocationDataUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getFetchClientListResult(final Context context) {
        return Transformations.switchMap(fetchClientListUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getFetchAddressBookResult(final Context context) {
        return Transformations.switchMap(fetchAddressBookUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<WorkInfo> getScanQrResult(final Context context) {
        return Transformations.switchMap(scanQrUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public void removeSearchTransportationByQrUUID() {
        this.scanQrUUID = new MutableLiveData<>();
    }

    public void searchTransportationByQrAndBindRequest(final String qr) {
        scanQrUUID.setValue(transportationRepository.searchTransportationByQrAndBindRequest(qr));
    }
}
