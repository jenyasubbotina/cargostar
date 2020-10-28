package uz.alexits.cargostar.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import uz.alexits.cargostar.R;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        final String cargoContent = "type=2,cargoId=2,qr=9876543211";

        try {
            final BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            final Bitmap bitmap = barcodeEncoder.encodeBitmap(cargoContent, BarcodeFormat.QR_CODE, 400, 400);
            final ImageView imageViewQrCode = (ImageView) findViewById(R.id.qr_image_view);
            imageViewQrCode.setImageBitmap(bitmap);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}