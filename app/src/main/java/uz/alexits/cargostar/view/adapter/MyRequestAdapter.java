package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.view.callback.RequestCallback;
import uz.alexits.cargostar.view.viewholder.MyRequestViewHolder;

import java.util.List;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestViewHolder> {
    private final Context context;
    private List<Request> myRequestList;
    private RequestCallback callback;

    public MyRequestAdapter(final Context context, final RequestCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setMyRequestList(final List<Request> myRequestList) {
        this.myRequestList = myRequestList;
    }

    @NonNull
    @Override
    public MyRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_my_request, parent, false);
        return new MyRequestViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestViewHolder holder, int position) {
        final Request currentRequest = myRequestList.get(position);

        if (currentRequest != null) {
            final String requestIndex = (position + 1) + ".";
            final String requestId = "# " + currentRequest.getId();
            holder.indexTextView.setText(requestIndex);
            holder.parcelIdTextView.setText(requestId);
            holder.fromTextView.setText(currentRequest.getSenderCity());

            holder.isNewIndicatorImageView.setVisibility(View.INVISIBLE);

            if (currentRequest.isNew()) {
                holder.isNewIndicatorImageView.setVisibility(View.VISIBLE);
            }
            else {
                holder.isNewIndicatorImageView.setVisibility(View.INVISIBLE);
            }
            if (currentRequest.getPaymentStatus() != null && currentRequest.getPaymentStatus().equalsIgnoreCase("succeeded")) {
                holder.isPaidIndicatorImageView.setImageResource(R.drawable.ic_dollar_green);
            }
            else {
                holder.isPaidIndicatorImageView.setImageResource(R.drawable.ic_dollar_red);
            }
        }
        holder.bind(currentRequest, callback);
    }

    @Override
    public int getItemCount() {
        return myRequestList != null ? myRequestList.size() : 0;
    }
}
