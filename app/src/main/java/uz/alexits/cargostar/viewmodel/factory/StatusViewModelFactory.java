package uz.alexits.cargostar.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import uz.alexits.cargostar.viewmodel.StatusViewModel;

public class StatusViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public StatusViewModelFactory(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StatusViewModel.class)) {
            return (T) new StatusViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
