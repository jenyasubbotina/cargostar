package uz.alexits.cargostar.view.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import javax.security.auth.callback.Callback;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceTwoEditTextsViewHolder extends RecyclerView.ViewHolder {
    public TextView firstTextView;
    public TextView secondTextView;
    public EditText firstEditText;
    public EditText secondEditText;

    private TextWatcher secondTextWatcher;

    public CreateInvoiceTwoEditTextsViewHolder(@NonNull View itemView) {
        super(itemView);
        firstTextView = itemView.findViewById(R.id.first_text_view);
        secondTextView = itemView.findViewById(R.id.second_text_view);
        firstEditText = itemView.findViewById(R.id.first_edit_text);
        secondEditText = itemView.findViewById(R.id.second_edit_text);
    }

    public void bindWatcherForSecondEditText(final int position, final CreateInvoiceCallback callback) {
        secondTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                callback.afterSecondEditTextChanged(position, s);
            }
        };
        secondEditText.addTextChangedListener(secondTextWatcher);
    }

    public void unbindWatchers() {
        secondEditText.removeTextChangedListener(secondTextWatcher);
    }

    public void bindTwoEditTexts(final int position, final CreateInvoiceCallback callback) {
        callback.bindTwoEditTexts(position, firstEditText, secondEditText);
    }
}
