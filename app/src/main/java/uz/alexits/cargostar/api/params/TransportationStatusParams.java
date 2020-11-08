package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportationStatusParams {
    @Expose
    @SerializedName("id")
    private final long id;

    @Expose
    @SerializedName("transportation_id")
    private final Long transportationId;

    @Expose
    @SerializedName("transit_point_id")
    private final Long transitPointId;

    @Expose
    @SerializedName("transportation_status_id")
    private final Long transportationStatusId;

    @Ignore
    public TransportationStatusParams(final Long transportationId, final Long transitPointId, final Long transportationStatusId) {
        this(-1, transportationId, transitPointId, transportationStatusId);
    }

    public TransportationStatusParams(final long id,
                                      final Long transportationId,
                                      final Long transitPointId,
                                      final Long transportationStatusId) {
        this.id = id;
        this.transportationId = transportationId;
        this.transitPointId = transitPointId;
        this.transportationStatusId = transportationStatusId;
    }

    public long getId() {
        return id;
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
                "id=" + id +
                ", transportationId=" + transportationId +
                ", transitPointId=" + transitPointId +
                ", transportationStatusId=" + transportationStatusId +
                '}';
    }
}
