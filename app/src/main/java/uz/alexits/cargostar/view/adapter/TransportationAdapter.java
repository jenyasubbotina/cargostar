package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationStatus;
import uz.alexits.cargostar.view.callback.TransportationCallback;
import uz.alexits.cargostar.view.viewholder.TransportationViewHolder;

import java.util.List;

public class TransportationAdapter extends RecyclerView.Adapter<TransportationViewHolder> {
    private final Context context;
    private List<Transportation> transportationList;

    private TransportationCallback callback;

    public TransportationAdapter(final Context context, final TransportationCallback callback) {
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
    public TransportationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_current_parcel, parent, false);
        return new TransportationViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportationViewHolder holder, int position) {
        final Transportation currentTransportation = transportationList.get(position);

        if (currentTransportation != null) {
            final String parcelIndex = (position + 1) + ".";

            holder.indexTextView.setText(parcelIndex);

            final Long invoiceId = currentTransportation.getInvoiceId();

            holder.fromTextView.setText(currentTransportation.getCityFrom());
            holder.toTextView.setText(currentTransportation.getCityTo());
            holder.transportationTypeTextView.setText(R.string.parcel);

            if (!TextUtils.isEmpty(currentTransportation.getTransportationStatusName())) {
                if (currentTransportation.getTransportationStatusName().equalsIgnoreCase(context.getString(R.string.in_transit))) {
                    holder.statusTextView.setText(context.getString(R.string.in_transit));
                    holder.statusTextView.setBackgroundResource(R.drawable.bg_purple);
                }
                else if (currentTransportation.getTransportationStatusName().equalsIgnoreCase(context.getString(R.string.on_the_way))) {
                    holder.statusTextView.setText(context.getString(R.string.on_the_way));
                    holder.statusTextView.setBackgroundResource(R.drawable.bg_blue);
                }
                else if (currentTransportation.getTransportationStatusName().equalsIgnoreCase(context.getString(R.string.delivered))) {
                    holder.statusTextView.setText(context.getString(R.string.delivered));
                    holder.statusTextView.setBackgroundResource(R.drawable.bg_green);
                }
                else if (currentTransportation.getTransportationStatusName().equalsIgnoreCase(context.getString(R.string.lost))) {
                    holder.statusTextView.setText(context.getString(R.string.lost));
                    holder.statusTextView.setBackgroundResource(R.drawable.bg_red);
                }
            }

            holder.bind(currentTransportation, callback);

            if (invoiceId != null) {
                holder.transportationIdTextView.setText(String.valueOf(invoiceId));
            }
        }
    }

    @Override
    public int getItemCount() {
        return transportationList != null ? transportationList.size() : 0;
    }
}