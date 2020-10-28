package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.location.Branche;

public class HeaderViewModel extends AndroidViewModel {
    private final Repository repository;

    public HeaderViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return repository.selectCourierByLogin(login);
    }

    public LiveData<Branche> selectBrancheById(final long brancheId) {
        return repository.selectBrancheById(brancheId);
    }

    public LiveData<Request> selectRequest(final long requestId) {
        return repository.selectRequest(requestId);
    }

    public LiveData<Integer> selectNewNotificationsCount() {
        return repository.selectNewNotificationsCount();
    }
}
