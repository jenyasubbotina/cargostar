package uz.alexits.cargostar.view.callback;

import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;

public interface CreateInvoiceCallback {
    void onSenderSignatureClicked();
    void onRecipientSignatureClicked();
    void afterFirstEditTextChanged(final int position, final Editable editable);
    void afterSecondEditTextChanged(final int position, final Editable editable);
    void onSpinnerItemChanged(final AdapterView<?> adapterView, final View view, final int i, final long l);
    void onSpinnerEditTextItemSelected(final int position, final Object country);
    void onFirstSpinnerItemSelected(final int position, final Region region);
    void onSecondSpinnerItemSelected(final int position, final City city);
    void onDeleteItemClicked(final int position);
}
