package uz.alexits.cargostar.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import java.time.Instant;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.FileManager;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.PaintView;

public class SignatureActivity extends AppCompatActivity {
    private PaintView paintView;
    private ImageView backImageView;
    private ImageView eraseImageView;
    private ImageView submitImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

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
                setResult(RESULT_OK, new Intent()
                        .putExtra(Constants.KEY_INVOICE_ID, getIntent().getLongExtra(Constants.KEY_INVOICE_ID, 0))
                        .putExtra(Constants.ADDRESSEE_FULL_NAME, getIntent().getStringExtra(Constants.ADDRESSEE_FULL_NAME))
                        .putExtra(Constants.ADDRESSEE_PHONE, getIntent().getStringExtra(Constants.ADDRESSEE_PHONE))
                        .putExtra(Constants.ADDRESSEE_ADDRESS, getIntent().getStringExtra(Constants.ADDRESSEE_ADDRESS))
                        .putExtra(Constants.ADDRESSEE_ORGANIZATION, getIntent().getStringExtra(Constants.ADDRESSEE_ORGANIZATION))
                        .putExtra(Constants.ADDRESSEE_COMMENT, getIntent().getStringExtra(Constants.ADDRESSEE_COMMENT))
                        .putExtra(Constants.ADDRESSEE_IS_ACCEPTED, getIntent().getBooleanExtra(Constants.ADDRESSEE_IS_ACCEPTED, false))
                        .putExtra(Constants.ADDRESSEE_SIGNATURE_DATE, Instant.now().toString())
                        .putExtra(IntentConstants.INTENT_RESULT_VALUE, screenshotFilename));
                finish();
            }
        });
    }

    private Bitmap makeScreenshot(final View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        final Bitmap source = view.getDrawingCache();
        final Bitmap result = cropBitmap(source);
        view.setDrawingCacheEnabled(false);
        return result;
    }

    private Bitmap cropBitmap(final Bitmap source) {
        if (source.getWidth() >= source.getHeight()){
            return Bitmap.createBitmap(source, source.getWidth() / 2 - source.getHeight() / 2, 0, source.getHeight(), source.getHeight());
        }
        return Bitmap.createBitmap(source, 0, source.getHeight() / 2 - source.getWidth() / 2, source.getWidth(), source.getWidth());
    }


    private static final String TAG = SignatureActivity.class.toString();
}