package com.example.cargostar.view.callback;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.model.shipping.ReceiptWithCargoList;

public interface RequestCallback {
    void onRequestSelected(final ReceiptWithCargoList currentItem, final RecyclerView.ViewHolder holder);
    void onPlusClicked(final Receipt currentItem);
}
