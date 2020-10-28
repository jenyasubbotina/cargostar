package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.view.callback.ParcelDataCallback;
import uz.alexits.cargostar.view.viewholder.ParcelDataHeadingViewHolder;
import uz.alexits.cargostar.view.viewholder.ParcelDataItemViewHolder;
import uz.alexits.cargostar.view.viewholder.ParcelDataStrokeViewHolder;

import java.util.List;

import static uz.alexits.cargostar.view.adapter.ParcelData.TYPE_HEADING;
import static uz.alexits.cargostar.view.adapter.ParcelData.TYPE_ITEM;
import static uz.alexits.cargostar.view.adapter.ParcelData.TYPE_STROKE;

public class ParcelDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final ParcelDataCallback callback;
    private List<ParcelData> itemList;

    public ParcelDataAdapter(@NonNull final Context context, final ParcelDataCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setItemList(List<ParcelData> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;
        if (viewType == TYPE_HEADING) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_parcel_data_heading, parent, false);
            return new ParcelDataHeadingViewHolder(root);
        }
        else if (viewType == TYPE_ITEM) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_parcel_data_item, parent, false);
            return new ParcelDataItemViewHolder(root);
        }
        else {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_parcel_data_stoke, parent, false);
            return new ParcelDataStrokeViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADING) {
            final ParcelDataHeadingViewHolder viewHolder = (ParcelDataHeadingViewHolder) holder;
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
            final ParcelDataItemViewHolder viewHolder = ((ParcelDataItemViewHolder) holder);
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
            final ParcelDataStrokeViewHolder viewHolder = ((ParcelDataStrokeViewHolder) holder);
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
