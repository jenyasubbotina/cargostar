package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.view.callback.PartialCallback;
import uz.alexits.cargostar.view.viewholder.PartialViewHolder;

public class PartialAdapter extends RecyclerView.Adapter<PartialViewHolder> {
    private final Context context;
    private final PartialCallback callback;
    private List<Transportation> transportationList;

    public PartialAdapter(final Context context, final PartialCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setTransportationList(List<Transportation> transportationList) {
        this.transportationList = transportationList;
    }

    public List<Transportation> getTransportationList() {
        return transportationList;
    }

    @NonNull
    @Override
    public PartialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_partial, parent, false);
        return new PartialViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PartialViewHolder holder, int position) {
        final Transportation currentTransportation = transportationList.get(position);

        if (currentTransportation != null) {
            final String parcelIndex = (position + 1) + ".";

            holder.indexTextView.setText(parcelIndex);

            final Long invoiceId = currentTransportation.getInvoiceId();

            holder.fromTextView.setText(currentTransportation.getCityFrom());
            holder.toTextView.setText(currentTransportation.getCityTo());

            holder.transportationIdTextView.setText(String.valueOf(invoiceId));
            holder.bind(currentTransportation, callback);
        }
    }

    @Override
    public int getItemCount() {
        return transportationList != null ? transportationList.size() : 0;
    }

    private static final String TAG = PartialAdapter.class.toString();
}