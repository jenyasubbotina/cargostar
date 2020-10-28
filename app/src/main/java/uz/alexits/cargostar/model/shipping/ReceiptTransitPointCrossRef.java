package uz.alexits.cargostar.model.shipping;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"id", "transit_point_id"}, indices = {@Index(value = "transit_point_id")})
public class ReceiptTransitPointCrossRef {
    @ColumnInfo(name = "id") private long receiptId;
    @ColumnInfo(name = "transit_point_id") private long transitPointId;

    public ReceiptTransitPointCrossRef(final long receiptId, final long transitPointId) {
        this.receiptId = receiptId;
        this.transitPointId = transitPointId;
    }

    public long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(final long receiptId) {
        this.receiptId = receiptId;
    }

    public long getTransitPointId() {
        return transitPointId;
    }

    public void setTransitPointId(long transitPointId) {
        this.transitPointId = transitPointId;
    }
}
