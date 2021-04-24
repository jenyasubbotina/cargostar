package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;

public class FetchConsignmentListByInvoiceIdWorker extends Worker {
    private final long requestId;
    private final long invoiceId;
    private final String number;
    private final long providerId;
    private final long tariffId;
    private final long courierId;
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
    private final int senderDiscount;
    private final String senderPassport;
    private final String senderInn;
    private final String senderCompany;
    private final String senderPhoto;
    private final String senderSignature;
    private final int senderType;

    private final long recipientId;
    private final long recipientUserId;
    private final String recipientEmail;
    private final String recipientFirstName;
    private final String recipientLastName;
    private final String recipientMiddleName;
    private final String recipientPhone;
    private final String recipientAddress;
    private final String recipientZip;
    private final long recipientCountryId;
    private final String recipientCityName;
    private final String recipientCargo;
    private final String recipientTnt;
    private final String recipientFedex;
    private final String recipientPassport;
    private final String recipientInn;
    private final String recipientCompany;
    private final String recipientSignature;
    private final int recipientType;

    private final long payerId;
    private final long payerUserId;
    private final String payerEmail;
    private final String payerFirstName;
    private final String payerLastName;
    private final String payerMiddleName;
    private final String payerPhone;
    private final String payerAddress;
    private final String payerZip;
    private final long payerCountryId;
    private final String payerCityName;
    private final String payerCargo;
    private final String payerTnt;
    private final String payerFedex;
    private final String payerInn;
    private final String payerCompany;
    private final String payerContractNumber;
    private final String payerPassport;
    private final int payerType;

    private final int consignmentQuantity;

    public FetchConsignmentListByInvoiceIdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        this.senderDiscount = getInputData().getInt(Constants.KEY_DISCOUNT, 0);
        this.senderSignature = getInputData().getString(Constants.KEY_SENDER_SIGNATURE);
        this.senderPassport = getInputData().getString(Constants.KEY_SENDER_PASSPORT);
        this.senderInn = getInputData().getString(Constants.KEY_SENDER_INN);
        this.senderCompany = getInputData().getString(Constants.KEY_SENDER_COMPANY_NAME);
        this.senderPhoto = getInputData().getString(Constants.KEY_SENDER_PHOTO);
        this.senderType = getInputData().getInt(Constants.KEY_SENDER_TYPE, 0);

