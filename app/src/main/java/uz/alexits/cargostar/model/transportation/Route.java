package uz.alexits.cargostar.model.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.shipping.Request;

@Entity(tableName = "route", foreignKeys = {
        @ForeignKey(entity = Transportation.class, parentColumns = "id", childColumns = "transportation_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = TransitPoint.class, parentColumns = "id", childColumns = "transit_point_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "transportation_id"), @Index(value = "transit_point_id")})
public class Route {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("transportation_id")
    @ColumnInfo(name = "transportation_id")
    private final Long transportationId;

    @Expose
    @SerializedName("transit_point_id")
    @ColumnInfo(name = "transit_point_id")
    private final Long transitPointId;

    public Route(final long id,
                 final Long transportationId,
                 final Long transitPointId) {
        this.id = id;
        this.transportationId = transportationId;
        this.transitPointId = transitPointId;
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

    @NonNull
    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", transportationId=" + transportationId +
                ", transitPointId=" + transitPointId +
                '}';
    }
}
