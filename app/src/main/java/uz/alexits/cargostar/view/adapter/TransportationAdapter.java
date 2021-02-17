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
import uz.alexits.cargostar.view.callback.TransportationCallback;
import uz.alexits.cargostar.view.viewholder.TransportationViewHolder;

import java.util.List;

public class TransportationAdapter extends RecyclerView.Adapter<TransportationViewHolder> {
    private final Context context;
    private final TransportationCallback callback;
    private List<Transportation> transportationList;

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
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_transportation, parent, false);
        return new TransportationViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportationViewHolder holder, int position) {
        final Transportation currentTransportation = transportationList.get(position);

        if (currentTransportation != null) {
            final String parcelIndex = (position + 1) + ".";

            holder.indexTextView.setText(parcelIndex);

            final Long transportId = currentTransportation.getId();

            holder.fromTextView.setText(currentTransportation.getCityFrom());
            holder.toTextView.setText(currentTransportation.getCityTo());

            if (currentTransportation.getPartialId() == null || currentTransportation.getPartialId() <= 0) {
                holder.transportationTypeTextView.setText(R.string.transportation);
            }
            else {
                holder.transportationTypeTextView.setText(R.string.partial);
            }

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
            holder.transportationIdTextView.setText(String.valueOf(transportId));
            holder.bind(currentTransportation, callback);
        }
    }

    @Override
    public int getItemCount() {
        return transportationList != null ? transportationList.size() : 0;
    }
}