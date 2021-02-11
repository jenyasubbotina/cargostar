package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.transportation.Invoice;
import uz.alexits.cargostar.model.transportation.Transportation;

import java.util.List;

public class CreateInvoiceViewModel extends AndroidViewModel {
    private final Repository repository;
//    private final LiveData<List<Country>> countryList;

    /* sender data */
    private final MutableLiveData<Long> senderCountryId;

    /* recipient data */
    private final MutableLiveData<Long> recipientCountryId;

    /* payer data */
    private final MutableLiveData<Long> payerCountryId;

    /* address book */
    private final MutableLiveData<String> senderEmail;
    private final MutableLiveData<Long> senderUserId;

    private final MutableLiveData<Long> senderId;
    private final MutableLiveData<Long> recipientId;
    private final MutableLiveData<Long> payerId;

    private final MutableLiveData<Long> courierId;
    private final MutableLiveData<Long> requestId;
    private final MutableLiveData<Long> invoiceId;

    private final MutableLiveData<Long> providerId;
    private final MutableLiveData<Long> tariffId;

    public CreateInvoiceViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);

        this.senderCountryId = new MutableLiveData<>();
        this.recipientCountryId = new MutableLiveData<>();
        this.payerCountryId = new MutableLiveData<>();

        this.senderEmail = new MutableLiveData<>();
        this.senderUserId = new MutableLiveData<>();

        this.senderId = new MutableLiveData<>();
        this.recipientId = new MutableLiveData<>();
        this.payerId = new MutableLiveData<>();

        this.courierId = new MutableLiveData<>();
        this.requestId = new MutableLiveData<>();
        this.invoiceId = new MutableLiveData<>();

        this.providerId = new MutableLiveData<>();
        this.tariffId = new MutableLiveData<>();
    }

    /* location data */
    public LiveData<List<Country>> getCountryList() {
        return repository.selectAllCountries();
    }

    public void setSenderCountryId(final long senderCountryId) {
        this.senderCountryId.setValue(senderCountryId);
    }

    public void setRecipientCountryId(final long recipientCountryId) {
        this.recipientCountryId.setValue(recipientCountryId);
    }

    public void setPayerCountryId(final long payerCountryId) {
        this.payerCountryId.setValue(payerCountryId);
    }

    /* address book data*/
    public LiveData<List<AddressBook>> getSenderAddressBook() {
        return Transformations.switchMap(senderUserId, repository::selectAddressBookBySenderId);
    }

    public void setSenderEmail(final String email) {
        this.senderEmail.setValue(email);
    }

    public LiveData<Customer> getSender() {
        return Transformations.switchMap(senderEmail, repository::selectSenderByEmail);
    }

    public void setSenderUserId(final long senderUserId) {
        this.senderUserId.setValue(senderUserId);
    }

    public LiveData<Invoice> getInvoice() {
        return Transformations.switchMap(invoiceId, repository::selectInvoiceById);
    }

    public LiveData<Provider> getProvider() {
        return Transformations.switchMap(providerId, repository::selectProviderById);
    }

    public LiveData<Transportation> getTransportation() {
        return Transformations.switchMap(invoiceId, repository::selectTransportationByInvoiceId);
    }

    public void setTariffId(final Long tariffId) {
        if (tariffId == null) {
            return;
        }
        this.tariffId .setValue(tariffId);
    }

    public void setRecipientId(final Long recipientId) {
        if (recipientId == null) {
            return;
        }
        this.recipientId.setValue(recipientId);
    }

    public void setPayerId(final Long payerId) {
        if (payerId == null) {
            return;
        }
        this.payerId.setValue(payerId);
    }

    public void setRequestId(final Long requestId) {
        this.requestId.setValue(requestId);
    }

    public void setInvoiceId(final Long invoiceId) {
        if (invoiceId == null) {
            return;
        }
        this.invoiceId.setValue(invoiceId);
    }

    public void setProviderId(final Long providerId) {
        if (providerId == null) {
            return;
        }
        this.providerId.setValue(providerId);
    }
    public void setCourierId(final Long courierId) {
        if (courierId == null) {
            return;
        }
        this.courierId.setValue(courierId);
    }

    public void setSenderId(final Long senderId) {
        if (senderId == null) {
            return;
        }
        this.senderId.setValue(senderId);
    }

    private static final String TAG = CreateInvoiceViewModel.class.toString();
}
