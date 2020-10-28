package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ReceiptWithCargoList {
    @Embedded private Receipt receipt;
    @Relation(parentColumn = "id", entityColumn = "receipt_id") private List<Cargo> cargoList;

    public List<Cargo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<Cargo> cargoList) {
        this.cargoList = cargoList;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReceiptWithCargoList{" +
                "receipt=" + receipt +
                ", cargoList=" + cargoList +
                '}';
    }
}
