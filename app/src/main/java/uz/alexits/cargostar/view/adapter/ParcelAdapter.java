package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.TransportationStatus;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.Parcel;
import uz.alexits.cargostar.view.callback.ParcelCallback;
import uz.alexits.cargostar.view.viewholder.ParcelViewHolder;

import java.util.List;

public class ParcelAdapter extends RecyclerView.Adapter<ParcelViewHolder> {
    private final Context context;
    private List<Parcel> parcelList;
    private ParcelCallback callback;

    public ParcelAdapter(final Context context, final ParcelCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setParcelList(List<Parcel> parcelList) {
        this.parcelList = parcelList;
    }

    public List<Parcel> getParcelList() {
        return parcelList;
    }

    @NonNull
    @Override
    public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_current_parcel, parent, false);
        return new ParcelViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelViewHolder holder, int position) {
        final Parcel currentParcel = parcelList.get(position);
        final Invoice currentInvoice = currentParcel.getInvoice();

        Log.i(ParcelAdapter.class.toString(), "position=" + position + " route=" + currentParcel.getRoute());

        if (currentInvoice != null) {
            final String parcelIndex = (position + 1) + ".";
            final String parcelId = "# " + currentInvoice.getId();
            holder.indexTextView.setText(parcelIndex);
            holder.parcelIdTextView.setText(parcelId);
            holder.fromTextView.setText(currentParcel.getRoute().get(0).getName());
            holder.toTextView.setText(currentParcel.getRoute().get(currentParcel.getRoute().size() - 1).getName());
            holder.parcelTypeTextView.setText(R.string.parcel);

//            if (currentInvoice.getTransportationStatus() == TransportationStatus.IN_TRANSIT) {
//                holder.statusTextView.setText(context.getString(R.string.in_transit));
//                holder.statusTextView.setBackgroundResource(R.drawable.bg_purple);
//            }
//            else if (currentInvoice.getTransportationStatus() == TransportationStatus.ON_THE_WAY) {
//                holder.statusTextView.setText(context.getString(R.string.on_the_way));
//                holder.statusTextView.setBackgroundResource(R.drawable.bg_blue);
//            }
//            else if (currentInvoice.getTransportationStatus() == TransportationStatus.DELIVERED) {
//                holder.statusTextView.setText(context.getString(R.string.delivered));
//                holder.statusTextView.setBackgroundResource(R.drawable.bg_green);
//            }
//            else if (currentInvoice.getTransportationStatus() == TransportationStatus.LOST) {
//                holder.statusTextView.setText(context.getString(R.string.lost));
//                holder.statusTextView.setBackgroundResource(R.drawable.bg_red);
//            }

            holder.bind(currentParcel, callback);
        }
    }

    @Override
    public int getItemCount() {
        return parcelList != null ? parcelList.size() : 0;
    }
}