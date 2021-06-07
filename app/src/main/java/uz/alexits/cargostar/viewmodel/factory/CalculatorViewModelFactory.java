package uz.alexits.cargostar.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import uz.alexits.cargostar.viewmodel.CalculatorViewModel;

public class CalculatorViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public CalculatorViewModelFactory(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CalculatorViewModel.class)) {
            return (T) new CalculatorViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
