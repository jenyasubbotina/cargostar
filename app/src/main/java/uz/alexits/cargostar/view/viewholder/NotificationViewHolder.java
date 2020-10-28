package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.Notification;
import uz.alexits.cargostar.view.callback.NotificationCallback;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView;
    public TextView linkTextView;
    public TextView toTextView;
    public TextView fromTextView;
    //hidden views
    public TextView dateTextView;
    public ImageView readIndicatorImageView;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.notification_title_text_view);
        linkTextView = itemView.findViewById(R.id.notification_link_text_view);
        toTextView = itemView.findViewById(R.id.to_text_view);
        fromTextView = itemView.findViewById(R.id.from_text_view);
        //hidden views
        dateTextView = itemView.findViewById(R.id.notification_date_text_view);
        readIndicatorImageView = itemView.findViewById(R.id.read_indicator_image_view);
    }

    public void bind(final Notification currentItem, final NotificationCallback callback) {
        itemView.setOnClickListener(v -> {
            callback.onNotificationSelected(currentItem, this);
        });
    }
}
