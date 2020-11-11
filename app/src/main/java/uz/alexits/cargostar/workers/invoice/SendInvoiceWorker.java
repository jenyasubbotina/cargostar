package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.api.params.CreateInvoiceParams;
import uz.alexits.cargostar.api.params.CreateInvoiceResponse;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.shipping.Consignment;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.utils.Constants;

public class SendInvoiceWorker extends Worker {
    private final Gson gson;

    private final long courierId;
    private final long operatorId;
    private final long accountantId;

    private final String senderSignature;
    private final String senderEmail;
    private final String senderCargostar;
    private final String senderTnt;
    private final String senderFedex;
    private final long senderCountryId;
    private final long senderRegionId;
    private final long senderCityId;
    private final String senderAddress;
    private final String senderZip;
    private final String senderFirstName;
    private final String senderMiddleName;
    private final String senderLastName;
    private final String senderPhone;

    private final String recipientSignature;
    private final String recipientEmail;
    private final String recipientCargo;
    private final String recipientTnt;
    private final String recipientFedex;
    private final long recipientCountryId;
    private final long recipientRegionId;
    private final long recipientCityId;
    private final String recipientAddress;
    private final String recipientZip;
    private final String recipientFirstName;
    private final String recipientMiddleName;
    private final String recipientLastName;
    private final String recipientPhone;

    private final String payerEmail;
    private final long payerCountryId;
    private final long payerRegionId;
    private final long payerCityId;
    private final String payerAddress;
    private final String payerZip;
    private final String payerFirstName;
    private final String payerMiddleName;
    private final String payerLastName;
    private final String payerPhone;
    private final String payerCargostar;
    private final String payerTnt;
    private final String payerFedex;
    private final double discount;

    private final String checkingAccount;
    private final String bank;
    private final String mfo;
    private final String oked;
    private final String registrationCode;

    private final String transportationQr;
    private final String instructions;

    private final long providerId;
    private final long packagingId;
    private final int deliveryType;
    private final int paymentMethod;

    private final double totalWeight;
    private final double totalVolume;
    private final double totalPrice;

    private final String serializedConsignmentList;

