package uz.alexits.cargostar.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.view.viewholder.ConsignmentViewHolder;

import java.util.List;

public class ConsignmentAdapter extends RecyclerView.Adapter<ConsignmentViewHolder> {
    private final Context context;
    private List<Consignment> itemList;
    private final CreateInvoiceCallback callback;

    public ConsignmentAdapter(final Context context, final List<Consignment> itemList, final CreateInvoiceCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    public void setItemList(final List<Consignment> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
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

        if (currentItem != null) {
            final String index = (position + 1) + ".";
            holder.indexTextView.setText(index);
            holder.packageTypeTextView.setText(currentItem.getPackagingId() != null ? String.valueOf(currentItem.getPackagingId()) : "");
            holder.weightTextView.setText(String.valueOf(currentItem.getWeight()));
            holder.dimensionsTextView.setText(currentItem.getDimensions());

            if (!TextUtils.isEmpty(currentItem.getQr())) {
                holder.qrTextView.setText(currentItem.getQr());
                holder.qrTextView.setTextColor(Color.GREEN);
            }
            else {
                holder.qrTextView.setText(R.string.absent);
                holder.qrTextView.setTextColor(Color.RED);
            }
            holder.bind(position, callback);
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}
