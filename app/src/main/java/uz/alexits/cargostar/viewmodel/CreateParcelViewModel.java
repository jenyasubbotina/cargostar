package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.database.cache.Repository;

import java.util.List;

public class CreateParcelViewModel extends AndroidViewModel {
    private final Repository repository;

    public CreateParcelViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<AddressBook> selectAddressBookEntryById(final long id) {
        return repository.selectAddressBookEntryById(id);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntries() {
        return repository.selectAddressBookEntries();
    }

//    public void updateReceipt(final Receipt updatedReceipt) {
//        repository.updateReceipt(updatedReceipt);
//    }

//    public LiveData<Customer> selectCustomer(final String userId) {
//        return repository.selectCustomer(userId);
//    }
//
//    public LiveData<Customer> selectCustomerByLogin(final String senderLogin) {
//        return repository.selectCustomerByLogin(senderLogin);
//    }
}
