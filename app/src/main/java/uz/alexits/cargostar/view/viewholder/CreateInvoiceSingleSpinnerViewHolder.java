package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceSingleSpinnerViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public Spinner spinner;

    public CreateInvoiceSingleSpinnerViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text_view);
        spinner = itemView.findViewById(R.id.spinner);
    }

    public void bindSpinner(final CreateInvoiceCallback callback) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                callback.onSpinnerItemChanged(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void unbindSpinner() {
        spinner.setOnItemSelectedListener(null);
    }
}
