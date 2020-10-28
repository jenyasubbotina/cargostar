package uz.alexits.cargostar.view.adapter;

import androidx.annotation.NonNull;

public class CreateParcelData {
    public static final int TYPE_HEADING = 0x02;
    public static final int TYPE_TWO_EDIT_TEXTS = 0x03;
    public static final int TYPE_EDIT_TEXT_SPINNER = 0x04;
    public static final int TYPE_STROKE = 0x05;
    public static final int TYPE_SINGLE_EDIT_TEXT = 0x06;
    public static final int TYPE_TWO_IMAGE_EDIT_TEXTS = 0x07;
    public static final int TYPE_SINGLE_IMAGE_EDIT_TEXT = 0x08;
    public static final int TYPE_ONE_IMAGE_EDIT_TEXT = 0x09;
    public static final int TYPE_BUTTON = 0x10;
    public static final int TYPE_CALCULATOR_ITEM = 0x11;
    public static final int TYPE_SINGLE_SPINNER = 0x12;

    public static final int INPUT_TYPE_NUMBER_DECIMAL = 0x01;
    public static final int INPUT_TYPE_NUMBER = 0x02;
    public static final int INPUT_TYPE_TEXT = 0x03;
    public static final int INPUT_TYPE_EMAIL = 0x04;
    public static final int INPUT_TYPE_PHONE = 0x05;

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
    public String source;
    public String destination;
    public String weight;
    public String dimensions;

    public CreateParcelData(final int type) {
        firstInputType = -1;
        secondInputType = -1;
        this.type = type;
    }

    public CreateParcelData(final String firstKey,
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

    public CreateParcelData(final String firstKey,
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
