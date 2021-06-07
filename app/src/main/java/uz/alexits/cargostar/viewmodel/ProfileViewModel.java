package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

import uz.alexits.cargostar.entities.location.Country;

public class ProfileViewModel extends HeaderViewModel {
    private final MutableLiveData<Long> profileCountryId;
    private final MutableLiveData<UUID> editProfileDataUUID;

    public ProfileViewModel(final Context context) {
        super(context);

        this.profileCountryId = new MutableLiveData<>();
        this.editProfileDataUUID = new MutableLiveData<>();
    }

    public void editProfileData(final String login,
                                final String email,
                                final String password,
                                final String firstName,
                                final String middleName,
                                final String lastName,
                                final String phone,
                                final String photoUrl) {
        editProfileDataUUID.setValue(courierRepository.editProfileData(login, email, password, firstName, middleName, lastName, phone, photoUrl));
    }

    public LiveData<Country> getProfileCountry() {
        return Transformations.switchMap(profileCountryId, locationRepository::selectCountryById);
    }
    public void setCourierCountryId(final long countryId) {
        profileCountryId.setValue(countryId);
    }

    public LiveData<WorkInfo> getEditProfileResult(final Context context) {
        return Transformations.switchMap(editProfileDataUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }
}
