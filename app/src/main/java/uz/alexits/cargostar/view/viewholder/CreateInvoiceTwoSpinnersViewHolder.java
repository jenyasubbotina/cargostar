package uz.alexits.cargostar.view.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceTwoSpinnersViewHolder extends RecyclerView.ViewHolder {
    public TextView regionTextView;
    public TextView cityTextView;
    public Spinner regionSpinner;
    public Spinner citySpinner;
    private RelativeLayout regionField;
    private RelativeLayout cityField;

    public CreateInvoiceTwoSpinnersViewHolder(@NonNull View itemView) {
        super(itemView);
        regionTextView = itemView.findViewById(R.id.first_text_view);
        cityTextView = itemView.findViewById(R.id.second_text_view);
        regionField = itemView.findViewById(R.id.first_field);
        cityField = itemView.findViewById(R.id.second_field);
        regionSpinner = itemView.findViewById(R.id.first_spinner);
        citySpinner = itemView.findViewById(R.id.second_spinner);
    }

    public void bindFirstSpinner(@NonNull final Context context, final int position, @NonNull final CreateInvoiceCallback callback) {
        /* firstSpinner -> regionSpinner */
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final Region region = (Region) adapterView.getSelectedItem();

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        regionSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                callback.onFirstSpinnerItemSelected(position, region);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void bindSecondSpinner(@NonNull final Context context, final int position, @NonNull final CreateInvoiceCallback callback) {
        /* secondSpinner -> citySpinner */
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final City city = (City) adapterView.getSelectedItem();

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        citySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                callback.onSecondSpinnerItemSelected(position, city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void unbindFirstSpinner() {
        regionSpinner.setOnItemSelectedListener(null);
    }

    public void unbindSecondSpinner() {
        citySpinner.setOnItemSelectedListener(null);
    }

    private static final String TAG = CreateInvoiceTwoSpinnersViewHolder.class.toString();
}
