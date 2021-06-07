package uz.alexits.cargostar.entities.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.entities.calculation.Provider;

@Entity(tableName = "invoice",
        foreignKeys = {
        @ForeignKey(entity = Client.class, parentColumns = "id", childColumns = "sender_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Request.class, parentColumns = "id", childColumns = "request_id", onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "sender_id"),
                @Index(value = "provider_id"),
                @Index(value = "request_id")})
public class Invoice {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("number")
    @ColumnInfo(name = "number")
    private final String number;

    @Expose
    @SerializedName("sender")
    @ColumnInfo(name = "sender_id")
    private final long senderId;

    @Expose
    @SerializedName("recipient")
    @ColumnInfo(name = "recipient_id")
    private final long recipientId;

    @Expose
    @SerializedName("payer")
    @ColumnInfo(name = "payer_id")
    private final long payerId;

    @Expose
    @SerializedName("provider")
    @ColumnInfo(name = "provider_id")
    private final long providerId;

    @Expose
    @SerializedName("request_id")
    @ColumnInfo(name = "request_id")
    private final long requestId;

    @Expose
    @SerializedName("signature")
    @ColumnInfo(name = "sender_signature")
    private String senderSignatureUrl;

    @Expose
    @SerializedName("recipient_signature")
    @ColumnInfo(name = "recipient_signature")
    private final String recipientSignature;

    @Expose
    @SerializedName("tarif")
    @ColumnInfo(name = "tariff_id")
    private final long tariffId;

    @Expose
    @SerializedName("price")
    @ColumnInfo(name = "price")
    private final double price;

    @Expose
    @SerializedName("payment")
    @ColumnInfo(name = "payment_method")
    private final int paymentMethod;

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
    @SerializedName("recipient_signature_date")
    @ColumnInfo(name = "addressee_signature_date")
    private final String recipientSignatureDate;

    @Expose
    @SerializedName("addressee_result")
    @ColumnInfo(name = "addressee_result")
    private final int accepted;

    public Invoice(final long id,
                   final String number,
                   final long senderId,
                   final long recipientId,
                   final long payerId,
                   final long providerId,
                   final long requestId,
                   final long tariffId,
                   final double price,
                   final int paymentMethod,
                   final String fullName,
                   final String phone,
                   final String address,
                   final String organization,
                   final String comment,
                   final String recipientSignature,
                   final String recipientSignatureDate,
                   final int accepted) {
        this.id = id;
        this.number = number;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.payerId = payerId;
        this.providerId = providerId;
        this.requestId = requestId;
        this.tariffId = tariffId;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.organization = organization;
        this.comment = comment;
        this.recipientSignature = recipientSignature;
        this.recipientSignatureDate = recipientSignatureDate;
        this.accepted = accepted;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public long getPayerId() {
        return payerId;
    }

    public long getProviderId() {
        return providerId;
    }

    public long getRequestId() {
        return requestId;
    }

    public long getTariffId() {
        return tariffId;
    }

    public double getPrice() {
        return price;
    }

    public String getSenderSignatureUrl() {
        return senderSignatureUrl;
    }

    public void setSenderSignatureUrl(final String senderSignatureUrl) {
        this.senderSignatureUrl = senderSignatureUrl;
    }

    public int getPaymentMethod() {
        return paymentMethod;
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

    public int getAccepted() {
        return accepted;
    }

    public String getRecipientSignature() {
        return recipientSignature;
    }

    public String getRecipientSignatureDate() {
        return recipientSignatureDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", recipientSignature=" + recipientSignature +
                ", payerId=" + payerId +
                ", providerId=" + providerId +
                ", requestId=" + requestId +
                ", tariffId=" + tariffId +
                ", price=" + price +
                '}';
    }
}
