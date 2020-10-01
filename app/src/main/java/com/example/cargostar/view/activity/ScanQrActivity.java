package com.example.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cargostar.R;
import com.example.cargostar.view.Constants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQrActivity extends AppCompatActivity {
    private int resultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

//        if (getIntent() != null) {
//            resultValue = getIntent().getIntExtra(Constants.RESULT_VALUE, -1);
//        }

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
            }
            else {
                Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();
                if (data != null) {
                    final String scannedData = data.getStringExtra("SCAN_RESULT");
                    if (scannedData != null) {
                        final Intent resultIntent = new Intent();

                        final String qr = scannedData.substring(21);

                        resultIntent.putExtra(Constants.INTENT_RESULT_VALUE, qr);
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
}