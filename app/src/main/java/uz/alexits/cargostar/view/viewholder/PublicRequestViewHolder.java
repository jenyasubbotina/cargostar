package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.view.callback.PublicRequestCallback;

public class PublicRequestViewHolder extends RecyclerView.ViewHolder {
    //static views
    public ImageView arrowDownImageView;
    public ImageView plusImageView;
    //views with data
    public TextView indexTextView;
    public TextView parcelIdTextView;
    public TextView fromTextView;
    //hidden views
    public ImageView isNewIndicatorImageView;
    public ImageView isPaidIndicatorImageView;

    public PublicRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        indexTextView = itemView.findViewById(R.id.index_text_view);
        arrowDownImageView = itemView.findViewById(R.id.arrow_down_image_view);
        parcelIdTextView = itemView.findViewById(R.id.parcel_id_text_view);
        fromTextView = itemView.findViewById(R.id.from_text_view);
        plusImageView = itemView.findViewById(R.id.plus_image_view);
        //hidden views
        isNewIndicatorImageView = itemView.findViewById(R.id.is_new_indicator_image_view);
        isPaidIndicatorImageView = itemView.findViewById(R.id.is_paid_image_view);
    }

    public void bind(final int position, final Request currentItem, final PublicRequestCallback callback) {
        itemView.setOnClickListener(v -> {
            callback.onRequestSelected(position, currentItem);
        });
        plusImageView.setOnClickListener(v -> {
            callback.onPlusClicked(currentItem);
        });
    }
}