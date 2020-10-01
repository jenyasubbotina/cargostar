package com.example.cargostar.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cargostar.R;
import com.example.cargostar.model.PaymentStatus;
import com.example.cargostar.model.TransportationStatus;
import com.example.cargostar.model.shipping.ReceiptWithCargoList;
import com.example.cargostar.view.callback.RequestCallback;
import com.example.cargostar.view.viewholder.PublicRequestViewHolder;

import java.util.List;

public class PublicRequestAdapter extends RecyclerView.Adapter<PublicRequestViewHolder> {
    private final Context context;
    private List<ReceiptWithCargoList> requestList;
    private RequestCallback callback;

    public PublicRequestAdapter(final Context context, final List<ReceiptWithCargoList> requestList, final RequestCallback callback) {
        this.context = context;
        this.callback = callback;
        this.requestList = requestList;
    }

    public PublicRequestAdapter(final Context context, final RequestCallback callback) {
        this(context, null, callback);
    }

    public void setRequestList(final List<ReceiptWithCargoList> requestList) {
        this.requestList = requestList;
        Log.i(PublicRequestAdapter.class.toString(), "setRequestList(): " + requestList);
    }

    @NonNull
    @Override
    public PublicRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_public_request, parent, false);
        return new PublicRequestViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicRequestViewHolder holder, int position) {
        final ReceiptWithCargoList currentRequest = requestList.get(position);

        if (currentRequest != null) {
            final String parcelIndex = (position + 1) + ".";
            final String parcelId = "# " + currentRequest.getReceipt().getId();
            holder.indexTextView.setText(parcelIndex);
            holder.parcelIdTextView.setText(parcelId);
            holder.fromTextView.setText(currentRequest.getReceipt().getSenderAddress().getCity());

            final PaymentStatus paymentStatus = currentRequest.getReceipt().getPaymentStatus();
            Log.i(PublicRequestAdapter.class.toString(), "paymentStatus: " + paymentStatus);

            if (!currentRequest.getReceipt().isRead()) {
                holder.isNewIndicatorImageView.setVisibility(View.VISIBLE);
            }
            else {
                holder.isNewIndicatorImageView.setVisibility(View.INVISIBLE);
            }
            if (paymentStatus == PaymentStatus.PAID || paymentStatus == PaymentStatus.PAID_PARTIALLY || paymentStatus == PaymentStatus.PAID_MORE) {
                holder.isPaidIndicatorImageView.setImageResource(R.drawable.ic_dollar_green);
            }
            else if (paymentStatus == PaymentStatus.WAITING_PAYMENT || paymentStatus == PaymentStatus.WAITING_CHECK) {
                holder.isPaidIndicatorImageView.setImageResource(R.drawable.ic_dollar_red);
            }
        }
        holder.bind(currentRequest, callback);
    }

    @Override
    public int getItemCount() {
        return requestList != null ? requestList.size() : 0;
    }
}