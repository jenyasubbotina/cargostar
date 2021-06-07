package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.entities.diffutil.RequestDiffUtil;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.view.callback.PublicRequestCallback;
import uz.alexits.cargostar.view.viewholder.PublicRequestViewHolder;

import java.util.List;

public class PublicRequestAdapter extends RecyclerView.Adapter<PublicRequestViewHolder> {
    private final Context context;
    private List<Request> requestList;
    private final PublicRequestCallback callback;

    public PublicRequestAdapter(final Context context, final PublicRequestCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setRequestList(@NonNull final List<Request> requestList) {
        final RequestDiffUtil diffUtil = new RequestDiffUtil(this.requestList, requestList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        this.requestList = requestList;
        diffResult.dispatchUpdatesTo(this);
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
            holder.isNewIndicatorImageView.setVisibility(View.INVISIBLE);

            if (TextUtils.isEmpty(currentRequest.getSenderCityName())) {
                holder.fromTextView.setText(currentRequest.getSenderCity());
            }
            else {
                holder.fromTextView.setText(currentRequest.getSenderCityName());
            }
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
        holder.bind(position, currentRequest, callback);
    }

    @Override
    public int getItemCount() {
        return requestList != null ? requestList.size() : 0;
    }

    private static final String TAG = PublicRequestAdapter.class.toString();
}