package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.model.transportation.Transportation;

public interface PartialCallback {
    void onPartialSelected(final Transportation currentItem);
}
