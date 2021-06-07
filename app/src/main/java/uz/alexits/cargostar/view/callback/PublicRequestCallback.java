package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.entities.transportation.Request;

public interface PublicRequestCallback extends RequestCallback {
    void onPlusClicked(final Request currentItem);
}
