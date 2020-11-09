package uz.alexits.cargostar.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkManager;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.actor.UpdateCourierWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneCountriesWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneSettingsWorker;
import uz.alexits.cargostar.workers.calculation.FetchZonesWorker;
import uz.alexits.cargostar.workers.actor.CreateUserWorker;
import uz.alexits.cargostar.workers.invoice.FetchCargoListWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceListWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceWorker;
import uz.alexits.cargostar.workers.invoice.FetchPayerDataWorker;
import uz.alexits.cargostar.workers.invoice.FetchRecipientDataWorker;
import uz.alexits.cargostar.workers.invoice.FetchSenderDataWorker;
import uz.alexits.cargostar.workers.invoice.GetInvoiceHeaderWorker;
import uz.alexits.cargostar.workers.invoice.InsertInvoiceWorker;
import uz.alexits.cargostar.workers.location.FetchBranchesWorker;
import uz.alexits.cargostar.workers.calculation.FetchPackagingTypesWorker;
import uz.alexits.cargostar.workers.calculation.FetchPackagingWorker;
import uz.alexits.cargostar.workers.location.FetchLocationDataWorker;
import uz.alexits.cargostar.workers.location.FetchTransitPointsWorker;
import uz.alexits.cargostar.workers.login.SignInWorker;
import uz.alexits.cargostar.workers.requests.BindRequestWorker;
import uz.alexits.cargostar.workers.requests.FetchMyRequestsWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestsWorker;
import uz.alexits.cargostar.workers.requests.InsertRequestWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationDataWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationRouteWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationStatusesWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationsWorker;
import uz.alexits.cargostar.workers.transportation.UpdateTransportationStatusWorker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SyncWorkRequest {
    /* Location Data */
    public static UUID fetchBranchesAndSignIn(@NonNull final Context context,
                                              final int perPage,
                                              final String login,
                                              final String password,
                                              final String token) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, perPage)
                .putString(Constants.KEY_LOGIN, login)
                .putString(Constants.KEY_PASSWORD, password)
                .putString(SharedPrefs.TOKEN, token)
                .build();

        final OneTimeWorkRequest fetchBranchesRequest = new OneTimeWorkRequest.Builder(FetchBranchesWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        final OneTimeWorkRequest signInRequest = new OneTimeWorkRequest.Builder(SignInWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(fetchBranchesRequest)
                .then(signInRequest)
                .enqueue();

        return signInRequest.getId();
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

    public static UUID fetchLocationData(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final OneTimeWorkRequest fetchLocationDataRequest = new OneTimeWorkRequest.Builder(FetchLocationDataWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(fetchLocationDataRequest);
        return fetchLocationDataRequest.getId();
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

        WorkManager.getInstance(context)
                .beginWith(fetchPackagingRequest)
                .then(fetchPackagingTypesRequest)
                .then(fetchZonesRequest)
                .then(fetchZoneCountriesRequest)
                .then(fetchZoneSettingsRequest)
                .enqueue();
    }

    /* Request */
    public static void fetchRequestData(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest fetchRequestsRequest = new OneTimeWorkRequest.Builder(FetchRequestsWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(fetchRequestsRequest);
    }

    public static void fetchRequestData(@NonNull final Context context, final long courierId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .putLong(Constants.KEY_COURIER_ID, courierId)
                .build();

        final OneTimeWorkRequest fetchRequestsRequest = new OneTimeWorkRequest.Builder(FetchMyRequestsWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(fetchRequestsRequest);
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
                                     final String comment,
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
                .build();

        final OneTimeWorkRequest createRequestRequest = new OneTimeWorkRequest.Builder(InsertRequestWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(createRequestRequest);
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
                                  final String region,
                                  final String city,
                                  final String address,
                                  final String geolocation,
                                  final String zip,
                                  final double discount,
                                  final int userType,
                                  final String passportSerial,
                                  final String inn,
                                  final String company,
                                  final String bank,
                                  final String mfo,
                                  final String oked,
                                  final String checkingAccount,
                                  final String vat,
                                  final String photoUrl,
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
                .putString(Constants.KEY_REGION, region)
                .putString(Constants.KEY_CITY, city)
                .putString(Constants.KEY_ADDRESS, address)
                .putString(Constants.KEY_GEOLOCATION, geolocation)
                .putString(Constants.KEY_ZIP, zip)
                .putDouble(Constants.KEY_DISCOUNT, discount)
                .putInt(Constants.KEY_USER_TYPE, userType)
                .putString(Constants.KEY_PASSPORT_SERIAL, passportSerial)
                .putString(Constants.KEY_INN, inn)
                .putString(Constants.KEY_COMPANY, company)
                .putString(Constants.KEY_BANK, bank)
                .putString(Constants.KEY_MFO, mfo)
                .putString(Constants.KEY_OKED, oked)
                .putString(Constants.KEY_CHECKING_ACCOUNT, checkingAccount)
                .putString(Constants.KEY_VAT, vat)
                .putString(Constants.KEY_PHOTO, photoUrl)
                .putString(Constants.KEY_SIGNATURE, signatureUrl)
                .build();

        final OneTimeWorkRequest createUserWorker = new OneTimeWorkRequest.Builder(CreateUserWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(createUserWorker);

        return createUserWorker.getId();
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

        Log.i(TAG, "password= " + password);

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
    public static UUID fetchInvoiceData(@NonNull final Context context, final long invoiceId, final long senderId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putLong(Constants.KEY_SENDER_ID, senderId)
                .build();

        final OneTimeWorkRequest fetchSenderDataRequest = new OneTimeWorkRequest.Builder(FetchSenderDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchInvoiceRequest = new OneTimeWorkRequest.Builder(FetchInvoiceWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchRecipientDataRequest = new OneTimeWorkRequest.Builder(FetchRecipientDataWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchPayerDataRequest = new OneTimeWorkRequest.Builder(FetchPayerDataWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest insertInvoiceRequest = new OneTimeWorkRequest.Builder(InsertInvoiceWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchCargoListRequest = new OneTimeWorkRequest.Builder(FetchCargoListWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(fetchSenderDataRequest)
                .then(fetchInvoiceRequest)
                .then(fetchRecipientDataRequest)
                .then(fetchPayerDataRequest)
                .then(insertInvoiceRequest)
                .then(fetchCargoListRequest)
                .enqueue();

        return insertInvoiceRequest.getId();
    }

    public static void fetchRequestsAndInvoices(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest fetchRequestsRequest = new OneTimeWorkRequest.Builder(FetchRequestsWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchInvoicesRequest = new OneTimeWorkRequest.Builder(FetchInvoiceListWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).beginWith(fetchRequestsRequest).then(fetchInvoicesRequest).enqueue();
    }

    public static UUID getInvoiceById(@NonNull final Context context, final long invoiceId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .build();

        final OneTimeWorkRequest getInvoiceRequest = new OneTimeWorkRequest.Builder(GetInvoiceHeaderWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(getInvoiceRequest);

        return getInvoiceRequest.getId();
    }

    /* Transportation */
    public static UUID fetchTransportationList(@NonNull final Context context) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putInt(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                .build();

        final OneTimeWorkRequest fetchTransportationsRequest = new OneTimeWorkRequest.Builder(FetchTransportationsWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(fetchTransportationsRequest);

        return fetchTransportationsRequest.getId();
    }

    public static UUID fetchTransportationData(@NonNull final Context context, final long transportationId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .build();

        final OneTimeWorkRequest fetchTransportationDataRequest = new OneTimeWorkRequest.Builder(FetchTransportationDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchTransportationDataRequest);

        return fetchTransportationDataRequest.getId();
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

        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(fetchTransportationRouteRequest);

        return fetchTransportationRouteRequest.getId();
    }

    public static UUID updateTransportationStatus(@NonNull final Context context,
                                                  final long transportationId,
                                                  final long transportationStatusId,
                                                  final long transitPointId) {
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
                .build();

        final OneTimeWorkRequest updateTransportationStatusRequest = new OneTimeWorkRequest.Builder(UpdateTransportationStatusWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationDataRequest = new OneTimeWorkRequest.Builder(FetchTransportationDataWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(updateTransportationStatusRequest)
                .then(fetchTransportationDataRequest)
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

        final OneTimeWorkRequest fetchCargoListRequest = new OneTimeWorkRequest.Builder(FetchCargoListWorker.class)
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
