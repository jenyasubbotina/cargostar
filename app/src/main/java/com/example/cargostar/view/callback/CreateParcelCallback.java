package com.example.cargostar.view.callback;

import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;

public interface CreateParcelCallback {
    void onAddBtnClicked();
    void onCameraImageClicked(final int position);
    void onSenderSignatureClicked();
    void onRecipientSignatureClicked();
    void afterFirstEditTextChanged(final int position, final Editable editable);
    void afterSecondEditTextChanged(final int position, final Editable editable);
    void onSpinnerItemChanged(final AdapterView<?> adapterView, final View view, final int i, final long l);
    void onDeleteItemClicked(final int position);
}
