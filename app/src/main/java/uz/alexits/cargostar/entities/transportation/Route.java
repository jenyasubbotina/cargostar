package uz.alexits.cargostar.entities.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.entities.location.TransitPoint;

@Entity(tableName = "route")
public class Route {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("transportation_id")
    @ColumnInfo(name = "transportation_id")
    private final long transportationId;

    @Expose
    @SerializedName("transit_point_id")
    @ColumnInfo(name = "transit_point_id")
    private final long transitPointId;

    public Route(final long id,
                 final long transportationId,
                 final long transitPointId) {
        this.id = id;
        this.transportationId = transportationId;
        this.transitPointId = transitPointId;
    }

    public long getId() {
        return id;
    }

    public long getTransportationId() {
        return transportationId;
    }

    public long getTransitPointId() {
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
