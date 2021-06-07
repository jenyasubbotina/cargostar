package uz.alexits.cargostar.entities.transportation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Partial {
    @Expose
    @SerializedName("id")
    private final long id;

    @Expose
    @SerializedName("composition")
    private final List<Long> transportationIdList;

    public Partial(final long id, final List<Long> transportationIdList) {
        this.id = id;
        this.transportationIdList = transportationIdList;
    }

    public long getId() {
        return id;
    }

    public List<Long> getTransportationIdList() {
        return transportationIdList;
    }
}
