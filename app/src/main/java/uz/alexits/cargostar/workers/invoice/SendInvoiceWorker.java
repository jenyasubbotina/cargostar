package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.api.params.CreateInvoiceParams;
import uz.alexits.cargostar.api.params.CreateInvoiceResponse;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Serializer;

public class SendInvoiceWorker extends Worker {
    private final Gson gson;

    /* sender data */
    private final String senderEmail;
    private final String senderSignature;
    private final boolean isSenderSignatureLocal;
    private final String senderFirstName;
    private final String senderMiddleName;
    private final String senderLastName;
    private final String senderPhone;
    private final String senderCountryId;
    private final String senderCity;
    private final String senderAddress;
    private final String senderZip;
    private final String senderTnt;
    private final String senderFedex;
    private final String senderCompanyName;
    private final int senderType;

    /* recipient data */
    private final String recipientEmail;
    private final String recipientFirstName;
    private final String recipientMiddleName;
    private final String recipientLastName;
    private final String recipientPhone;
    private final String recipientAddress;
    private final String recipientCountryId;
    private final String recipientCity;
    private final String recipientZip;
    private final String recipientCargo;
    private final String recipientTnt;
    private final String recipientFedex;
    private final String recipientCompanyName;
    private final int recipientType;

    /* payer data */
    private final String payerEmail;
    private final String payerFirstName;
    private final String payerMiddleName;
    private final String payerLastName;
    private final String payerPhone;
    private final String payerAddress;
    private final String payerCountryId;
    private final String payerCity;
    private final String payerZip;
    private final String payerCargostar;
    private final String payerTnt;
    private final String payerFedex;
    private final String payerTntTaxId;
    private final String payerFedexTaxId;
    private final int payerType;

    /* account numbers */
    private final double discount;
    private final String payerInn;
    private final String payerCompanyName;
    private final String checkingAccount;
    private final String bank;
    private final String mfo;
    private final String oked;
    private final String registrationCode;

    /* invoice data */
    private final long requestId;
    private final long invoiceId;

    private final long courierId;
    private final String instructions;

    private final long providerId;
    private final long packagingId;
    private final int deliveryType;
    private final int paymentMethod;

    private final double totalWeight;
    private final double totalVolume;
    private final String totalPrice;

    private final String serializedConsignmentList;

