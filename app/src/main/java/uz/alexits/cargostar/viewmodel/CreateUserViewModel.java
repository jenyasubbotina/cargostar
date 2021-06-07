package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import java.util.List;
import java.util.UUID;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.repository.ClientRepository;

public class CreateUserViewModel extends HeaderViewModel {
    private final ClientRepository clientRepository;

    private final MutableLiveData<UUID> createUserUUID;

    public CreateUserViewModel(final Context context) {
        super(context);
        this.clientRepository = new ClientRepository(context);
        this.createUserUUID = new MutableLiveData<>();
    }

    public LiveData<List<Country>> getCountryList() {
        return locationRepository.selectAllCountries();
    }

    public void setCountryId(final long id) {
    }

    public void createUser(final String email,
                           final String password,
                           final String cargostarAccountNumber,
                           final String tntAccountNumber,
                           final String fedexAccountNumber,
                           final String firstName,
                           final String middleName,
                           final String lastName,
                           final String phone,
                           final String country,
                           final String cityName,
                           final String address,
                           final String geolocation,
                           final String zip,
                           final double discount,
                           final int userType,
                           final String passportSerial,
                           final String inn,
                           final String company,
                           final String contractNumber,
                           final String signatureUrl) {
        createUserUUID.setValue(clientRepository.createClient(
                email,
                password,
                cargostarAccountNumber,
                tntAccountNumber,
                fedexAccountNumber,
                firstName,
                middleName,
                lastName,
                phone,
                country,
                cityName,
                address,
                geolocation,
                zip,
                discount,
                userType,
                passportSerial,
                inn,
                company,
                contractNumber,
                signatureUrl));
    }

    public LiveData<WorkInfo> getCreateClientResult(final Context context) {
        return Transformations.switchMap(createUserUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }
}
