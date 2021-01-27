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
import uz.alexits.cargostar.view.callback.TariffCallback;
import uz.alexits.cargostar.view.viewholder.TariffPriceRadioViewHolder;

public class TariffPriceRadioAdapter extends RecyclerView.Adapter<TariffPriceRadioViewHolder> {
    private final Context context;
    private List<TariffPrice> itemList;
    private final TariffCallback callback;
    private int position;
    private int lastCheckedPosition;

    public TariffPriceRadioAdapter(final Context context, final TariffCallback callback) {
        this.context = context;
        this.callback = callback;
        this.lastCheckedPosition = -1;
    }

    public void setItemList(final List<TariffPrice> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public String getSelectedPrice() {
        return itemList == null || itemList.isEmpty() || lastCheckedPosition < 0 ? null : itemList.get(lastCheckedPosition).getPrice();
    }

    public long getSelectedPackagingId() {
        return itemList == null || itemList.isEmpty() || lastCheckedPosition < 0 ? 0 : itemList.get(lastCheckedPosition).getTariffId();
    }

    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    public void setLastCheckedPosition(int lastCheckedPosition) {
        this.lastCheckedPosition = lastCheckedPosition;
    }

    public List<TariffPrice> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public TariffPriceRadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_tariff_price_radio, parent, false);
        return new TariffPriceRadioViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffPriceRadioViewHolder holder, int position) {
        final TariffPrice currentItem = itemList.get(position);

        this.position = position;

        if (currentItem != null) {
            holder.tariffRadioBtn.setText(currentItem.getTariff());
            holder.priceTextView.setText(context.getString(R.string.rounded_total_price, currentItem.getPrice()));

            if (lastCheckedPosition != position) {
                holder.tariffRadioBtn.setChecked(false);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TariffPriceRadioViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ((TariffPriceRadioViewHolder) holder).bindTariffList(position, lastCheckedPosition, callback);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TariffPriceRadioViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((TariffPriceRadioViewHolder) holder).unbindTariffList();
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}