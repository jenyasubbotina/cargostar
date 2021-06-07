package uz.alexits.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQrActivity extends AppCompatActivity {
    private int resultValue;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        if (getIntent() != null) {
            this.position = getIntent().getIntExtra(Constants.KEY_QR_POSITION, -1);
        }
        final IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Отсканируйте QR-код");
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                if (data != null) {
                    String scannedData = data.getStringExtra("SCAN_RESULT");
                    int scanType = 0;

                    if (scannedData != null) {
                        final Intent resultIntent = new Intent();

                        //fedex or tnt tracking code -> do request on server
                        if (scannedData.length() - 12 >= 0) {
                            scannedData = scannedData.substring(scannedData.length() - 12);
                            scanType = 2;
                        }
                        else {
                            scanType = 1;
                        }
                        resultIntent.putExtra(IntentConstants.INTENT_RESULT_VALUE, scannedData);
                        resultIntent.putExtra(Constants.KEY_QR_POSITION, position);
                        resultIntent.putExtra(Constants.SCAN_TYPE, scanType);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private static final String TAG = ScanQrActivity.class.toString();
}