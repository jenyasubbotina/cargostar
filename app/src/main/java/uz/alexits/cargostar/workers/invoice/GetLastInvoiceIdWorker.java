package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.utils.Constants;

public class GetLastInvoiceIdWorker extends Worker {
    public GetLastInvoiceIdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final long lastId = LocalCache.getInstance(getApplicationContext()).invoiceDao().getLastInvoiceId();
        return Result.success(
                new Data.Builder()
                        .putLong(Constants.LAST_INVOICE_ID, lastId)
                        .build());
    }
}
