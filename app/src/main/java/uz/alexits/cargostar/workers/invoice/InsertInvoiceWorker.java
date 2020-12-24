package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.Date;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.transportation.Invoice;
import uz.alexits.cargostar.utils.Constants;

public class InsertInvoiceWorker extends Worker {
    private final long requestId;
    private final long invoiceId;
    private final String number;
    private final long providerId;
    private final long courierId;
    private final long tariffId;
    private final long senderId;
    private final long recipientId;
    private final String recipientSignatureUrl;
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

    private final String payerEmail;
    private final String payerFirstName;
    private final String payerLastName;
    private final String payerMiddleName;
    private final String payerPhone;
    private final String payerAddress;
    private final long payerCountryId;
    private final long payerRegionId;
    private final long payerCityId;
    private final String payerZip;
    private final String payerCompany;
    private final String payerCargo;
    private final String payerTnt;
    private final String payerFedex;
    private final String payerInn;
    private final String payerCheckingAccount;
    private final String payerBank;
    private final String payerRegistrationCode;
    private final String payerMfo;
    private final String payerOked;

    private final int consignmentQuantity;

    public InsertInvoiceWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
        this.number = getInputData().getString(Constants.KEY_NUMBER);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, -1L);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);
        this.tariffId = getInputData().getLong(Constants.KEY_TARIFF_ID, -1L);
        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1L);
        this.recipientId = getInputData().getLong(Constants.KEY_RECIPIENT_ID, -1L);
        this.recipientSignatureUrl = getInputData().getString(Constants.KEY_RECIPIENT_SIGNATURE);
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

        this.payerEmail = getInputData().getString(Constants.KEY_PAYER_EMAIL);
        this.payerFirstName = getInputData().getString(Constants.KEY_PAYER_FIRST_NAME);
        this.payerLastName = getInputData().getString(Constants.KEY_PAYER_LAST_NAME);
        this.payerMiddleName = getInputData().getString(Constants.KEY_PAYER_MIDDLE_NAME);
        this.payerPhone = getInputData().getString(Constants.KEY_PAYER_PHONE);
        this.payerAddress = getInputData().getString(Constants.KEY_PAYER_ADDRESS);
        this.payerCountryId = getInputData().getLong(Constants.KEY_PAYER_COUNTRY_ID, -1L);
        this.payerRegionId = getInputData().getLong(Constants.KEY_PAYER_REGION_ID, -1L);
        this.payerCityId = getInputData().getLong(Constants.KEY_PAYER_CITY_ID, -1L);
        this.payerZip = getInputData().getString(Constants.KEY_PAYER_ZIP);
        this.payerCompany = getInputData().getString(Constants.KEY_PAYER_COMPANY_NAME);
        this.payerCargo = getInputData().getString(Constants.KEY_PAYER_CARGOSTAR);
        this.payerTnt = getInputData().getString(Constants.KEY_PAYER_TNT);
        this.payerFedex = getInputData().getString(Constants.KEY_PAYER_FEDEX);

        this.payerInn = getInputData().getString(Constants.KEY_PAYER_INN);
        this.payerBank = getInputData().getString(Constants.KEY_PAYER_BANK);
        this.payerCheckingAccount = getInputData().getString(Constants.KEY_PAYER_CHECKING_ACCOUNT);
        this.payerRegistrationCode = getInputData().getString(Constants.KEY_PAYER_REGISTRATION_CODE);
        this.payerMfo = getInputData().getString(Constants.KEY_PAYER_MFO);
        this.payerOked = getInputData().getString(Constants.KEY_PAYER_OKED);

        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId <= 0) {
            Log.e(TAG, "insertInvoiceData(): invoiceId <= 0");
            return Result.failure();
        }
        if (providerId <= 0) {
            Log.e(TAG, "insertInvoiceData(): providerId <= 0");
            return Result.failure();
        }
        if (requestId <= 0) {
            Log.e(TAG, "insertInvoiceData(): requestId <= 0");
            return Result.failure();
        }
        if (recipientId <= 0) {
            Log.e(TAG, "insertInvoiceData(): recipientId <= 0");
            return Result.failure();
        }
        if (payerId <= 0) {
            Log.e(TAG, "insertInvoiceData(): payerId <= 0");
            return Result.failure();
        }
        try {
            final Invoice invoice = new Invoice(
                    invoiceId,
                    number,
                    senderId,
                    recipientId,
                    recipientSignatureUrl,
                    payerId,
                    providerId,
                    requestId,
                    tariffId,
                    price,
                    status,
                    createdAtTime > 0 ? new Date(createdAtTime) : null,
                    updatedAtTime > 0 ? new Date(updatedAtTime) : null);

            Log.i(TAG, "invoice: " + invoice);

            final long rowInserted = LocalCache.getInstance(getApplicationContext()).invoiceDao().insertInvoice(invoice);

            if (rowInserted == -1) {
                Log.e(TAG, "insertInvoiceData(): couldn't insert invoice " + invoice);
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
                    .putString(Constants.KEY_RECIPIENT_SIGNATURE, recipientSignatureUrl)
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

                    .putString(Constants.KEY_PAYER_EMAIL, payerEmail)
                    .putString(Constants.KEY_PAYER_FIRST_NAME, payerFirstName)
                    .putString(Constants.KEY_PAYER_LAST_NAME, payerLastName)
                    .putString(Constants.KEY_PAYER_MIDDLE_NAME, payerMiddleName)
                    .putString(Constants.KEY_PAYER_PHONE, payerPhone)
                    .putString(Constants.KEY_PAYER_ADDRESS, payerAddress)
                    .putLong(Constants.KEY_PAYER_COUNTRY_ID, payerCountryId)
                    .putLong(Constants.KEY_PAYER_REGION_ID, payerRegionId)
                    .putLong(Constants.KEY_PAYER_CITY_ID, payerCityId)
                    .putString(Constants.KEY_PAYER_ZIP, payerZip)
                    .putString(Constants.KEY_PAYER_COMPANY_NAME, payerCompany)
                    .putString(Constants.KEY_PAYER_CARGOSTAR, payerCargo)
                    .putString(Constants.KEY_PAYER_TNT, payerTnt)
                    .putString(Constants.KEY_PAYER_FEDEX, payerFedex)
                    .putString(Constants.KEY_PAYER_INN, payerInn)
                    .putString(Constants.KEY_PAYER_CHECKING_ACCOUNT, payerCheckingAccount)
                    .putString(Constants.KEY_PAYER_BANK, payerBank)
                    .putString(Constants.KEY_PAYER_REGISTRATION_CODE, payerRegistrationCode)
                    .putString(Constants.KEY_PAYER_MFO, payerMfo)
                    .putString(Constants.KEY_PAYER_OKED, payerOked)
                    .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                    .build();

            Log.i(TAG, "insertInvoiceData(): successfully inserted invoice " + invoice);
            return Result.success(outputData);
        }
        catch (Exception e) {
            Log.e(TAG, "doWork(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = InsertInvoiceWorker.class.toString();
}
