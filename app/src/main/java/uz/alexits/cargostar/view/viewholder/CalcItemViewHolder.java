package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CalcItemViewHolder extends RecyclerView.ViewHolder {
    public TextView indexTextView;
    public TextView packageTypeTextView;
    public TextView weightTextView;
    public TextView dimensionsTextView;
    public ImageView deleteItemImageView;

    public CalcItemViewHolder(@NonNull View itemView) {
        super(itemView);
        indexTextView = itemView.findViewById(R.id.index_text_view);
        packageTypeTextView = itemView.findViewById(R.id.package_type_text_view);
        weightTextView = itemView.findViewById(R.id.weight_value_text_view);
        dimensionsTextView = itemView.findViewById(R.id.dimensions_value_text_view);
        deleteItemImageView = itemView.findViewById(R.id.delete_item_image_view);
    }

    public void bind(final int position, final CreateInvoiceCallback callback) {
        deleteItemImageView.setOnClickListener(v -> {
            callback.onDeleteItemClicked(position);
        });
    }

    public void unbind() {
        deleteItemImageView.setOnClickListener(null);
    }
}
