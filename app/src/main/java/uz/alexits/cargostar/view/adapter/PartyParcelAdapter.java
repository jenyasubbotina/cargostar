package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.view.callback.PartyCallback;
import uz.alexits.cargostar.view.viewholder.PartyParcelViewHolder;

public class PartyParcelAdapter extends RecyclerView.Adapter<PartyParcelViewHolder> {
    private final Context context;
//    private List<Shipping> parcelList;
    private PartyCallback callback;

    public PartyParcelAdapter(final Context context, final PartyCallback callback) {
        this.context = context;
//        this.parcelList = parcelList;
        this.callback = callback;
    }

//    public void setParcelList(final List<Shipping> parcelList) {
//        this.parcelList = parcelList;
//    }

    @NonNull
    @Override
    public PartyParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_party_parcel, parent, false);
        return new PartyParcelViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PartyParcelViewHolder holder, int position) {
//        final Shipping currentShipping = (Shipping) parcelList.get(position);
//
//        if (currentShipping != null) {
//            final String parcelIndex = (position + 1) + ".";
//            final String parcelId = "# " + currentShipping.getId();
//            holder.indexTextView.setText(parcelIndex);
//            holder.parcelIdTextView.setText(parcelId);
//            holder.fromTextView.setText(currentShipping.getSenderAddress().getCity().getCode());
//            holder.toTextView.setText(currentShipping.getRecipientAddress().getCity().getCode());
//            holder.bind(currentShipping, callback);
//        }
    }

    @Override
    public int getItemCount() {
//        return parcelList != null ? parcelList.size() : 0;
        return 0;
    }
}
