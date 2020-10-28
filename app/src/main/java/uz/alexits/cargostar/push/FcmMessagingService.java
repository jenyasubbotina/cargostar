package uz.alexits.cargostar.push;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        super.onCreate();
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
        if (!remoteMessage.getData().isEmpty()) {
            Log.i(TAG, "dataPayload: " + remoteMessage.getData());
        }
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
