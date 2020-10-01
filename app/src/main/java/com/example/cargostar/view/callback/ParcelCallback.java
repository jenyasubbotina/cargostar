package com.example.cargostar.view.callback;

import com.example.cargostar.model.shipping.Parcel;

public interface ParcelCallback {
    void onParcelSelected(final Parcel currentItem);
}
