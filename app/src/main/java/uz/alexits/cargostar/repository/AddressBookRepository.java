package uz.alexits.cargostar.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.AddressBookDao;
import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.workers.actor.FetchAddressBookWorker;
import uz.alexits.cargostar.workers.actor.GetLastAddressBookId;

public class AddressBookRepository {
    private final Context context;
    private final AddressBookDao addressBookDao;

    public AddressBookRepository(final Context context) {
        this.context = context;
        this.addressBookDao = LocalCache.getInstance(context).addressBookDao();
    }

    public long createAddressBookEntry(final AddressBook addressBook) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Callable<Long> callable = () -> addressBookDao.insertAddressBookEntry(addressBook);
        final Future<Long> value = executorService.submit(callable);
        executorService.shutdown();
        try {
            return value.get();
        }
        catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getString(): ", e);
            return 0L;
        }
    }

    public UUID fetchAddressBook() {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final OneTimeWorkRequest getLastAddressBookIdRequest = new OneTimeWorkRequest.Builder(GetLastAddressBookId.class)
                .setConstraints(dbConstraints)
                .build();
        final OneTimeWorkRequest fetchAddressBookListRequest = new OneTimeWorkRequest.Builder(FetchAddressBookWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).beginWith(getLastAddressBookIdRequest).then(fetchAddressBookListRequest).enqueue();
        return fetchAddressBookListRequest.getId();
    }

    public void updateAddressBookEntry(final AddressBook updatedAddressBook) {
        new Thread(() -> addressBookDao.updateAddressBookEntry(updatedAddressBook)).start();
    }

    public LiveData<AddressBook> selectAddressBookEntryById(final long id) {
        return addressBookDao.selectAddressBookEntryById(id);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntries() {
        return addressBookDao.selectAllAddressBookEntries();
    }

    public LiveData<List<AddressBook>> selectAddressBookBySenderUserId(final long senderId) {
        return addressBookDao.selectAddressBookBySenderUserId(senderId);
    }

    public LiveData<List<AddressBook>> selectAddressBookBySenderEmail(final String senderEmail) {
        return addressBookDao.selectAddressBookBySenderEmail(senderEmail);
    }

    private static final String TAG = AddressBookRepository.class.toString();
}
