package uz.alexits.cargostar.entities.diffutil;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;
import uz.alexits.cargostar.entities.transportation.Request;

public class RequestDiffUtil extends DiffUtil.Callback {
    final List<Request> oldList;
    final List<Request> newList;

    public RequestDiffUtil(final List<Request> oldList, final List<Request> newList) {
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
        if (oldList.get(oldItemPosition).getSenderCountryId() != newList.get(newItemPosition).getSenderCountryId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getSenderEmail() != null && newList.get(newItemPosition).getSenderEmail() != null) {
            if (!oldList.get(oldItemPosition).getSenderEmail().equals(newList.get(newItemPosition).getSenderEmail())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getSenderCityName() != null && newList.get(newItemPosition).getSenderCityName() != null) {
            if (!oldList.get(oldItemPosition).getSenderCityName().equals(newList.get(newItemPosition).getSenderCityName())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getSenderFirstName() != null && newList.get(newItemPosition).getSenderFirstName() != null) {
            if (!oldList.get(oldItemPosition).getSenderFirstName().equals(newList.get(newItemPosition).getSenderFirstName())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getSenderMiddleName() != null && newList.get(newItemPosition).getSenderMiddleName() != null) {
            if (!oldList.get(oldItemPosition).getSenderMiddleName().equals(newList.get(newItemPosition).getSenderMiddleName())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getSenderLastName() != null && newList.get(newItemPosition).getSenderLastName() != null) {
            if (!oldList.get(oldItemPosition).getSenderLastName().equals(newList.get(newItemPosition).getSenderLastName())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getSenderPhone() != null && newList.get(newItemPosition).getSenderPhone() != null) {
            if (!oldList.get(oldItemPosition).getSenderPhone().equals( newList.get(newItemPosition).getSenderPhone())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getSenderAddress() != null && newList.get(newItemPosition).getSenderAddress() != null) {
            if (!oldList.get(oldItemPosition).getSenderAddress().equals( newList.get(newItemPosition).getSenderAddress())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getRecipientCityName() != null && newList.get(newItemPosition).getRecipientCityName() != null) {
            if (!oldList.get(oldItemPosition).getRecipientCityName().equals( newList.get(newItemPosition).getRecipientCityName())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getComment() != null && newList.get(newItemPosition).getComment() != null) {
            if (oldList.get(oldItemPosition).getComment().equals( newList.get(newItemPosition).getComment())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getPaymentStatus() != null && newList.get(newItemPosition).getPaymentStatus() != null) {
            if (!oldList.get(oldItemPosition).getPaymentStatus().equals(newList.get(newItemPosition).getPaymentStatus())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getRecipientCountryId() != newList.get(newItemPosition).getRecipientCountryId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getClientId() != newList.get(newItemPosition).getClientId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getCourierId() != newList.get(newItemPosition).getCourierId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getInvoiceId() != newList.get(newItemPosition).getInvoiceId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getProviderId() != newList.get(newItemPosition).getProviderId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getUserId() != newList.get(newItemPosition).getUserId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getConsignmentQuantity() != newList.get(newItemPosition).getConsignmentQuantity()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getDeliveryType() != newList.get(newItemPosition).getDeliveryType()) {
            return false;
        }
        return oldList.get(oldItemPosition).isNew() == (newList.get(newItemPosition).isNew());
    }
}