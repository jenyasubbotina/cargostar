package uz.alexits.cargostar.view.callback;

import android.widget.TextView;

import uz.alexits.cargostar.model.transportation.Transportation;

public interface TransportationCallback {
    void onTransportationSelected(final Transportation currentItem);
//    void setTransportationSrcCity(final Transportation currentItem, final TextView srcCityTextView);
//    void setTransportationDestCity(final Transportation currentItem, final TextView destCityTextView);
//    void setTransportationType(final Transportation currentItem, final TextView typeTextView);
//    void setTransportationStatus(final Transportation currentItem, final TextView transportationStatus);
}
