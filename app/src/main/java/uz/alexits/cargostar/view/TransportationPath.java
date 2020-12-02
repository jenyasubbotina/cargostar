package uz.alexits.cargostar.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

public class TransportationPath extends View {
    private Paint mPaint;
    private float aX;
    private float aY;
    private float bX;
    private float bY;

    public TransportationPath(Context context) {
        this(context, null);
    }

    public TransportationPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void init(final float aX, final float aY, final float bX, final float bY) {
        this.aX = aX;
        this.aY = aY;
        this.bX = bX;
        this.bY = bY;
    }

    public void clear() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw: " + aX + " " + aY + " " + bX);
        canvas.drawLine(aX, aY, bX, bY, mPaint);
    }

    private static final String TAG = TransportationPath.class.toString();
}
