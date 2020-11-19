package uz.alexits.cargostar.view.callback;

import android.widget.TextView;

import uz.alexits.cargostar.model.transportation.Transportation;

public interface TransportationCallback {
    void onTransportationSelected(final Transportation currentItem);
}
