package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.TariffCallback;

public class TariffPriceRadioViewHolder extends RecyclerView.ViewHolder {
    public RadioButton tariffRadioBtn;
    public TextView priceTextView;

    public TariffPriceRadioViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tariffRadioBtn = itemView.findViewById(R.id.tariff_radio_btn);
        this.priceTextView = itemView.findViewById(R.id.price_text_view);
    }

    public void bindTariffList(final int position, final int lastCheckedPosition, final TariffCallback callback) {
        tariffRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            callback.onTariffSelected(position, lastCheckedPosition);
        });
    }

    public void unbindTariffList() {
        tariffRadioBtn.setOnCheckedChangeListener(null);
    }
}
