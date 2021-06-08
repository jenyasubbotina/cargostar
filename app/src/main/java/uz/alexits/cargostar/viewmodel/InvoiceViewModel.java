package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.UUID;

import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.entities.calculation.Packaging;
import uz.alexits.cargostar.entities.calculation.Provider;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.repository.AddressBookRepository;
import uz.alexits.cargostar.repository.ClientRepository;
import uz.alexits.cargostar.repository.ConsignmentRepository;
import uz.alexits.cargostar.repository.InvoiceRepository;
import uz.alexits.cargostar.repository.ProviderRepository;
import uz.alexits.cargostar.repository.TransportationRepository;

public class InvoiceViewModel extends CalculatorViewModel {
    protected final InvoiceRepository invoiceRepository;
    protected final ClientRepository clientRepository;
    protected final AddressBookRepository addressBookRepository;
    private final TransportationRepository transportationRepository;
    private final ConsignmentRepository consignmentRepository;
    private final ProviderRepository providerRepository;

    private boolean isRequest;
    private boolean isPublic;
    private long requestId;
    private long invoiceId;
    protected long recipientCountryIdPlain;
    private long packagingId;
    private int paymentMethod;
    private int consignmentQuantity;
    private long clientId;
    private long recipientId;
    private long payerId;

    private final MutableLiveData<Request> currentRequest;

    private final MutableLiveData<Long> currentRequestId;
    private final MutableLiveData<Long> currentInvoiceId;
    private final MutableLiveData<Long> currentClientId;
    private final MutableLiveData<Integer> currentConsignmentQuantity;

    private final MutableLiveData<Long> currentRecipientId;
    private final MutableLiveData<Long> currentPayerId;

    private final MutableLiveData<Long> senderCountryId;
    private final MutableLiveData<Long> recipientCountryId;
    private final MutableLiveData<Long> payerCountryId;

    private final MutableLiveData<Long> currentProviderId;
    private final MutableLiveData<Long> currentPackagingId;

    private final MutableLiveData<UUID> fetchInvoiceDataUUID;

    public InvoiceViewModel(final Context context) {
        super(context);

        this.invoiceRepository = new InvoiceRepository(context);
        this.clientRepository = new ClientRepository(context);
        this.addressBookRepository = new AddressBookRepository(context);
        this.transportationRepository = new TransportationRepository(context);
        this.consignmentRepository = new ConsignmentRepository(context);
        this.providerRepository = new ProviderRepository(context);

        this.isRequest = false;
        this.isPublic = false;
        this.requestId = 0;
        this.invoiceId = 0;
        this.clientId = 0;
        this.recipientId = 0;
        this.payerId = 0;
        this.packagingId = 0;
        this.consignmentQuantity = 0;
        this.paymentMethod = 0;

        this.currentRequest = new MutableLiveData<>();

        this.currentRequestId = new MutableLiveData<>();
        this.currentInvoiceId = new MutableLiveData<>();
        this.currentClientId = new MutableLiveData<>();
        this.currentConsignmentQuantity = new MutableLiveData<>();

        this.currentRecipientId = new MutableLiveData<>();
        this.currentPayerId = new MutableLiveData<>();
        this.currentProviderId = new MutableLiveData<>();
        this.currentPackagingId = new MutableLiveData<>();

        this.senderCountryId = new MutableLiveData<>();
        this.recipientCountryId = new MutableLiveData<>();
        this.payerCountryId = new MutableLiveData<>();

        this.fetchInvoiceDataUUID = new MutableLiveData<>();
    }

    /* for InvoiceFragment and CreateInvoiceFragment */
    public void setIsRequest(final boolean isRequest) {
        this.isRequest = isRequest;
    }

