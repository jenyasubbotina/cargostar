package uz.alexits.cargostar.entities.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Import;

public class ImportDiffUtil extends DiffUtil.Callback {
    private final List<Import> oldList;
    private final List<Import> newList;

    public ImportDiffUtil(final List<Import> oldList, final List<Import> newList) {
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
        return oldList.get(oldItemPosition).getInvoiceId() == newList.get(newItemPosition).getInvoiceId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldList.get(oldItemPosition).getInvoiceId() != newList.get(newItemPosition).getInvoiceId()) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getImportStatus().equals(newList.get(newItemPosition).getImportStatus())) {
            return false;
        }
        if (oldList.get(oldItemPosition).getTransportationType() != newList.get(newItemPosition).getTransportationType()) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getFullName().equals(newList.get(newItemPosition).getFullName())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getPhone().equals( newList.get(newItemPosition).getPhone())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getAddress().equals(newList.get(newItemPosition).getAddress())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getOrganization().equals(newList.get(newItemPosition).getOrganization())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getComment().equals( newList.get(newItemPosition).getComment())) {
            return false;
        }
        if (!oldList.get(oldItemPosition).getRecipientSignature().equals( newList.get(newItemPosition).getRecipientSignature())) {
            return false;
        }
        return oldList.get(oldItemPosition).getRecipientSignatureDate().equals(newList.get(newItemPosition).getRecipientSignatureDate());
    }
}