    public SendInvoiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);

        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);

        this.senderSignature = getInputData().getString(Constants.KEY_SENDER_SIGNATURE);
        this.isSenderSignatureLocal = getInputData().getBoolean(Constants.KEY_IS_SENDER_SIGNATURE_LOCAL, true);
        this.senderEmail = getInputData().getString(Constants.KEY_SENDER_EMAIL);
        this.senderTnt = getInputData().getString(Constants.KEY_SENDER_TNT);
        this.senderFedex = getInputData().getString(Constants.KEY_SENDER_FEDEX);
        this.senderCountryId = getInputData().getString(Constants.KEY_SENDER_COUNTRY_ID);
        this.senderCity = getInputData().getString(Constants.KEY_SENDER_CITY_ID);
        this.senderAddress = getInputData().getString(Constants.KEY_SENDER_ADDRESS);
        this.senderZip = getInputData().getString(Constants.KEY_SENDER_ZIP);
        this.senderFirstName = getInputData().getString(Constants.KEY_SENDER_FIRST_NAME);
        this.senderMiddleName = getInputData().getString(Constants.KEY_SENDER_MIDDLE_NAME);
        this.senderLastName = getInputData().getString(Constants.KEY_SENDER_LAST_NAME);
        this.senderPhone = getInputData().getString(Constants.KEY_SENDER_PHONE);
        this.senderCompanyName = getInputData().getString(Constants.KEY_SENDER_COMPANY_NAME);
        this.senderType = getInputData().getInt(Constants.KEY_SENDER_TYPE, 0);

        this.recipientEmail = getInputData().getString(Constants.KEY_RECIPIENT_EMAIL);
        this.recipientCargo = getInputData().getString(Constants.KEY_RECIPIENT_CARGOSTAR);
        this.recipientTnt = getInputData().getString(Constants.KEY_RECIPIENT_TNT);
        this.recipientFedex = getInputData().getString(Constants.KEY_RECIPIENT_FEDEX);
        this.recipientCountryId = getInputData().getString(Constants.KEY_RECIPIENT_COUNTRY_ID);
        this.recipientCity = getInputData().getString(Constants.KEY_RECIPIENT_CITY_ID);
        this.recipientAddress = getInputData().getString(Constants.KEY_RECIPIENT_ADDRESS);
        this.recipientZip = getInputData().getString(Constants.KEY_RECIPIENT_ZIP);
        this.recipientFirstName = getInputData().getString(Constants.KEY_RECIPIENT_FIRST_NAME);
        this.recipientMiddleName = getInputData().getString(Constants.KEY_RECIPIENT_MIDDLE_NAME);
        this.recipientLastName = getInputData().getString(Constants.KEY_RECIPIENT_LAST_NAME);
        this.recipientPhone = getInputData().getString(Constants.KEY_RECIPIENT_PHONE);
        this.recipientCompanyName = getInputData().getString(Constants.KEY_RECIPIENT_COMPANY_NAME);
        this.recipientType = getInputData().getInt(Constants.KEY_RECIPIENT_TYPE, 0);

        this.payerEmail = getInputData().getString(Constants.KEY_PAYER_EMAIL);
        this.payerCountryId = getInputData().getString(Constants.KEY_PAYER_COUNTRY_ID);
        this.payerCity = getInputData().getString(Constants.KEY_PAYER_CITY_ID);
        this.payerAddress = getInputData().getString(Constants.KEY_PAYER_ADDRESS);
        this.payerZip = getInputData().getString(Constants.KEY_PAYER_ZIP);
        this.payerFirstName = getInputData().getString(Constants.KEY_PAYER_FIRST_NAME);
        this.payerMiddleName = getInputData().getString(Constants.KEY_PAYER_MIDDLE_NAME);
        this.payerLastName = getInputData().getString(Constants.KEY_PAYER_LAST_NAME);
        this.payerPhone = getInputData().getString(Constants.KEY_PAYER_PHONE);
        this.payerCargostar = getInputData().getString(Constants.KEY_PAYER_CARGOSTAR);
        this.payerTnt = getInputData().getString(Constants.KEY_PAYER_TNT);
        this.payerFedex = getInputData().getString(Constants.KEY_PAYER_FEDEX);

        this.payerTntTaxId = getInputData().getString(Constants.KEY_PAYER_TNT_TAX_ID);
        this.payerFedexTaxId = getInputData().getString(Constants.KEY_PAYER_FEDEX_TAX_ID);
        this.payerType = getInputData().getInt(Constants.KEY_PAYER_TYPE, 0);

        this.discount = getInputData().getDouble(Constants.KEY_DISCOUNT, -1);
        this.payerInn = getInputData().getString(Constants.KEY_PAYER_INN);
        this.payerCompanyName = getInputData().getString(Constants.KEY_PAYER_COMPANY_NAME);
        this.checkingAccount = getInputData().getString(Constants.KEY_PAYER_CHECKING_ACCOUNT);
        this.bank = getInputData().getString(Constants.KEY_PAYER_BANK);
        this.mfo = getInputData().getString(Constants.KEY_PAYER_MFO);
        this.oked = getInputData().getString(Constants.KEY_PAYER_OKED);
        this.registrationCode = getInputData().getString(Constants.KEY_PAYER_REGISTRATION_CODE);

        this.instructions = getInputData().getString(Constants.KEY_INSTRUCTIONS);

        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, -1L);
        this.packagingId = getInputData().getLong(Constants.KEY_PACKAGING_ID, -1L);
        this.deliveryType = getInputData().getInt(Constants.KEY_DELIVERY_TYPE, -1);
        this.paymentMethod = getInputData().getInt(Constants.KEY_PAYMENT_METHOD, -1);

        this.totalWeight = getInputData().getDouble(Constants.KEY_TOTAL_WEIGHT, -1);
        this.totalVolume = getInputData().getDouble(Constants.KEY_TOTAL_VOLUME, -1);
        this.totalPrice = getInputData().getString(Constants.KEY_TOTAL_PRICE);

        this.serializedConsignmentList = getInputData().getString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST);

        this.gson = new Gson();
    }

    @NonNull
    @Override
    public Result doWork() {
        final CreateInvoiceParams createInvoiceParams = new CreateInvoiceParams();

        if (requestId != -1L) {
            createInvoiceParams.setRequestId(requestId);
        }
        if (invoiceId != -1L) {
            createInvoiceParams.setInvoiceId(invoiceId);
        }

        createInvoiceParams.setCourierId(courierId);

        /* sender data */
        if (senderSignature != null && !TextUtils.isEmpty(senderSignature)) {
            final String signature = isSenderSignatureLocal ? senderSignature : Serializer.fileToBase64(senderSignature);
            createInvoiceParams.setSenderSignature(signature != null ? signature : senderSignature);
        }
        createInvoiceParams.setSenderEmail(senderEmail);
        createInvoiceParams.setSenderTntAccountNumber(senderTnt);
        createInvoiceParams.setSenderFedexAccountNumber(senderFedex);
        createInvoiceParams.setSenderAddress(senderAddress);
        createInvoiceParams.setSenderCountryId(senderCountryId);
        createInvoiceParams.setSenderCityName(senderCity);
        createInvoiceParams.setSenderZip(senderZip);
        createInvoiceParams.setSenderFirstName(senderFirstName);
        createInvoiceParams.setSenderMiddleName(senderMiddleName);
        createInvoiceParams.setSenderLastName(senderLastName);
        createInvoiceParams.setSenderPhone(senderPhone);
        createInvoiceParams.setSenderCompanyName(senderCompanyName);
        createInvoiceParams.setSenderType(senderType);

        /* recipient data */
        createInvoiceParams.setRecipientEmail(recipientEmail);
        createInvoiceParams.setRecipientCargostarAccountNumber(recipientCargo);
        createInvoiceParams.setRecipientTntAccountNumber(recipientTnt);
        createInvoiceParams.setRecipientFedexAccountNumber(recipientFedex);
        createInvoiceParams.setRecipientAddress(recipientAddress);
        createInvoiceParams.setRecipientCountryId(recipientCountryId);
        createInvoiceParams.setRecipientCityName(recipientCity);
        createInvoiceParams.setRecipientZip(recipientZip);
        createInvoiceParams.setRecipientFirstName(recipientFirstName);
        createInvoiceParams.setRecipientMiddleName(recipientMiddleName);
        createInvoiceParams.setRecipientLastName(recipientLastName);
        createInvoiceParams.setRecipientPhone(recipientPhone);
        createInvoiceParams.setRecipientCompanyName(recipientCompanyName);
        createInvoiceParams.setRecipientType(recipientType);

        /* payer data */
        createInvoiceParams.setPayerEmail(payerEmail);
        createInvoiceParams.setPayerCountryId(payerCountryId);
        createInvoiceParams.setPayerCityName(payerCity);
        createInvoiceParams.setPayerAddress(payerAddress);
        createInvoiceParams.setPayerZip(payerZip);
        createInvoiceParams.setPayerFirstName(payerFirstName);
        createInvoiceParams.setPayerMiddleName(payerMiddleName);
        createInvoiceParams.setPayerLastName(payerLastName);
        createInvoiceParams.setPayerPhone(payerPhone);
        createInvoiceParams.setDiscount(discount);
        createInvoiceParams.setPayerCargostarAccountNumber(payerCargostar);
        createInvoiceParams.setPayerTntAccountNumber(payerTnt);
        createInvoiceParams.setPayerFedexAccountNumber(payerFedex);
        createInvoiceParams.setPayerTntTaxId(payerTntTaxId);
        createInvoiceParams.setPayerFedexTaxId(payerFedexTaxId);
        createInvoiceParams.setPayerType(payerType);
        createInvoiceParams.setPayerInn(payerInn);
        createInvoiceParams.setPayerCompanyName(payerCompanyName);

        /* account data */
        createInvoiceParams.setCheckingAccount(checkingAccount);
        createInvoiceParams.setBank(bank);
        createInvoiceParams.setRegistrationCode(registrationCode);
        createInvoiceParams.setMfo(mfo);
        createInvoiceParams.setOked(oked);

        createInvoiceParams.setInstructions(instructions);

        createInvoiceParams.setProviderId(providerId);

        final List<Consignment> consignmentList = Serializer.deserializeConsignmentList(serializedConsignmentList);

        createInvoiceParams.setConsignmentList(consignmentList);

        /* calculation data */
        createInvoiceParams.setConsignmentQuantity(consignmentList != null ? consignmentList.size() : 0);
        createInvoiceParams.setTotalWeight(totalWeight);
        createInvoiceParams.setTotalVolume(totalVolume);
        createInvoiceParams.setTotalPrice(totalPrice);
        createInvoiceParams.setPackagingId(packagingId);
        createInvoiceParams.setDeliveryType(deliveryType);
        createInvoiceParams.setPaymentMethod(paymentMethod);

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
