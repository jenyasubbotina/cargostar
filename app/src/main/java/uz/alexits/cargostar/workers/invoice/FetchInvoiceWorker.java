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
    private final long invoiceId;

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

    private final int consignmentQuantity;

    public FetchInvoiceWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1);

        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1L);
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

        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
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
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
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

                            .putLong(Constants.KEY_REQUEST_ID, invoice.getRequestId() != null ? invoice.getRequestId() : -1L)
                            .putLong(Constants.KEY_INVOICE_ID, invoice.getId())
                            .putString(Constants.KEY_NUMBER, invoice.getNumber())
                            .putLong(Constants.KEY_PROVIDER_ID, invoice.getProviderId() != null ? invoice.getProviderId() : -1L)
                            .putLong(Constants.KEY_TARIFF_ID, invoice.getTariffId() != null ? invoice.getTariffId() : -1L)
                            .putLong(Constants.KEY_SENDER_ID, invoice.getSenderId() != null ? invoice.getSenderId() : -1L)
                            .putLong(Constants.KEY_RECIPIENT_ID, invoice.getRecipientId() != null ? invoice.getRecipientId() : -1L)
                            .putLong(Constants.KEY_PAYER_ID, invoice.getPayerId() != null ? invoice.getPayerId() : -1L)
                            .putDouble(Constants.KEY_PRICE, invoice.getPrice())
                            .putLong(Constants.KEY_STATUS, invoice.getStatus())
                            .putLong(Constants.KEY_CREATED_AT, invoice.getCreatedAt() != null ? invoice.getCreatedAt().getTime() : -1L)
                            .putLong(Constants.KEY_UPDATED_AT, invoice.getUpdatedAt() != null ? invoice.getUpdatedAt().getTime() : -1L)
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
