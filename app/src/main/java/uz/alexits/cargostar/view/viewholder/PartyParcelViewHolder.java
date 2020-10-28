package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;

public class PartyParcelViewHolder extends RecyclerView.ViewHolder {
    //views with data
    public TextView indexTextView;
    public TextView parcelIdTextView;
    public TextView toTextView;
    public TextView fromTextView;
    public ImageView nextImageView;

    public PartyParcelViewHolder(@NonNull View itemView) {
        super(itemView);
        indexTextView = itemView.findViewById(R.id.index_text_view);
        parcelIdTextView = itemView.findViewById(R.id.parcel_id_text_view);
        toTextView = itemView.findViewById(R.id.to_text_view);
        fromTextView = itemView.findViewById(R.id.from_text_view);
        nextImageView = itemView.findViewById(R.id.is_paid_image_view);
    }

//    public void bind(final Shipping currentItem, final PartyCallback callback) {
//        itemView.setOnClickListener(v -> {
//            callback.onPartySelected(currentItem);
//        });
//    }
}
