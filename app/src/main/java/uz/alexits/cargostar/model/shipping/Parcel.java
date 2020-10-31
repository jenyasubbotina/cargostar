package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import uz.alexits.cargostar.model.location.TransitPoint;

import java.util.List;

public class Parcel {
    @Embedded private Invoice invoice;
    @Relation(parentColumn = "id", entityColumn = "id", associateBy = @Junction(ReceiptTransitPointCrossRef.class)) private List<TransitPoint> route;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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
                "receipt=" + invoice +
                ", route=" + route +
                '}';
    }
}
