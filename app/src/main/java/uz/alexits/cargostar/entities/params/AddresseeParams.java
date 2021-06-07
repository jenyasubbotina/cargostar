package uz.alexits.cargostar.entities.params;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddresseeParams {
    @Expose
    @SerializedName("invoice_id")
    private final long invoiceId;

    @Expose
    @SerializedName("addressee_full_name")
    private final String fullName;

    @Expose
    @SerializedName("addressee_phone")
    private final String phone;

    @Expose
    @SerializedName("addressee_address")
    private final String address;

    @Expose
    @SerializedName("addressee_company")
    private final String organization;

    @Expose
    @SerializedName("addressee_comment")
    private final String comment;

    @Expose
    @SerializedName("recipient_signature")
    private final String recipientSignature;

    @Expose
    @SerializedName("recipient_signature_date")
    private final String recipientSignatureDate;

    @Expose
    @SerializedName("addressee_result")
    private final boolean accepted;

    public AddresseeParams(final long invoiceId,
                           final String fullName,
                           final String phone,
                           final String address,
                           final String organization,
                           final String comment,
                           final String recipientSignature,
                           final String recipientSignatureDate,
                           final boolean accepted) {
        this.invoiceId = invoiceId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.organization = organization;
        this.comment = comment;
        this.recipientSignature = recipientSignature;
        this.recipientSignatureDate = recipientSignatureDate;
        this.accepted = accepted;
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
}
