package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.transportation.Consignment;

public class CreateInvoiceResponse {
    @Expose
    @SerializedName("sender")
    private final Customer sender;

    @Expose
    @SerializedName("recipient")
    private final AddressBook recipient;

    @Expose
    @SerializedName("payer")
    private final AddressBook payer;

    @Expose
    @SerializedName("partial")
    private final List<Consignment> consignmentList;

    @Expose
    @SerializedName("id")
    private final Long invoiceId;

    @Expose
    @SerializedName("request_id")
    private final Long requestId;

    @Expose
    @SerializedName("provider")
    private final Long providerId;

    @Expose
    @SerializedName("tarif")
    private final Long packagingId;

    @Expose
    @SerializedName("price")
    private final double price;

    public CreateInvoiceResponse(final Customer sender,
                                 final AddressBook recipient,
                                 final AddressBook payer,
                                 final List<Consignment> consignmentList,
                                 final Long invoiceId,
                                 final Long requestId,
                                 final Long providerId,
                                 final Long packagingId,
                                 final double price) {
        this.sender = sender;
        this.recipient = recipient;
        this.payer = payer;
        this.consignmentList = consignmentList;
        this.invoiceId = invoiceId;
        this.requestId = requestId;
        this.providerId = providerId;
        this.packagingId = packagingId;
        this.price = price;
    }

    public Customer getSender() {
        return sender;
    }

    public AddressBook getRecipient() {
        return recipient;
    }

    public AddressBook getPayer() {
        return payer;
    }

    public List<Consignment> getConsignmentList() {
        return consignmentList;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public Long getPackagingId() {
        return packagingId;
    }

    public double getPrice() {
        return price;
    }

    @NonNull
    @Override
    public String toString() {
        return "CreateInvoiceResponse{" +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", payer=" + payer +
                ", consignmentList=" + consignmentList +
                ", invoiceId=" + invoiceId +
                ", requestId=" + requestId +
                ", providerId=" + providerId +
                ", packagingId=" + packagingId +
                ", price=" + price +
                '}';
    }
}
