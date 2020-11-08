package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.Date;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.utils.Constants;

public class InsertInvoiceWorker extends Worker {
    private final long invoiceId;
    private final String number;
    private final long providerId;
    private final long requestId;
    private final long tariffId;

    private final long senderId;
    private final long recipientId;
    private final long payerId;

    private final double price;
    private final int status;
    private final long createdAtTime;
    private final long updatedAtTime;

    public InsertInvoiceWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
        this.number = getInputData().getString(Constants.KEY_NUMBER);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, -1L);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.tariffId = getInputData().getLong(Constants.KEY_TARIFF_ID, -1L);

        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1L);
        this.recipientId = getInputData().getLong(Constants.KEY_RECIPIENT_ID, -1L);
        this.payerId = getInputData().getLong(Constants.KEY_PAYER_ID, -1L);

        this.price = getInputData().getLong(Constants.KEY_PRICE, -1L);
        this.status = getInputData().getInt(Constants.KEY_STATUS, -1);
        this.createdAtTime = getInputData().getLong(Constants.KEY_CREATED_AT, -1L);
        this.updatedAtTime = getInputData().getLong(Constants.KEY_UPDATED_AT, -1L);    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId <= 0) {
            Log.e(TAG, "insertInvoiceData(): invoiceId <= 0");
            return Result.failure();
        }
        if (providerId <= 0) {
            Log.e(TAG, "insertInvoiceData(): providerId <= 0");
            return Result.failure();
        }
        if (requestId <= 0) {
            Log.e(TAG, "insertInvoiceData(): requestId <= 0");
            return Result.failure();
        }
        if (tariffId <= 0) {
            Log.e(TAG, "insertInvoiceData(): tariffId <= 0");
            return Result.failure();
        }
        if (recipientId <= 0) {
            Log.e(TAG, "insertInvoiceData(): recipientId <= 0");
            return Result.failure();
        }
        if (payerId <= 0) {
            Log.e(TAG, "insertInvoiceData(): payerId <= 0");
            return Result.failure();
        }
        try {
            final Invoice invoice = new Invoice(
                    invoiceId,
                    number,
                    senderId,
                    recipientId,
                    payerId,
                    providerId,
                    requestId,
                    tariffId,
                    price,
                    status,
                    createdAtTime > 0 ? new Date(createdAtTime) : null,
                    updatedAtTime > 0 ? new Date(updatedAtTime) : null);

            Log.i(TAG, "invoice: " + invoice);

            final long rowInserted = LocalCache.getInstance(getApplicationContext()).invoiceDao().insertInvoice(invoice);

            if (rowInserted == -1) {
                Log.e(TAG, "insertInvoiceData(): couldn't insert invoice " + invoice);
                return Result.failure();
            }

            Log.i(TAG, "insertInvoiceData(): successfully inserted invoice " + invoice);
            return Result.success();
        }
        catch (Exception e) {
            Log.e(TAG, "doWork(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = InsertInvoiceWorker.class.toString();
}
