package uz.alexits.cargostar.entities.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Consignment;

public class ConsignmentDiffUtil extends DiffUtil.Callback {
    private final List<Consignment> oldList;
    private final List<Consignment> newList;

    public ConsignmentDiffUtil(final List<Consignment> oldList, final List<Consignment> newList) {
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
        if (oldList.get(oldItemPosition).getName() == null || newList.get(newItemPosition).getName() == null) {
            return false;
        }
        if (oldList.get(oldItemPosition).getCost() == null || newList.get(newItemPosition).getCost() == null) {
            return false;
        }
        if (oldList.get(oldItemPosition).getDimensions() == null || newList.get(newItemPosition).getDimensions() == null) {
            return false;
        }
        if (oldList.get(oldItemPosition).getDescription() == null || newList.get(newItemPosition).getDescription() == null) {
            return false;
        }
        if (oldList.get(oldItemPosition).getQr() == null || newList.get(newItemPosition).getQr() == null) {
            return false;
        }
        if (oldList.get(oldItemPosition).getPackagingType() == null || newList.get(newItemPosition).getPackagingType() == null) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName())) {
            return false;
        }
        if (oldList.get(oldItemPosition).getHeight() != newList.get(newItemPosition).getHeight()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getLength() != newList.get(newItemPosition).getLength()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getWidth() != newList.get(newItemPosition).getWidth()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getWeight() != newList.get(newItemPosition).getWeight()) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getCost().equals(newList.get(newItemPosition).getCost())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getDimensions().equals( newList.get(newItemPosition).getDimensions())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getDescription().equals( newList.get(newItemPosition).getDescription())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getPackagingType().equals( newList.get(newItemPosition).getPackagingType())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getQr().equals( newList.get(newItemPosition).getQr())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getRequestId().equals(newList.get(newItemPosition).getRequestId())) {
            return false;
        }
        return oldList.get(oldItemPosition).getPackagingId().equals(newList.get(newItemPosition).getPackagingId());
    }
}