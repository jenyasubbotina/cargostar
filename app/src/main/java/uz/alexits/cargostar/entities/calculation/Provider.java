package uz.alexits.cargostar.entities.calculation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "provider")
public class Provider {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @Expose
    @SerializedName("name_uz")
    @ColumnInfo(name = "name_uz")
    @Nullable private String nameUz;

    @Expose
    @SerializedName("name_en")
    @ColumnInfo(name = "name_en")
    @Nullable private String nameEn;

    @Expose
    @SerializedName("exportable")
    @ColumnInfo(name = "exportable")
    private boolean exportable;

    @Expose
    @SerializedName("importable")
    @ColumnInfo(name = "importable")
    private boolean importable;

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    @Nullable private String description;

    @Expose
    @SerializedName("description_en")
    @ColumnInfo(name = "description_en")
    @Nullable private String descriptionEn;

    @Expose
    @SerializedName("description_uz")
    @ColumnInfo(name = "description_uz")
    @Nullable private String descriptionUz;

    @Expose
    @SerializedName("fuel")
    @ColumnInfo(name = "fuel")
    private double fuel;

    public Provider(final long id,
                    @NonNull final String name,
                    @Nullable final String nameUz,
                    @Nullable final String nameEn,
                    final boolean exportable,
                    final boolean importable,
                    @Nullable final String description,
                    @Nullable final String descriptionEn,
                    @Nullable final String descriptionUz,
                    final double fuel) {
        this.id = id;
        this.name = name;
        this.nameUz = nameUz;
        this.nameEn = nameEn;
        this.exportable = exportable;
        this.importable = importable;
        this.description = description;
        this.descriptionEn = descriptionEn;
        this.descriptionUz = descriptionUz;
        this.fuel = fuel;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(@Nullable String nameUz) {
        this.nameUz = nameUz;
    }

    @Nullable
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(@Nullable String nameEn) {
        this.nameEn = nameEn;
    }

    public boolean isExportable() {
        return exportable;
    }

    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    public boolean isImportable() {
        return importable;
    }

    public void setImportable(boolean importable) {
        this.importable = importable;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(@Nullable String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    @Nullable
    public String getDescriptionUz() {
        return descriptionUz;
    }

    public void setDescriptionUz(@Nullable String descriptionUz) {
        this.descriptionUz = descriptionUz;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
