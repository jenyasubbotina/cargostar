package com.example.cargostar.view.callback;

import com.example.cargostar.model.Notification;
import com.example.cargostar.view.viewholder.NotificationViewHolder;

public interface NotificationCallback {
    void onNotificationSelected(final Notification currentItem, final NotificationViewHolder holder);
}
