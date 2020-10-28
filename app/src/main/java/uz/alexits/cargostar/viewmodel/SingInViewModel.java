package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.actor.Courier;

public class SingInViewModel extends AndroidViewModel {
    private final Repository repository;

    public SingInViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<Courier> selectCourierById(final long courierId) {
        return repository.selectCourier(courierId);
    }

    public LiveData<Courier> selectCourierByLogin(@NonNull final String login) {
        return repository.selectCourierByLogin(login);
    }

    public long createCourier(final Courier newCourier) {
        return repository.createCourier(newCourier);
    }
}
