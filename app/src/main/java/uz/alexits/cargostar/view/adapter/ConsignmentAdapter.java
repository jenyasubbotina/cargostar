package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.view.callback.ConsignmentCallback;
import uz.alexits.cargostar.view.viewholder.ConsignmentViewHolder;

import java.util.List;

public class ConsignmentAdapter extends RecyclerView.Adapter<ConsignmentViewHolder> {
    private final Context context;
    private final ConsignmentCallback callback;
    private List<Consignment> itemList;
    private final int viewHolderType;
    public ConsignmentAdapter(final Context context, final ConsignmentCallback callback, final int viewHolderType) {
        this.context = context;
        this.callback = callback;
        this.viewHolderType = viewHolderType;
    }

    public void setItemList(final List<Consignment> newItemList) {
//        final ConsignmentDiffUtil diffUtil = new ConsignmentDiffUtil(itemList, newItemList);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        this.itemList = newItemList;
//        diffResult.dispatchUpdatesTo(this);
    }

    public List<Consignment> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public ConsignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_consignment, parent, false);
        return new ConsignmentViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsignmentViewHolder holder, int position) {
        final Consignment currentItem = itemList.get(position);

        holder.indexTextView.setText(context.getString(R.string.consignment_index, position + 1));
        holder.packageTypeTextView.setText(currentItem.getPackagingId() != null ? String.valueOf(currentItem.getPackagingId()) : "");
        holder.weightTextView.setText(String.valueOf(currentItem.getWeight()));
        holder.dimensionsTextView.setText(currentItem.getDimensions());

        holder.bind(position, callback);

        /*calculator item*/
        if (viewHolderType == 1) {
            return;
        }
        if (viewHolderType == 2) {
            holder.qrTextView.setVisibility(View.VISIBLE);
            holder.qrValueTextView.setVisibility(View.VISIBLE);
            holder.scanConsignmentImageView.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(currentItem.getQr())) {
                holder.qrValueTextView.setText(R.string.absent);
                holder.qrValueTextView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorRed, null));
            }
            else {
                holder.qrValueTextView.setText(currentItem.getQr());
                holder.qrValueTextView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorGreen, null));
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}