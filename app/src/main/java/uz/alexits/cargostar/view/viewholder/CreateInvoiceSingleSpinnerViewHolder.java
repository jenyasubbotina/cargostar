package uz.alexits.cargostar.view.viewholder;

import android.content.Context;
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

    public void bindSpinner(final Context context, final CreateInvoiceCallback callback) {
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
                callback.onBigSpinnerItemSelected(i, selectedObject);
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
