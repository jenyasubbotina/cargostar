package uz.alexits.cargostar.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import uz.alexits.cargostar.viewmodel.CreateUserViewModel;

public class CreateUserViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public CreateUserViewModelFactory(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateUserViewModel.class)) {
            return (T) new CreateUserViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
