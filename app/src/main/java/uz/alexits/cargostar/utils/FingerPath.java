package uz.alexits.cargostar.utils;

import android.graphics.Path;

public class FingerPath {
    public int colorResource;
    public boolean emboss;
    public boolean blur;
    public int lineWidth;
    public Path path;

    public FingerPath(final int colorResource, final boolean emboss, final boolean blur, final int lineWidth, final Path path) {
        this.colorResource = colorResource;
        this.emboss = emboss;
        this.blur = blur;
        this.lineWidth = lineWidth;
        this.path = path;
    }
}
