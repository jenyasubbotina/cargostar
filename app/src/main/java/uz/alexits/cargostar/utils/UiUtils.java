package uz.alexits.cargostar.utils;

import android.widget.EditText;

import uz.alexits.cargostar.R;

public class UiUtils {
    public static void onFocusChanged(final EditText editText, final boolean hasFocus) {
        if (hasFocus) {
            editText.setBackgroundResource(R.drawable.edit_text_active);
            return;
        }
        if (editText.getText().length() > 0) {
            editText.setBackgroundResource(R.drawable.edit_text_active);
            return;
        }
        editText.setBackgroundResource(R.drawable.edit_text_locked);
    }
}
