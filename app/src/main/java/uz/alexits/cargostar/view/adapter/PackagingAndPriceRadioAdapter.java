package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.calculation.PackagingAndPrice;
import uz.alexits.cargostar.entities.diffutil.PackagingAndPriceDiffUtil;
import uz.alexits.cargostar.view.callback.TariffCallback;
import uz.alexits.cargostar.view.viewholder.TariffPriceRadioViewHolder;

public class PackagingAndPriceRadioAdapter extends RecyclerView.Adapter<TariffPriceRadioViewHolder> {
    private final Context context;
    private List<PackagingAndPrice> resultList;

    private final TariffCallback callback;

    private int lastCheckedPosition;

    public PackagingAndPriceRadioAdapter(final Context context, final TariffCallback callback) {
        this.context = context;
        this.callback = callback;
        this.lastCheckedPosition = -1;
    }

    public void setResultList(final List<PackagingAndPrice> newResultList) {
        final PackagingAndPriceDiffUtil diffUtil = new PackagingAndPriceDiffUtil(resultList, newResultList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        this.resultList = newResultList;
        diffResult.dispatchUpdatesTo(this);
    }

    public double getSelectedPrice() {
        return resultList == null || resultList.isEmpty() || lastCheckedPosition <= 0 ? 0 : resultList.get(lastCheckedPosition).getPrice();
    }

    public long getSelectedPackagingId() {
        return resultList == null || resultList.isEmpty() || lastCheckedPosition <= 0 ? 0 : resultList.get(lastCheckedPosition).getPackaging().getId();
    }

    public void setLastCheckedPosition(int lastCheckedPosition) {
        this.lastCheckedPosition = lastCheckedPosition;
    }

    public List<PackagingAndPrice> getResultList() {
        return resultList;
    }

    @NonNull
    @Override
    public TariffPriceRadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_tariff_price_radio, parent, false);
        return new TariffPriceRadioViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffPriceRadioViewHolder holder, int position) {
        final PackagingAndPrice currentItem = resultList.get(position);

        if (currentItem != null) {
            holder.tariffRadioBtn.setText(currentItem.getPackaging().getName());
            holder.priceTextView.setText(context.getString(R.string.rounded_total_price, String.valueOf(currentItem.getPrice())));
            holder.bindPackagingList(position, currentItem, holder, callback);

            if (lastCheckedPosition > -1) {
                holder.tariffRadioBtn.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return resultList != null ? resultList.size() : 0;
    }
}