package uz.alexits.cargostar.utils;

public class IntentConstants {
    public static final String INTENT_REQUEST_KEY = "intent_request_key";
    public static final String INTENT_PUSH_KEY = "intent_push_key";
    public static final int REQUEST_UPLOAD_PHOTO = 0x01;
    public static final int REQUEST_UPLOAD_DOCUMENT = 0x02;
    public static final int REQUEST_SENDER_SIGNATURE = 0x03;
    public static final int REQUEST_RECIPIENT_SIGNATURE = 0x04;
    public static final int REQUEST_FIND_REQUEST = 0x06;
    public static final int REQUEST_EDIT_INVOICE = 0x07;
    public static final int REQUEST_FIND_CUSTOMER = 0x08;
    public static final int REQUEST_SCAN_QR_PARCEL = 0x11;
    public static final int REQUEST_SCAN_QR_CARGO = 0x12;
    public static final int REQUEST_SCAN_QR_MENU = 0x13;

    public static final String INTENT_REQUEST_VALUE = "intent_request_value";
    public static final String INTENT_RESULT_VALUE = "intent_result_value";
    public static final String INTENT_REQUEST_OR_PARCEL = "intent_request_or_parcel";
    public static final int INTENT_TRANSPORTATION = 0x01;
    public static final int INTENT_REQUEST = 0x02;

    public static final int FRAGMENT_CURRENT_TRANSPORT = 0x01;
    public static final int FRAGMENT_DELIVERY_TRANSPORT = 0x02;

    public static final String NOTIFICATIONS_ACTIVITY = ".view.activity.NotificationsActivity";
    public static final String REQUEST_PUBLIC_REQUESTS = "PUBLIC_REQUESTS";
    public static final String REQUEST_MY_REQUESTS = "MY_REQUESTS";
    public static final String REQUEST_CURRENT_TRANSPORTATIONS = "CURRENT_TRANSPORTATIONS";
    /* FCM */
    public static final String TOKEN = "token";
    public static final String REQUEST_FIND_TRANSPORTATION = "intent_request_find_transportation";
}
