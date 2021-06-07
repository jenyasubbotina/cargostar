package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.entities.transportation.Transportation;

public interface PartialCallback {
    void onPartialSelected(final Transportation currentItem);
}
