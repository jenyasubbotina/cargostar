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

public class FetchConsignmentListByRequestIdWorker extends Worker {
    private final long senderId;
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

    private final long requestId;
    private final long courierId;
    private final long providerId;
    private final long invoiceId;
    private final long userId;
    private final long recipientCountryId;
    private final long recipientCityId;
    private final String recipientCity;
    private final int deliveryType;
    private final String paymentStatus;
    private final int consignmentQuantity;
    private final String comment;

    public FetchConsignmentListByRequestIdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID,-1L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
        this.userId = getInputData().getLong(Constants.KEY_USER_ID, -1L);
        this.recipientCountryId = getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
        this.recipientCityId = getInputData().getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L);
        this.recipientCity = getInputData().getString(Constants.KEY_RECIPIENT_CITY);
        this.deliveryType = getInputData().getInt(Constants.KEY_DELIVERY_TYPE, 0);
        this.paymentStatus = getInputData().getString(Constants.KEY_PAYMENT_STATUS);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
        this.comment = getInputData().getString(Constants.KEY_COMMENT);

        Log.i(TAG, "FetchConsignmentListByRequestIdWorker(): count=" + consignmentQuantity);

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
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (requestId <= 0) {
            Log.e(TAG, "fetchCargoList(): requestId <= 0");
            return ListenableWorker.Result.failure();
        }
        if (consignmentQuantity <= 0) {
            Log.e(TAG, "fetchCargoList(): consignmentQuantity <= 0");
            return ListenableWorker.Result.failure();
        }

        final Data.Builder outputDataBuilder = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_COURIER_ID, courierId)
                .putLong(Constants.KEY_PROVIDER_ID, providerId)
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putLong(Constants.KEY_USER_ID, userId)
                .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                .putLong(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId)
                .putString(Constants.KEY_RECIPIENT_CITY, recipientCity)
                .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                .putString(Constants.KEY_PAYMENT_STATUS, paymentStatus)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                .putString(Constants.KEY_COMMENT, comment)
                .putLong(Constants.KEY_SENDER_ID, senderId)
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
                .putInt(Constants.KEY_DISCOUNT, discount);
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<List<Consignment>> response = RetrofitClient.getInstance(getApplicationContext()).getCargoListByRequestId(requestId);

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
                Log.e(TAG, "fetchCargoList(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchCargoList(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchConsignmentListByRequestIdWorker.class.toString();
}
