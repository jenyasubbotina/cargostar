package com.example.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cargostar.model.actor.AddressBook;
import com.example.cargostar.model.database.Repository;
import com.example.cargostar.model.shipping.Receipt;

import java.util.List;

public class CreateParcelViewModel extends AndroidViewModel {
    private final Repository repository;

    public CreateParcelViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntriesBySenderLogin(final String senderLogin) {
        return repository.selectAddressBookEntriesBySenderLogin(senderLogin);
    }

    public LiveData<AddressBook> selectAddressBookEntryById(final long id) {
        return repository.selectAddressBookEntryById(id);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntries() {
        return repository.selectAddressBookEntries();
    }

    public void updateReceipt(final Receipt updatedReceipt) {
        repository.updateReceipt(updatedReceipt);
    }
}
