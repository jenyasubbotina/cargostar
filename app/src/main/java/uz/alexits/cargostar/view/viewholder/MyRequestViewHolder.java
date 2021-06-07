package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.view.callback.RequestCallback;

public class MyRequestViewHolder extends RecyclerView.ViewHolder {
    //views with data
    public TextView indexTextView;
    public TextView parcelIdTextView;
    public TextView fromTextView;
    //hidden views
    public ImageView isNewIndicatorImageView;
    public ImageView isPaidIndicatorImageView;

    public MyRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        indexTextView = itemView.findViewById(R.id.index_text_view);
        parcelIdTextView = itemView.findViewById(R.id.parcel_id_text_view);
        fromTextView = itemView.findViewById(R.id.from_text_view);
        //hidden views
        isNewIndicatorImageView = itemView.findViewById(R.id.is_new_indicator_image_view);
        isPaidIndicatorImageView = itemView.findViewById(R.id.is_paid_image_view);
    }

    public void bind(final int position, final Request currentItem, final RequestCallback callback) {
        itemView.setOnClickListener(v -> {
            callback.onRequestSelected(position, currentItem);
        });
    }
}
