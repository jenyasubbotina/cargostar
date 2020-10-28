package uz.alexits.cargostar.view.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateParcelCallback;

public class CreateParcelEditTextSpinnerViewHolder extends RecyclerView.ViewHolder {
    public TextView firstTextView;
    public TextView secondTextView;
    public EditText editText;
    public Spinner spinner;

    public CreateParcelEditTextSpinnerViewHolder(@NonNull View itemView) {
        super(itemView);
        firstTextView = itemView.findViewById(R.id.first_text_view);
        secondTextView = itemView.findViewById(R.id.second_text_view);
        editText = itemView.findViewById(R.id.edit_text);
        spinner = itemView.findViewById(R.id.spinner);
    }

    public void bindWatcher(final int position, final CreateParcelCallback callback) {
        editText.addTextChangedListener(new TextWatcher() {
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
        });
    }

    public void bindSpinner(final SpinnerAdapter adapter, final int position, final CreateParcelCallback callback) {
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(callback::onSpinnerItemChanged);
    }
}
