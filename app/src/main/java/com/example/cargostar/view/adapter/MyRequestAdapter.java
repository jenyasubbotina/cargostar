package com.example.cargostar.view.adapter;

import android.content.Context;
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
import com.example.cargostar.view.viewholder.MyRequestViewHolder;

import java.util.List;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestViewHolder> {
    private final Context context;
    private List<ReceiptWithCargoList> myRequestList;
    private RequestCallback callback;

    public MyRequestAdapter(final Context context, final RequestCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setMyRequestList(final List<ReceiptWithCargoList> myRequestList) {
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
        final ReceiptWithCargoList currentRequest = myRequestList.get(position);

        if (currentRequest != null) {
            final String parcelIndex = (position + 1) + ".";
            final String parcelId = "# " + currentRequest.getReceipt().getId();
            holder.indexTextView.setText(parcelIndex);
            holder.parcelIdTextView.setText(parcelId);
            holder.fromTextView.setText(currentRequest.getReceipt().getSenderAddress().getCity());

            final PaymentStatus paymentStatus = currentRequest.getReceipt().getPaymentStatus();

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
        return myRequestList != null ? myRequestList.size() : 0;
    }
}
