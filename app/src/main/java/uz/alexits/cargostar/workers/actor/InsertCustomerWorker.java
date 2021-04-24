package uz.alexits.cargostar.workers.actor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Serializer;

public class InsertCustomerWorker extends Worker {
    private final String email;
    private final String password;
    private final String cargostarAccountNumber;
    private final String tntAccountNumber;
    private final String fedexAccountNumber;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String phone;
    private final String country;
    private final String cityName;
    private final String address;
    private final String geolocation;
    private final String zip;
    private final double discount;
    private final int userType;
    private final String passportSerial;
    private final String inn;
    private final String company;
    private final String contractNumber;
    private final String signatureUrl;

    public InsertCustomerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.email = getInputData().getString(Constants.KEY_EMAIL);
        this.password = getInputData().getString(Constants.KEY_PASSWORD);
        this.cargostarAccountNumber = getInputData().getString(Constants.KEY_CARGOSTAR);
        this.tntAccountNumber = getInputData().getString(Constants.KEY_TNT);
        this.fedexAccountNumber = getInputData().getString(Constants.KEY_FEDEX);
        this.firstName = getInputData().getString(Constants.KEY_FIRST_NAME);
        this.middleName = getInputData().getString(Constants.KEY_MIDDLE_NAME);
        this.lastName = getInputData().getString(Constants.KEY_LAST_NAME);
        this.phone = getInputData().getString(Constants.KEY_PHONE);
        this.country = getInputData().getString(Constants.KEY_COUNTRY);
        this.cityName = getInputData().getString(Constants.KEY_CITY_NAME);
        this.address = getInputData().getString(Constants.KEY_ADDRESS);
        this.geolocation = getInputData().getString(Constants.KEY_GEOLOCATION);
        this.zip = getInputData().getString(Constants.KEY_ZIP);
        this.discount = getInputData().getDouble(Constants.KEY_DISCOUNT, 0);
        this.userType = getInputData().getInt(Constants.KEY_USER_TYPE, 0);
        this.passportSerial = getInputData().getString(Constants.KEY_PASSPORT_SERIAL);
        this.inn = getInputData().getString(Constants.KEY_INN);
        this.company = getInputData().getString(Constants.KEY_COMPANY);
        this.contractNumber = getInputData().getString(Constants.KEY_PAYER_CONTRACT_NUMBER);
        this.signatureUrl = getInputData().getString(Constants.KEY_SIGNATURE);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));

            String signatureBytesStr = null;

            if (!TextUtils.isEmpty(signatureUrl)) {
                signatureBytesStr = Serializer.fileToBase64(signatureUrl);
            }

            final Response<Customer> response = RetrofitClient.getInstance(getApplicationContext())
                    .createClient(
                            email,
                            password,
                            email,
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
                            signatureBytesStr);
            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final Customer newCustomer = response.body();

                    if (newCustomer == null) {
                        Log.e(TAG, "createUser(): customer is NULL");
                        return Result.failure();
                    }
                    newCustomer.setEmail(email);
                    newCustomer.setSignatureUrl(signatureUrl);

                    Log.i(TAG, "createUser(): " + newCustomer);

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).actorDao().createCustomer(newCustomer);

                    if (rowInserted == -1) {
                        Log.e(TAG, "createUser(): couldn't insert customer " + newCustomer);
                        return Result.failure();
                    }
                    Log.i(TAG, "createUser(): successfully inserted customer " + newCustomer);
                    return Result.success();
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "createUser(): ", e);
            return Result.failure();
        }
        Log.e(TAG, "createUser(): unknown error");
        return Result.failure();
    }

    private static final String TAG = InsertCustomerWorker.class.toString();
}
