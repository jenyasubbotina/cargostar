package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.view.callback.RequestCallback;
import uz.alexits.cargostar.view.viewholder.PublicRequestViewHolder;

import java.util.List;

public class PublicRequestAdapter extends RecyclerView.Adapter<PublicRequestViewHolder> {
    private final Context context;
    private List<Request> requestList;
    private final RequestCallback callback;

    public PublicRequestAdapter(@NonNull final Context context,
                                @NonNull final List<Request> requestList,
                                @NonNull final RequestCallback callback) {
        this.context = context;
        this.callback = callback;
        this.requestList = requestList;
    }

    public PublicRequestAdapter(final Context context, final RequestCallback callback) {
        this(context, null, callback);
    }

    public void setRequestList(@NonNull final List<Request> requestList) {
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
        final Request currentRequest = requestList.get(position);

        if (currentRequest != null) {
            final String requestIndex = (position + 1) + ".";
            final String requestId = "# " + currentRequest.getId();
            holder.indexTextView.setText(requestIndex);
            holder.parcelIdTextView.setText(requestId);
            holder.fromTextView.setText(String.valueOf(currentRequest.getSenderCityId()));

            if (currentRequest.isNew()) {
                holder.isNewIndicatorImageView.setVisibility(View.VISIBLE);
            }
            else {
                holder.isNewIndicatorImageView.setVisibility(View.INVISIBLE);
            }
            //todo: get payment status from Invoice via invoiceId
//            final PaymentStatus paymentStatus = currentRequest.getPaymentStatus();
//            if (paymentStatus == PaymentStatus.PAID || paymentStatus == PaymentStatus.PAID_PARTIALLY || paymentStatus == PaymentStatus.PAID_MORE) {
//                holder.isPaidIndicatorImageView.setImageResource(R.drawable.ic_dollar_green);
//            }
//            else if (paymentStatus == PaymentStatus.WAITING_PAYMENT || paymentStatus == PaymentStatus.WAITING_CHECK) {
//                holder.isPaidIndicatorImageView.setImageResource(R.drawable.ic_dollar_red);
//            }
        }
        holder.bind(currentRequest, callback);
    }

    @Override
    public int getItemCount() {
        return requestList != null ? requestList.size() : 0;
    }
}