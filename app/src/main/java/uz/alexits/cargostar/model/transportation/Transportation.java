package uz.alexits.cargostar.model.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.shipping.Invoice;

@Entity(tableName = "transportation",
        foreignKeys = {
                @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Invoice.class, parentColumns = "id", childColumns = "invoice_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = TransportationStatus.class, parentColumns = "id", childColumns = "transportation_status_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "provider_id"), @Index(value = "invoice_id"), @Index(value = "transportation_status_id")})
public class Transportation {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("provider_id")
    @ColumnInfo(name = "provider_id")
    private final Long providerId;

    @Expose
    @SerializedName("employee_id")
    @ColumnInfo(name = "courier_id")
    private final Long courierId;

    @Expose
    @SerializedName("invoice_id")
    @ColumnInfo(name = "invoice_id")
    private final Long invoiceId;

    @Expose
    @SerializedName("status_transportation")
    @ColumnInfo(name = "transportation_status_id")
    private final Long transportationStatusId;

    @Expose
    @SerializedName("status_payment")
    @ColumnInfo(name = "payment_status_id")
    private final Long paymentStatusId;

    @Expose
    @SerializedName("current_point")
    @ColumnInfo(name = "current_transition_point_id")
    private final Long currentTransitionPointId;

    @Expose
    @SerializedName("date")
    @ColumnInfo(name = "arrival_date")
    private String arrivalDate;

    @Expose
    @SerializedName("tracking_code")
    @ColumnInfo(name = "tracking_code")
    private final String trackingCode;

    @Expose
    @SerializedName("qr_code")
    @ColumnInfo(name = "qr_code")
    private final String qrCode;

    @Expose
    @SerializedName("party_qr_code")
    @ColumnInfo(name = "party_qr_code")
    private final String partyQrCode;

    @Expose
    @SerializedName("instructions")
    @ColumnInfo(name = "instructions")
    private final String instructions;

    @Expose
    @SerializedName("direction")
    @ColumnInfo(name = "direction")
    private final String direction;

    @Expose
    @SerializedName("status")
    @ColumnInfo(name = "status")
    private int status;

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @Expose
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    public Transportation(final long id,
                          final Long providerId,
                          final Long courierId,
                          final Long invoiceId,
                          final Long transportationStatusId,
                          final Long paymentStatusId,
                          final Long currentTransitionPointId,
                          final String arrivalDate,
                          final String trackingCode,
                          final String qrCode,
                          final String partyQrCode,
                          final String instructions,
                          final String direction,
                          final int status,
                          final Date createdAt,
                          final Date updatedAt) {
        this.id = id;
        this.providerId = providerId;
        this.courierId = courierId;
        this.invoiceId = invoiceId;
        this.transportationStatusId = transportationStatusId;
        this.paymentStatusId = paymentStatusId;
        this.currentTransitionPointId = currentTransitionPointId;
        this.arrivalDate = arrivalDate;
        this.trackingCode = trackingCode;
        this.qrCode = qrCode;
        this.partyQrCode = partyQrCode;
        this.instructions = instructions;
        this.direction = direction;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public Long getProviderId() {
        return providerId;
    }

    public Long getCourierId() {
        return courierId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public Long getTransportationStatusId() {
        return transportationStatusId;
    }

    public Long getPaymentStatusId() {
        return paymentStatusId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getPartyQrCode() {
        return partyQrCode;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getDirection() {
        return direction;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCurrentTransitionPointId() {
        return currentTransitionPointId;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Transportation{" +
                "id=" + id +
                ", providerId=" + providerId +
                ", courierId=" + courierId +
                ", invoiceId=" + invoiceId +
                ", transportationStatusId=" + transportationStatusId +
                ", paymentStatusId=" + paymentStatusId +
                ", currentTransitionPointId=" + currentTransitionPointId +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", trackingCode='" + trackingCode + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", partyQrCode='" + partyQrCode + '\'' +
                ", instructions='" + instructions + '\'' +
                ", direction='" + direction + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}