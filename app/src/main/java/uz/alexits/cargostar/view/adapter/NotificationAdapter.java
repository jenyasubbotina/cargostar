package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.entities.diffutil.NotificationDiffUtil;
import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.utils.DateUtils;
import uz.alexits.cargostar.view.callback.NotificationCallback;
import uz.alexits.cargostar.view.viewholder.NotificationViewHolder;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private final Context context;
    private final NotificationCallback callback;
    private List<Notification> notificationList;

    public NotificationAdapter(final Context context, final NotificationCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setNotificationList(final List<Notification> notificationList) {
        final NotificationDiffUtil diffUtil = new NotificationDiffUtil(this.notificationList, notificationList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        this.notificationList = notificationList;
        diffResult.dispatchUpdatesTo(this);
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
            holder.linkTextView.setText(currentNot.getBody());
            holder.dateTextView.setText(DateUtils.dateToStr(currentNot.getReceiveDate()));

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
