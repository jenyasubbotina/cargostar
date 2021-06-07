package uz.alexits.cargostar.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import uz.alexits.cargostar.viewmodel.CreateUserViewModel;
import uz.alexits.cargostar.viewmodel.ImportViewModel;

public class ImportViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ImportViewModelFactory(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImportViewModel.class)) {
            return (T) new ImportViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}