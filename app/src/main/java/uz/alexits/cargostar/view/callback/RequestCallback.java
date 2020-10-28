package uz.alexits.cargostar.view.callback;

import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.model.shipping.Request;

public interface RequestCallback {
    void onRequestSelected(final Request currentItem, final RecyclerView.ViewHolder holder);
    void onPlusClicked(final Request currentItem);
}
