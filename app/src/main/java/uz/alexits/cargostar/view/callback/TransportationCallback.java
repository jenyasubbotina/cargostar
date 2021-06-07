package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.entities.transportation.Transportation;

public interface TransportationCallback {
    void onTransportationSelected(final Transportation currentItem);
}
