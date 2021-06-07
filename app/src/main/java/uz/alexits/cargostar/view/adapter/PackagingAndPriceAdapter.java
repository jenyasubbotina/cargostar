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
import uz.alexits.cargostar.view.viewholder.TariffPriceViewHolder;

public class PackagingAndPriceAdapter extends RecyclerView.Adapter<TariffPriceViewHolder> {
    private final Context context;
    private List<PackagingAndPrice> resultList;

    public PackagingAndPriceAdapter(final Context context) {
        this.context = context;
    }

    public void setResultList(final List<PackagingAndPrice> newResultList) {
        final PackagingAndPriceDiffUtil diffUtil = new PackagingAndPriceDiffUtil(resultList, newResultList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        this.resultList = newResultList;
        diffResult.dispatchUpdatesTo(this);
    }

    public List<PackagingAndPrice> getItemList() {
        return resultList;
    }

    @NonNull
    @Override
    public TariffPriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_tariff_price_text_views, parent, false);
        return new TariffPriceViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffPriceViewHolder holder, int position) {
        final PackagingAndPrice currentItem = resultList.get(position);

        if (currentItem != null) {
            holder.tariffTextView.setText(currentItem.getPackaging().getName());
            holder.priceTextView.setText(context.getString(R.string.rounded_total_price, String.valueOf(currentItem.getPrice())));
        }
    }

    @Override
    public int getItemCount() {
        return resultList != null ? resultList.size() : 0;
    }
}
