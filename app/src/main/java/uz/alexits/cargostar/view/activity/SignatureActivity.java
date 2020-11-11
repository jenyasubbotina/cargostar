package uz.alexits.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.FileManager;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.PaintView;

public class SignatureActivity extends AppCompatActivity {
    private PaintView paintView;
    private ImageView backImageView;
    private ImageView eraseImageView;
    private ImageView submitImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        int resultValue = -1;

        if (getIntent() != null) {
            resultValue = getIntent().getIntExtra(IntentConstants.INTENT_RESULT_VALUE, -1);
        }

        paintView = findViewById(R.id.paint_view);
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        backImageView = findViewById(R.id.back_arrow_image_view);
        eraseImageView = findViewById(R.id.erase_image_view);
        submitImageView = findViewById(R.id.submit_image_view);

        backImageView.setOnClickListener(v -> {
            onBackPressed();
        });

        eraseImageView.setOnClickListener(v -> {
            paintView.clear();
        });

        submitImageView.setOnClickListener(v -> {
            final Bitmap screenshot = makeScreenshot(getWindow().getDecorView().getRootView());
            final String screenshotFilename = FileManager.getInstance(this).storeBitmap(screenshot);
            if (screenshotFilename != null) {
                final Intent resultIntent = new Intent();
                resultIntent.putExtra(IntentConstants.INTENT_RESULT_VALUE, screenshotFilename);
                setResult(RESULT_OK, resultIntent);
                finish();
                return;
            }
            Log.i(SignatureActivity.class.toString(), "makeScreenshot(): error occurred while was trying to make a screenshot");
        });
    }

    private Bitmap makeScreenshot(final View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return b;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Log.i(TAG, "onActivityResult(): canceled");
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentConstants.REQUEST_SCAN_QR_MENU) {
                Log.i(TAG, "onActivityResult(): " + data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
            }
        }
    }

    private static final String TAG = SignatureActivity.class.toString();
}