package uz.alexits.cargostar.entities.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "country")
public class Country {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @SerializedName("code")
    @Expose
    @ColumnInfo(name = "code")
    @Nullable private final String code;

    @SerializedName("code2")
    @Expose
    @ColumnInfo(name = "code2")
    private String code2;

    @SerializedName("name_en")
    @Expose
    @ColumnInfo(name = "name_en")
    private String nameEn;


    public Country(final long id,
                   @NonNull final String name,
                   @Nullable final String code,
                   @Nullable final String code2,
                   @Nullable final String nameEn) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.code2 = code2;
        this.nameEn = nameEn;
    }

    @Ignore
    public Country(@NonNull final String name) {
        this.name = name;
        this.code = null;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getCode() {
        return code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @NonNull
    @Override
    public String toString() {
        return nameEn;
    }
}
