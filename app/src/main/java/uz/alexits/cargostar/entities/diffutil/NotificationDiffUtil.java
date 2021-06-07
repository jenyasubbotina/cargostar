package uz.alexits.cargostar.entities.diffutil;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;
import uz.alexits.cargostar.push.Notification;

public class NotificationDiffUtil extends DiffUtil.Callback {
    private final List<Notification> oldList;
    private final List<Notification> newList;

    public NotificationDiffUtil(final List<Notification> oldList, final List<Notification> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldList.get(oldItemPosition).getId() != newList.get(newItemPosition).getId()) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getActivity().equals(newList.get(newItemPosition).getActivity())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getBody().equals(newList.get(newItemPosition).getBody())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getLink().equals(newList.get(newItemPosition).getLink())) {
            return false;
        }
        if (oldList.get(oldItemPosition).getReceiveDate() != newList.get(newItemPosition).getReceiveDate()) {
            return false;
        }
        return oldList.get(oldItemPosition).getTitle().equals(newList.get(newItemPosition).getTitle());
    }
}