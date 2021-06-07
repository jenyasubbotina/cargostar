package uz.alexits.cargostar.entities.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.entities.location.TransitPoint;

@Entity(tableName = "transportationData",
        foreignKeys = {
                @ForeignKey(entity = TransitPoint.class, parentColumns = "id", childColumns = "transit_point_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = TransportationStatus.class, parentColumns = "id", childColumns = "transportation_status_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Transportation.class, parentColumns = "id", childColumns = "transportation_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "transit_point_id"), @Index(value = "transportation_status_id"), @Index(value = "transportation_id")})
public class TransportationData {
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
        @SerializedName("transportation_status")
        @ColumnInfo(name = "transportation_status_id")
        private final long transportationStatusId;

        @Expose
        @SerializedName("transit_point_id")
        @ColumnInfo(name = "transit_point_id")
        private final long transitPointId;

        public TransportationData(final long id, final long transportationId, final long transportationStatusId, final long transitPointId) {
                this.id = id;
                this.transportationId = transportationId;
                this.transportationStatusId = transportationStatusId;
                this.transitPointId = transitPointId;
        }

        public long getId() {
                return id;
        }

        public long getTransportationId() {
                return transportationId;
        }

        public long getTransportationStatusId() {
                return transportationStatusId;
        }

        public long getTransitPointId() {
                return transitPointId;
        }

        @NonNull
        @Override
        public String toString() {
                return "TransportationData{" +
                        "id=" + id +
                        ", transportationId=" + transportationId +
                        ", transportationStatusId=" + transportationStatusId +
                        ", transitPointId=" + transitPointId +
                        '}';
        }
}
