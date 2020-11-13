package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;

public class TariffPriceViewHolder extends RecyclerView.ViewHolder {
    public TextView tariffTextView;
    public TextView priceTextView;

    public TariffPriceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tariffTextView = itemView.findViewById(R.id.tariff_text_view);
        this.priceTextView = itemView.findViewById(R.id.price_text_view);
    }
}
