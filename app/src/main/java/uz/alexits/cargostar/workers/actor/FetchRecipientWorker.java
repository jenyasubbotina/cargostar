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
    private final double price;
    private final int deliveryType;
    private final String paymentStatus;
    private final int paymentMethod;
    private final String comment;

    private final int status;
    private final long createdAtTime;
    private final long updatedAtTime;

    private final long senderId;
    private final long senderUserId;
    private final String senderEmail;
    private final String senderFirstName;
    private final String senderLastName;
    private final String senderMiddleName;
    private final String senderPhone;
    private final String senderAddress;
    private final String senderZip;
    private final long senderCountryId;
    private final String senderCityName;
    private final String senderCargo;
    private final String senderTnt;
    private final String senderFedex;
    private final String senderSignature;
    private final String senderPassport;
    private final String senderInn;
    private final String senderCompany;
    private final String senderPhoto;
    private final int senderDiscount;
    private final int senderType;

    private final long recipientId;
    private final String recipientSignature;
    private final long payerId;

    private final int consignmentQuantity;

    public FetchRecipientWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, 0L);
        this.number = getInputData().getString(Constants.KEY_NUMBER);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, 0L);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, 0L);
        this.tariffId = getInputData().getLong(Constants.KEY_TARIFF_ID, 0L);
        this.price = getInputData().getDouble(Constants.KEY_PRICE, 0);
        this.deliveryType = getInputData().getInt(Constants.KEY_DELIVERY_TYPE, 0);
        this.paymentStatus = getInputData().getString(Constants.KEY_PAYMENT_STATUS);
        this.paymentMethod = getInputData().getInt(Constants.KEY_PAYMENT_METHOD, 0);
        this.comment = getInputData().getString(Constants.KEY_COMMENT);

        this.status = getInputData().getInt(Constants.KEY_STATUS, 0);
        this.createdAtTime = getInputData().getLong(Constants.KEY_CREATED_AT, 0L);
        this.updatedAtTime = getInputData().getLong(Constants.KEY_UPDATED_AT, 0L);

        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, 0L);
        this.senderUserId = getInputData().getLong(Constants.KEY_SENDER_USER_ID, 0L);
        this.senderEmail = getInputData().getString(Constants.KEY_SENDER_EMAIL);
        this.senderFirstName = getInputData().getString(Constants.KEY_SENDER_FIRST_NAME);
        this.senderLastName = getInputData().getString(Constants.KEY_SENDER_LAST_NAME);
        this.senderMiddleName = getInputData().getString(Constants.KEY_SENDER_MIDDLE_NAME);
        this.senderPhone = getInputData().getString(Constants.KEY_SENDER_PHONE);
        this.senderAddress = getInputData().getString(Constants.KEY_SENDER_ADDRESS);
        this.senderCountryId = getInputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, 0L);
        this.senderCityName = getInputData().getString(Constants.KEY_SENDER_CITY_NAME);
        this.senderZip = getInputData().getString(Constants.KEY_SENDER_ZIP);
        this.senderCargo = getInputData().getString(Constants.KEY_SENDER_CARGOSTAR);
        this.senderTnt = getInputData().getString(Constants.KEY_SENDER_TNT);
        this.senderFedex = getInputData().getString(Constants.KEY_SENDER_FEDEX);
        this.senderSignature = getInputData().getString(Constants.KEY_SENDER_SIGNATURE);
        this.senderPassport = getInputData().getString(Constants.KEY_SENDER_PASSPORT);
        this.senderInn = getInputData().getString(Constants.KEY_SENDER_INN);
        this.senderCompany = getInputData().getString(Constants.KEY_SENDER_COMPANY_NAME);
        this.senderPhoto = getInputData().getString(Constants.KEY_SENDER_PHOTO);
        this.senderType = getInputData().getInt(Constants.KEY_SENDER_TYPE, 0);
        this.senderDiscount = getInputData().getInt(Constants.KEY_DISCOUNT, 0);

        this.recipientId = getInputData().getLong(Constants.KEY_RECIPIENT_ID, 0L);
        this.recipientSignature = getInputData().getString(Constants.KEY_RECIPIENT_SIGNATURE);
        this.payerId = getInputData().getLong(Constants.KEY_PAYER_ID, 0L);

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
                            .putDouble(Constants.KEY_PRICE, price)
                            .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                            .putString(Constants.KEY_PAYMENT_STATUS, paymentStatus)
                            .putInt(Constants.KEY_PAYMENT_METHOD, paymentMethod)
                            .putString(Constants.KEY_COMMENT, comment)

                            .putLong(Constants.KEY_STATUS, status)
                            .putLong(Constants.KEY_CREATED_AT, createdAtTime)
                            .putLong(Constants.KEY_UPDATED_AT, updatedAtTime)

                            .putLong(Constants.KEY_SENDER_ID, senderId)
                            .putLong(Constants.KEY_SENDER_USER_ID, senderUserId)
                            .putString(Constants.KEY_SENDER_EMAIL, senderEmail)
                            .putString(Constants.KEY_SENDER_SIGNATURE, senderSignature)
                            .putString(Constants.KEY_SENDER_FIRST_NAME, senderFirstName)
                            .putString(Constants.KEY_SENDER_LAST_NAME, senderLastName)
                            .putString(Constants.KEY_SENDER_MIDDLE_NAME, senderMiddleName)
                            .putString(Constants.KEY_SENDER_PHONE, senderPhone)
                            .putString(Constants.KEY_SENDER_ADDRESS, senderAddress)
                            .putString(Constants.KEY_SENDER_ZIP, senderZip)
                            .putLong(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId)
                            .putString(Constants.KEY_SENDER_CITY_NAME, senderCityName)
                            .putString(Constants.KEY_SENDER_CARGOSTAR, senderCargo)
                            .putString(Constants.KEY_SENDER_TNT, senderTnt)
                            .putString(Constants.KEY_SENDER_FEDEX, senderFedex)
                            .putString(Constants.KEY_SENDER_PASSPORT, senderPassport)
                            .putString(Constants.KEY_SENDER_COMPANY_NAME, senderCompany)
                            .putString(Constants.KEY_SENDER_INN, senderInn)
                            .putString(Constants.KEY_SENDER_PHOTO, senderPhoto)
                            .putInt(Constants.KEY_DISCOUNT, senderDiscount)
                            .putInt(Constants.KEY_SENDER_TYPE, senderType)

                            .putLong(Constants.KEY_RECIPIENT_ID, recipient.getId())
                            .putLong(Constants.KEY_RECIPIENT_USER_ID, recipient.getUserId())
                            .putString(Constants.KEY_RECIPIENT_EMAIL, recipient.getEmail())
                            .putString(Constants.KEY_RECIPIENT_FIRST_NAME, recipient.getFirstName())
                            .putString(Constants.KEY_RECIPIENT_LAST_NAME, recipient.getLastName())
                            .putString(Constants.KEY_RECIPIENT_MIDDLE_NAME, recipient.getMiddleName())
                            .putString(Constants.KEY_RECIPIENT_PHONE, recipient.getPhone())
                            .putString(Constants.KEY_RECIPIENT_ADDRESS, recipient.getAddress())
                            .putString(Constants.KEY_RECIPIENT_ZIP, recipient.getZip())
                            .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipient.getCountryId())
                            .putString(Constants.KEY_RECIPIENT_CITY_NAME, recipient.getCityName())
                            .putString(Constants.KEY_RECIPIENT_CARGOSTAR, recipient.getCargostarAccountNumber())
                            .putString(Constants.KEY_RECIPIENT_TNT, recipient.getTntAccountNumber())
                            .putString(Constants.KEY_RECIPIENT_FEDEX, recipient.getFedexAccountNumber())
                            .putString(Constants.KEY_RECIPIENT_SIGNATURE, recipientSignature)
                            .putString(Constants.KEY_RECIPIENT_PASSPORT, recipient.getPassportSerial())
                            .putString(Constants.KEY_RECIPIENT_INN, recipient.getInn())
                            .putString(Constants.KEY_RECIPIENT_COMPANY_NAME, recipient.getCompany())
                            .putInt(Constants.KEY_RECIPIENT_TYPE, recipient.getType())

                            .putLong(Constants.KEY_PAYER_ID, payerId)

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
