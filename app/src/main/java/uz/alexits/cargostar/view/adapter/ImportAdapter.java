package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.diffutil.ImportDiffUtil;
import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.view.callback.ImportCallback;

public class ImportAdapter extends RecyclerView.Adapter<ImportAdapter.ImportViewHolder> {
    private final Context context;
    private final ImportCallback callback;
    private List<Import> importList;

    public ImportAdapter(final Context context, final ImportCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setImportList(final List<Import> importList) {
        final ImportDiffUtil diffUtil = new ImportDiffUtil(this.importList, importList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        this.importList = importList;
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ImportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(context).inflate(R.layout.view_holder_import, parent, false);
        return new ImportViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ImportViewHolder holder, int position) {
        holder.invoiceIdTextView.setText(context.getString(R.string.delivery_invoice_id, importList.get(position).getInvoiceId()));

        if (importList.get(position).getTrackingCode() != null) {
            holder.trackingCodeTextView.setText(context.getString(R.string.tracking_code, importList.get(position).getTrackingCode()));
        }
        else {
            holder.trackingCodeTextView.setText(context.getString(R.string.delivery_recipient_full_name, context.getString(R.string.empty)));
        }
        if (importList.get(position).getFullName() != null) {
            holder.fullNameTextView.setText(context.getString(R.string.delivery_recipient_full_name, importList.get(position).getFullName()));
        }
        else {
            holder.fullNameTextView.setText(context.getString(R.string.delivery_recipient_full_name, context.getString(R.string.empty)));
        }
        if (importList.get(position).getPhone() != null) {
            holder.phoneTextView.setText(context.getString(R.string.delivery_recipient_phone, importList.get(position).getPhone()));
        }
        else {
            holder.phoneTextView.setText(context.getString(R.string.delivery_recipient_phone, context.getString(R.string.empty)));
        }
        if (importList.get(position).getAddress() != null) {
            holder.addressTextView.setText(context.getString(R.string.addressee_address, importList.get(position).getAddress()));
        }
        else {
            holder.addressTextView.setText(context.getString(R.string.addressee_address, context.getString(R.string.empty)));
        }
        if (importList.get(position).getOrganization() != null) {
            holder.organizationTextView.setText(context.getString(R.string.delivery_organization, importList.get(position).getOrganization()));
        }
        else {
            holder.organizationTextView.setText(context.getString(R.string.delivery_organization, context.getString(R.string.empty)));
        }
        if (importList.get(position).getRecipientSignatureDate() != null) {
            holder.signatureDateTextView.setText(context.getString(R.string.delivery_signature_date, importList.get(position).getRecipientSignatureDate()));
        }
        else {
            holder.signatureDateTextView.setText(context.getString(R.string.delivery_signature_date, context.getString(R.string.empty)));
        }
        if (importList.get(position).getComment() == null) {
            holder.commentTextView.setText(context.getString(R.string.delivery_comment, context.getString(R.string.empty)));
        }
        else {
            holder.commentTextView.setText(context.getString(R.string.delivery_comment, importList.get(position).getComment()));
        }

        holder.importStatusTextView.setText(importList.get(position).getImportStatus());
        holder.bind(position, importList.get(position), callback);

        if (importList.get(position).getImportStatus().equalsIgnoreCase(context.getString(R.string.office))) {
            holder.importStatusTextView.setBackgroundResource(R.drawable.bg_purple);
            return;
        }
        if (importList.get(position).getImportStatus().equalsIgnoreCase(context.getString(R.string.van))) {
            holder.importStatusTextView.setBackgroundResource(R.drawable.bg_blue);
            return;
        }
        if (importList.get(position).getImportStatus().equalsIgnoreCase(context.getString(R.string.pod))) {
            holder.importStatusTextView.setBackgroundResource(R.drawable.bg_green);
            return;
        }
        if (importList.get(position).getImportStatus().equalsIgnoreCase(context.getString(R.string.nr))) {
            holder.importStatusTextView.setBackgroundResource(R.drawable.bg_red);
            return;
        }
        if (importList.get(position).getImportStatus().equalsIgnoreCase(context.getString(R.string.ok))) {
            holder.importStatusTextView.setBackgroundResource(R.drawable.bg_grey);
        }
    }

    @Override
    public int getItemCount() {
        return importList != null ? importList.size() : 0;
    }

    static class ImportViewHolder extends RecyclerView.ViewHolder {
        TextView invoiceIdTextView;
        TextView trackingCodeTextView;
        TextView fullNameTextView;
        TextView phoneTextView;
        TextView addressTextView;
        TextView organizationTextView;
        TextView signatureDateTextView;
        TextView commentTextView;
        TextView importStatusTextView;

        public ImportViewHolder(@NonNull View itemView) {
            super(itemView);

            invoiceIdTextView = itemView.findViewById(R.id.invoice_id_text_view);
            trackingCodeTextView = itemView.findViewById(R.id.tracking_code_text_view);
            fullNameTextView = itemView.findViewById(R.id.recipient_full_name_text_view);
            phoneTextView = itemView.findViewById(R.id.recipient_phone_text_view);
            addressTextView = itemView.findViewById(R.id.delivery_address_text_view);
            organizationTextView = itemView.findViewById(R.id.organization_text_view);
            signatureDateTextView = itemView.findViewById(R.id.signature_date_text_view);
            commentTextView = itemView.findViewById(R.id.comment_text_view);
            importStatusTextView = itemView.findViewById(R.id.delivery_status_text_view);
        }

        public void bind(final int position, final Import anImport, final ImportCallback callback) {
            itemView.setOnClickListener(v -> callback.onImportItemClicked(position, anImport, callback));
        }
    }
}