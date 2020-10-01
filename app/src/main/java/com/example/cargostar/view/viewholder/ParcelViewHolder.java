package com.example.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cargostar.R;
import com.example.cargostar.model.shipping.Parcel;
import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.view.callback.ParcelCallback;

public class ParcelViewHolder extends RecyclerView.ViewHolder {
    //static views
    public ImageView nextImageView;
    //views with data
    public TextView indexTextView;
    public TextView parcelIdTextView;
    public TextView parcelTypeTextView;
    public TextView toTextView;
    public TextView fromTextView;
    //hidden views
    public TextView statusTextView;

    public ParcelViewHolder(@NonNull View itemView) {
        super(itemView);
        indexTextView = itemView.findViewById(R.id.index_text_view);
        nextImageView = itemView.findViewById(R.id.next_image_view);
        parcelIdTextView = itemView.findViewById(R.id.parcel_id_text_view);
        parcelTypeTextView = itemView.findViewById(R.id.parcel_type_text_view);
        toTextView = itemView.findViewById(R.id.to_text_view);
        fromTextView = itemView.findViewById(R.id.from_text_view);
        statusTextView = itemView.findViewById(R.id.status_text_view);
    }

    public void bind(final Parcel currentItem, final ParcelCallback callback) {
        itemView.setOnClickListener(v -> {
            callback.onParcelSelected(currentItem);
        });
    }
}