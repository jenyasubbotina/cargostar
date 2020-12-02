package uz.alexits.cargostar.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import uz.alexits.cargostar.R;

public class UiUtils {
    public static void onFocusChanged(final View v, final boolean hasFocus) {
        Log.i(TAG, "onFocusChanged: editText " + hasFocus);
        if (hasFocus) {
            v.setBackgroundResource(R.drawable.edit_text_active);
            return;
        }
        if (((EditText) v).getText().length() > 0) {
            v.setBackgroundResource(R.drawable.edit_text_active);
            return;
        }
        v.setBackgroundResource(R.drawable.edit_text_locked);
    }

    public static void hideSoftKeyboard (final Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private static final String TAG = UiUtils.class.toString();
}
