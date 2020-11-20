package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipientSignatureParams {
    @Expose
    @SerializedName("recipient_signature")
    private final String recipientSignature;

    @Expose
    @SerializedName("invoice_id")
    private final Long invoiceId;

    public String getRecipientSignature() {
        return recipientSignature;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public RecipientSignatureParams(final long invoiceId, final String recipientSignature) {
        this.invoiceId = invoiceId;
        this.recipientSignature = recipientSignature;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipientSignatureParams{" +
                "recipientSignature='" + recipientSignature + '\'' +
                ", invoiceId=" + invoiceId +
                '}';
    }
}
