package uz.alexits.cargostar.entities.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "transportationStatus")
public class TransportationStatus {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    private final String name;

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    private final String description;

    public TransportationStatus(final long id,
                                final String name,
                                final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

