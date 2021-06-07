package uz.alexits.cargostar.view.callback;

import uz.alexits.cargostar.entities.calculation.PackagingAndPrice;
import uz.alexits.cargostar.view.viewholder.TariffPriceRadioViewHolder;

public interface TariffCallback {
    void onTariffSelected(final int position, final PackagingAndPrice item, TariffPriceRadioViewHolder holder);
}
