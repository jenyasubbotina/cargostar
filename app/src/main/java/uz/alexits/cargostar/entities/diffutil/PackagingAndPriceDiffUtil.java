package uz.alexits.cargostar.entities.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import uz.alexits.cargostar.entities.calculation.PackagingAndPrice;

public class PackagingAndPriceDiffUtil extends DiffUtil.Callback {
    final List<PackagingAndPrice> oldList;
    final List<PackagingAndPrice> newList;

    public PackagingAndPriceDiffUtil(final List<PackagingAndPrice> oldList, final List<PackagingAndPrice> newList) {
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
        return oldList.get(oldItemPosition).getPackaging().getId() == newList.get(newItemPosition).getPackaging().getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldList.get(oldItemPosition).getPackaging().getId() != newList.get(newItemPosition).getPackaging().getId()) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getPackaging().getName().equals(newList.get(newItemPosition).getPackaging().getName())) {
            return false;
        }
        if (oldList.get(oldItemPosition).getPackaging().getProviderId() != newList.get(newItemPosition).getPackaging().getProviderId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getPackaging().getParcelFee() != newList.get(newItemPosition).getPackaging().getParcelFee()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getPackaging().getVolumex() != newList.get(newItemPosition).getPackaging().getVolumex()) {
            return false;
        }
        return oldList.get(oldItemPosition).getPrice() == (newList.get(newItemPosition).getPrice());
    }
}