    public void setIsRequestPublic(final boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setCurrentRequest(final Request request) {
        this.currentRequest.setValue(request);
        this.currentRequestId.setValue(request.getId());
        this.requestId = request.getId();
    }

    public void setInvoiceId(final long invoiceId) {
        this.currentInvoiceId.setValue(invoiceId);
        this.invoiceId = invoiceId;
    }

    public void setClientId(final long clientId) {
        this.currentClientId.setValue(clientId);
        this.clientId = clientId;
    }

    public void setCurrentConsignmentQuantity(final int quantity) {
        this.currentConsignmentQuantity.setValue(quantity);
        this.consignmentQuantity = quantity;
    }

    public void setCurrentRecipientId(final long recipientId) {
        this.currentRecipientId.setValue(recipientId);
        this.recipientId = recipientId;
    }

    public void setCurrentPayerId(final long payerId) {
        this.currentPayerId.setValue(payerId);
        this.payerId = payerId;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public long getRequestId() {
        return requestId;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public long getClientId() {
        return clientId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public long getPayerId() {
        return payerId;
    }

    public long getProviderId() {
        return providerId;
    }

    public long getPackagingId() {
        return packagingId;
    }

    public void setPackagingId(final long packagingId) {
        this.packagingId = packagingId;
    }

    public int getType() {
        return type;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setCurrentPackagingId(final long packagingId) {
        this.currentPackagingId.setValue(packagingId);
        this.packagingId = packagingId;
    }

    public void setSenderCountryId(final long countryId) {
        this.senderCountryId.setValue(countryId);
    }

    public void setRecipientCountryId(final long countryId) {
        this.recipientCountryId.setValue(countryId);
        this.recipientCountryIdPlain = countryId;
    }

    public void setPayerCountryId(final long countryId) {
        this.payerCountryId.setValue(countryId);
    }

    public void setProviderId(final long providerId) {
        this.currentProviderId.setValue(providerId);
        this.providerId = providerId;
    }

    public void setPaymentMethod(final int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void fetchInvoiceData() {
        fetchInvoiceDataUUID.setValue(invoiceRepository.fetchInvoiceData(requestId, invoiceId, clientId, consignmentQuantity));
    }

    public LiveData<WorkInfo> getFetchInvoiceDataResult(final Context context) {
        return Transformations.switchMap(fetchInvoiceDataUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public LiveData<Request> getCurrentRequest() {
        return currentRequest;
    }

    public LiveData<Invoice> getInvoiceData() {
        return Transformations.switchMap(currentInvoiceId, invoiceRepository::selectInvoiceById);
    }

    public LiveData<Client> getSenderData() {
        return Transformations.switchMap(currentClientId, clientRepository::selectClientById);
    }

    public LiveData<AddressBook> getRecipientData() {
        return Transformations.switchMap(currentRecipientId, addressBookRepository::selectAddressBookEntryById);
    }

    public LiveData<AddressBook> getPayerData() {
        return Transformations.switchMap(currentPayerId, addressBookRepository::selectAddressBookEntryById);
    }

    public LiveData<Transportation> getTransportation() {
        return Transformations.switchMap(currentInvoiceId, transportationRepository::selectTransportationByInvoiceId);
    }

    public LiveData<List<Consignment>> getReceivedConsignmentList() {
        return Transformations.switchMap(currentRequestId, consignmentRepository::selectConsignmentListByRequestId);
    }

    public LiveData<Country> getSenderCountry() {
        return Transformations.switchMap(senderCountryId, locationRepository::selectCountryById);
    }

    public LiveData<Country> getRecipientCountry() {
        return Transformations.switchMap(recipientCountryId, locationRepository::selectCountryById);
    }

    public LiveData<Country> getPayerCountry() {
        return Transformations.switchMap(payerCountryId, locationRepository::selectCountryById);
    }

    public LiveData<Packaging> getPackagingData() {
        return Transformations.switchMap(currentPackagingId, packagingRepository::selectPackagingById);
    }

    public LiveData<Provider> getProvider() {
        return Transformations.switchMap(currentProviderId, providerRepository::selectProviderById);
    }

    private static final String TAG = InvoiceViewModel.class.toString();
}
