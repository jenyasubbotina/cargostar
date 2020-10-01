package com.example.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.cargostar.model.location.TransitPoint;

@Entity(tableName = "consolidation", indices = {@Index(value = "consolidation_qr", unique = true)})
public class Consolidation {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "consolidation_number") private long consolidationNumber;
    @ColumnInfo(name = "consolidation_qr") @NonNull private String consolidationQr;

    public Consolidation(@NonNull final String consolidationQr) {
        this.consolidationQr = consolidationQr;
    }

    public long getConsolidationNumber() {
        return consolidationNumber;
    }

    public void setConsolidationNumber(final long consolidationNumber) {
        this.consolidationNumber = consolidationNumber;
    }

    @NonNull
    public String getConsolidationQr() {
        return consolidationQr;
    }

    public void setConsolidationQr(@NonNull final String consolidationQr) {
        this.consolidationQr = consolidationQr;
    }
}
