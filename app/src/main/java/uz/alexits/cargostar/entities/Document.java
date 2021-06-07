package uz.alexits.cargostar.entities;

import androidx.annotation.NonNull;

public class Document {
    @NonNull private final String name;
    @NonNull private final String link;

    public Document(@NonNull String name, @NonNull String link) {
        this.name = name;
        this.link = link;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getLink() {
        return link;
    }
}
