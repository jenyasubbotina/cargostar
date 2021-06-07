package uz.alexits.cargostar.viewmodel.factory;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import uz.alexits.cargostar.viewmodel.NotificationViewModel;

public class NotificationViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public NotificationViewModelFactory(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NotificationViewModel.class)) {
            return (T) new NotificationViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
