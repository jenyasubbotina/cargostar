package uz.alexits.cargostar.model.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.calculation.Provider;

import java.util.Date;

@Entity(tableName = "invoice",
        foreignKeys = {
        @ForeignKey(entity = Customer.class, parentColumns = "id", childColumns = "sender_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = AddressBook.class, parentColumns = "id", childColumns = "recipient_id", onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = AddressBook.class, parentColumns = "id", childColumns = "payer_id", onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Request.class, parentColumns = "id", childColumns = "request_id", onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "sender_id"),
                @Index(value = "recipient_id"),
                @Index(value = "payer_id"),
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
    private final Long senderId;

    @Expose
    @SerializedName("recipient")
    @ColumnInfo(name = "recipient_id")
    private final Long recipientId;

    @Expose
    @SerializedName("payer")
    @ColumnInfo(name = "payer_id")
    private final Long payerId;

    @Expose
    @SerializedName("provider")
    @ColumnInfo(name = "provider_id")
    private final Long providerId;

    @Expose
    @SerializedName("request_id")
    @ColumnInfo(name = "request_id")
    private final Long requestId;

    @Expose
    @SerializedName("signature")
    @ColumnInfo(name = "sender_signature")
    private String senderSignatureUrl;

    @Expose
    @SerializedName("recipient_signature")
    @ColumnInfo(name = "recipient_signature")
    private String recipientSignatureUrl;

    @Expose
    @SerializedName("tarif")
    @ColumnInfo(name = "tariff_id")
    private final Long tariffId;

    @Expose
    @SerializedName("price")
    @ColumnInfo(name = "price")
    private final double price;

    @Expose
    @SerializedName("status")
    @ColumnInfo(name = "status")
    private final int status;

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private final Date createdAt;

    @Expose
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    private final Date updatedAt;

    public Invoice(final long id,
                   final String number,
                   final Long senderId,
                   final Long recipientId,
                   final String recipientSignatureUrl,
                   final Long payerId,
                   final Long providerId,
                   final Long requestId,
                   final Long tariffId,
                   final double price,
                   final int status,
                   final Date createdAt,
                   final Date updatedAt) {
        this.id = id;
        this.number = number;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.recipientSignatureUrl = recipientSignatureUrl;
        this.payerId = payerId;
        this.providerId = providerId;
        this.requestId = requestId;
        this.tariffId = tariffId;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public Long getPayerId() {
        return payerId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public double getPrice() {
        return price;
    }

    public String getSenderSignatureUrl() {
        return senderSignatureUrl;
    }

    public String getRecipientSignatureUrl() {
        return recipientSignatureUrl;
    }

    public void setRecipientSignatureUrl(String recipientSignatureUrl) {
        this.recipientSignatureUrl = recipientSignatureUrl;
    }

    public void setSenderSignatureUrl(String senderSignatureUrl) {
        this.senderSignatureUrl = senderSignatureUrl;
    }

    public int getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", recipientSignatureUrl=" + recipientSignatureUrl +
                ", payerId=" + payerId +
                ", providerId=" + providerId +
                ", requestId=" + requestId +
                ", tariffId=" + tariffId +
                ", price=" + price +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
