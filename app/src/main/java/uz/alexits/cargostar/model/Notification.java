package uz.alexits.cargostar.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import uz.alexits.cargostar.model.location.Address;
import uz.alexits.cargostar.model.shipping.Invoice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "notification",
        foreignKeys = {@ForeignKey(entity = Invoice.class, parentColumns = "id", childColumns = "receipt_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "receipt_id", unique = true)})
public class Notification {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

    @PrimaryKey @ColumnInfo(name = "receipt_id") private long receiptId;
    @ColumnInfo(name = "courier_id", defaultValue = "-1") private long courierId;
    @Embedded(prefix = "sender_")  @NonNull private final Address senderAddress;
    @Embedded(prefix = "recipient_") @Nullable private Address recipientAddress;
    @ColumnInfo(name = "is_read", defaultValue = "false") private boolean isRead;
    @ColumnInfo(name = "title") @NonNull private String title;
    @ColumnInfo(name = "link") @NonNull private String link;
    @ColumnInfo(name = "receive_date") @NonNull private final Date receiveDate;


    public Notification(final long receiptId,
                        final long courierId,
                        @NonNull final Address senderAddress,
                        @Nullable final Address recipientAddress,
                        @NonNull final Date receiveDate,
                        final boolean isRead) {
        this.receiptId = receiptId;
        this.courierId = courierId;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.receiveDate = receiveDate;
        this.isRead = isRead;
        this.title = "Новая заявка в ";
        if (courierId > 0) {
            this.link = "Мои заявки";
        }
        else {
            this.link = "Общие заявки";
        }
    }

    public void setRecipientAddress(@Nullable Address recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setLink(@NonNull String link) {
        this.link = link;
    }

    public long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(long receiptId) {
        this.receiptId = receiptId;
    }

    @NonNull
    public Address getSenderAddress() {
        return senderAddress;
    }

    @Nullable
    public Address getRecipientAddress() {
        return recipientAddress;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getLink() {
        return link;
    }

    @NonNull
    public Date getReceiveDate() {
        return receiveDate;
    }

    public long getCourierId() {
        return courierId;
    }

    public void setCourierId(long courierId) {
        this.courierId = courierId;
    }
}
