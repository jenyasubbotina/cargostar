package uz.alexits.cargostar.model;

public class TariffPrice {
    private final String tariff;
    private final String price;
    private long tariffId;

    public TariffPrice(String tariff, String price, long tariffId) {
        this.tariff = tariff;
        this.price = price;
        this.tariffId = tariffId;
    }

    public long getTariffId() {
        return tariffId;
    }

    public void setTariffId(long tariffId) {
        this.tariffId = tariffId;
    }

    public String getTariff() {
        return tariff;
    }

    public String getPrice() {
        return price;
    }
}
