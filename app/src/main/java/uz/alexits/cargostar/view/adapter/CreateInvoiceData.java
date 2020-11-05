package uz.alexits.cargostar.view.adapter;

import androidx.annotation.NonNull;

public class CreateInvoiceData {
    public static final int TYPE_HEADING = 1;
    public static final int TYPE_TWO_EDIT_TEXTS = 2;
    public static final int TYPE_EDIT_TEXT_SPINNER = 3;
    public static final int TYPE_STROKE = 4;
    public static final int TYPE_SINGLE_EDIT_TEXT = 5;
    public static final int TYPE_TWO_IMAGE_EDIT_TEXTS = 6;
    public static final int TYPE_SINGLE_IMAGE_EDIT_TEXT = 7;
    public static final int TYPE_ONE_IMAGE_EDIT_TEXT = 8;
    public static final int TYPE_BUTTON = 9;
    public static final int TYPE_CALCULATOR_ITEM = 10;
    public static final int TYPE_SINGLE_SPINNER = 11;
    public static final int TYPE_TWO_CARD_VIEWS = 12;
    public static final int TYPE_TWO_RADIO_BTNS = 13;
    public static final int TYPE_TWO_SPINNERS = 14;

    public static final int INPUT_TYPE_NUMBER_DECIMAL = 1;
    public static final int INPUT_TYPE_NUMBER = 2;
    public static final int INPUT_TYPE_TEXT = 3;
    public static final int INPUT_TYPE_EMAIL = 4;
    public static final int INPUT_TYPE_PHONE = 5;

    public String firstKey;
    public String secondKey;
    public String firstValue;
    public String secondValue;
    public int type;

    public final int firstInputType;
    public final int secondInputType;
    public boolean firstEnabled;
    public boolean secondEnabled;
    //for calc item
    public String index;
    public String packageType;
    public String weight;
    public String dimensions;

    public CreateInvoiceData(final int type) {
        firstInputType = -1;
        secondInputType = -1;
        this.type = type;
    }

    public CreateInvoiceData(final String firstKey,
                             final String secondKey,
                             final String firstValue,
                             final String secondValue,
                             final int type,
                             final int firstInputType,
                             final int secondInputType,
                             final boolean firstEnabled,
                             final boolean secondEnabled) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.type = type;
        this.firstInputType = firstInputType;
        this.secondInputType = secondInputType;
        this.firstEnabled = firstEnabled;
        this.secondEnabled = secondEnabled;
    }

    public CreateInvoiceData(final String firstKey,
                             final String secondKey,
                             final String firstValue,
                             final String secondValue,
                             final int type) {
        this(firstKey, secondKey, firstValue, secondValue, type, -1, -1, false, false);
    }


    @NonNull
    @Override
    public String toString() {
        return "CreateParcelData{" +
                "firstKey='" + firstKey + '\'' +
                ", secondKey='" + secondKey + '\'' +
                ", firstValue='" + firstValue + '\'' +
                ", secondValue='" + secondValue + '\'' +
                ", type=" + type +
                ", firstInputType=" + firstInputType +
                ", secondInputType=" + secondInputType +
                ", firstEnabled=" + firstEnabled +
                ", secondEnabled=" + secondEnabled +
                '}';
    }
}
