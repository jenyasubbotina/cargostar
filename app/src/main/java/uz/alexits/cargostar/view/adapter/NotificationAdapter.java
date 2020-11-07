package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.Notification;
import uz.alexits.cargostar.view.callback.NotificationCallback;
import uz.alexits.cargostar.view.viewholder.NotificationViewHolder;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private final Context context;
    private final NotificationCallback callback;
    private List<Notification> notificationList;

    public NotificationAdapter(final Context context, final List<Notification> notificationList, final NotificationCallback callback) {
        this.context = context;
        this.notificationList = notificationList;
        this.callback = callback;
    }

    public void setNotificationList(final List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_notification, parent, false);
        return new NotificationViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        final Notification currentNot = notificationList.get(position);

        if (currentNot != null) {
            holder.titleTextView.setText(currentNot.getTitle());
            holder.linkTextView.setText(currentNot.getLink());
//            holder.fromTextView.setText(currentNot.getSenderAddress().getCity());
//            holder.toTextView.setText(currentNot.getRecipientAddress() != null && !TextUtils.isEmpty(currentNot.getRecipientAddress().getCity()) ? currentNot.getRecipientAddress().getCity() : null);
//            holder.dateTextView.setText(currentNot.getReceiveDate());

            if (currentNot.isRead()) {
                holder.readIndicatorImageView.setVisibility(View.INVISIBLE);
            }
            else {
                holder.readIndicatorImageView.setVisibility(View.VISIBLE);
            }
            holder.bind(currentNot, callback);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }
}