        this.recipientId = getInputData().getLong(Constants.KEY_RECIPIENT_ID, 0L);
        this.recipientUserId = getInputData().getLong(Constants.KEY_RECIPIENT_USER_ID, 0L);
        this.recipientEmail = getInputData().getString(Constants.KEY_RECIPIENT_EMAIL);
        this.recipientFirstName = getInputData().getString(Constants.KEY_RECIPIENT_FIRST_NAME);
        this.recipientLastName = getInputData().getString(Constants.KEY_RECIPIENT_LAST_NAME);
        this.recipientMiddleName = getInputData().getString(Constants.KEY_RECIPIENT_MIDDLE_NAME);
        this.recipientPhone = getInputData().getString(Constants.KEY_RECIPIENT_PHONE);
        this.recipientAddress = getInputData().getString(Constants.KEY_RECIPIENT_ADDRESS);
        this.recipientZip = getInputData().getString(Constants.KEY_RECIPIENT_ZIP);
        this.recipientCountryId = getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, 0L);
        this.recipientCityName = getInputData().getString(Constants.KEY_RECIPIENT_CITY_NAME);
        this.recipientCargo = getInputData().getString(Constants.KEY_RECIPIENT_CARGOSTAR);
        this.recipientTnt = getInputData().getString(Constants.KEY_RECIPIENT_TNT);
        this.recipientFedex = getInputData().getString(Constants.KEY_RECIPIENT_FEDEX);
        this.recipientSignature = getInputData().getString(Constants.KEY_RECIPIENT_SIGNATURE);
        this.recipientPassport = getInputData().getString(Constants.KEY_RECIPIENT_PASSPORT);
        this.recipientInn = getInputData().getString(Constants.KEY_RECIPIENT_INN);
        this.recipientCompany = getInputData().getString(Constants.KEY_RECIPIENT_COMPANY_NAME);
        this.recipientType = getInputData().getInt(Constants.KEY_RECIPIENT_TYPE, 0);

        this.payerId = getInputData().getLong(Constants.KEY_PAYER_ID, 0L);
        this.payerUserId = getInputData().getLong(Constants.KEY_PAYER_USER_ID, 0L);
        this.payerEmail = getInputData().getString(Constants.KEY_PAYER_EMAIL);
        this.payerFirstName = getInputData().getString(Constants.KEY_PAYER_FIRST_NAME);
        this.payerLastName = getInputData().getString(Constants.KEY_PAYER_LAST_NAME);
        this.payerMiddleName = getInputData().getString(Constants.KEY_PAYER_MIDDLE_NAME);
        this.payerPhone = getInputData().getString(Constants.KEY_PAYER_PHONE);
        this.payerAddress = getInputData().getString(Constants.KEY_PAYER_ADDRESS);
        this.payerCountryId = getInputData().getLong(Constants.KEY_PAYER_COUNTRY_ID, 0L);
        this.payerCityName = getInputData().getString(Constants.KEY_PAYER_CITY_NAME);
        this.payerZip = getInputData().getString(Constants.KEY_PAYER_ZIP);
        this.payerCargo = getInputData().getString(Constants.KEY_PAYER_CARGOSTAR);
        this.payerTnt = getInputData().getString(Constants.KEY_PAYER_TNT);
        this.payerFedex = getInputData().getString(Constants.KEY_PAYER_FEDEX);
        this.payerPassport = getInputData().getString(Constants.KEY_PAYER_PASSPORT);
        this.payerType = getInputData().getInt(Constants.KEY_PAYER_TYPE, 0);
        this.payerInn = getInputData().getString(Constants.KEY_PAYER_INN);
        this.payerCompany = getInputData().getString(Constants.KEY_PAYER_COMPANY_NAME);
        this.payerContractNumber = getInputData().getString(Constants.KEY_PAYER_CONTRACT_NUMBER);

        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (invoiceId <= 0) {
            Log.e(TAG, "fetchCargoList(): invoiceId <= 0");
            return Result.failure();
        }

        final Data.Builder outputDataBuilder = new Data.Builder()
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
                .putInt(Constants.KEY_DISCOUNT, senderDiscount)
                .putString(Constants.KEY_SENDER_PASSPORT, senderPassport)
                .putString(Constants.KEY_SENDER_COMPANY_NAME, senderCompany)
                .putString(Constants.KEY_SENDER_INN, senderInn)
                .putString(Constants.KEY_SENDER_PHOTO, senderPhoto)
                .putInt(Constants.KEY_SENDER_TYPE, senderType)

                .putLong(Constants.KEY_RECIPIENT_ID, recipientId)
                .putLong(Constants.KEY_RECIPIENT_USER_ID, recipientUserId)
                .putString(Constants.KEY_RECIPIENT_EMAIL, recipientEmail)
                .putString(Constants.KEY_RECIPIENT_FIRST_NAME, recipientFirstName)
                .putString(Constants.KEY_RECIPIENT_LAST_NAME, recipientLastName)
                .putString(Constants.KEY_RECIPIENT_MIDDLE_NAME, recipientMiddleName)
                .putString(Constants.KEY_RECIPIENT_PHONE, recipientPhone)
                .putString(Constants.KEY_RECIPIENT_ADDRESS, recipientAddress)
                .putString(Constants.KEY_RECIPIENT_ZIP, recipientZip)
                .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                .putString(Constants.KEY_RECIPIENT_CITY_NAME, recipientCityName)
                .putString(Constants.KEY_RECIPIENT_CARGOSTAR, recipientCargo)
                .putString(Constants.KEY_RECIPIENT_TNT, recipientTnt)
                .putString(Constants.KEY_RECIPIENT_FEDEX, recipientFedex)
                .putString(Constants.KEY_RECIPIENT_SIGNATURE, recipientSignature)
                .putString(Constants.KEY_RECIPIENT_PASSPORT, recipientPassport)
                .putString(Constants.KEY_RECIPIENT_INN, recipientInn)
                .putString(Constants.KEY_RECIPIENT_COMPANY_NAME, recipientCompany)
                .putInt(Constants.KEY_RECIPIENT_TYPE, recipientType)

                .putLong(Constants.KEY_PAYER_ID, payerId)
                .putLong(Constants.KEY_PAYER_USER_ID, payerUserId)
                .putString(Constants.KEY_PAYER_EMAIL, payerEmail)
                .putString(Constants.KEY_PAYER_FIRST_NAME, payerFirstName)
                .putString(Constants.KEY_PAYER_LAST_NAME, payerLastName)
                .putString(Constants.KEY_PAYER_MIDDLE_NAME, payerMiddleName)
                .putString(Constants.KEY_PAYER_PHONE, payerPhone)
                .putString(Constants.KEY_PAYER_ADDRESS, payerAddress)
                .putString(Constants.KEY_PAYER_ZIP, payerZip)
                .putLong(Constants.KEY_PAYER_COUNTRY_ID, payerCountryId)
                .putString(Constants.KEY_PAYER_CITY_NAME, payerCityName)
                .putString(Constants.KEY_PAYER_CARGOSTAR, payerCargo)
                .putString(Constants.KEY_PAYER_TNT, payerTnt)
                .putString(Constants.KEY_PAYER_FEDEX, payerFedex)
                .putString(Constants.KEY_PAYER_CONTRACT_NUMBER, payerContractNumber)
                .putString(Constants.KEY_PAYER_PASSPORT, payerPassport)
                .putString(Constants.KEY_PAYER_INN, payerInn)
                .putString(Constants.KEY_PAYER_COMPANY_NAME, payerCompany)
                .putInt(Constants.KEY_PAYER_TYPE, payerType)

                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity);
        if (consignmentQuantity <= 0) {
            Log.i(TAG, "fetchCargoList(): consignmentQuantity is 0");
            return Result.success(outputDataBuilder.build());
        }
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<List<Consignment>> response = RetrofitClient.getInstance(getApplicationContext()).getCargoListByInvoiceId(invoiceId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchCargoList(): response=" + response.body());

                    final List<Consignment> consignmentList = response.body();

                    LocalCache.getInstance(getApplicationContext()).invoiceDao().insertCargoList(consignmentList);

                    final String serializedConsignmentList = new Gson().toJson(consignmentList);
                    outputDataBuilder.putString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST, serializedConsignmentList);

                    return ListenableWorker.Result.success(outputDataBuilder.build());
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

    private static final String TAG = FetchConsignmentListByInvoiceIdWorker.class.toString();
}
