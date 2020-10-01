package com.example.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "transit_point",
        foreignKeys = {@ForeignKey(entity = Branch.class,
                parentColumns = "id",
                childColumns = "branch_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "branch_id")})
public class TransitPoint {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "transit_point_id") private long transitPointId;
    @ColumnInfo(name = "branch_id") private long branchId;
    @ColumnInfo(name = "name") @NonNull private final String name;

    public TransitPoint(final long branchId, @NonNull final String name) {
        this.branchId = branchId;
        this.name = name;
    }

    public long getTransitPointId() {
        return transitPointId;
    }

    public void setTransitPointId(long transitPointId) {
        this.transitPointId = transitPointId;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
