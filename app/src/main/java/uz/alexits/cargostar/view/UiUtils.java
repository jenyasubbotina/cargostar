package uz.alexits.cargostar.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import uz.alexits.cargostar.R;

public class UiUtils {
    public static void onFocusChanged(final View v, final boolean hasFocus) {
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

    public static NavController getNavController(@NonNull final Activity activity, @IdRes int resId) {
        return Navigation.findNavController(activity, resId);
    }

    private static final String TAG = UiUtils.class.toString();
}
