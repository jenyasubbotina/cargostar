package uz.alexits.cargostar.entities.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Transportation;

public class TransportationDiffUtil extends DiffUtil.Callback {
    private final List<Transportation> oldList;
    private final List<Transportation> newList;

    public TransportationDiffUtil(final List<Transportation> oldList, final List<Transportation> newList) {
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
        if (oldList.get(oldItemPosition).getCityTo() != null && newList.get(newItemPosition).getCityTo() != null) {
            if (!oldList.get(oldItemPosition).getCityTo().equals(newList.get(newItemPosition).getCityTo())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getCityFrom() != null && newList.get(newItemPosition).getCityFrom() != null) {
            if (!oldList.get(oldItemPosition).getCityFrom().equals(newList.get(newItemPosition).getCityFrom())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getTransportationStatusName() != null && newList.get(newItemPosition).getTransportationStatusName() != null) {
            if (!oldList.get(oldItemPosition).getTransportationStatusName().equals(newList.get(newItemPosition).getTransportationStatusName())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getArrivalDate() != null && newList.get(newItemPosition).getArrivalDate() != null) {
            if (!oldList.get(oldItemPosition).getArrivalDate().equals(newList.get(newItemPosition).getArrivalDate())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getDirection() != null && newList.get(newItemPosition).getDirection() != null) {
            if (!oldList.get(oldItemPosition).getDirection().equals(newList.get(newItemPosition).getDirection())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getInstructions() != null && newList.get(newItemPosition).getInstructions() != null) {
            if (!oldList.get(oldItemPosition).getInstructions().equals( newList.get(newItemPosition).getInstructions())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getPartyQrCode() != null && newList.get(newItemPosition).getPartyQrCode() != null) {
            if (!oldList.get(oldItemPosition).getPartyQrCode().equals( newList.get(newItemPosition).getPartyQrCode())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getTrackingCode() != null && newList.get(newItemPosition).getTrackingCode() != null) {
            if (!oldList.get(oldItemPosition).getTrackingCode().equals( newList.get(newItemPosition).getTrackingCode())) {
                return false;
            }
        }
        if (oldList.get(oldItemPosition).getQrCode() != null && newList.get(newItemPosition).getQrCode() != null) {
            if (oldList.get(oldItemPosition).getQrCode().equals( newList.get(newItemPosition).getQrCode())) {
                return false;
            }
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
        if (oldList.get(oldItemPosition).getBrancheId() != newList.get(newItemPosition).getBrancheId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getPartialId() != newList.get(newItemPosition).getPartialId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getRequestId() != newList.get(newItemPosition).getRequestId()) {
            return false;
        }

        if (oldList.get(oldItemPosition).getCurrentTransitionPointId() != newList.get(newItemPosition).getCurrentTransitionPointId()) {
            return false;
        }
        if (oldList.get(oldItemPosition).getTransportationStatusId() != newList.get(newItemPosition).getTransportationStatusId()) {
            return false;
        }
        return oldList.get(oldItemPosition).getPaymentStatusId() == newList.get(newItemPosition).getPaymentStatusId();
    }
}
