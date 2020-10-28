package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.model.Notification;
import uz.alexits.cargostar.view.viewholder.NotificationViewHolder;

public interface NotificationCallback {
    void onNotificationSelected(final Notification currentItem, final NotificationViewHolder holder);
}
