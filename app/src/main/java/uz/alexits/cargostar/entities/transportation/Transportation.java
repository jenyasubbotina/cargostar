package uz.alexits.cargostar.entities.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "transportation")
public class Transportation {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("provider_id")
    @ColumnInfo(name = "provider_id")
    private final long providerId;

    @Expose
    @SerializedName("employee_id")
    @ColumnInfo(name = "courier_id")
    private final long courierId;

    @Expose
    @SerializedName("invoice_id")
    @ColumnInfo(name = "invoice_id")
    private final long invoiceId;

    @Expose
    @SerializedName("request_id")
    @ColumnInfo(name = "request_id")
    private final long requestId;

    @Expose
    @SerializedName("branche_id")
    @ColumnInfo(name = "branche_id")
    private final long brancheId;

    @Expose
    @SerializedName("status_transport")
    @ColumnInfo(name = "transportation_status_id")
    private long transportationStatusId;

    @Expose
    @SerializedName("status_payment")
    @ColumnInfo(name = "payment_status_id")
    private final long paymentStatusId;

    @Expose
    @SerializedName("current_point")
    @ColumnInfo(name = "current_transition_point_id")
    private long currentTransitionPointId;

    @Expose
    @SerializedName("party_id")
    @ColumnInfo(name = "partial_id")
    private final long partialId;

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
    @SerializedName("cityFrom")
    @ColumnInfo(name = "city_from")
    private final String cityFrom;

    @Expose
    @SerializedName("cityTo")
    @ColumnInfo(name = "city_to")
    private final String cityTo;

    @Expose
    @SerializedName("statusName")
    @ColumnInfo(name = "transportation_status_name")
    private final String transportationStatusName;

    //fields for import
    //1 - transportation, 2 - import
    @Expose
    @SerializedName("transportation_type")
    @ColumnInfo(name = "transportation_type")
    private final int transportationType;

    @Expose
    @SerializedName("import_status")
    @ColumnInfo(name = "import_status")
    private final String importStatus;


    public Transportation(final long id,
                          final long providerId,
                          final long courierId,
                          final long invoiceId,
                          final long requestId,
                          final long brancheId,
                          final long transportationStatusId,
                          final long paymentStatusId,
                          final long currentTransitionPointId,
                          final long partialId,
                          final String arrivalDate,
                          final String trackingCode,
                          final String qrCode,
                          final String partyQrCode,
                          final String instructions,
                          final String direction,
                          final String cityFrom,
                          final String cityTo,
                          final String transportationStatusName,
                          final int transportationType,
                          final String importStatus) {
        this.id = id;
        this.providerId = providerId;
        this.courierId = courierId;
        this.invoiceId = invoiceId;
        this.requestId = requestId;
        this.brancheId = brancheId;
        this.transportationStatusId = transportationStatusId;
        this.paymentStatusId = paymentStatusId;
        this.currentTransitionPointId = currentTransitionPointId;
        this.partialId = partialId;
        this.arrivalDate = arrivalDate;
        this.trackingCode = trackingCode;
        this.qrCode = qrCode;
        this.partyQrCode = partyQrCode;
        this.instructions = instructions;
        this.direction = direction;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.transportationStatusName = transportationStatusName;
        this.transportationType = transportationType;
        this.importStatus = importStatus;
    }

    public long getId() {
        return id;
    }

    public long getBrancheId() {
        return brancheId;
    }

    public long getProviderId() {
        return providerId;
    }

    public long getCourierId() {
        return courierId;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public long getTransportationStatusId() {
        return transportationStatusId;
    }

    public long getPaymentStatusId() {
        return paymentStatusId;
    }

    public long getPartialId() {
        return partialId;
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

    public long getCurrentTransitionPointId() {
        return currentTransitionPointId;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public String getTransportationStatusName() {
        return transportationStatusName;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setTransportationStatusId(long transportationStatusId) {
        this.transportationStatusId = transportationStatusId;
    }

    public void setCurrentTransitionPointId(long currentTransitionPointId) {
        this.currentTransitionPointId = currentTransitionPointId;
    }

    public int getTransportationType() {
        return transportationType;
    }

    public String getImportStatus() {
        return importStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "Transportation{" +
                "id=" + id +
                ", providerId=" + providerId +
                ", courierId=" + courierId +
                ", invoiceId=" + invoiceId +
                ", requestId=" + requestId +
                ", brancheId=" + brancheId +
                ", transportationStatusId=" + transportationStatusId +
                ", paymentStatusId=" + paymentStatusId +
                ", currentTransitionPointId=" + currentTransitionPointId +
                ", partialId=" + partialId +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", trackingCode='" + trackingCode + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", partyQrCode='" + partyQrCode + '\'' +
                ", instructions='" + instructions + '\'' +
                ", direction='" + direction + '\'' +
                ", cityFrom='" + cityFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", transportationStatusName='" + transportationStatusName + '\'' +
                ", transportationType=" + transportationType +
                ", importStatus='" + importStatus + '\'' +
                '}';
    }
}
