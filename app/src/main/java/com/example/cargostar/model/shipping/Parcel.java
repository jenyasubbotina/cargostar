package com.example.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import com.example.cargostar.model.TransportationStatus;
import com.example.cargostar.model.location.TransitPoint;

import java.util.List;

public class Parcel {
    @Embedded private Receipt receipt;
    @Relation(parentColumn = "id", entityColumn = "transit_point_id", associateBy = @Junction(ReceiptTransitPointCrossRef.class)) private List<TransitPoint> route;

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public List<TransitPoint> getRoute() {
        return route;
    }

    public void setRoute(List<TransitPoint> route) {
        this.route = route;
    }

    @NonNull
    @Override
    public String toString() {
        return "Parcel{" +
                "receipt=" + receipt +
                ", route=" + route +
                '}';
    }
}
