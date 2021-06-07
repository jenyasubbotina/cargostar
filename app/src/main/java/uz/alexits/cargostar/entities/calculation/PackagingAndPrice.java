package uz.alexits.cargostar.entities.calculation;

import androidx.annotation.NonNull;

public class PackagingAndPrice {
    private final Packaging packaging;
    private final double price;

    public PackagingAndPrice(final Packaging packaging,
                             final double price) {
        this.packaging = packaging;
        this.price = price;
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public double getPrice() {
        return price;
    }

    @NonNull
    @Override
    public String toString() {
        return "PackagingAndPrice{" +
                "packaging=" + packaging +
                ", price=" + price +
                '}';
    }
}
