package uz.alexits.cargostar.workers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.api.params.RecipientSignatureParams;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Serializer;

public class SendRecipientSignatureWorker extends Worker {
    private final long invoiceId;
    private final String recipientSignatureFilePath;

    public SendRecipientSignatureWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
        this.recipientSignatureFilePath = getInputData().getString(Constants.KEY_RECIPIENT_SIGNATURE);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (invoiceId == -1L) {
            Log.e(TAG, "sendRecipientSignature(): empty invoiceId");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }
        if (recipientSignatureFilePath == null) {
            Log.e(TAG, "sendRecipientSignature(): empty recipientSignature");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<RecipientSignatureParams> response = RetrofitClient.getInstance(getApplicationContext())
                    .sendRecipientSignature(invoiceId, Serializer.fileToBase64(recipientSignatureFilePath));

            if (response.code() == 200 || response.code() == 201) {
                if (response.isSuccessful()) {
                    final RecipientSignatureParams recipientSignatureParams = response.body();

                    Log.i(TAG, "sendRecipientSignature(): response=" + recipientSignatureParams);

                    return Result.success();
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
