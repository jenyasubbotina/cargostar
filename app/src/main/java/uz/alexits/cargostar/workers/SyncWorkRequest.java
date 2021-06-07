package uz.alexits.cargostar.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.actor.FetchAddressBookWorker;
import uz.alexits.cargostar.workers.calculation.FetchProvidersWorker;
import uz.alexits.cargostar.workers.calculation.FetchVatWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneCountriesWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneSettingsWorker;
import uz.alexits.cargostar.workers.calculation.FetchZonesWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceListWorker;
import uz.alexits.cargostar.workers.actor.FetchSenderListWorker;
import uz.alexits.cargostar.workers.location.FetchBranchesWorker;
import uz.alexits.cargostar.workers.calculation.FetchPackagingTypesWorker;
import uz.alexits.cargostar.workers.calculation.FetchPackagingWorker;
import uz.alexits.cargostar.workers.location.FetchCitiesWorker;
import uz.alexits.cargostar.workers.location.FetchCountriesWorker;
import uz.alexits.cargostar.workers.location.FetchRegionsWorker;
import uz.alexits.cargostar.workers.location.FetchTransitPointsWorker;
import uz.alexits.cargostar.workers.login.SignInWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestsWorker;
import uz.alexits.cargostar.workers.requests.InsertRequestWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationStatusesWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationsWorker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SyncWorkRequest {
    /* Synchronization */
    public static UUID synchronizeFirstTime(final Context context, final String login, final String password, final String token) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .putString(Constants.KEY_LOGIN, login)
                .putString(Constants.KEY_PASSWORD, password)
                .putString(Constants.KEY_TOKEN, token)
                .build();
        /* fetch location data from server*/
        final OneTimeWorkRequest fetchCountriesRequest = new OneTimeWorkRequest.Builder(FetchCountriesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        final OneTimeWorkRequest fetchRegionsRequest = new OneTimeWorkRequest.Builder(FetchRegionsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        final OneTimeWorkRequest fetchCitiesRequest = new OneTimeWorkRequest.Builder(FetchCitiesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch branches from server */
        final OneTimeWorkRequest fetchBranchesRequest = new OneTimeWorkRequest.Builder(FetchBranchesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch transit points from server */
        final OneTimeWorkRequest fetchTransitPointsRequest = new OneTimeWorkRequest.Builder(FetchTransitPointsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch provider data from server */
        final OneTimeWorkRequest fetchProviderListRequest = new OneTimeWorkRequest.Builder(FetchProvidersWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch packaging data from server */
        final OneTimeWorkRequest fetchPackagingRequest = new OneTimeWorkRequest.Builder(FetchPackagingWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch packaging type data from server */
        final OneTimeWorkRequest fetchPackagingTypesRequest = new OneTimeWorkRequest.Builder(FetchPackagingTypesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch zone data from server */
        final OneTimeWorkRequest fetchZonesRequest = new OneTimeWorkRequest.Builder(FetchZonesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch zone country data from server */
        final OneTimeWorkRequest fetchZoneCountriesRequest = new OneTimeWorkRequest.Builder(FetchZoneCountriesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch zone settings data from server */
        final OneTimeWorkRequest fetchZoneSettingsRequest = new OneTimeWorkRequest.Builder(FetchZoneSettingsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch vat data from server */
        final OneTimeWorkRequest fetchVatRequest = new OneTimeWorkRequest.Builder(FetchVatWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch request data from server */
        final OneTimeWorkRequest fetchRequestListRequest = new OneTimeWorkRequest.Builder(FetchRequestsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch sender data from server */
        final OneTimeWorkRequest fetchSenderListRequest = new OneTimeWorkRequest.Builder(FetchSenderListWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch address book data from server */
        final OneTimeWorkRequest fetchAddressBookRequest = new OneTimeWorkRequest.Builder(FetchAddressBookWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch invoice data from server */
        final OneTimeWorkRequest fetchInvoicesRequest = new OneTimeWorkRequest.Builder(FetchInvoiceListWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch transportation status list from server */
        final OneTimeWorkRequest fetchTransportationStatusesRequest = new OneTimeWorkRequest.Builder(FetchTransportationStatusesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* fetch transportation list from server */
        final OneTimeWorkRequest fetchTransportationsRequest = new OneTimeWorkRequest.Builder(FetchTransportationsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        /* last one -> sign in */
        final OneTimeWorkRequest signInRequest = new OneTimeWorkRequest.Builder(SignInWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .addTag("synchronize")
                .build();
        final WorkContinuation syncChain = WorkManager.getInstance(context)
                .beginWith(fetchCountriesRequest)
                .then(fetchRegionsRequest)
                .then(fetchCitiesRequest)
                .then(fetchBranchesRequest)
                .then(fetchTransitPointsRequest)
                .then(fetchProviderListRequest)
                .then(fetchPackagingRequest)
                .then(fetchPackagingTypesRequest)
                .then(fetchZonesRequest)
                .then(fetchZoneCountriesRequest)
                .then(fetchZoneSettingsRequest)
                .then(fetchVatRequest)
                .then(fetchRequestListRequest)
                .then(fetchSenderListRequest)
                .then(fetchAddressBookRequest)
                .then(fetchInvoicesRequest)
                .then(fetchTransportationStatusesRequest)
                .then(fetchTransportationsRequest)
                .then(signInRequest);

        syncChain.enqueue();

        return signInRequest.getId();
    }

    public static void createRequest(@NonNull final Context context,
                                     final long requestId,
                                     final long invoiceId,
                                     final long senderCountryId,
                                     final long senderRegionId,
                                     final long senderCityId,
                                     final String senderCityName,
                                     final long userId,
                                     final long senderId,
                                     final long courierId,
                                     final long providerId,
                                     final String senderFirstName,
                                     final String senderMiddleName,
                                     final String senderLastName,
                                     final String senderEmail,
                                     final String senderPhone,
                                     final String senderAddress,
                                     final long recipientCountryId,
                                     final long recipientCityId,
                                     final String recipientCityName,
                                     final String comment,
                                     final String cityFrom,
                                     final String cityTo,
                                     final int consignmentQuantity,
                                     final String paymentStatus,
                                     final int deliveryType) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putLong(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId)
                .putLong(Constants.KEY_SENDER_REGION_ID, senderRegionId)
                .putLong(Constants.KEY_SENDER_CITY_ID, senderCityId)
                .putLong(Constants.KEY_USER_ID, userId)
                .putLong(Constants.KEY_SENDER_ID, senderId)
                .putLong(Constants.KEY_COURIER_ID, courierId)
                .putLong(Constants.KEY_PROVIDER_ID, providerId)
                .putString(Constants.KEY_SENDER_FIRST_NAME, senderFirstName)
                .putString(Constants.KEY_SENDER_MIDDLE_NAME, senderMiddleName)
                .putString(Constants.KEY_SENDER_LAST_NAME, senderLastName)
                .putString(Constants.KEY_EMAIL, senderEmail)
                .putString(Constants.KEY_SENDER_PHONE, senderPhone)
                .putString(Constants.KEY_SENDER_ADDRESS, senderAddress)
                .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                .putLong(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId)
                .putString(Constants.KEY_COMMENT, comment)
                .putString(Constants.KEY_CITY_FROM, cityFrom)
                .putString(Constants.KEY_CITY_TO, cityTo)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                .putString(Constants.KEY_PAYMENT_STATUS, paymentStatus)
                .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                .putString(Constants.KEY_SENDER_CITY_NAME, senderCityName)
                .putString(Constants.KEY_RECIPIENT_CITY_NAME, recipientCityName)
                .build();

        final OneTimeWorkRequest createRequestRequest = new OneTimeWorkRequest.Builder(InsertRequestWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(createRequestRequest);
    }

    public static final String KEY_PER_PAGE = "per-page";
    public static final int DEFAULT_PER_PAGE = 100000;
    private static final int DEFAULT_DELAY = 60000;

    private static final String TAG = SyncWorkRequest.class.toString();
}
