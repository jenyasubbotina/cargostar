package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.model.TariffPrice;
import uz.alexits.cargostar.view.viewholder.TariffPriceViewHolder;

public class TariffPriceAdapter extends RecyclerView.Adapter<TariffPriceViewHolder> {
    private final Context context;
    private List<TariffPrice> itemList;

    public TariffPriceAdapter(final Context context) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(final List<TariffPrice> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public List<TariffPrice> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public TariffPriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_tariff_price_text_views, parent, false);
        return new TariffPriceViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffPriceViewHolder holder, int position) {
        final TariffPrice currentItem = itemList.get(position);

        if (currentItem != null) {
            holder.tariffTextView.setText(currentItem.getTariff());
            holder.priceTextView.setText(context.getString(R.string.rounded_total_price, currentItem.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}
