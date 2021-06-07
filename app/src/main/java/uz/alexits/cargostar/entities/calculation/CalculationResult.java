package uz.alexits.cargostar.entities.calculation;

import androidx.annotation.NonNull;

public class CalculationResult {
    private final int totalQuantity;
    private final double totalVolume;
    private final double totalWeight;
    private final double totalPrice;

    public CalculationResult(final int totalQuantity,
                             final double totalVolume,
                             final double totalWeight,
                             final double totalPrice) {
        this.totalQuantity = totalQuantity;
        this.totalVolume = totalVolume;
        this.totalWeight = totalWeight;
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @NonNull
    @Override
    public String toString() {
        return "CalculationResult{" +
                "totalQuantity=" + totalQuantity +
                ", totalVolume=" + totalVolume +
                ", totalWeight=" + totalWeight +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
