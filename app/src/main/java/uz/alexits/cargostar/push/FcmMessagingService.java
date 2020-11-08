package uz.alexits.cargostar.push;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.media.RingtoneManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;

public class FcmMessagingService extends FirebaseMessagingService {
    private static int notificationId = 0;
    private static String DEFAULT_CHANNEL_ID;
    private static String PACKAGE_NAME;

    private static String PUBLIC_REQUESTS_BODY;
    private static String MY_REQUESTS_BODY;
    private static String TRANSPORTATION_NEW_BODY;
    private static String TRANSPORTATION_STATUS_CHANGE_BODY;

    private static String INTENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();
        DEFAULT_CHANNEL_ID = getString(R.string.implicit_notification_channel_id);
        PACKAGE_NAME = getPackageName();

        PUBLIC_REQUESTS_BODY = getString(R.string.public_requests_body);
        MY_REQUESTS_BODY = getString(R.string.my_requests_body);
        TRANSPORTATION_NEW_BODY = getString(R.string.new_transportation);
        TRANSPORTATION_STATUS_CHANGE_BODY = getString(R.string.transportation_status_changed);

        INTENT_KEY = Constants.MAIN_ACTIVITY_FRAGMENT;
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
        String link = ".view.activity.MainActivity";
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
                    body = PUBLIC_REQUESTS_BODY;
                    intentValue = Constants.KEY_PUBLIC_REQUESTS;
                }
                else if (request.getCourierId() == SharedPrefs.getInstance(getApplicationContext()).getLong(SharedPrefs.ID)) {
                    body = MY_REQUESTS_BODY;
                    intentValue = Constants.KEY_MY_REQUESTS;
                }
            }
            else if (type2.equalsIgnoreCase(Constants.SUBTYPE_UPDATE)) {
                Log.i(TAG, "onMessageReceived(): request update");
            }
            //todo: insert request through worker
        }
        else if (type.equalsIgnoreCase(Constants.TYPE_TRANSPORTATION)) {
            final Transportation transportation = parseTransportation(dataPayload);
            intentValue = Constants.KEY_CURRENT_TRANSPORTATION;

            if (type2.equalsIgnoreCase(Constants.SUBTYPE_NEW)) {
                Log.i(TAG, "onMessageReceived(): new transportation");
                body = TRANSPORTATION_NEW_BODY;
            }
            else if (type2.equalsIgnoreCase(Constants.SUBTYPE_STATUS_UPDATE)) {
                Log.i(TAG, "onMessageReceived(): transportation status update");
                body = TRANSPORTATION_STATUS_CHANGE_BODY;
            }
            //todo: insert transportation through worker
        }
        if (intentValue == null || body == null) {
            return;
        }
        showMessage(title, body, PACKAGE_NAME, link, intentValue);
        //todo: insert notification into cache through new Thread or Worker
    }

    public void showMessage(final String title,
                            final String body,
                            final String packageName,
                            final String link,
                            final String intentValue) {
        if (packageName != null && link != null && intentValue != null) {
            final Intent notifyIntent = new Intent();
            notifyIntent.setComponent(new ComponentName(packageName, packageName + link));
            notifyIntent.putExtra(INTENT_KEY, intentValue);
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

        return new Request(
                requestId != null && !TextUtils.isEmpty(requestId) ? Long.parseLong(requestId) : -1L,
                senderFirstName,
                senderMiddleName,
                senderLastName,
                senderEmail,
                senderPhone,
                senderAddress,
                senderCountryId != null && !TextUtils.isEmpty(senderCountryId) ? Long.parseLong(senderCountryId) : null,
                senderRegionId != null && !TextUtils.isEmpty(senderRegionId) ? Long.parseLong(senderRegionId) : null,
                senderCityId != null && !TextUtils.isEmpty(senderCityId) ? Long.parseLong(senderCityId) : null,
                recipientCountryId != null && !TextUtils.isEmpty(recipientCountryId) ? Long.parseLong(recipientCountryId) : null,
                recipientCityId != null && !TextUtils.isEmpty(recipientCityId) ? Long.parseLong(recipientCityId) : null,
                comment,
                userId != null && !TextUtils.isEmpty(userId) ? Long.parseLong(userId) : null,
                senderId != null && !TextUtils.isEmpty(senderId) ? Long.parseLong(senderId) : null,
                courierId != null && !TextUtils.isEmpty(courierId) ? Long.parseLong(courierId) : null,
                providerId != null && !TextUtils.isEmpty(providerId) ? Long.parseLong(providerId) : null,
                invoiceId != null && !TextUtils.isEmpty(invoiceId) ? Long.parseLong(invoiceId) : null,
                status != null && !TextUtils.isEmpty(status) ? Integer.parseInt(status) : -1,
                new Date(createdAt),
                new Date(updatedAt));
    }

    private Transportation parseTransportation(final Map<String, String> dataPayload) {
        final String transportationId = dataPayload.get(Constants.KEY_ID);
        final String courierId = dataPayload.get(Constants.KEY_EMPLOYEE_ID);
        final String providerId = dataPayload.get(Constants.KEY_PROVIDER_ID);
        final String invoiceId = dataPayload.get(Constants.KEY_INVOICE_ID);
        final String transportationStatusId = dataPayload.get(Constants.KEY_TRANSPORTATION_STATUS);
        final String paymentStatusId = dataPayload.get(Constants.KEY_PAYMENT_STATUS_ID);
        final String currentTransitPointId = dataPayload.get(Constants.KEY_CURRENT_TRANSIT_POINT_ID);
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

        final Transportation transportation = new Transportation(
                transportationId != null && !TextUtils.isEmpty(transportationId) ? Long.parseLong(transportationId) : -1L,
                providerId != null && !TextUtils.isEmpty(providerId) ? Long.parseLong(providerId) : null,
                courierId != null && !TextUtils.isEmpty(courierId) ? Long.parseLong(courierId) : null,
                invoiceId != null && !TextUtils.isEmpty(invoiceId) ? Long.parseLong(invoiceId) : null,
                transportationStatusId != null && !TextUtils.isEmpty(transportationStatusId) ? Long.parseLong(transportationStatusId) : null,
                paymentStatusId != null && !TextUtils.isEmpty(paymentStatusId) ? Long.parseLong(paymentStatusId) : null,
                currentTransitPointId != null && !TextUtils.isEmpty(currentTransitPointId) ? Long.parseLong(currentTransitPointId) : null,
                arrivalDate,
                trackingCode,
                qrCode,
                partyQrCode,
                instructions,
                direction,
                status != null && !TextUtils.isEmpty(status) ? Integer.parseInt(status) : -1,
                new Date(createdAt),
                new Date(updatedAt));
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
