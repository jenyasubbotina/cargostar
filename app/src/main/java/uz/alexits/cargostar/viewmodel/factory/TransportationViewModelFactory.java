package uz.alexits.cargostar.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.viewmodel.TransportationViewModel;

public class TransportationViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public TransportationViewModelFactory(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TransportationViewModel.class)) {
            return (T) new TransportationViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}