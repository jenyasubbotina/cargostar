package uz.alexits.cargostar.entities.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportationStatusParams {
    @Expose
    @SerializedName("transportation_id")
    private final Long transportationId;

    @Expose
    @SerializedName("transit_point_id")
    private final Long transitPointId;

    @Expose
    @SerializedName("transportation_status")
    private final Long transportationStatusId;

    public TransportationStatusParams(final Long transportationId,
                                      final Long transitPointId,
                                      final Long transportationStatusId) {
        this.transportationId = transportationId;
        this.transitPointId = transitPointId;
        this.transportationStatusId = transportationStatusId;
    }


    public Long getTransportationId() {
        return transportationId;
    }

    public Long getTransitPointId() {
        return transitPointId;
    }

    public Long getTransportationStatusId() {
        return transportationStatusId;
    }

    @NonNull
    @Override
    public String toString() {
        return "TransportationStatusParams{" +
                ", transportationId=" + transportationId +
                ", transitPointId=" + transitPointId +
                ", transportationStatusId=" + transportationStatusId +
                '}';
    }
}
