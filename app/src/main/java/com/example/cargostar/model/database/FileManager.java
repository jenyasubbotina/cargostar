package com.example.cargostar.model.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

public class FileManager {
    private final String appDirname;
    private final String signatureDirname;
    private final String documentDirname;
    private static FileManager instance;

    private FileManager(final Context context) {
        this.appDirname = context.getExternalFilesDir(null).getAbsolutePath();
        this.signatureDirname = appDirname + File.separator + "signatures";
        this.documentDirname = appDirname + File.separator + "documents";
        makeFiles();
    }

    private void makeFiles() {
        final File appDir = new File(appDirname);

        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        final File signatureDir = new File(signatureDirname);

        if (!signatureDir.exists()) {
            signatureDir.mkdir();
        }
        final File documentDir = new File(documentDirname);

        if (!documentDir.exists()) {
            documentDir.mkdir();
        }
    }

    public static FileManager getInstance(final Context context) {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null) {
                    instance = new FileManager(context);
                }
            }
        }
        return instance;
    }

    public String storeBitmap(final Bitmap bitmap) {
        final String screenshotFilename = signatureDirname + File.separator + Instant.now().getEpochSecond() + ".png";
        final File screenshotFile = new File(screenshotFilename);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(screenshotFile);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
                return screenshotFilename;
            }
            // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void storeFile() {

    }
}
