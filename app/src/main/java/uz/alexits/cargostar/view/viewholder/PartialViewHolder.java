package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.view.callback.PartialCallback;

public class PartialViewHolder extends RecyclerView.ViewHolder {
    //views with data
    public TextView indexTextView;
    public TextView transportationIdTextView;
    public TextView toTextView;
    public TextView fromTextView;
    public ImageView nextImageView;

    public PartialViewHolder(@NonNull View itemView) {
        super(itemView);
        indexTextView = itemView.findViewById(R.id.index_text_view);
        transportationIdTextView = itemView.findViewById(R.id.parcel_id_text_view);
        toTextView = itemView.findViewById(R.id.to_text_view);
        fromTextView = itemView.findViewById(R.id.from_text_view);
        nextImageView = itemView.findViewById(R.id.is_paid_image_view);
    }

    public void bind(final Transportation currentItem, final PartialCallback callback) {
        itemView.setOnClickListener(v -> {
            callback.onPartialSelected(currentItem);
        });
    }
}
