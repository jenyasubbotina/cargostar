package uz.alexits.cargostar.workers.invoice;

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
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Invoice;
import uz.alexits.cargostar.utils.Constants;

public class FetchInvoiceWorker extends Worker {
    private final long requestId;
    private final long courierId;
    private final long providerId;
    private final long invoiceId;
    private final int deliveryType;
    private final String paymentStatus;
    private final int consignmentQuantity;
    private final String comment;

    private final long recipientCountryId;
    private final String recipientCityName;
    private final String recipientCity;

    private final long senderId;
    private final long senderUserId;
    private final String senderEmail;
    private final String senderFirstName;
    private final String senderLastName;
    private final String senderMiddleName;
    private final String senderPhone;
    private final String senderAddress;
    private final long senderCountryId;
    private final long senderCityId;
    private final String senderCityName;
    private final String senderZip;
    private final String senderCargo;
    private final String senderTnt;
    private final String senderFedex;
    private final int senderDiscount;
    private final String senderInn;
    private final String senderCompany;
    private final String senderPassport;
    private final String senderPhoto;
    private final String senderSignature;
    private final int senderType;

    public FetchInvoiceWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, -1L);
        this.deliveryType = getInputData().getInt(Constants.KEY_DELIVERY_TYPE, 0);
        this.paymentStatus = getInputData().getString(Constants.KEY_PAYMENT_STATUS);
        this.comment = getInputData().getString(Constants.KEY_COMMENT);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);

        this.recipientCountryId = getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
        this.recipientCityName = getInputData().getString(Constants.KEY_RECIPIENT_CITY_NAME);
        this.recipientCity = getInputData().getString(Constants.KEY_RECIPIENT_CITY);

        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1L);
        this.senderUserId = getInputData().getLong(Constants.KEY_SENDER_USER_ID, -1L);
        this.senderEmail = getInputData().getString(Constants.KEY_SENDER_EMAIL);
        this.senderSignature = getInputData().getString(Constants.KEY_SENDER_SIGNATURE);
        this.senderFirstName = getInputData().getString(Constants.KEY_SENDER_FIRST_NAME);
        this.senderLastName = getInputData().getString(Constants.KEY_SENDER_LAST_NAME);
        this.senderMiddleName = getInputData().getString(Constants.KEY_SENDER_MIDDLE_NAME);
        this.senderPhone = getInputData().getString(Constants.KEY_SENDER_PHONE);
        this.senderAddress = getInputData().getString(Constants.KEY_SENDER_ADDRESS);
        this.senderCountryId = getInputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L);
        this.senderCityId = getInputData().getLong(Constants.KEY_SENDER_CITY_ID, -1L);
        this.senderCityName = getInputData().getString(Constants.KEY_SENDER_CITY_NAME);
        this.senderZip = getInputData().getString(Constants.KEY_SENDER_ZIP);
        this.senderCargo = getInputData().getString(Constants.KEY_SENDER_CARGOSTAR);
        this.senderTnt = getInputData().getString(Constants.KEY_SENDER_TNT);
        this.senderFedex = getInputData().getString(Constants.KEY_SENDER_FEDEX);
        this.senderDiscount = getInputData().getInt(Constants.KEY_DISCOUNT, 0);
        this.senderInn = getInputData().getString(Constants.KEY_SENDER_INN);
        this.senderCompany = getInputData().getString(Constants.KEY_SENDER_COMPANY_NAME);
        this.senderPhoto = getInputData().getString(Constants.KEY_SENDER_PHOTO);
        this.senderPassport = getInputData().getString(Constants.KEY_SENDER_PASSPORT);
        this.senderType = getInputData().getInt(Constants.KEY_SENDER_TYPE, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId < 0) {
            Log.e(TAG, "fetchInvoice(): invoiceId < 0");
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Invoice> response = RetrofitClient.getInstance(getApplicationContext()).getInvoice(invoiceId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchInvoice(): response=" + response.body());
                    final Invoice invoice = response.body();

                    if (invoice == null) {
                        Log.e(TAG, "fetchInvoice(): invoice is NULL");
                        return Result.failure();
                    }
                    Log.i(TAG, "fetchInvoice(): successfully received invoice " + invoice);

                    final Data outputData = new Data.Builder()
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
                            .putLong(Constants.KEY_SENDER_CITY_ID, senderCityId)
                            .putString(Constants.KEY_SENDER_CITY_NAME, senderCityName)
                            .putString(Constants.KEY_SENDER_CARGOSTAR, senderCargo)
                            .putString(Constants.KEY_SENDER_TNT, senderTnt)
                            .putString(Constants.KEY_SENDER_FEDEX, senderFedex)
                            .putInt(Constants.KEY_DISCOUNT, senderDiscount)
                            .putString(Constants.KEY_SENDER_PASSPORT, senderPassport)
                            .putString(Constants.KEY_SENDER_COMPANY_NAME, senderCompany)
                            .putString(Constants.KEY_SENDER_INN, senderInn)
                            .putString(Constants.KEY_SENDER_PHOTO, senderPhoto)
                            .putInt(Constants.KEY_SENDER_TYPE, senderType)

                            .putLong(Constants.KEY_REQUEST_ID, requestId)
                            .putLong(Constants.KEY_INVOICE_ID, invoice.getId())
                            .putString(Constants.KEY_NUMBER, invoice.getNumber())
                            .putLong(Constants.KEY_PROVIDER_ID, providerId)
                            .putLong(Constants.KEY_COURIER_ID, courierId)
                            .putLong(Constants.KEY_TARIFF_ID, invoice.getTariffId() != null ? invoice.getTariffId() : -1L)
                            .putDouble(Constants.KEY_PRICE, invoice.getPrice())
                            .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                            .putString(Constants.KEY_PAYMENT_STATUS, paymentStatus)
                            .putString(Constants.KEY_COMMENT, comment)

                            .putLong(Constants.KEY_STATUS, invoice.getStatus())
                            .putLong(Constants.KEY_CREATED_AT, invoice.getCreatedAt() != null ? invoice.getCreatedAt().getTime() : -1L)
                            .putLong(Constants.KEY_UPDATED_AT, invoice.getUpdatedAt() != null ? invoice.getUpdatedAt().getTime() : -1L)

                            .putLong(Constants.KEY_RECIPIENT_ID, invoice.getRecipientId() != null ? invoice.getRecipientId() : -1L)
                            .putString(Constants.KEY_RECIPIENT_SIGNATURE, invoice.getRecipientSignatureUrl())
                            .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                            .putString(Constants.KEY_RECIPIENT_CITY_NAME, recipientCityName)
                            .putString(Constants.KEY_RECIPIENT_CITY, recipientCity)
                            .putLong(Constants.KEY_PAYER_ID, invoice.getPayerId() != null ? invoice.getPayerId() : -1L)

                            .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                            .build();
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

    private static final String TAG = FetchInvoiceWorker.class.toString();
}
