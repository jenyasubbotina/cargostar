package uz.alexits.cargostar.view.viewholder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceEditTextSpinnerViewHolder extends RecyclerView.ViewHolder {
    public TextView firstTextView;
    public TextView secondTextView;
    public EditText editText;
    public Spinner spinner;

    private TextWatcher textWatcher;

    public CreateInvoiceEditTextSpinnerViewHolder(@NonNull View itemView) {
        super(itemView);
        firstTextView = itemView.findViewById(R.id.first_text_view);
        secondTextView = itemView.findViewById(R.id.second_text_view);
        editText = itemView.findViewById(R.id.first_edit_text);
        spinner = itemView.findViewById(R.id.second_spinner);
    }

    public void bindWatcher(final int position, final CreateInvoiceCallback callback) {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callback.afterFirstEditTextChanged(position, charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }

    public void bindSpinner(@NonNull final Context context, final int position, @NonNull final CreateInvoiceCallback callback) {
        /* firstSpinner -> regionSpinner */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final Object selectedObject = adapterView.getSelectedItem();

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        spinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                callback.onSpinnerEditTextItemSelected(position, selectedObject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void unbindWatcher() {
        editText.removeTextChangedListener(textWatcher);
    }

    public void unbindSpinner() {
        spinner.setOnItemSelectedListener(null);
    }
}
