package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.model.transportation.Transportation;

public interface ParcelCallback {
    void onParcelSelected(final Transportation currentItem);
}
