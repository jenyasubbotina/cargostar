package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.model.shipping.Parcel;

public interface ParcelCallback {
    void onParcelSelected(final Parcel currentItem);
}
