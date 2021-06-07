package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.database.dao.InvoiceDao;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.actor.FetchPayerWorker;
import uz.alexits.cargostar.workers.actor.FetchRecipientWorker;
import uz.alexits.cargostar.workers.actor.FetchSenderWorker;
import uz.alexits.cargostar.workers.invoice.FetchConsignmentListByRequestIdWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceWorker;
import uz.alexits.cargostar.workers.invoice.SendInvoiceWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestDataWorker;
import uz.alexits.cargostar.workers.requests.GetRequestDataWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationDataWorker;

public class InvoiceRepository {
    private final Context context;
    private final InvoiceDao invoiceDao;

    public InvoiceRepository(final Context context) {
        this.context = context;
        this.invoiceDao = LocalCache.getInstance(context).invoiceDao();
    }

    public LiveData<Invoice> selectInvoiceById(final long invoiceId) {
        return invoiceDao.selectInvoiceById(invoiceId);
    }

    public UUID fetchInvoiceData(final long requestId, final long invoiceId, final long clientId, final int consignmentQuantity) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putLong(Constants.KEY_SENDER_ID, clientId)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                .build();
        final OneTimeWorkRequest fetchRequestDataRequest = new OneTimeWorkRequest.Builder(FetchRequestDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchSenderDataRequest = new OneTimeWorkRequest.Builder(FetchSenderWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchInvoiceRequest = new OneTimeWorkRequest.Builder(FetchInvoiceWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchRecipientDataRequest = new OneTimeWorkRequest.Builder(FetchRecipientWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchPayerDataRequest = new OneTimeWorkRequest.Builder(FetchPayerWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchConsignmentListByRequestId = new OneTimeWorkRequest.Builder(FetchConsignmentListByRequestIdWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkContinuation workChain = null;

        //invoice and client exist
        if (invoiceId > 0 && clientId > 0) {
            workChain = WorkManager.getInstance(context)
                    .beginWith(fetchRequestDataRequest)
                    .then(fetchSenderDataRequest)
                    .then(fetchInvoiceRequest)
                    .then(fetchRecipientDataRequest)
                    .then(fetchPayerDataRequest)
                    .then(fetchConsignmentListByRequestId);
        }
        //no invoice, client exists
        else if (clientId > 0) {
            workChain = WorkManager.getInstance(context)
                    .beginWith(fetchRequestDataRequest)
                    .then(fetchSenderDataRequest)
                    .then(fetchConsignmentListByRequestId);
        }
        //no invoice, no client, only request
        else {
            workChain = WorkManager.getInstance(context)
                    .beginWith(fetchRequestDataRequest)
                    .then(fetchConsignmentListByRequestId);
        }
        //no client, request and invoice exist
        workChain.enqueue();
        return fetchConsignmentListByRequestId.getId();
    }

    public UUID createInvoice(final long requestId,
                              final long invoiceId,
                              final long providerId,
                              final long packagingId,
                              final int deliveryType,
                              final int paymentMethod,
                              final double totalWeight,
                              final double totalVolume,
                              final double totalPrice,
                              final String senderFirstName,
                              final String senderMiddleName,
                              final String senderLastName,
                              final String senderEmail,
                              final String senderPhone,
                              final long senderCountryId,
                              final String senderCityName,
                              final String senderAddress,
                              final String senderZip,
                              final String senderTnt,
                              final String senderFedex,
                              final String senderCompanyName,
                              final String senderSignature,
                              final String recipientFirstName,
                              final String recipientMiddleName,
                              final String recipientLastName,
                              final String recipientEmail,
                              final String recipientPhone,
                              final long recipientCountryId,
                              final String recipientCityName,
                              final String recipientAddress,
                              final String recipientZip,
                              final String recipientCargo,
                              final String recipientTnt,
                              final String recipientFedex,
                              final String recipientCompanyName,
                              final String payerFirstName,
                              final String payerMiddleName,
                              final String payerLastName,
                              final String payerEmail,
                              final String payerPhone,
                              final long payerCountryId,
                              final String payerCityName,
                              final String payerAddress,
                              final String payerZip,
                              final String payerCargostar,
                              final String payerTnt,
                              final String payerFedex,
                              final String payerTntTaxId,
                              final String payerFedexTaxId,
                              final String payerCompanyName,
                              final double discount,
                              final String payerInn,
                              final String contractNumber,
                              final String instructions,
                              final List<Consignment> consignmentList) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final String serializedConsignmentList = new Gson().toJson(consignmentList);

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putLong(Constants.KEY_COURIER_ID, SharedPrefs.getInstance(context).getLong(SharedPrefs.ID))
                .putLong(Constants.KEY_PROVIDER_ID, providerId)
                .putLong(Constants.KEY_PACKAGING_ID, packagingId)
                .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                .putInt(Constants.KEY_PAYMENT_METHOD, paymentMethod)
                .putDouble(Constants.KEY_TOTAL_WEIGHT, totalWeight)
                .putDouble(Constants.KEY_TOTAL_VOLUME, totalVolume)
                .putDouble(Constants.KEY_TOTAL_PRICE, totalPrice)

                .putString(Constants.KEY_SENDER_FIRST_NAME, senderFirstName)
                .putString(Constants.KEY_SENDER_MIDDLE_NAME, senderMiddleName)
                .putString(Constants.KEY_SENDER_LAST_NAME, senderLastName)
                .putString(Constants.KEY_SENDER_EMAIL, senderEmail)
                .putString(Constants.KEY_SENDER_PHONE, senderPhone)
                .putString(Constants.KEY_SENDER_ADDRESS, senderAddress)
                .putString(Constants.KEY_SENDER_ZIP, senderZip)
                .putLong(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId)
                .putString(Constants.KEY_SENDER_CITY_NAME, senderCityName)
                .putString(Constants.KEY_SENDER_TNT, senderTnt)
                .putString(Constants.KEY_SENDER_FEDEX, senderFedex)
                .putString(Constants.KEY_SENDER_COMPANY_NAME, senderCompanyName)
                .putString(Constants.KEY_SENDER_SIGNATURE, senderSignature)

                .putString(Constants.KEY_RECIPIENT_FIRST_NAME, recipientFirstName)
                .putString(Constants.KEY_RECIPIENT_MIDDLE_NAME, recipientMiddleName)
                .putString(Constants.KEY_RECIPIENT_LAST_NAME, recipientLastName)
                .putString(Constants.KEY_RECIPIENT_EMAIL, recipientEmail)
                .putString(Constants.KEY_RECIPIENT_PHONE, recipientPhone)
                .putString(Constants.KEY_RECIPIENT_ADDRESS, recipientAddress)
                .putString(Constants.KEY_RECIPIENT_ZIP, recipientZip)
                .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                .putString(Constants.KEY_RECIPIENT_CITY_NAME, recipientCityName)
                .putString(Constants.KEY_RECIPIENT_CARGOSTAR, recipientCargo)
                .putString(Constants.KEY_RECIPIENT_TNT, recipientTnt)
                .putString(Constants.KEY_RECIPIENT_FEDEX, recipientFedex)
                .putString(Constants.KEY_RECIPIENT_COMPANY_NAME, recipientCompanyName)

                .putString(Constants.KEY_PAYER_FIRST_NAME, payerFirstName)
                .putString(Constants.KEY_PAYER_MIDDLE_NAME, payerMiddleName)
                .putString(Constants.KEY_PAYER_LAST_NAME, payerLastName)
                .putString(Constants.KEY_PAYER_EMAIL, payerEmail)
                .putString(Constants.KEY_PAYER_PHONE, payerPhone)
                .putString(Constants.KEY_PAYER_ADDRESS, payerAddress)
                .putString(Constants.KEY_PAYER_ZIP, payerZip)
                .putLong(Constants.KEY_PAYER_COUNTRY_ID, payerCountryId)
                .putString(Constants.KEY_PAYER_CITY_NAME, payerCityName)
                .putString(Constants.KEY_PAYER_CARGOSTAR, payerCargostar)
                .putString(Constants.KEY_PAYER_TNT, payerTnt)
                .putString(Constants.KEY_PAYER_FEDEX, payerFedex)
                .putString(Constants.KEY_PAYER_TNT_TAX_ID, payerTntTaxId)
                .putString(Constants.KEY_PAYER_FEDEX_TAX_ID, payerFedexTaxId)
                .putString(Constants.KEY_PAYER_COMPANY_NAME, payerCompanyName)
                .putString(Constants.KEY_PAYER_INN, payerInn)
                .putDouble(Constants.KEY_DISCOUNT, discount)
                .putString(Constants.KEY_PAYER_CONTRACT_NUMBER, contractNumber)

                .putString(Constants.KEY_INSTRUCTIONS, instructions)
                .putString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST, serializedConsignmentList)
                .build();
        final OneTimeWorkRequest sendInvoiceRequest = new OneTimeWorkRequest.Builder(SendInvoiceWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(sendInvoiceRequest);
        return sendInvoiceRequest.getId();
    }
}
