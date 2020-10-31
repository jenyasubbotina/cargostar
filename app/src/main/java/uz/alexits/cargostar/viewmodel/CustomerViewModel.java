package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import uz.alexits.cargostar.database.cache.Repository;

//todo: create customerList, passportDataList and paymentDataList
public class CustomerViewModel extends AndroidViewModel {
    private final Repository repository;
//    private LiveData<List<Customer>> customerList;

    public CustomerViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
//        this.customerList = repository.selectAllCustomers();
    }

    /* Customer queries */
//    public long createCustomer(final Customer newCustomer) {
//        return repository.createCustomer(newCustomer);
//    }
//
//    public LiveData<List<Customer>> selectAllCustomers() {
//        return customerList;
//    }
//
//    public void dropCustomers() {
//        repository.dropCustomers();
//    }
//
//    /* Passport data queries */
//    public void createPassportData(final PassportData passportData) {
//        repository.createPassportData(passportData);
//    }
//
//    public LiveData<PassportData> selectPassportData(final String userId) {
//        return repository.selectPassportData(userId);
//    }
//
//    public LiveData<List<PassportData>> selectAllPassportData() {
//        return repository.selectAllPassportData();
//    }
//
//    public void dropPassportData() {
//        repository.dropPassportData();
//    }
//
//    /* Payment data queries */
//    public void createPaymentData(final PaymentData paymentData) {
//        repository.createPaymentData(paymentData);
//    }
//
//    public LiveData<PaymentData> selectPaymentData(final String userId) {
//        return repository.selectPaymentData(userId);
//    }
//
//    public LiveData<List<PaymentData>> selectAllPaymentData() {
//        return repository.selectAllPaymentData();
//    }
//
//    public void dropPaymentData() {
//        repository.dropPaymentData();
//    }
}
