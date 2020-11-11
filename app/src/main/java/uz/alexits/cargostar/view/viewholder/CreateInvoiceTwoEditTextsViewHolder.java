package uz.alexits.cargostar.view.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceTwoEditTextsViewHolder extends RecyclerView.ViewHolder {
    public TextView firstTextView;
    public TextView secondTextView;
    public EditText firstEditText;
    public EditText secondEditText;

    private TextWatcher firstTextWatcher;
    private TextWatcher secondTextWatcher;

    public CreateInvoiceTwoEditTextsViewHolder(@NonNull View itemView) {
        super(itemView);
        firstTextView = itemView.findViewById(R.id.first_text_view);
        secondTextView = itemView.findViewById(R.id.second_text_view);
        firstEditText = itemView.findViewById(R.id.first_edit_text);
        secondEditText = itemView.findViewById(R.id.second_edit_text);
    }

    public void bindWatchers(final int position, final CreateInvoiceCallback callback) {
        firstTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                callback.afterFirstEditTextChanged(position, editable);
            }
        };
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
        firstEditText.addTextChangedListener(firstTextWatcher);
        secondEditText.addTextChangedListener(secondTextWatcher);
    }

    public void unbindWatchers() {
        firstEditText.removeTextChangedListener(firstTextWatcher);
        secondEditText.removeTextChangedListener(firstTextWatcher);
    }
}
