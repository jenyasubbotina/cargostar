package uz.alexits.cargostar.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.UUID;

import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.entities.transportation.Consignment;

public class CreateInvoiceViewModel extends InvoiceViewModel {
    private int action;

    private final MutableLiveData<AddressBook> recipientAddressBookData;
    private final MutableLiveData<AddressBook> payerAddressBookData;
    private final MutableLiveData<Long> payerCountryId;
    private final MutableLiveData<Country> payerCountry;

    private final MutableLiveData<String> clientEmail;
    private final MutableLiveData<UUID> createInvoiceUUID;

    public CreateInvoiceViewModel(final Context context) {
        super(context);

        this.action = 0;
        this.recipientAddressBookData = new MutableLiveData<>();
        this.payerAddressBookData = new MutableLiveData<>();
        this.payerCountryId = new MutableLiveData<>();
        this.payerCountry = new MutableLiveData<>();
        this.clientEmail = new MutableLiveData<>();
        this.createInvoiceUUID = new MutableLiveData<>();
    }

    public void setPayerCountry(final Country country) {
        this.payerCountry.setValue(country);
        this.payerCountryId.setValue(country.getId());
    }

    public void updateRecipientData(final AddressBook addressBook) {
        this.recipientAddressBookData.setValue(addressBook);
    }

    public void updatePayerData(final AddressBook addressBook) {
        this.payerAddressBookData.setValue(addressBook);
    }

    public void createInvoice(final String senderFirstName,
                              final String senderMiddleName,
                              final String senderLastName,
                              final String senderEmail,
                              final String senderPhone,
                              final long senderCountryId,
                              final String senderCityName,
                              final String senderAddress,
                              final String senderZip,
                              final String senderTnt,
                              final String senderFedex,
                              final String senderCompanyName,
                              final String senderSignature,
                              final String recipientFirstName,
                              final String recipientMiddleName,
                              final String recipientLastName,
                              final String recipientEmail,
                              final String recipientPhone,
                              final long recipientCountryId,
                              final String recipientCityName,
                              final String recipientAddress,
                              final String recipientZip,
                              final String recipientCargo,
                              final String recipientTnt,
                              final String recipientFedex,
                              final String recipientCompanyName,
                              final String payerFirstName,
                              final String payerMiddleName,
                              final String payerLastName,
                              final String payerEmail,
                              final String payerPhone,
                              final long payerCountryId,
                              final String payerCityName,
                              final String payerAddress,
                              final String payerZip,
                              final String payerCargostar,
                              final String payerTnt,
                              final String payerFedex,
                              final String payerTntTaxId,
                              final String payerFedexTaxId,
                              final String payerCompanyName,
                              final double discount,
                              final String payerInn,
                              final String contractNumber,
                              final String instructions) {
        if (consignmentList == null || consignmentList.isEmpty()) {
            Log.e(TAG, "createInvoice(): consignmentList empty");
            return;
        }
        if (getPackagingId() <= 0) {
            Log.e(TAG, "createInvoice(): packaging <= 0");
            return;
        }
        if (getTotalPrice() <= 0) {
            Log.e(TAG, "createInvoice(): totalPrice <= 0");
            return;
        }

        createInvoiceUUID.setValue(invoiceRepository.createInvoice(
                getRequestId(), getInvoiceId(), getProviderId(), getPackagingId(), getType(), getPaymentMethod(), getTotalWeight(), getTotalVolume(), getTotalPrice(),
                senderFirstName, senderMiddleName, senderLastName, senderEmail, senderPhone, senderCountryId, senderCityName, senderAddress, senderZip,
                senderTnt, senderFedex, senderCompanyName, senderSignature,
                recipientFirstName, recipientMiddleName, recipientLastName, recipientEmail, recipientPhone, recipientCountryId, recipientCityName, recipientAddress, recipientZip,
                recipientCargo, recipientTnt, recipientFedex, recipientCompanyName,
                payerFirstName, payerMiddleName, payerLastName, payerEmail, payerPhone, payerCountryId, payerCityName, payerAddress, payerZip, payerCargostar, payerTnt, payerFedex,
                payerTntTaxId, payerFedexTaxId, payerCompanyName,  discount, payerInn, contractNumber,
                instructions, consignmentList));
    }

    public LiveData<WorkInfo> getCreateInvoiceResult(final Context context) {
        return Transformations.switchMap(createInvoiceUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public void setSenderEmail(final String senderEmail) {
        this.clientEmail.setValue(senderEmail);
    }

    public void setCurrentConsignmentList(final List<Consignment> deserializeConsignmentList) {
        this.consignmentList = deserializeConsignmentList;
        this.observableConsignmentList.setValue(deserializeConsignmentList);
    }

    public LiveData<List<AddressBook>> getAddressBook() {
        return Transformations.switchMap(clientEmail, addressBookRepository::selectAddressBookBySenderEmail);
    }

    public LiveData<Client> getClientDataFromAddressBook() {
        return Transformations.switchMap(clientEmail, clientRepository::selectClientByEmail);
    }

    public LiveData<AddressBook> getRecipientAddressBook() {
        return recipientAddressBookData;
    }

    public LiveData<AddressBook> getPayerAddressBook() {
        return payerAddressBookData;
    }

    public void setAction(final int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public long getRecipientCountryId() {
        return recipientCountryIdPlain;
    }

    //CreateInvoiceFragment navigation types
    //0 -> DEFAULT (from mainFragment to CreateInvoiceFragment)
    //1 -> EDIT_INVOICE (from invoiceFragment to CreateInvoiceFragment)
    public static final int CREATE_INVOICE_ACTION_DEFAULT = 0;
    public static final int CREATE_INVOICE_ACTION_EDIT = 1;
    private static final String TAG = CreateInvoiceViewModel.class.toString();
}