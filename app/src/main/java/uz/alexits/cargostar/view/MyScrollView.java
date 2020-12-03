package uz.alexits.cargostar.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class MyScrollView extends NestedScrollView {
    public MyScrollView(@NonNull Context context) {
        super(context);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        Log.i(TAG, "onRequestFocusInDescendants(): ");
        return false;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        Log.i(TAG, "computeScrollDeltaToGetChildRectOnScreen(): ");
        return 0;
    }

    @Override
    public boolean fullScroll(int direction) {
        Log.i(TAG, "fullScroll(): ");
        return super.fullScroll(direction);
    }

    @Override
    public boolean pageScroll(int direction) {
        Log.i(TAG, "pageScroll(): ");
        return super.pageScroll(direction);
    }

    private static final String TAG = MyScrollView.class.toString();
}