    public SendInvoiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);
        this.operatorId = getInputData().getLong(Constants.KEY_OPERATOR_ID, -1L);
        this.accountantId = getInputData().getLong(Constants.KEY_ACCOUNTANT_ID, -1L);

        this.senderSignature = getInputData().getString(Constants.KEY_SENDER_SIGNATURE);
        this.senderEmail = getInputData().getString(Constants.KEY_SENDER_EMAIL);
        this.senderCargostar = getInputData().getString(Constants.KEY_SENDER_CARGOSTAR);
        this.senderTnt = getInputData().getString(Constants.KEY_SENDER_TNT);
        this.senderFedex = getInputData().getString(Constants.KEY_SENDER_FEDEX);
        this.senderCountryId = getInputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L);
        this.senderRegionId = getInputData().getLong(Constants.KEY_SENDER_REGION_ID, -1L);
        this.senderCityId = getInputData().getLong(Constants.KEY_SENDER_CITY_ID, -1L);
        this.senderAddress = getInputData().getString(Constants.KEY_SENDER_ADDRESS);
        this.senderZip = getInputData().getString(Constants.KEY_SENDER_ZIP);
        this.senderFirstName = getInputData().getString(Constants.KEY_SENDER_FIRST_NAME);
        this.senderMiddleName = getInputData().getString(Constants.KEY_SENDER_MIDDLE_NAME);
        this.senderLastName = getInputData().getString(Constants.KEY_SENDER_LAST_NAME);
        this.senderPhone = getInputData().getString(Constants.KEY_SENDER_PHONE);

        this.recipientSignature = getInputData().getString(Constants.KEY_RECIPIENT_SIGNATURE);
        this.recipientEmail = getInputData().getString(Constants.KEY_RECIPIENT_EMAIL);
        this.recipientCargo = getInputData().getString(Constants.KEY_RECIPIENT_CARGOSTAR);
        this.recipientTnt = getInputData().getString(Constants.KEY_RECIPIENT_TNT);
        this.recipientFedex = getInputData().getString(Constants.KEY_RECIPIENT_FEDEX);
        this.recipientCountryId = getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
        this.recipientRegionId = getInputData().getLong(Constants.KEY_RECIPIENT_REGION_ID, -1L);
        this.recipientCityId = getInputData().getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L);
        this.recipientAddress = getInputData().getString(Constants.KEY_RECIPIENT_ADDRESS);
        this.recipientZip = getInputData().getString(Constants.RECIPIENT_ZIP);
        this.recipientFirstName = getInputData().getString(Constants.KEY_RECIPIENT_FIRST_NAME);
        this.recipientMiddleName = getInputData().getString(Constants.KEY_RECIPIENT_MIDDLE_NAME);
        this.recipientLastName = getInputData().getString(Constants.KEY_RECIPIENT_LAST_NAME);
        this.recipientPhone = getInputData().getString(Constants.KEY_RECIPIENT_PHONE);

        this.payerEmail = getInputData().getString(Constants.KEY_PAYER_EMAIL);
        this.payerCountryId = getInputData().getLong(Constants.KEY_PAYER_COUNTRY_ID, -1L);
        this.payerRegionId = getInputData().getLong(Constants.KEY_PAYER_REGION_ID, -1L);
        this.payerCityId = getInputData().getLong(Constants.KEY_PAYER_CITY_ID, -1L);
        this.payerAddress = getInputData().getString(Constants.KEY_PAYER_ADDRESS);
        this.payerZip = getInputData().getString(Constants.KEY_PAYER_ZIP);
        this.payerFirstName = getInputData().getString(Constants.KEY_PAYER_FIRST_NAME);
        this.payerMiddleName = getInputData().getString(Constants.KEY_PAYER_MIDDLE_NAME);
        this.payerLastName = getInputData().getString(Constants.KEY_PAYER_LAST_NAME);
        this.payerPhone = getInputData().getString(Constants.KEY_PAYER_PHONE);
        this.payerCargostar = getInputData().getString(Constants.KEY_PAYER_CARGOSTAR);
        this.payerTnt = getInputData().getString(Constants.KEY_PAYER_TNT);
        this.payerFedex = getInputData().getString(Constants.KEY_PAYER_FEDEX);
        this.discount = getInputData().getDouble(Constants.KEY_DISCOUNT, -1);

        this.checkingAccount = getInputData().getString(Constants.KEY_CHECKING_ACCOUNT);
        this.bank = getInputData().getString(Constants.KEY_BANK);
        this.mfo = getInputData().getString(Constants.KEY_MFO);
        this.oked = getInputData().getString(Constants.KEY_OKED);
        this.registrationCode = getInputData().getString(Constants.KEY_REGISTRATION_CODE);

        this.transportationQr = getInputData().getString(Constants.KEY_TRANSPORTATION_QR);
        this.instructions = getInputData().getString(Constants.KEY_INSTRUCTIONS);

        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, -1L);
        this.packagingId = getInputData().getLong(Constants.KEY_PACKAGING_ID, -1L);
        this.deliveryType = getInputData().getInt(Constants.KEY_DELIVERY_TYPE, -1);
        this.paymentMethod = getInputData().getInt(Constants.KEY_PAYMENT_METHOD, -1);

        this.totalWeight = getInputData().getDouble(Constants.KEY_TOTAL_WEIGHT, -1);
        this.totalVolume = getInputData().getDouble(Constants.KEY_TOTAL_VOLUME, -1);
        this.totalPrice = getInputData().getDouble(Constants.KEY_TOTAL_PRICE, -1);

        this.serializedConsignmentList = getInputData().getString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST);

        this.gson = new Gson();
    }

    @NonNull
    @Override
    public Result doWork() {
        final CreateInvoiceParams createInvoiceParams = new CreateInvoiceParams();
        createInvoiceParams.setCourierId(courierId);
        createInvoiceParams.setOperatorId(operatorId);
        createInvoiceParams.setAccountantId(accountantId);

        /* sender data */
        createInvoiceParams.setSenderSignature(senderSignature);
        createInvoiceParams.setSenderEmail(senderEmail);
        createInvoiceParams.setSenderCargostarAccountNumber(senderCargostar);
        createInvoiceParams.setSenderTntAccountNumber(senderTnt);
        createInvoiceParams.setSenderFedexAccountNumber(senderFedex);
        createInvoiceParams.setSenderAddress(senderAddress);
        createInvoiceParams.setSenderCountryId(senderCountryId);
        createInvoiceParams.setSenderRegionId(senderRegionId);
        createInvoiceParams.setSenderCityId(senderCityId);
        createInvoiceParams.setSenderZip(senderZip);
        createInvoiceParams.setSenderFirstName(senderFirstName);
        createInvoiceParams.setSenderMiddleName(senderMiddleName);
        createInvoiceParams.setSenderLastName(senderLastName);
        createInvoiceParams.setSenderPhone(senderPhone);

        /* recipient data */
        createInvoiceParams.setRecipientSignature(recipientSignature);
        createInvoiceParams.setRecipientEmail(recipientEmail);
        createInvoiceParams.setRecipientCargostarAccountNumber(recipientCargo);
        createInvoiceParams.setRecipientTntAccountNumber(recipientTnt);
        createInvoiceParams.setRecipientFedexAccountNumber(recipientFedex);
        createInvoiceParams.setRecipientAddress(recipientAddress);
        createInvoiceParams.setRecipientCountryId(recipientCountryId);
        createInvoiceParams.setRecipientRegionId(recipientRegionId);
        createInvoiceParams.setRecipientCityId(recipientCityId);
        createInvoiceParams.setRecipientZip(recipientZip);
        createInvoiceParams.setRecipientFirstName(recipientFirstName);
        createInvoiceParams.setRecipientMiddleName(recipientMiddleName);
        createInvoiceParams.setRecipientLastName(recipientLastName);
        createInvoiceParams.setRecipientPhone(recipientPhone);

        /* payer data */
        createInvoiceParams.setPayerEmail(payerEmail);
        createInvoiceParams.setPayerCountryId(payerCountryId);
        createInvoiceParams.setPayerRegionId(payerRegionId);
        createInvoiceParams.setPayerCityId(payerCityId);
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

        /* account data */
        createInvoiceParams.setCheckingAccount(checkingAccount);
        createInvoiceParams.setBank(bank);
        createInvoiceParams.setRegistrationCode(registrationCode);
        createInvoiceParams.setMfo(mfo);
        createInvoiceParams.setOked(oked);

        /* transportation data */
        createInvoiceParams.setTransportationQr(transportationQr);
        createInvoiceParams.setInstructions(instructions);

        createInvoiceParams.setProviderId(providerId);

        final List<Consignment> consignmentList = deserializeConsignmentList(serializedConsignmentList);

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

    private List<Consignment> deserializeConsignmentList(final String serializedConsignmentList) {
        if (serializedConsignmentList == null) {
            return null;
        }
        return gson.fromJson(serializedConsignmentList, new TypeToken<List<Consignment>>(){}.getType());
    }

    private static final String TAG = SendInvoiceWorker.class.toString();
}
