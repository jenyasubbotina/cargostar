package uz.alexits.cargostar.utils;

public class IntentConstants {
    public static final String INTENT_REQUEST_KEY = "intent_request_key";
    public static final String INTENT_PUSH_KEY = "intent_push_key";
    public static final int REQUEST_UPLOAD_PHOTO = 0x01;
    public static final int REQUEST_UPLOAD_DOCUMENT = 0x02;
    public static final int REQUEST_SENDER_SIGNATURE = 0x03;
    public static final int REQUEST_RECIPIENT_SIGNATURE = 0x04;
    public static final int REQUEST_FIND_INVOICE = 0x06;
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

    public static final String STATUS_FLAG = "status_flag";
    public static final int IN_TRANSIT = 0x01;
    public static final int ON_THE_WAY = 0x02;
    public static final int DELIVERED = 0x03;
    public static final int LOST = 0x04;

    public static final String REQUEST_PUBLIC_REQUESTS = ".view.fragment.PublicRequestsFragment";
    public static final String REQUEST_MY_REQUESTS = ".view.fragment.MyRequestsFragment";
    public static final String REQUEST_CURRENT_TRANSPORTATIONS = ".view.fragment.CurrentTransportationsFragment";
    /* FCM */
    public static final String TOKEN = "token";
    public static final String REQUEST_FIND_TRANSPORTATION = "intent_request_find_transportation";
}
