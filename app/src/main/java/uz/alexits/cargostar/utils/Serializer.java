package uz.alexits.cargostar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import uz.alexits.cargostar.model.transportation.Consignment;

public class Serializer {
    public static String bitmapToBase64(@NonNull final Context context, @NonNull final String filePath) {
        final Bitmap photoBitmap = getBitmapFromUri(context, Uri.parse(filePath));

        if (photoBitmap == null) {
            Log.e(TAG, "bitmapToBase64(): bitmap is NULL");
            return null;
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
            final byte[] photoBytes = bos.toByteArray();
            photoBitmap.recycle();
            return Base64.encodeToString(photoBytes, Base64.DEFAULT);
        }

        Log.e(TAG, "bitmapToBase64(): couldn't compress image");
        return null;
    }

    private static Bitmap getBitmapFromUri(@NonNull final Context context, @NonNull final Uri imageUri) {
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(imageUri);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "getBitmapFromUri(): ", e);
        }
        return is != null ? BitmapFactory.decodeStream(is) : null;
    }

    private void convertBitmapToFile(@NonNull final File destFile, @NonNull final Bitmap bitmap) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        byte[] bitmapBytes;

        try {
            if (!destFile.createNewFile()) {
                Log.e(TAG, "convertBitmapToFile(): couldn't create file " + destFile);
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bitmapBytes = bos.toByteArray();
            fos = new FileOutputStream(destFile);
            fos.write(bitmapBytes);
        }
        catch (IOException e) {
            Log.e(TAG, "convertBitmapToFile(): ", e);
        }
        finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "convertBitmapToFile(): ", e);
                }
            }
        }
    }

    public static String fileToBase64(@NonNull final String filePath) {
        try {
            final byte[] signatureBytes = Files.readAllBytes(Paths.get(filePath));
            return Base64.encodeToString(signatureBytes, Base64.DEFAULT);
        }
        catch (IOException e) {
            Log.e(TAG, "getSignature(): ", e);
        }
        Log.e(TAG, "fileToBase64: couldn't read bytes from file " + filePath);
        return null;
    }

    public static List<Consignment> deserializeConsignmentList(final String serializedConsignmentList) {
        if (serializedConsignmentList == null) {
            return null;
        }
        return new Gson().fromJson(serializedConsignmentList, new TypeToken<List<Consignment>>(){}.getType());
    }

    private static final String TAG = Serializer.class.toString();
}
