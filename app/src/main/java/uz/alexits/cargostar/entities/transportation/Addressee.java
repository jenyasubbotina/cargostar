package uz.alexits.cargostar.entities.transportation;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Addressee {
    @Expose
    @SerializedName("addressee_full_name")
    @ColumnInfo(name = "addressee_full_name")
    private final String fullName;

    @Expose
    @SerializedName("addressee_phone")
    @ColumnInfo(name = "addressee_phone")
    private final String phone;

    @Expose
    @SerializedName("addressee_address")
    @ColumnInfo(name = "addressee_address")
    private final String address;

    @Expose
    @SerializedName("addressee_company")
    @ColumnInfo(name = "addressee_company")
    private final String organization;

    @Expose
    @SerializedName("addressee_comment")
    @ColumnInfo(name = "addressee_comment")
    private final String comment;

    @Expose
    @SerializedName("recipient_signature")
    @ColumnInfo(name = "recipient_signature")
    private final String recipientSignature;

    @Expose
    @SerializedName("recipient_signature_date")
    @ColumnInfo(name = "addressee_signature_date")
    private final String recipientSignatureDate;

    @Expose
    @SerializedName("addressee_result")
    @ColumnInfo(name = "addressee_result")
    private final boolean accepted;

    public Addressee(final String fullName,
                     final String phone,
                     final String address,
                     final String organization,
                     final String comment,
                     final String recipientSignature,
                     final String recipientSignatureDate,
                     final boolean accepted) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.organization = organization;
        this.comment = comment;
        this.recipientSignature = recipientSignature;
        this.recipientSignatureDate = recipientSignatureDate;
        this.accepted = accepted;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getOrganization() {
        return organization;
    }

    public String getComment() {
        return comment;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getRecipientSignature() {
        return recipientSignature;
    }

    public String getRecipientSignatureDate() {
        return recipientSignatureDate;
    }
}
