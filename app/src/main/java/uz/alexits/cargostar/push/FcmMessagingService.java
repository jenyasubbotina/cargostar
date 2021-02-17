package uz.alexits.cargostar.push;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.media.RingtoneManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FcmMessagingService extends FirebaseMessagingService {
    private static int notificationId = 0;
    private static String DEFAULT_CHANNEL_ID;
    private static String PACKAGE_NAME;
    private long courierId;

    @Override
    public void onCreate() {
        super.onCreate();
        DEFAULT_CHANNEL_ID = getString(R.string.implicit_notification_channel_id);
        PACKAGE_NAME = getPackageName();
        new Thread(() -> {
            courierId = SharedPrefs.getInstance(getApplicationContext()).getLong(SharedPrefs.ID);
        }).start();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i(TAG, "onNewToken(): " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i(TAG, "remoteMessage" +
                "\nfrom=" + remoteMessage.getFrom() +
                "\nto=" + remoteMessage.getTo() +
                "\noriginalPriority=" + remoteMessage.getOriginalPriority() +
                "\npriority=" + remoteMessage.getPriority() +
                "\nid=" + remoteMessage.getMessageId() +
                "\nsender_id=" + remoteMessage.getSenderId() +
                "\nttl=" + remoteMessage.getTtl() +
                "\ntype=" + remoteMessage.getMessageType());

        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "title=" + remoteMessage.getNotification().getTitle() + " body=" + remoteMessage.getNotification().getBody());
        }

        final Map<String, String> dataPayload = remoteMessage.getData();

        if (dataPayload.isEmpty()) {
            Log.e(TAG, "onMessageReceived(): dataPayload is empty");
            return;
        }
        Log.i(TAG, "dataPayload: " + remoteMessage.getData());

        final String title = dataPayload.get(Constants.KEY_TITLE);
        final String type = dataPayload.get(Constants.PUSH_TYPE);
        final String type2 = dataPayload.get(Constants.PUSH_SUBTYPE);
        String link = null;
        String body = null;
        String intentValue = null;

        if (type == null || TextUtils.isEmpty(type)) {
            Log.e(TAG, "onMessageReceived(): type is empty");
            return;
        }
        if (type2 == null || TextUtils.isEmpty(type2)) {
            Log.e(TAG, "onMessageReceived(): type2 is empty");
            return;
        }
        if (type.equalsIgnoreCase(Constants.TYPE_REQUEST)) {
            final Request request = parseRequest(dataPayload);

            if (type2.equalsIgnoreCase(Constants.SUBTYPE_NEW)) {
                Log.i(TAG, "onMessageReceived(): new request");
                if (request.getCourierId() == null) {
                    intentValue = IntentConstants.MAIN_ACTIVITY;
                    link = IntentConstants.REQUEST_PUBLIC_REQUESTS;
                }
                else if (request.getCourierId() == SharedPrefs.getInstance(getApplicationContext()).getLong(SharedPrefs.ID)) {
                    intentValue = IntentConstants.MAIN_ACTIVITY;
                    link = IntentConstants.REQUEST_MY_REQUESTS;
                }
            }
            else if (type2.equalsIgnoreCase(Constants.SUBTYPE_UPDATE)) {
                Log.i(TAG, "onMessageReceived(): request update");
                if (request.getCourierId() == null) {
                    intentValue = IntentConstants.MAIN_ACTIVITY;
                    link = IntentConstants.REQUEST_PUBLIC_REQUESTS;
                }
                else if (request.getCourierId() == SharedPrefs.getInstance(getApplicationContext()).getLong(SharedPrefs.ID)) {
                    intentValue = IntentConstants.MAIN_ACTIVITY;
                    link = IntentConstants.REQUEST_MY_REQUESTS;
                }
            }
            body = String.valueOf(request.getId());
            SyncWorkRequest.fetchRequestData(getApplicationContext(), request.getId(), request.getConsignmentQuantity());
        }
        else if (type.equalsIgnoreCase(Constants.TYPE_TRANSPORTATION)) {
            final Transportation transportation = parseTransportation(dataPayload);
            intentValue = IntentConstants.MAIN_ACTIVITY;

            if (type2.equalsIgnoreCase(Constants.SUBTYPE_NEW)) {
                Log.i(TAG, "onMessageReceived(): new transportation");
                try {
                    new Thread(() -> {
                        LocalCache.getInstance(getApplicationContext()).transportationDao().insertTransportation(transportation);
                    }).start();
                }
                catch (Exception e) {
                    Log.e(TAG, "insertTransportation(): ", e);
                }
            }
            else if (type2.equalsIgnoreCase(Constants.SUBTYPE_STATUS_UPDATE)) {
                Log.i(TAG, "onMessageReceived(): transportation status update");
            }
            body = String.valueOf(transportation.getId());
            link = IntentConstants.REQUEST_CURRENT_TRANSPORTATIONS;
        }
        if (intentValue == null) {
            return;
        }
        showMessage(title, body, PACKAGE_NAME, intentValue, link);
        final Notification notification = new Notification(title, body, false, intentValue, link, new Date());
        try {
            new Thread(() -> {
                LocalCache.getInstance(getApplicationContext()).notificationDao().createNotification(notification);
            }).start();
        }
        catch (Exception e) {
            Log.e(TAG, "insertNotification(): ", e);
        }
    }

    public void showMessage(final String title, final String body, final String packageName, final String intentValue, final String link) {
        if (packageName != null && intentValue != null && link != null) {
            final Intent notifyIntent = new Intent();
            notifyIntent.setComponent(new ComponentName(packageName, packageName + intentValue));
            notifyIntent.putExtra(IntentConstants.INTENT_PUSH_KEY, link);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntentWithParentStack(notifyIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);

            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), DEFAULT_CHANNEL_ID);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(body);
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            notificationBuilder.setOnlyAlertOnce(true);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(R.drawable.img_cargostar_logo);
            notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            NotificationManagerCompat.from(this).notify(notificationId++, notificationBuilder.build());
        }
    }

    private Request parseRequest(final Map<String, String> dataPayload) {
        final String requestId = dataPayload.get(Constants.KEY_ID);
        final String senderCountryId = dataPayload.get(Constants.KEY_SENDER_COUNTRY_ID);
        final String senderRegionId = dataPayload.get(Constants.KEY_SENDER_REGION_ID);
        final String senderCityId = dataPayload.get(Constants.KEY_SENDER_CITY_ID);
        final String userId = dataPayload.get(Constants.KEY_USER_ID);
        final String senderId = dataPayload.get(Constants.KEY_CLIENT_ID);
        final String courierId = dataPayload.get(Constants.KEY_EMPLOYEE_ID);
        final String providerId = dataPayload.get(Constants.KEY_PROVIDER_ID);
        final String invoiceId = dataPayload.get(Constants.KEY_INVOICE_ID);
        final String senderFirstName = dataPayload.get(Constants.KEY_SENDER_FIRST_NAME);
        final String senderMiddleName = dataPayload.get(Constants.KEY_SENDER_MIDDLE_NAME);
        final String senderLastName = dataPayload.get(Constants.KEY_SENDER_LAST_NAME);
        final String senderEmail = dataPayload.get(Constants.KEY_EMAIL);
        final String senderPhone = dataPayload.get(Constants.KEY_SENDER_PHONE);
        final String senderAddress = dataPayload.get(Constants.KEY_SENDER_ADDRESS);
        final String recipientCountryId = dataPayload.get(Constants.KEY_RECIPIENT_COUNTRY_ID);
        final String recipientCityId = dataPayload.get(Constants.KEY_RECIPIENT_CITY_ID);
        final String comment = dataPayload.get(Constants.KEY_COMMENT);
        final String status = dataPayload.get(Constants.KEY_STATUS);
        final String createdAt = dataPayload.get(Constants.KEY_CREATED_AT);
        final String updatedAt = dataPayload.get(Constants.KEY_UPDATED_AT);
        final String cityFrom = dataPayload.get(Constants.KEY_SENDER_CITY);
        final String cityTo = dataPayload.get(Constants.KEY_RECIPIENT_CITY);
        final String paymentStatus = dataPayload.get(Constants.KEY_PAYMENT_STATUS);
        final String deliveryType = dataPayload.get(Constants.KEY_DELIVERY_TYPE);
        final String consignmentQuantity = dataPayload.get(Constants.KEY_CONSIGNMENT_QUANTITY);

        final long request_id = requestId != null && !TextUtils.isEmpty(requestId) ? Long.parseLong(requestId.trim()) : -1L;
        final Long sender_country_id = senderCountryId != null && !TextUtils.isEmpty(senderCountryId) ? Long.parseLong(senderCountryId.trim()) : null;
        final Long sender_region_id = senderRegionId != null && !TextUtils.isEmpty(senderRegionId) ? Long.parseLong(senderRegionId.trim()) : null;
        final Long sender_city_id = senderCityId != null && !TextUtils.isEmpty(senderCityId) ? Long.parseLong(senderCityId.trim()) : null;
        final Long recipient_country_id = recipientCountryId != null && !TextUtils.isEmpty(recipientCountryId) ? Long.parseLong(recipientCountryId.trim()) : null;
        final Long recipient_city_id = recipientCityId != null && !TextUtils.isEmpty(recipientCityId) ? Long.parseLong(recipientCityId.trim()) : null;
        final Long user_id = userId != null && !TextUtils.isEmpty(userId) ? Long.parseLong(userId.trim()) : null;
        final Long sender_id = senderId != null && !TextUtils.isEmpty(senderId) ? Long.parseLong(senderId.trim()) : null;
        final Long courier_id = courierId != null && !TextUtils.isEmpty(courierId) ? Long.parseLong(courierId.trim()) : null;
        final Long provider_id = providerId != null && !TextUtils.isEmpty(providerId) ? Long.parseLong(providerId.trim()) : null;
        final Long invoice_id = invoiceId != null && !TextUtils.isEmpty(invoiceId) ? Long.parseLong(invoiceId.trim()) : null;
        final Long created_at = createdAt != null && !TextUtils.isEmpty(createdAt) ? Long.parseLong(createdAt.trim()) : null;
        final Long updated_at = updatedAt != null && !TextUtils.isEmpty(updatedAt) ? Long.parseLong(updatedAt.trim()) : null;
        final int delivery_type = deliveryType != null && !TextUtils.isEmpty(deliveryType) ? Integer.parseInt(deliveryType) : 0;

        final String senderCityName = dataPayload.get("city_name");
        final String recipientCityName = dataPayload.get("city_to_name");

        return new Request(
                request_id,
                senderFirstName,
                senderMiddleName,
                senderLastName,
                senderEmail,
                senderPhone,
                senderAddress,
                sender_country_id,
                sender_region_id,
                sender_city_id,
                senderCityName,
                recipient_country_id,
                recipient_city_id,
                recipientCityName,
                comment,
                user_id,
                sender_id,
                courier_id,
                provider_id,
                invoice_id,
                1,
                new Date(),
                new Date(),
                cityFrom,
                cityTo,
                consignmentQuantity != null ? Integer.parseInt(consignmentQuantity) : 0,
                paymentStatus,
                delivery_type);
    }

    private Transportation parseTransportation(final Map<String, String> dataPayload) {
        final String transportationId = dataPayload.get(Constants.KEY_ID);
        final String courierId = dataPayload.get(Constants.KEY_EMPLOYEE_ID);
        final String providerId = dataPayload.get(Constants.KEY_PROVIDER_ID);
        final String invoiceId = dataPayload.get(Constants.KEY_INVOICE_ID);
        final String requestId = dataPayload.get(Constants.KEY_REQUEST_ID);
        final String brancheId = dataPayload.get(Constants.KEY_BRANCHE_ID);
        final String transportationStatusId = dataPayload.get(Constants.KEY_TRANSPORTATION_STATUS);
        final String paymentStatusId = dataPayload.get(Constants.KEY_PAYMENT_STATUS_ID);
        final String currentTransitPointId = dataPayload.get(Constants.KEY_CURRENT_TRANSIT_POINT_ID);
        final String partialId = dataPayload.get(Constants.KEY_CURRENT_TRANSIT_POINT_ID);
        final String arrivalDate = dataPayload.get(Constants.KEY_ARRIVAL_DATE);
        final String trackingCode = dataPayload.get(Constants.KEY_TRACKING_CODE);
        final String qrCode = dataPayload.get(Constants.KEY_QR_CODE);
        final String partyQrCode = dataPayload.get(Constants.KEY_PARTY_QR_CODE);
        final String instructions = dataPayload.get(Constants.KEY_INSTRUCTIONS);
        final String direction = dataPayload.get(Constants.KEY_DIRECTION);
        final String cityFrom = dataPayload.get(Constants.KEY_CITY_FROM);
        final String cityTo = dataPayload.get(Constants.KEY_CITY_TO);
        final String statusName = dataPayload.get(Constants.KEY_STATUS_NAME);
        final String status = dataPayload.get(Constants.KEY_STATUS);
        final String createdAt = dataPayload.get(Constants.KEY_CREATED_AT);
        final String updatedAt = dataPayload.get(Constants.KEY_UPDATED_AT);

        final long transportation_id = transportationId != null && !TextUtils.isEmpty(transportationId) ? Long.parseLong(transportationId.trim()) : -1L;
        final Long provider_id = providerId != null && !TextUtils.isEmpty(providerId) ? Long.parseLong(providerId.trim()) : null;
        final Long courier_id = courierId != null && !TextUtils.isEmpty(courierId) ? Long.parseLong(courierId.trim()) : null;
        final Long invoice_id = invoiceId != null && !TextUtils.isEmpty(invoiceId) ? Long.parseLong(invoiceId.trim()) : null;
        final Long request_id = requestId != null && !TextUtils.isEmpty(requestId) ? Long.parseLong(requestId.trim()) : null;
        final Long branche_id = brancheId != null && !TextUtils.isEmpty(brancheId) ? Long.parseLong(brancheId.trim()) : null;
        final Long transportation_status_id = transportationStatusId != null && !TextUtils.isEmpty(transportationStatusId) ? Long.parseLong(transportationStatusId.trim()) : null;
        final Long payment_status_id = paymentStatusId != null && !TextUtils.isEmpty(paymentStatusId) ? Long.parseLong(paymentStatusId.trim()) : null;
        final Long partial_id = partialId != null && !TextUtils.isEmpty(partialId) ? Long.parseLong(partialId.trim()) : null;
        final Long current_transit_point_id = currentTransitPointId != null && !TextUtils.isEmpty(currentTransitPointId) ? Long.parseLong(currentTransitPointId.trim()) : null;
        final Long created_at = createdAt != null && !TextUtils.isEmpty(createdAt) ? Long.parseLong(createdAt.trim()) : null;
        final Long updated_at = updatedAt != null && !TextUtils.isEmpty(updatedAt) ? Long.parseLong(updatedAt.trim()) : null;

        final Transportation transportation = new Transportation(
                transportation_id,
                provider_id,
                courier_id,
                invoice_id,
                request_id,
                branche_id,
                transportation_status_id,
                payment_status_id,
                current_transit_point_id,
                partial_id,
                arrivalDate,
                trackingCode,
                qrCode,
                partyQrCode,
                instructions,
                direction,
                status != null && !TextUtils.isEmpty(status) ? Integer.parseInt(status) : 1,
                new Date(),
                new Date());
        transportation.setCityTo(cityFrom);
        transportation.setCityTo(cityTo);
        transportation.setTransportationStatusName(statusName);
        return transportation;
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
        Log.i(TAG, "onMessageSent(): " + s);
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
        Log.e(TAG, "onSendError(): ", e);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static final String TAG = FcmMessagingService.class.toString();
}
