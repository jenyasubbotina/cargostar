package com.example.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cargostar.R;
import com.example.cargostar.model.shipping.Cargo;
import com.example.cargostar.view.callback.CreateParcelCallback;
import com.example.cargostar.view.viewholder.CalcItemViewHolder;

import java.util.List;

public class CalculatorAdapter extends RecyclerView.Adapter<CalcItemViewHolder> {
    private final Context context;
    private List<Cargo> itemList;
    private final CreateParcelCallback callback;

    public CalculatorAdapter(Context context, List<Cargo> itemList, CreateParcelCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    public void setItemList(List<Cargo> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public List<Cargo> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public CalcItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_calculator, parent, false);
        return new CalcItemViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CalcItemViewHolder holder, int position) {
        final Cargo currentItem = itemList.get(position);

        if (currentItem != null) {
            final String index = (position + 1) + ".";
            final String dimensions = currentItem.getLength() + "x" + currentItem.getWidth() + "x" + currentItem.getHeight();
            holder.indexTextView.setText(index);
//            holder.sourceTextView.setText(currentItem.getSource().getCity().getCode());
//            holder.destinationTextView.setText(currentItem.getDestination().getCity().getCode());
            holder.packageTypeTextView.setText(currentItem.getPackageType());
            holder.weightTextView.setText(String.valueOf(currentItem.getWeight()));
            holder.dimensionsTextView.setText(dimensions);
            holder.bind(position, callback);
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}
