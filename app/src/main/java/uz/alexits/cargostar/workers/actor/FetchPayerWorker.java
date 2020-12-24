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

public class FetchPayerWorker extends Worker {
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

    private final String recipientEmail;
    private final String recipientFirstName;
    private final String recipientLastName;
    private final String recipientMiddleName;
    private final String recipientPhone;
    private final String recipientAddress;
    private final long recipientCountryId;
    private final long recipientRegionId;
    private final long recipientCityId;
    private final String recipientZip;
    private final String recipientCompany;
    private final String recipientCargo;
    private final String recipientTnt;
    private final String recipientFedex;

    private final int consignmentQuantity;

    public FetchPayerWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
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

        this.recipientEmail = getInputData().getString(Constants.KEY_RECIPIENT_EMAIL);
        this.recipientFirstName = getInputData().getString(Constants.KEY_RECIPIENT_FIRST_NAME);
        this.recipientLastName = getInputData().getString(Constants.KEY_RECIPIENT_LAST_NAME);
        this.recipientMiddleName = getInputData().getString(Constants.KEY_RECIPIENT_MIDDLE_NAME);
        this.recipientPhone = getInputData().getString(Constants.KEY_RECIPIENT_PHONE);
        this.recipientAddress = getInputData().getString(Constants.KEY_RECIPIENT_ADDRESS);
        this.recipientCountryId = getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
        this.recipientRegionId = getInputData().getLong(Constants.KEY_RECIPIENT_REGION_ID, -1L);
        this.recipientCityId = getInputData().getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L);
        this.recipientZip = getInputData().getString(Constants.KEY_RECIPIENT_ZIP);
        this.recipientCompany = getInputData().getString(Constants.KEY_RECIPIENT_COMPANY_NAME);
        this.recipientCargo = getInputData().getString(Constants.KEY_RECIPIENT_CARGOSTAR);
        this.recipientTnt = getInputData().getString(Constants.KEY_RECIPIENT_TNT);
        this.recipientFedex = getInputData().getString(Constants.KEY_RECIPIENT_FEDEX);

        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId <= 0) {
            Log.e(TAG, "fetchPayerDataWorker(): invoiceId <= 0");
            return Result.failure();
        }
        if (providerId <= 0) {
            Log.e(TAG, "fetchPayerDataWorker(): providerId <= 0");
            return Result.failure();
        }
        if (requestId <= 0) {
            Log.e(TAG, "fetchPayerDataWorker(): requestId <= 0");
            return Result.failure();
        }
        if (recipientId <= 0) {
            Log.e(TAG, "fetchPayerDataWorker(): recipientId <= 0");
            return Result.failure();
        }
        if (payerId <= 0) {
            Log.e(TAG, "fetchPayerDataWorker(): payerId <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<AddressBook> response = RetrofitClient.getInstance(getApplicationContext()).getAddressBookEntry(payerId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchPayerDataWorker(): response=" + response.body());
                    final AddressBook payer = response.body();

                    if (payer == null) {
                        Log.e(TAG, "fetchPayerDataWorker(): payer is NULL");
                        return Result.failure();
                    }

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).invoiceDao().insertAddressBookEntry(payer);

                    if (rowInserted == -1) {
                        Log.e(TAG, "fetchPayerDataWorker(): couldn't insert entry " + payer);
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

                            .putString(Constants.KEY_RECIPIENT_EMAIL, recipientEmail)
                            .putString(Constants.KEY_RECIPIENT_FIRST_NAME, recipientFirstName)
                            .putString(Constants.KEY_RECIPIENT_LAST_NAME, recipientLastName)
                            .putString(Constants.KEY_RECIPIENT_MIDDLE_NAME, recipientMiddleName)
                            .putString(Constants.KEY_RECIPIENT_PHONE, recipientPhone)
                            .putString(Constants.KEY_RECIPIENT_ADDRESS, recipientAddress)
                            .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                            .putLong(Constants.KEY_RECIPIENT_REGION_ID, recipientRegionId)
                            .putLong(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId)
                            .putString(Constants.KEY_RECIPIENT_ZIP, recipientZip)
                            .putString(Constants.KEY_RECIPIENT_COMPANY_NAME, recipientCompany)
                            .putString(Constants.KEY_RECIPIENT_CARGOSTAR, recipientCargo)
                            .putString(Constants.KEY_RECIPIENT_TNT, recipientTnt)
                            .putString(Constants.KEY_RECIPIENT_FEDEX, recipientFedex)

                            .putString(Constants.KEY_PAYER_EMAIL, payer.getEmail())
                            .putString(Constants.KEY_PAYER_FIRST_NAME, payer.getFirstName())
                            .putString(Constants.KEY_PAYER_LAST_NAME, payer.getLastName())
                            .putString(Constants.KEY_PAYER_MIDDLE_NAME, payer.getMiddleName())
                            .putString(Constants.KEY_PAYER_PHONE, payer.getPhone())
                            .putString(Constants.KEY_PAYER_ADDRESS, payer.getAddress())
                            .putLong(Constants.KEY_PAYER_COUNTRY_ID, payer.getCountryId())
                            .putLong(Constants.KEY_PAYER_REGION_ID, payer.getRegionId() != null ? payer.getRegionId() : -1L)
                            .putLong(Constants.KEY_PAYER_CITY_ID, payer.getCityId())
                            .putString(Constants.KEY_PAYER_ZIP, payer.getZip())
                            .putString(Constants.KEY_PAYER_COMPANY_NAME, payer.getCompany())
                            .putString(Constants.KEY_PAYER_CARGOSTAR, payer.getCargostarAccountNumber())
                            .putString(Constants.KEY_PAYER_TNT, payer.getTntAccountNumber())
                            .putString(Constants.KEY_PAYER_FEDEX, payer.getFedexAccountNumber())
                            .putString(Constants.KEY_PAYER_INN, payer.getInn())
                            .putString(Constants.KEY_PAYER_CHECKING_ACCOUNT, payer.getCheckingAccount())
                            .putString(Constants.KEY_PAYER_BANK, payer.getBank())
                            .putString(Constants.KEY_PAYER_REGISTRATION_CODE, payer.getRegistrationCode())
                            .putString(Constants.KEY_PAYER_MFO, payer.getMfo())
                            .putString(Constants.KEY_PAYER_OKED, payer.getOked())
                            .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                            .build();

                    Log.i(TAG, "fetchRecipientDataWorker(): successfully inserted entry " + payer);
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

    private static final String TAG = FetchPayerWorker.class.toString();
}
