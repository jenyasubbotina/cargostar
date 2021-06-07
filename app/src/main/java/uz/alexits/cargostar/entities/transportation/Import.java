package uz.alexits.cargostar.entities.transportation;

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
        "t.transportation_type " +
        "FROM invoice i INNER JOIN transportation t ON i.id = t.invoice_id " +
        "WHERE t.transportation_type == 2 ORDER BY invoice_id DESC")
public class Import {
    @ColumnInfo(name = "invoice_id")
    private final long invoiceId;

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
    private final boolean accepted;

    @ColumnInfo(name = "transportation_type")
    private final int transportationType;

    @ColumnInfo(name = "import_status")
    private final String importStatus;

    public Import(final long invoiceId,
                  final String fullName,
                  final String phone,
                  final String address,
                  final String organization,
                  final String comment,
                  final String recipientSignature,
                  final String recipientSignatureDate,
                  final boolean accepted,
                  final int transportationType,
                  final String importStatus) {
        this.invoiceId = invoiceId;
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
    }

    public long getInvoiceId() {
        return invoiceId;
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

    public boolean isAccepted() {
        return accepted;
    }

    public int getTransportationType() {
        return transportationType;
    }

    public String getImportStatus() {
        return importStatus;
    }
}
