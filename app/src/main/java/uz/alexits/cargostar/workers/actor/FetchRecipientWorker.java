package uz.alexits.cargostar.workers.actor;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.utils.Constants;

public class FetchRecipientWorker extends Worker {
    private final long requestId;
    private final long invoiceId;
    private final String number;
    private final long providerId;
    private final long courierId;
    private final long tariffId;
    private final long senderId;
    private final long recipientId;
    private final String recipientSignature;
    private final long payerId;
    private final double price;
    private final int status;
    private final long createdAtTime;
    private final long updatedAtTime;

    private final String senderEmail;
    private final String senderSignature;
    private final String senderFirstName;
    private final String senderLastName;
    private final String senderMiddleName;
    private final String senderPhone;
    private final String senderAddress;
    private final long senderCountryId;
    private final long senderRegionId;
    private final long senderCityId;
    private final String senderZip;
    private final String senderCompany;
    private final String senderCargo;
    private final String senderTnt;
    private final String senderFedex;
    private final int discount;

    private final int consignmentQuantity;

    public FetchRecipientWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
        this.number = getInputData().getString(Constants.KEY_NUMBER);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, -1L);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);
        this.tariffId = getInputData().getLong(Constants.KEY_TARIFF_ID, -1L);
        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1L);
        this.recipientId = getInputData().getLong(Constants.KEY_RECIPIENT_ID, -1L);
        this.recipientSignature = getInputData().getString(Constants.KEY_RECIPIENT_SIGNATURE);
        this.payerId = getInputData().getLong(Constants.KEY_PAYER_ID, -1L);
        this.price = getInputData().getDouble(Constants.KEY_PRICE, -1);
        this.status = getInputData().getInt(Constants.KEY_STATUS, -1);
        this.createdAtTime = getInputData().getLong(Constants.KEY_CREATED_AT, -1L);
        this.updatedAtTime = getInputData().getLong(Constants.KEY_UPDATED_AT, -1L);

        this.senderEmail = getInputData().getString(Constants.KEY_SENDER_EMAIL);
        this.senderSignature = getInputData().getString(Constants.KEY_SENDER_SIGNATURE);
        this.senderFirstName = getInputData().getString(Constants.KEY_SENDER_FIRST_NAME);
        this.senderLastName = getInputData().getString(Constants.KEY_SENDER_LAST_NAME);
        this.senderMiddleName = getInputData().getString(Constants.KEY_SENDER_MIDDLE_NAME);
        this.senderPhone = getInputData().getString(Constants.KEY_SENDER_PHONE);
        this.senderAddress = getInputData().getString(Constants.KEY_SENDER_ADDRESS);
        this.senderCountryId = getInputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L);
        this.senderRegionId = getInputData().getLong(Constants.KEY_SENDER_REGION_ID, -1L);
        this.senderCityId = getInputData().getLong(Constants.KEY_SENDER_CITY_ID, -1L);
        this.senderZip = getInputData().getString(Constants.KEY_SENDER_ZIP);
        this.senderCompany = getInputData().getString(Constants.KEY_SENDER_COMPANY_NAME);
        this.senderCargo = getInputData().getString(Constants.KEY_SENDER_CARGOSTAR);
        this.senderTnt = getInputData().getString(Constants.KEY_SENDER_TNT);
        this.senderFedex = getInputData().getString(Constants.KEY_SENDER_FEDEX);
        this.discount = getInputData().getInt(Constants.KEY_DISCOUNT, 0);

        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId <= 0) {
            Log.e(TAG, "fetchRecipientData(): invoiceId <= 0");
            return Result.failure();
        }
        if (providerId <= 0) {
            Log.e(TAG, "fetchRecipientData(): providerId <= 0");
            return Result.failure();
        }
        if (requestId <= 0) {
            Log.e(TAG, "fetchRecipientData(): requestId <= 0");
            return Result.failure();
        }
        if (recipientId <= 0) {
            Log.e(TAG, "fetchRecipientData(): recipientId <= 0");
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<AddressBook> response = RetrofitClient.getInstance(getApplicationContext()).getAddressBookEntry(recipientId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchRecipientDataWorker(): response=" + response.body());
                    final AddressBook recipient = response.body();

                    if (recipient == null) {
                        Log.e(TAG, "fetchRecipientDataWorker(): recipient is NULL");
                        return Result.failure();
                    }

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).invoiceDao().insertAddressBookEntry(recipient);

                    if (rowInserted == -1) {
                        Log.e(TAG, "fetchRecipientDataWorker(): couldn't insert entry " + recipient);
                        return Result.failure();
                    }

                    final Data outputData = new Data.Builder()
                            .putLong(Constants.KEY_REQUEST_ID, requestId)
                            .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                            .putString(Constants.KEY_NUMBER, number)
                            .putLong(Constants.KEY_PROVIDER_ID, providerId)
                            .putLong(Constants.KEY_COURIER_ID, courierId)
                            .putLong(Constants.KEY_TARIFF_ID, tariffId)
                            .putLong(Constants.KEY_SENDER_ID, senderId)
                            .putLong(Constants.KEY_RECIPIENT_ID, recipientId)
                            .putString(Constants.KEY_RECIPIENT_SIGNATURE, recipientSignature)
                            .putLong(Constants.KEY_PAYER_ID, payerId)
                            .putDouble(Constants.KEY_PRICE, price)
                            .putLong(Constants.KEY_STATUS, status)
                            .putLong(Constants.KEY_CREATED_AT, createdAtTime)
                            .putLong(Constants.KEY_UPDATED_AT, updatedAtTime)

                            .putString(Constants.KEY_SENDER_EMAIL, senderEmail)
                            .putString(Constants.KEY_SENDER_SIGNATURE, senderSignature)
                            .putString(Constants.KEY_SENDER_FIRST_NAME, senderFirstName)
                            .putString(Constants.KEY_SENDER_LAST_NAME, senderLastName)
                            .putString(Constants.KEY_SENDER_MIDDLE_NAME, senderMiddleName)
                            .putString(Constants.KEY_SENDER_PHONE, senderPhone)
                            .putString(Constants.KEY_SENDER_ADDRESS, senderAddress)
                            .putLong(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId)
                            .putLong(Constants.KEY_SENDER_REGION_ID, senderRegionId)
                            .putLong(Constants.KEY_SENDER_CITY_ID, senderCityId)
                            .putString(Constants.KEY_SENDER_ZIP, senderZip)
                            .putString(Constants.KEY_SENDER_COMPANY_NAME, senderCompany)
                            .putString(Constants.KEY_SENDER_CARGOSTAR, senderCargo)
                            .putString(Constants.KEY_SENDER_TNT, senderTnt)
                            .putString(Constants.KEY_SENDER_FEDEX, senderFedex)
                            .putInt(Constants.KEY_DISCOUNT, discount)

                            .putString(Constants.KEY_RECIPIENT_EMAIL, recipient.getEmail())
                            .putString(Constants.KEY_RECIPIENT_FIRST_NAME, recipient.getFirstName())
                            .putString(Constants.KEY_RECIPIENT_LAST_NAME, recipient.getLastName())
                            .putString(Constants.KEY_RECIPIENT_MIDDLE_NAME, recipient.getMiddleName())
                            .putString(Constants.KEY_RECIPIENT_PHONE, recipient.getPhone())
                            .putString(Constants.KEY_RECIPIENT_ADDRESS, recipient.getAddress())
                            .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipient.getCountryId())
                            .putLong(Constants.KEY_RECIPIENT_REGION_ID, recipient.getRegionId() != null ? recipient.getRegionId() : -1L)
                            .putLong(Constants.KEY_RECIPIENT_CITY_ID, recipient.getCityId())
                            .putString(Constants.KEY_RECIPIENT_ZIP, recipient.getZip())
                            .putString(Constants.KEY_RECIPIENT_COMPANY_NAME, recipient.getCompany())
                            .putString(Constants.KEY_RECIPIENT_CARGOSTAR, recipient.getCargostarAccountNumber())
                            .putString(Constants.KEY_RECIPIENT_TNT, recipient.getTntAccountNumber())
                            .putString(Constants.KEY_RECIPIENT_FEDEX, recipient.getFedexAccountNumber())
                            .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                            .build();

                    Log.i(TAG, "fetchRecipientDataWorker(): successfully inserted entry " + recipient);
                    return Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "doWork(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchRecipientWorker.class.toString();
}
