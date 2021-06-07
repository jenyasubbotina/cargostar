package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.entities.transportation.Import;

public interface ImportCallback {
    void onImportItemClicked(final int position, final Import item, final ImportCallback callback);
}
