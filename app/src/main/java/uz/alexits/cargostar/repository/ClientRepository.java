package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.ClientDao;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.actor.CreateClientWorker;
import uz.alexits.cargostar.workers.actor.FetchSenderListWorker;
import uz.alexits.cargostar.workers.actor.GetLastSenderId;

public class ClientRepository {
    private final Context context;
    private final ClientDao clientDao;

    public ClientRepository(final Context context) {
        this.context = context;
        this.clientDao = LocalCache.getInstance(context).clientDao();
    }

    public LiveData<Client> selectClientById(final long clientId) {
        return clientDao.selectClientByUserId(clientId);
    }

    public LiveData<Client> selectClientByEmail(final String senderEmail) {
        return clientDao.selectClientByEmail(senderEmail);
    }

    public UUID fetchClientList() {
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
        final OneTimeWorkRequest getSenderLastIdRequest = new OneTimeWorkRequest.Builder(GetLastSenderId.class)
                .setConstraints(dbConstraints)
                .build();
        final OneTimeWorkRequest fetchSenderListRequest = new OneTimeWorkRequest.Builder(FetchSenderListWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).beginWith(getSenderLastIdRequest).then(fetchSenderListRequest).enqueue();

        return fetchSenderListRequest.getId();
    }

    public UUID createClient(final String email,
                             final String password,
                             final String cargostarAccountNumber,
                             final String tntAccountNumber,
                             final String fedexAccountNumber,
                             final String firstName,
                             final String middleName,
                             final String lastName,
                             final String phone,
                             final String country,
                             final String cityName,
                             final String address,
                             final String geolocation,
                             final String zip,
                             final double discount,
                             final int userType,
                             final String passportSerial,
                             final String inn,
                             final String company,
                             final String contractNumber,
                             final String signatureUrl) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putString(Constants.KEY_EMAIL, email)
                .putString(Constants.KEY_PASSWORD, password)
                .putString(Constants.KEY_CARGOSTAR, cargostarAccountNumber)
                .putString(Constants.KEY_TNT, tntAccountNumber)
                .putString(Constants.KEY_FEDEX, fedexAccountNumber)
                .putString(Constants.KEY_FIRST_NAME, firstName)
                .putString(Constants.KEY_MIDDLE_NAME, middleName)
                .putString(Constants.KEY_LAST_NAME, lastName)
                .putString(Constants.KEY_PHONE, phone)
                .putString(Constants.KEY_COUNTRY, country)
                .putString(Constants.KEY_CITY_NAME, cityName)
                .putString(Constants.KEY_ADDRESS, address)
                .putString(Constants.KEY_GEOLOCATION, geolocation)
                .putString(Constants.KEY_ZIP, zip)
                .putDouble(Constants.KEY_DISCOUNT, discount)
                .putInt(Constants.KEY_USER_TYPE, userType)
                .putString(Constants.KEY_PASSPORT_SERIAL, passportSerial)
                .putString(Constants.KEY_INN, inn)
                .putString(Constants.KEY_COMPANY, company)
                .putString(Constants.KEY_PAYER_CONTRACT_NUMBER, contractNumber)
                .putString(Constants.KEY_SIGNATURE, signatureUrl)
                .build();

        final OneTimeWorkRequest createUserWorker = new OneTimeWorkRequest.Builder(CreateClientWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(createUserWorker);

        return createUserWorker.getId();
    }
}
