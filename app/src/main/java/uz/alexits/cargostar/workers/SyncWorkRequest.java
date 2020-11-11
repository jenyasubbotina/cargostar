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
import uz.alexits.cargostar.workers.calculation.FetchProvidersWorker;
import uz.alexits.cargostar.workers.calculation.FetchVatWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneCountriesWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneSettingsWorker;
import uz.alexits.cargostar.workers.calculation.FetchZonesWorker;
import uz.alexits.cargostar.workers.actor.InsertCustomerWorker;
import uz.alexits.cargostar.workers.invoice.FetchCargoListWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceListWorker;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceWorker;
import uz.alexits.cargostar.workers.invoice.FetchPayerDataWorker;
import uz.alexits.cargostar.workers.invoice.FetchRecipientDataWorker;
import uz.alexits.cargostar.workers.invoice.FetchSenderDataWorker;
import uz.alexits.cargostar.workers.invoice.GetInvoiceHeaderWorker;
import uz.alexits.cargostar.workers.invoice.InsertInvoiceWorker;
import uz.alexits.cargostar.workers.invoice.SendInvoiceWorker;
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
import uz.alexits.cargostar.workers.transportation.SearchTransportationWorker;
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

        final OneTimeWorkRequest createUserWorker = new OneTimeWorkRequest.Builder(InsertCustomerWorker.class)
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

    public static UUID sendInvoice(@NonNull final Context context,
                                   final long requestId,
                                   final long invoiceId,
                                   final long courierId,
                                   final long operatorId,
                                   final long accountantId,
                                   final String senderSignature,
                                   final String senderEmail,
                                   final String senderCargostar,
                                   final String senderTnt,
                                   final String senderFedex,
                                   final long senderCountryId,
                                   final long senderRegionId,
                                   final long senderCityId,
                                   final String senderAddress,
                                   final String senderZip,
                                   final String senderFirstName,
                                   final String senderMiddleName,
                                   final String senderLastName,
                                   final String senderPhone,
                                   final String recipientSignature,
                                   final String recipientEmail,
                                   final String recipientCargo,
                                   final String recipientTnt,
                                   final String recipientFedex,
                                   final long recipientCountryId,
                                   final long recipientRegionId,
                                   final long recipientCityId,
                                   final String recipientAddress,
                                   final String recipientZip,
                                   final String recipientFirstName,
                                   final String recipientMiddleName,
                                   final String recipientLastName,
                                   final String recipientPhone,
                                   final String payerEmail,
                                   final long payerCountryId,
                                   final long payerRegionId,
                                   final long payerCityId,
                                   final String payerAddress,
                                   final String payerZip,
                                   final String payerFirstName,
                                   final String payerMiddleName,
                                   final String payerLastName,
                                   final String payerPhone,
                                   final String payerCargostar,
                                   final String payerTnt,
                                   final String payerFedex,
                                   final double discount,
                                   final String checkingAccount,
                                   final String bank,
                                   final String mfo,
                                   final String oked,
                                   final String registrationCode,
                                   final String instructions,
                                   final long providerId,
                                   final long packagingId,
                                   final int deliveryType,
                                   final int paymentMethod,
                                   final double totalWeight,
                                   final double totalVolume,
                                   final double totalPrice,
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
                .putLong(Constants.KEY_OPERATOR_ID, operatorId)
                .putLong(Constants.KEY_ACCOUNTANT_ID, accountantId)

                .putString(Constants.KEY_SENDER_SIGNATURE, senderSignature)
                .putString(Constants.KEY_SENDER_EMAIL, senderEmail)
                .putString(Constants.KEY_SENDER_CARGOSTAR, senderCargostar)
                .putString(Constants.KEY_SENDER_TNT, senderTnt)
                .putString(Constants.KEY_SENDER_FEDEX, senderFedex)
                .putLong(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId)
                .putLong(Constants.KEY_SENDER_REGION_ID, senderRegionId)
                .putLong(Constants.KEY_SENDER_CITY_ID, senderCityId)
                .putString(Constants.KEY_SENDER_ADDRESS, senderAddress)
                .putString(Constants.KEY_SENDER_ZIP, senderZip)
                .putString(Constants.KEY_SENDER_FIRST_NAME, senderFirstName)
                .putString(Constants.KEY_SENDER_MIDDLE_NAME, senderMiddleName)
                .putString(Constants.KEY_SENDER_LAST_NAME, senderLastName)
                .putString(Constants.KEY_SENDER_PHONE, senderPhone)

                .putString(Constants.KEY_RECIPIENT_SIGNATURE, recipientSignature)
                .putString(Constants.KEY_RECIPIENT_EMAIL, recipientEmail)
                .putString(Constants.KEY_RECIPIENT_CARGOSTAR, recipientCargo)
                .putString(Constants.KEY_RECIPIENT_TNT, recipientTnt)
                .putString(Constants.KEY_RECIPIENT_FEDEX, recipientFedex)
                .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                .putLong(Constants.KEY_RECIPIENT_REGION_ID, recipientRegionId)
                .putLong(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId)
                .putString(Constants.KEY_RECIPIENT_ADDRESS, recipientAddress)
                .putString(Constants.RECIPIENT_ZIP, recipientZip)
                .putString(Constants.KEY_RECIPIENT_FIRST_NAME, recipientFirstName)
                .putString(Constants.KEY_RECIPIENT_MIDDLE_NAME, recipientMiddleName)
                .putString(Constants.KEY_RECIPIENT_LAST_NAME, recipientLastName)
                .putString(Constants.KEY_RECIPIENT_PHONE, recipientPhone)

                .putString(Constants.KEY_PAYER_EMAIL, payerEmail)
                .putLong(Constants.KEY_PAYER_COUNTRY_ID, payerCountryId)
                .putLong(Constants.KEY_PAYER_REGION_ID, payerRegionId)
                .putLong(Constants.KEY_PAYER_CITY_ID, payerCityId)
                .putString(Constants.KEY_PAYER_ADDRESS, payerAddress)
                .putString(Constants.KEY_PAYER_ZIP, payerZip)
                .putString(Constants.KEY_PAYER_FIRST_NAME, payerFirstName)
                .putString(Constants.KEY_PAYER_MIDDLE_NAME, payerMiddleName)
                .putString(Constants.KEY_PAYER_LAST_NAME, payerLastName)
                .putString(Constants.KEY_PAYER_PHONE, payerPhone)
                .putString(Constants.KEY_PAYER_CARGOSTAR, payerCargostar)
                .putString(Constants.KEY_PAYER_TNT, payerTnt)
                .putString(Constants.KEY_PAYER_FEDEX, payerFedex)
                .putDouble(Constants.KEY_DISCOUNT, discount)

                .putString(Constants.KEY_CHECKING_ACCOUNT, checkingAccount)
                .putString(Constants.KEY_BANK, bank)
                .putString(Constants.KEY_MFO, mfo)
                .putString(Constants.KEY_OKED, oked)
                .putString(Constants.KEY_REGISTRATION_CODE, registrationCode)

                .putString(Constants.KEY_INSTRUCTIONS, instructions)

                .putLong(Constants.KEY_PROVIDER_ID, providerId)
                .putLong(Constants.KEY_PACKAGING_ID, packagingId)
                .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                .putInt(Constants.KEY_PAYMENT_METHOD, paymentMethod)
                .putDouble(Constants.KEY_TOTAL_WEIGHT, totalWeight)
                .putDouble(Constants.KEY_TOTAL_VOLUME, totalVolume)
                .putDouble(Constants.KEY_TOTAL_PRICE, totalPrice)

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

    public static UUID searchInvoice(@NonNull final Context context, final long invoiceId) {
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
