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
import uz.alexits.cargostar.workers.actor.GetLastAddressBookId;
import uz.alexits.cargostar.workers.actor.GetLastSenderId;
import uz.alexits.cargostar.workers.actor.UpdateCourierWorker;
import uz.alexits.cargostar.workers.calculation.FetchProvidersWorker;
import uz.alexits.cargostar.workers.calculation.FetchVatWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneCountriesWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneSettingsWorker;
import uz.alexits.cargostar.workers.calculation.FetchZonesWorker;
import uz.alexits.cargostar.workers.actor.InsertCustomerWorker;
import uz.alexits.cargostar.workers.invoice.FetchConsignmentListByInvoiceIdWorker;
import uz.alexits.cargostar.workers.invoice.FetchConsignmentListByRequestIdWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceListWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceWorker;
import uz.alexits.cargostar.workers.actor.FetchPayerWorker;
import uz.alexits.cargostar.workers.actor.FetchRecipientWorker;
import uz.alexits.cargostar.workers.actor.FetchSenderWorker;
import uz.alexits.cargostar.workers.actor.FetchSenderListWorker;
import uz.alexits.cargostar.workers.invoice.SearchLocalRequestWorker;
import uz.alexits.cargostar.workers.invoice.InsertInvoiceWorker;
import uz.alexits.cargostar.workers.invoice.SendInvoiceWorker;
import uz.alexits.cargostar.workers.location.FetchBranchesWorker;
import uz.alexits.cargostar.workers.calculation.FetchPackagingTypesWorker;
import uz.alexits.cargostar.workers.calculation.FetchPackagingWorker;
import uz.alexits.cargostar.workers.location.FetchCitiesWorker;
import uz.alexits.cargostar.workers.location.FetchCountriesWorker;
import uz.alexits.cargostar.workers.location.FetchRegionsWorker;
import uz.alexits.cargostar.workers.location.FetchTransitPointsWorker;
import uz.alexits.cargostar.workers.location.GetLastCityId;
import uz.alexits.cargostar.workers.location.GetLastCountryId;
import uz.alexits.cargostar.workers.location.GetLastRegionId;
import uz.alexits.cargostar.workers.login.SignInWorker;
import uz.alexits.cargostar.workers.requests.BindRequestWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestDataWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestsWorker;
import uz.alexits.cargostar.workers.requests.GetLastRequestId;
import uz.alexits.cargostar.workers.requests.InsertRequestWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationDataWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationRouteWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationStatusesWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationsWorker;
import uz.alexits.cargostar.workers.transportation.GetLastTransportationId;
import uz.alexits.cargostar.workers.transportation.SearchTransportationWorker;
import uz.alexits.cargostar.workers.transportation.UpdateTransportationStatusWorker;

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

    /* Location Data */
    public static void fetchLocationData(@NonNull final Context context) {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest getLastCountryIdRequest = new OneTimeWorkRequest.Builder(GetLastCountryId.class)
                .setConstraints(dbConstraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchCountryDataRequest = new OneTimeWorkRequest.Builder(FetchCountriesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest getLastRegionIdRequest = new OneTimeWorkRequest.Builder(GetLastRegionId.class)
                .setConstraints(dbConstraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchRegionDataRequest = new OneTimeWorkRequest.Builder(FetchRegionsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest getLastCityIdRequest = new OneTimeWorkRequest.Builder(GetLastCityId.class)
                .setConstraints(dbConstraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchCityDataRequest = new OneTimeWorkRequest.Builder(FetchCitiesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(getLastCountryIdRequest)
                .then(fetchCountryDataRequest)
                .then(getLastRegionIdRequest)
                .then(fetchRegionDataRequest)
                .then(getLastCityIdRequest)
                .then(fetchCityDataRequest)
                .enqueue();
    }

    public static UUID fetchTransitPoints(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest fetchTransitPointsRequest = new OneTimeWorkRequest.Builder(FetchTransitPointsWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .enqueue(fetchTransitPointsRequest);

        return fetchTransitPointsRequest.getId();
    }

    /* Calculation Data */
    public static void fetchPackagingData(@NonNull final Context context, final int perPage) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, perPage)
                .build();

        final OneTimeWorkRequest fetchProviderListRequest = new OneTimeWorkRequest.Builder(FetchProvidersWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchPackagingRequest = new OneTimeWorkRequest.Builder(FetchPackagingWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchPackagingTypesRequest = new OneTimeWorkRequest.Builder(FetchPackagingTypesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchZonesRequest = new OneTimeWorkRequest.Builder(FetchZonesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchZoneCountriesRequest = new OneTimeWorkRequest.Builder(FetchZoneCountriesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchZoneSettingsRequest = new OneTimeWorkRequest.Builder(FetchZoneSettingsWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest fetchVatRequest = new OneTimeWorkRequest.Builder(FetchVatWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(fetchProviderListRequest)
                .then(fetchPackagingRequest)
                .then(fetchPackagingTypesRequest)
                .then(fetchZonesRequest)
                .then(fetchZoneCountriesRequest)
                .then(fetchZoneSettingsRequest)
                .then(fetchVatRequest)
                .enqueue();
    }

    /* Request */
    public static void fetchRequests(@NonNull final Context context) {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest getLastIdRequest = new OneTimeWorkRequest.Builder(GetLastRequestId.class)
                .setConstraints(dbConstraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchRequestsRequest = new OneTimeWorkRequest.Builder(FetchRequestsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(getLastIdRequest)
                .then(fetchRequestsRequest)
                .enqueue();
    }

    public static UUID bindRequest(@NonNull final Context context, final long requestId, final long courierId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_COURIER_ID, courierId)
                .build();

        final OneTimeWorkRequest bindRequest = new OneTimeWorkRequest.Builder(BindRequestWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(bindRequest);

        return bindRequest.getId();
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
                                     final int deliveryType,
                                     final int status,
                                     final long createdAt,
                                     final long updatedAt) {
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
                .putInt(Constants.KEY_STATUS, status)
                .putLong(Constants.KEY_CREATED_AT, createdAt)
                .putLong(Constants.KEY_UPDATED_AT, updatedAt)
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

    public static UUID searchRequest(@NonNull final Context context, final long requestId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .build();

        final OneTimeWorkRequest getInvoiceRequest = new OneTimeWorkRequest.Builder(SearchLocalRequestWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(getInvoiceRequest);

        return getInvoiceRequest.getId();
    }

    /* Client */
    public static UUID createUser(@NonNull final Context context,
                                  final String email,
                                  final String password,
                                  final String cargostarAccountNumber,
                                  final String tntAccountNumber,
                                  final String fedexAccountNumber,
                                  final String firstName,
                                  final String middleName,
                                  final String lastName,
                                  final String phone,
                                  final String country,
                                  final String cityName,
                                  final String address,
                                  final String geolocation,
                                  final String zip,
                                  final double discount,
                                  final int userType,
                                  final String passportSerial,
                                  final String inn,
                                  final String company,
                                  final String contractNumber,
                                  final String signatureUrl) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putString(Constants.KEY_EMAIL, email)
                .putString(Constants.KEY_PASSWORD, password)
                .putString(Constants.KEY_CARGOSTAR, cargostarAccountNumber)
                .putString(Constants.KEY_TNT, tntAccountNumber)
                .putString(Constants.KEY_FEDEX, fedexAccountNumber)
                .putString(Constants.KEY_FIRST_NAME, firstName)
                .putString(Constants.KEY_MIDDLE_NAME, middleName)
                .putString(Constants.KEY_LAST_NAME, lastName)
                .putString(Constants.KEY_PHONE, phone)
                .putString(Constants.KEY_COUNTRY, country)
                .putString(Constants.KEY_CITY_NAME, cityName)
                .putString(Constants.KEY_ADDRESS, address)
                .putString(Constants.KEY_GEOLOCATION, geolocation)
                .putString(Constants.KEY_ZIP, zip)
                .putDouble(Constants.KEY_DISCOUNT, discount)
                .putInt(Constants.KEY_USER_TYPE, userType)
                .putString(Constants.KEY_PASSPORT_SERIAL, passportSerial)
                .putString(Constants.KEY_INN, inn)
                .putString(Constants.KEY_COMPANY, company)
                .putString(Constants.KEY_PAYER_CONTRACT_NUMBER, contractNumber)
                .putString(Constants.KEY_SIGNATURE, signatureUrl)
                .build();

        final OneTimeWorkRequest createUserWorker = new OneTimeWorkRequest.Builder(InsertCustomerWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(createUserWorker);

        return createUserWorker.getId();
    }

    public static UUID fetchSenderList(@NonNull final Context context) {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest getSenderLastIdRequest = new OneTimeWorkRequest.Builder(GetLastSenderId.class)
                .setConstraints(dbConstraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchSenderListRequest = new OneTimeWorkRequest.Builder(FetchSenderListWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).beginWith(getSenderLastIdRequest).then(fetchSenderListRequest).enqueue();

        return fetchSenderListRequest.getId();
    }

    /* Courier */
    public static UUID fetchAddressBookList(@NonNull final Context context) {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest getLastAddressBookIdRequest = new OneTimeWorkRequest.Builder(GetLastAddressBookId.class)
                .setConstraints(dbConstraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchAddressBookListRequest = new OneTimeWorkRequest.Builder(FetchAddressBookWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).beginWith(getLastAddressBookIdRequest).then(fetchAddressBookListRequest).enqueue();

        return fetchAddressBookListRequest.getId();
    }

    /* Courier */
    public static UUID updateCourierData(@NonNull final Context context,
                                         final long courierId,
                                         final String login,
                                         final String email,
                                         final String password,
                                         final String firstName,
                                         final String middleName,
                                         final String lastName,
                                         final String phone,
                                         final String photoUrl) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_COURIER_ID, courierId)
                .putString(Constants.KEY_LOGIN, login)
                .putString(Constants.KEY_EMAIL, email)
                .putString(Constants.KEY_PASSWORD, password)
                .putString(Constants.KEY_FIRST_NAME, firstName)
                .putString(Constants.KEY_MIDDLE_NAME, middleName)
                .putString(Constants.KEY_LAST_NAME, lastName)
                .putString(Constants.KEY_PHONE, phone)
                .putString(Constants.KEY_PHOTO, photoUrl)
                .build();

        final OneTimeWorkRequest updateCourierRequest = new OneTimeWorkRequest.Builder(UpdateCourierWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(updateCourierRequest);

        return updateCourierRequest.getId();
    }

    /* Invoice */
    public static UUID fetchInvoiceData(@NonNull final Context context, final long requestId, final long invoiceId, final long senderId, final int consignmentQuantity) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putLong(Constants.KEY_SENDER_ID, senderId)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                .build();

        final OneTimeWorkRequest fetchRequestDataRequest = new OneTimeWorkRequest.Builder(FetchRequestDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchSenderDataRequest = new OneTimeWorkRequest.Builder(FetchSenderWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchInvoiceRequest = new OneTimeWorkRequest.Builder(FetchInvoiceWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchRecipientDataRequest = new OneTimeWorkRequest.Builder(FetchRecipientWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchPayerDataRequest = new OneTimeWorkRequest.Builder(FetchPayerWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest insertInvoiceRequest = new OneTimeWorkRequest.Builder(InsertInvoiceWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchConsignmentListByInvoiceId = new OneTimeWorkRequest.Builder(FetchConsignmentListByInvoiceIdWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        //invoiceId > 0, fetch invoice data and return
        WorkManager.getInstance(context)
                .beginWith(fetchRequestDataRequest)
                .then(fetchSenderDataRequest)
                .then(fetchInvoiceRequest)
                .then(fetchRecipientDataRequest)
                .then(fetchPayerDataRequest)
                .then(insertInvoiceRequest)
                .then(fetchConsignmentListByInvoiceId)
                .enqueue();
        return fetchConsignmentListByInvoiceId.getId();
    }

    public static UUID fetchSenderData(Context context, final long requestId, final long senderId, final int consignmentQuantity) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_SENDER_ID, senderId)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                .build();

        final OneTimeWorkRequest fetchRequestDataRequest = new OneTimeWorkRequest.Builder(FetchRequestDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchSenderDataRequest = new OneTimeWorkRequest.Builder(FetchSenderWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchConsignmentListByRequestId = new OneTimeWorkRequest.Builder(FetchConsignmentListByRequestIdWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        if (consignmentQuantity <= 0) {
            WorkManager.getInstance(context)
                    .beginWith(fetchRequestDataRequest)
                    .then(fetchSenderDataRequest)
                    .enqueue();
            return fetchSenderDataRequest.getId();
        }
        WorkManager.getInstance(context)
                .beginWith(fetchRequestDataRequest)
                .then(fetchSenderDataRequest)
                .then(fetchConsignmentListByRequestId)
                .enqueue();
        return fetchConsignmentListByRequestId.getId();
    }

    public static UUID fetchRequestData(Context context, final long requestId, final int consignmentQuantity) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                .build();

        final OneTimeWorkRequest fetchRequestDataRequest = new OneTimeWorkRequest.Builder(FetchRequestDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchConsignmentListByRequestId = new OneTimeWorkRequest.Builder(FetchConsignmentListByRequestIdWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).beginWith(fetchRequestDataRequest).then(fetchConsignmentListByRequestId).enqueue();
        return fetchConsignmentListByRequestId.getId();
    }

    public static UUID sendInvoice(@NonNull final Context context,
                                   final long requestId,
                                   final long invoiceId,
                                   final long courierId,
                                   final String senderSignature,
                                   final boolean isSenderSignatureLocal,
                                   final String senderEmail,
                                   final String senderTnt,
                                   final String senderFedex,
                                   final String senderCountryId,
                                   final String senderCityId,
                                   final String senderAddress,
                                   final String senderZip,
                                   final String senderFirstName,
                                   final String senderMiddleName,
                                   final String senderLastName,
                                   final String senderPhone,
                                   final String senderCompanyName,
                                   final int senderType,
                                   final String recipientEmail,
                                   final String recipientCargo,
                                   final String recipientTnt,
                                   final String recipientFedex,
                                   final String recipientCountryId,
                                   final String recipientCityId,
                                   final String recipientAddress,
                                   final String recipientZip,
                                   final String recipientFirstName,
                                   final String recipientMiddleName,
                                   final String recipientLastName,
                                   final String recipientPhone,
                                   final String recipientCompanyName,
                                   final int recipientType,
                                   final String payerEmail,
                                   final String payerCountryId,
                                   final String payerCityId,
                                   final String payerAddress,
                                   final String payerZip,
                                   final String payerFirstName,
                                   final String payerMiddleName,
                                   final String payerLastName,
                                   final String payerPhone,
                                   final String payerCargostar,
                                   final String payerTnt,
                                   final String payerFedex,
                                   final String payerTntTaxId,
                                   final String payerFedexTaxId,
                                   final String payerCompanyName,
                                   final int payerType,
                                   final double discount,
                                   final String payerInn,
                                   final String contractNumber,
                                  final String instructions,
                                   final long providerId,
                                   final long packagingId,
                                   final int deliveryType,
                                   final int paymentMethod,
                                   final double totalWeight,
                                   final double totalVolume,
                                   final String totalPrice,
                                   final String serializedConsignmentList) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putLong(Constants.KEY_COURIER_ID, courierId)

                .putString(Constants.KEY_SENDER_SIGNATURE, senderSignature)
                .putBoolean(Constants.KEY_IS_SENDER_SIGNATURE_LOCAL, isSenderSignatureLocal)
                .putString(Constants.KEY_SENDER_EMAIL, senderEmail)
                .putString(Constants.KEY_SENDER_TNT, senderTnt)
                .putString(Constants.KEY_SENDER_FEDEX, senderFedex)
                .putString(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId)
                .putString(Constants.KEY_SENDER_CITY_ID, senderCityId)
                .putString(Constants.KEY_SENDER_ADDRESS, senderAddress)
                .putString(Constants.KEY_SENDER_ZIP, senderZip)
                .putString(Constants.KEY_SENDER_FIRST_NAME, senderFirstName)
                .putString(Constants.KEY_SENDER_MIDDLE_NAME, senderMiddleName)
                .putString(Constants.KEY_SENDER_LAST_NAME, senderLastName)
                .putString(Constants.KEY_SENDER_PHONE, senderPhone)
                .putString(Constants.KEY_SENDER_COMPANY_NAME, senderCompanyName)
                .putInt(Constants.KEY_SENDER_TYPE, senderType)

                .putString(Constants.KEY_RECIPIENT_EMAIL, recipientEmail)
                .putString(Constants.KEY_RECIPIENT_CARGOSTAR, recipientCargo)
                .putString(Constants.KEY_RECIPIENT_TNT, recipientTnt)
                .putString(Constants.KEY_RECIPIENT_FEDEX, recipientFedex)
                .putString(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                .putString(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId)
                .putString(Constants.KEY_RECIPIENT_ADDRESS, recipientAddress)
                .putString(Constants.KEY_RECIPIENT_ZIP, recipientZip)
                .putString(Constants.KEY_RECIPIENT_FIRST_NAME, recipientFirstName)
                .putString(Constants.KEY_RECIPIENT_MIDDLE_NAME, recipientMiddleName)
                .putString(Constants.KEY_RECIPIENT_LAST_NAME, recipientLastName)
                .putString(Constants.KEY_RECIPIENT_PHONE, recipientPhone)
                .putString(Constants.KEY_RECIPIENT_COMPANY_NAME, recipientCompanyName)
                .putInt(Constants.KEY_RECIPIENT_TYPE, recipientType)

                .putString(Constants.KEY_PAYER_EMAIL, payerEmail)
                .putString(Constants.KEY_PAYER_COUNTRY_ID, payerCountryId)
                .putString(Constants.KEY_PAYER_CITY_ID, payerCityId)
                .putString(Constants.KEY_PAYER_ADDRESS, payerAddress)
                .putString(Constants.KEY_PAYER_ZIP, payerZip)
                .putString(Constants.KEY_PAYER_FIRST_NAME, payerFirstName)
                .putString(Constants.KEY_PAYER_MIDDLE_NAME, payerMiddleName)
                .putString(Constants.KEY_PAYER_LAST_NAME, payerLastName)
                .putString(Constants.KEY_PAYER_PHONE, payerPhone)
                .putString(Constants.KEY_PAYER_CARGOSTAR, payerCargostar)
                .putString(Constants.KEY_PAYER_TNT, payerTnt)
                .putString(Constants.KEY_PAYER_FEDEX, payerFedex)
                .putString(Constants.KEY_PAYER_TNT_TAX_ID, payerTntTaxId)
                .putString(Constants.KEY_PAYER_FEDEX_TAX_ID, payerFedexTaxId)
                .putString(Constants.KEY_PAYER_COMPANY_NAME, payerCompanyName)
                .putInt(Constants.KEY_PAYER_TYPE, payerType)
                .putString(Constants.KEY_PAYER_INN, payerInn)
                .putDouble(Constants.KEY_DISCOUNT, discount)

                .putString(Constants.KEY_PAYER_CONTRACT_NUMBER, contractNumber)
                .putString(Constants.KEY_INSTRUCTIONS, instructions)

                .putLong(Constants.KEY_PROVIDER_ID, providerId)
                .putLong(Constants.KEY_PACKAGING_ID, packagingId)
                .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                .putInt(Constants.KEY_PAYMENT_METHOD, paymentMethod)
                .putDouble(Constants.KEY_TOTAL_WEIGHT, totalWeight)
                .putDouble(Constants.KEY_TOTAL_VOLUME, totalVolume)
                .putString(Constants.KEY_TOTAL_PRICE, totalPrice)

                .putString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST, serializedConsignmentList)
                .build();

        final OneTimeWorkRequest sendInvoiceRequest = new OneTimeWorkRequest.Builder(SendInvoiceWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(sendInvoiceRequest);
        return sendInvoiceRequest.getId();
    }

    public static UUID searchTransportation(@NonNull final Context context, final String transportationQr) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putString(Constants.KEY_TRANSPORTATION_QR, transportationQr)
                .build();

        final OneTimeWorkRequest getInvoiceRequest = new OneTimeWorkRequest.Builder(SearchTransportationWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(getInvoiceRequest);

        return getInvoiceRequest.getId();
    }

    /* Transportation */
    public static UUID fetchTransportationList(@NonNull final Context context) {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest getLastTransportationIdRequest = new OneTimeWorkRequest.Builder(GetLastTransportationId.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationsRequest = new OneTimeWorkRequest.Builder(FetchTransportationsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(getLastTransportationIdRequest)
                .then(fetchTransportationsRequest)
                .enqueue();

        return fetchTransportationsRequest.getId();
    }

    public static UUID fetchTransportationRoute(@NonNull final Context context, final long transportationId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .build();

        final OneTimeWorkRequest fetchTransportationRequest = new OneTimeWorkRequest.Builder(FetchTransportationDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(fetchTransportationRequest)
                .then(fetchTransportationRouteRequest)
                .enqueue();
        return fetchTransportationRouteRequest.getId();
    }

    public static UUID updateTransportationStatus(@NonNull final Context context,
                                                  final long transportationId,
                                                  final long transportationStatusId,
                                                  final long transitPointId,
                                                  final long partialId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportationStatusId)
                .putLong(Constants.KEY_TRANSIT_POINT_ID, transitPointId)
                .putLong(Constants.KEY_PARTIAL_ID, partialId)
                .build();

        final OneTimeWorkRequest updateTransportationStatusRequest = new OneTimeWorkRequest.Builder(UpdateTransportationStatusWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(updateTransportationStatusRequest)
                .then(fetchTransportationRouteRequest)
                .enqueue();

        return fetchTransportationRouteRequest.getId();
    }

    public static UUID fetchTransportationStatuses(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest fetchTransportationStatusListRequest = new OneTimeWorkRequest.Builder(FetchTransportationStatusesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .enqueue(fetchTransportationStatusListRequest);

        return fetchTransportationStatusListRequest.getId();
    }

    public static UUID sendRecipientSignatureAndUpdateStatusDelivered(@NonNull final Context context,
                                                                      final long invoiceId,
                                                                      final String recipientSignature,
                                                                      final long transportationId,
                                                                      final long transportationStatusId,
                                                                      final long transitPointId,
                                                                      final long partial) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data sendSignatureData = new Data.Builder()
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putString(Constants.KEY_RECIPIENT_SIGNATURE, recipientSignature)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportationStatusId)
                .putLong(Constants.KEY_TRANSIT_POINT_ID, transitPointId)
                .putLong(Constants.KEY_PARTIAL_ID, partial)
                .build();

        final OneTimeWorkRequest sendRecipientSignatureRequest = new OneTimeWorkRequest.Builder(SendRecipientSignatureWorker.class)
                .setConstraints(constraints)
                .setInputData(sendSignatureData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest updateTransportationStatusRequest = new OneTimeWorkRequest.Builder(UpdateTransportationStatusWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(sendRecipientSignatureRequest)
                .then(updateTransportationStatusRequest)
                .then(fetchTransportationRouteRequest)
                .enqueue();

        return fetchTransportationRouteRequest.getId();
    }


    /* Cargo */
    public static UUID fetchCargoList(@NonNull final Context context, final Long requestId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .build();

        final OneTimeWorkRequest fetchCargoListRequest = new OneTimeWorkRequest.Builder(FetchConsignmentListByInvoiceIdWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchCargoListRequest);

        return fetchCargoListRequest.getId();
    }

    public static final String KEY_PER_PAGE = "per-page";
    public static final int DEFAULT_PER_PAGE = 100000;
    private static final int DEFAULT_DELAY = 60000;

    private static final String TAG = SyncWorkRequest.class.toString();
}
