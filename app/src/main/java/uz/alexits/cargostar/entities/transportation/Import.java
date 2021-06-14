package uz.alexits.cargostar.entities.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@DatabaseView("SELECT i.id AS invoice_id, " +
        "i.addressee_full_name, " +
        "i.addressee_phone, " +
        "i.addressee_address, " +
        "i.addressee_company, " +
        "i.recipient_signature, " +
        "i.addressee_signature_date, " +
        "i.addressee_comment, " +
        "i.addressee_result, " +
        "t.import_status, " +
        "t.transportation_type, " +
        "t.tracking_code, " +
        "r.courier_id, " +
        "i.created_at " +
        "FROM invoice i INNER JOIN transportation t ON i.id = t.invoice_id INNER JOIN request r ON i.id = r.invoice_id " +
        "WHERE t.transportation_type == 2 ORDER BY invoice_id DESC")
public class Import {
    @ColumnInfo(name = "invoice_id")
    private final long invoiceId;

    @ColumnInfo(name = "tracking_code")
    private final String trackingCode;

    @ColumnInfo(name = "addressee_full_name")
    private final String fullName;

    @ColumnInfo(name = "addressee_phone")
    private final String phone;

    @ColumnInfo(name = "addressee_address")
    private final String address;

    @ColumnInfo(name = "addressee_company")
    private final String organization;

    @ColumnInfo(name = "addressee_comment")
    private final String comment;

    @ColumnInfo(name = "recipient_signature")
    private final String recipientSignature;

    @ColumnInfo(name = "addressee_signature_date")
    private final String recipientSignatureDate;

    @ColumnInfo(name = "addressee_result")
    private final int accepted;

    @ColumnInfo(name = "transportation_type")
    private final int transportationType;

    @ColumnInfo(name = "import_status")
    private final String importStatus;

    @ColumnInfo(name = "courier_id")
    private final long courierId;

    @ColumnInfo(name = "created_at")
    private final long createdAt;

    public Import(final long invoiceId,
                  final String trackingCode,
                  final String fullName,
                  final String phone,
                  final String address,
                  final String organization,
                  final String comment,
                  final String recipientSignature,
                  final String recipientSignatureDate,
                  final int accepted,
                  final int transportationType,
                  final String importStatus,
                  final long courierId,
                  final long createdAt) {
        this.invoiceId = invoiceId;
        this.trackingCode = trackingCode;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.organization = organization;
        this.comment = comment;
        this.recipientSignature = recipientSignature;
        this.recipientSignatureDate = recipientSignatureDate;
        this.accepted = accepted;
        this.transportationType = transportationType;
        this.importStatus = importStatus;
        this.courierId = courierId;
        this.createdAt = createdAt;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public String getTrackingCode() {
        return trackingCode;
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

    public String getRecipientSignature() {
        return recipientSignature;
    }

    public String getRecipientSignatureDate() {
        return recipientSignatureDate;
    }

    public int getAccepted() {
        return accepted;
    }

    public int getTransportationType() {
        return transportationType;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public long getCourierId() {
        return courierId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "Import{" +
                "invoiceId=" + invoiceId +
                ", trackingCode=" + trackingCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", organization='" + organization + '\'' +
                ", comment='" + comment + '\'' +
                ", recipientSignature='" + recipientSignature + '\'' +
                ", recipientSignatureDate='" + recipientSignatureDate + '\'' +
                ", accepted=" + accepted +
                ", transportationType=" + transportationType +
                ", importStatus='" + importStatus + '\'' +
                ", courierId=" + courierId +
                ", createdAt=" + createdAt +
                '}';
    }
}
