package uz.alexits.cargostar.workers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.FileManager;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.entities.params.AddresseeParams;
import uz.alexits.cargostar.entities.transportation.Addressee;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Serializer;

public class SendRecipientSignatureWorker extends Worker {
    private final AddresseeParams params;

    public SendRecipientSignatureWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.params = new AddresseeParams(
                getInputData().getLong(Constants.KEY_INVOICE_ID, 0),
                getInputData().getString(Constants.ADDRESSEE_FULL_NAME),
                getInputData().getString(Constants.ADDRESSEE_PHONE),
                getInputData().getString(Constants.ADDRESSEE_ADDRESS),
                getInputData().getString(Constants.ADDRESSEE_ORGANIZATION),
                getInputData().getString(Constants.ADDRESSEE_COMMENT),
                getInputData().getString(Constants.ADDRESSEE_SIGNATURE) != null ? Serializer.fileToBase64(getInputData().getString(Constants.ADDRESSEE_SIGNATURE)) : null,
                getInputData().getString(Constants.ADDRESSEE_SIGNATURE_DATE),
                getInputData().getBoolean(Constants.ADDRESSEE_IS_ACCEPTED, false));
    }

    @NonNull
    @Override
    public Result doWork() {
        if (params == null) {
            Log.e(TAG, "sendRecipientSignature(): params are NULL");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Invoice> response = RetrofitClient.getInstance(getApplicationContext()).sendRecipientSignature(params);

            if (response.code() == 200 || response.code() == 201) {
                if (response.isSuccessful()) {
                    final Invoice invoice = response.body();

                    if (invoice == null) {
                        return Result.failure();
                    }

                    LocalCache.getInstance(getApplicationContext()).invoiceDao().insertAddressee(
                            params.getInvoiceId(),
                            invoice.getFullName(),
                            invoice.getPhone(),
                            invoice.getAddress(),
                            invoice.getOrganization(),
                            invoice.getComment(),
                            invoice.getRecipientSignature(),
                            invoice.getRecipientSignatureDate(),
                            invoice.getAccepted());

                    return Result.success(new Data.Builder().putBoolean(Constants.ADDRESSEE_IS_ACCEPTED, params.isAccepted()).build());
                }
            }
            else {
                Log.e(TAG, "doWork(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = SendRecipientSignatureWorker.class.toString();
}
