package uz.alexits.cargostar.model.transportation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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
