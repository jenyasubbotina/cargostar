package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.entities.params.CreateInvoiceParams;
import uz.alexits.cargostar.entities.params.CreateInvoiceResponse;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Serializer;

public class SendInvoiceWorker extends Worker {
    private final CreateInvoiceParams createInvoiceParams;

    public SendInvoiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.createInvoiceParams = new CreateInvoiceParams();
        this.createInvoiceParams.setRequestId(getInputData().getLong(Constants.KEY_REQUEST_ID, 0L));
        this.createInvoiceParams.setInvoiceId(getInputData().getLong(Constants.KEY_INVOICE_ID, 0L));
        this.createInvoiceParams.setCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));
        this.createInvoiceParams.setProviderId(getInputData().getLong(Constants.KEY_PROVIDER_ID, 0L));
        this.createInvoiceParams.setPackagingId(getInputData().getLong(Constants.KEY_PACKAGING_ID, 0L));
        this.createInvoiceParams.setDeliveryType(getInputData().getInt(Constants.KEY_DELIVERY_TYPE, 0));
        this.createInvoiceParams.setPaymentMethod(getInputData().getInt(Constants.KEY_PAYMENT_METHOD, 0));

        this.createInvoiceParams.setSenderEmail(getInputData().getString(Constants.KEY_SENDER_EMAIL));
        this.createInvoiceParams.setSenderTntAccountNumber(getInputData().getString(Constants.KEY_SENDER_TNT));
        this.createInvoiceParams.setSenderFedexAccountNumber(getInputData().getString(Constants.KEY_SENDER_FEDEX));
        this.createInvoiceParams.setSenderAddress(getInputData().getString(Constants.KEY_SENDER_ADDRESS));
        this.createInvoiceParams.setSenderCountryId(getInputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, 0L));
        this.createInvoiceParams.setSenderCityName(getInputData().getString(Constants.KEY_SENDER_CITY_NAME));
        this.createInvoiceParams.setSenderZip(getInputData().getString(Constants.KEY_SENDER_ZIP));
        this.createInvoiceParams.setSenderFirstName(getInputData().getString(Constants.KEY_SENDER_FIRST_NAME));
        this.createInvoiceParams.setSenderMiddleName(getInputData().getString(Constants.KEY_SENDER_MIDDLE_NAME));
        this.createInvoiceParams.setSenderLastName(getInputData().getString(Constants.KEY_SENDER_LAST_NAME));
        this.createInvoiceParams.setSenderPhone(getInputData().getString(Constants.KEY_SENDER_PHONE));
        this.createInvoiceParams.setSenderCompanyName(getInputData().getString(Constants.KEY_SENDER_COMPANY_NAME));

        this.createInvoiceParams.setRecipientEmail(getInputData().getString(Constants.KEY_RECIPIENT_EMAIL));
        this.createInvoiceParams.setRecipientCargostarAccountNumber(getInputData().getString(Constants.KEY_RECIPIENT_CARGOSTAR));
        this.createInvoiceParams.setRecipientTntAccountNumber(getInputData().getString(Constants.KEY_RECIPIENT_TNT));
        this.createInvoiceParams.setRecipientFedexAccountNumber(getInputData().getString(Constants.KEY_RECIPIENT_FEDEX));
        this.createInvoiceParams.setRecipientAddress(getInputData().getString(Constants.KEY_RECIPIENT_ADDRESS));
        this.createInvoiceParams.setRecipientCountryId(getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, 0L));
        this.createInvoiceParams.setRecipientCityName(getInputData().getString(Constants.KEY_RECIPIENT_CITY_NAME));
        this.createInvoiceParams.setRecipientZip(getInputData().getString(Constants.KEY_RECIPIENT_ZIP));
        this.createInvoiceParams.setRecipientFirstName(getInputData().getString(Constants.KEY_RECIPIENT_FIRST_NAME));
        this.createInvoiceParams.setRecipientMiddleName(getInputData().getString(Constants.KEY_RECIPIENT_MIDDLE_NAME));
        this.createInvoiceParams.setRecipientLastName(getInputData().getString(Constants.KEY_RECIPIENT_LAST_NAME));
        this.createInvoiceParams.setRecipientPhone(getInputData().getString(Constants.KEY_RECIPIENT_PHONE));
        this.createInvoiceParams.setRecipientCompanyName(getInputData().getString(Constants.KEY_RECIPIENT_COMPANY_NAME));

        this.createInvoiceParams.setPayerEmail(getInputData().getString(Constants.KEY_PAYER_EMAIL));
        this.createInvoiceParams.setPayerCountryId(getInputData().getLong(Constants.KEY_PAYER_COUNTRY_ID, 0L));
        this.createInvoiceParams.setPayerCityName(getInputData().getString(Constants.KEY_PAYER_CITY_NAME));
        this.createInvoiceParams.setPayerAddress(getInputData().getString(Constants.KEY_PAYER_ADDRESS));
        this.createInvoiceParams.setPayerZip(getInputData().getString(Constants.KEY_PAYER_ZIP));
        this.createInvoiceParams.setPayerFirstName(getInputData().getString(Constants.KEY_PAYER_FIRST_NAME));
        this.createInvoiceParams.setPayerMiddleName(getInputData().getString(Constants.KEY_PAYER_MIDDLE_NAME));
        this.createInvoiceParams.setPayerLastName(getInputData().getString(Constants.KEY_PAYER_LAST_NAME));
        this.createInvoiceParams.setPayerPhone(getInputData().getString(Constants.KEY_PAYER_PHONE));
        this.createInvoiceParams.setDiscount(getInputData().getDouble(Constants.KEY_DISCOUNT, 0));
        this.createInvoiceParams.setPayerCargostarAccountNumber(getInputData().getString(Constants.KEY_PAYER_CARGOSTAR));
        this.createInvoiceParams.setPayerTntAccountNumber(getInputData().getString(Constants.KEY_PAYER_TNT));
        this.createInvoiceParams.setPayerFedexAccountNumber(getInputData().getString(Constants.KEY_PAYER_FEDEX));
        this.createInvoiceParams.setPayerTntTaxId(getInputData().getString(Constants.KEY_PAYER_TNT_TAX_ID));
        this.createInvoiceParams.setPayerFedexTaxId(getInputData().getString(Constants.KEY_PAYER_FEDEX_TAX_ID));
        this.createInvoiceParams.setPayerInn(getInputData().getString(Constants.KEY_PAYER_INN));
        this.createInvoiceParams.setPayerCompanyName(getInputData().getString(Constants.KEY_PAYER_COMPANY_NAME));
        this.createInvoiceParams.setContractNumber(getInputData().getString(Constants.KEY_PAYER_CONTRACT_NUMBER));

        this.createInvoiceParams.setTotalWeight(getInputData().getDouble(Constants.KEY_TOTAL_WEIGHT, 0));
        this.createInvoiceParams.setTotalVolume(getInputData().getDouble(Constants.KEY_TOTAL_VOLUME, 0));
        this.createInvoiceParams.setTotalPrice(getInputData().getString(Constants.KEY_TOTAL_PRICE));
        this.createInvoiceParams.setInstructions(getInputData().getString(Constants.KEY_INSTRUCTIONS));

        createInvoiceParams.setSenderSignature(Serializer.fileToBase64(getInputData().getString(Constants.KEY_SENDER_SIGNATURE)) == null
                ? getInputData().getString(Constants.KEY_SENDER_SIGNATURE)
                : Serializer.fileToBase64(getInputData().getString(Constants.KEY_SENDER_SIGNATURE)));

        this.createInvoiceParams.setSenderType(!TextUtils.isEmpty(getInputData().getString(Constants.KEY_SENDER_COMPANY_NAME)) ? 2 : 1);
        this.createInvoiceParams.setRecipientType(!TextUtils.isEmpty(getInputData().getString(Constants.KEY_RECIPIENT_COMPANY_NAME)) ? 2 : 1);
        this.createInvoiceParams.setPayerType(!TextUtils.isEmpty(getInputData().getString(Constants.KEY_PAYER_COMPANY_NAME)) ? 2 : 1);

        final List<Consignment> consignmentList = Serializer.deserializeConsignmentList(getInputData().getString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST));
        this.createInvoiceParams.setConsignmentList(consignmentList);
        this.createInvoiceParams.setConsignmentQuantity(consignmentList != null ? consignmentList.size() : 0);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<CreateInvoiceResponse> response = RetrofitClient.getInstance(getApplicationContext()).createInvoice(createInvoiceParams);
            if (response.code() == 200 || response.code() == 201) {
                if (response.isSuccessful()) {
                    final CreateInvoiceResponse createInvoiceResponse = response.body();

                    if (createInvoiceResponse == null) {
                        Log.e(TAG, "sendInvoice(): null response");
                        return Result.failure();
                    }
                    return ListenableWorker.Result.success();
                }
                Log.e(TAG, "sendInvoice(): response NOT successful");
                return Result.failure();
            }
            Log.e(TAG, "sendInvoice(): " + response.errorBody());
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "sendInvoice(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = SendInvoiceWorker.class.toString();
}
