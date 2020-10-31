package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ReceiptWithCargoList {
    @Embedded private Invoice invoice;
    @Relation(parentColumn = "id", entityColumn = "invoice_id") private List<Cargo> cargoList;

    public List<Cargo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<Cargo> cargoList) {
        this.cargoList = cargoList;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReceiptWithCargoList{" +
                "receipt=" + invoice +
                ", cargoList=" + cargoList +
                '}';
    }
}
