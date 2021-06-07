package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.entities.transportation.Request;

public interface RequestCallback {
    void onRequestSelected(final int position, final Request currentItem);
}
