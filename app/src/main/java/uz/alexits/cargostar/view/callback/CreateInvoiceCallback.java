package uz.alexits.cargostar.view.callback;

import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;

public interface CreateInvoiceCallback {
    void onSenderSignatureClicked();
    void onRecipientSignatureClicked();
    void afterFirstEditTextChanged(final int position, final CharSequence text);
    void afterSecondEditTextChanged(final int position, final CharSequence text);
    void onSpinnerEditTextItemSelected(final int position, final Object country);
    void onFirstSpinnerItemSelected(final int position, final Region region);
    void onSecondSpinnerItemSelected(final int position, final City city);
    void onBigSpinnerItemSelected(final int position, final Object entry);
    void onDeleteItemClicked(final int position);
}
