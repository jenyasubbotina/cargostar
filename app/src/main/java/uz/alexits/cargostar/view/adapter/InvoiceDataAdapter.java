package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.view.callback.InvoiceDataCallback;
import uz.alexits.cargostar.view.viewholder.InvoiceDataHeadingViewHolder;
import uz.alexits.cargostar.view.viewholder.InvoiceDataItemViewHolder;
import uz.alexits.cargostar.view.viewholder.InvoiceDataStrokeViewHolder;

import static uz.alexits.cargostar.view.adapter.InvoiceData.TYPE_HEADING;
import static uz.alexits.cargostar.view.adapter.InvoiceData.TYPE_ITEM;
import static uz.alexits.cargostar.view.adapter.InvoiceData.TYPE_STROKE;


public class InvoiceDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final InvoiceDataCallback callback;
    private List<InvoiceData> itemList;

    public InvoiceDataAdapter(@NonNull final Context context, final InvoiceDataCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setItemList(List<InvoiceData> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;
        if (viewType == TYPE_HEADING) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_invoice_data_heading, parent, false);
            return new InvoiceDataHeadingViewHolder(root);
        }
        else if (viewType == TYPE_ITEM) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_invoice_data_item, parent, false);
            return new InvoiceDataItemViewHolder(root);
        }
        else {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_invoice_data_stoke, parent, false);
            return new InvoiceDataStrokeViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADING) {
            final InvoiceDataHeadingViewHolder viewHolder = (InvoiceDataHeadingViewHolder) holder;
            viewHolder.headingTextView.setText(itemList.get(position).key);
            viewHolder.bindArrow(callback, position);

            if (itemList.get(position).isHidden) {
                viewHolder.arrowImageView.setImageResource(R.drawable.ic_down_arrow);
            }
            else {
                viewHolder.arrowImageView.setImageResource(R.drawable.ic_up_arrow);
            }
        }
        else if (getItemViewType(position) == TYPE_ITEM) {
            final InvoiceDataItemViewHolder viewHolder = ((InvoiceDataItemViewHolder) holder);
            viewHolder.itemKeyTextView.setText(itemList.get(position).key);

            if (!TextUtils.isEmpty(itemList.get(position).value)) {
                viewHolder.itemValueTextView.setText(itemList.get(position).value);
                viewHolder.itemValueTextView.setTextColor(context.getColor(R.color.colorBlack));
            }
            else {
                viewHolder.itemValueTextView.setText(context.getString(R.string.absent));
                viewHolder.itemValueTextView.setTextColor(context.getColor(R.color.colorRed));
            }
        }
        else {
            final InvoiceDataStrokeViewHolder viewHolder = ((InvoiceDataStrokeViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).type == TYPE_HEADING) {
            return TYPE_HEADING;
        }
        if (itemList.get(position).type == TYPE_ITEM) {
            return TYPE_ITEM;
        }
        if (itemList.get(position).type == TYPE_STROKE) {
            return TYPE_STROKE;
        }
        return -1;
    }
}
