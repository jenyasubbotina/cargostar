package uz.alexits.cargostar.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import uz.alexits.cargostar.viewmodel.CreateUserViewModel;
import uz.alexits.cargostar.viewmodel.InvoiceViewModel;

public class InvoiceViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public InvoiceViewModelFactory(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(InvoiceViewModel.class)) {
            return (T) new InvoiceViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
