package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

public class CreateInvoiceSearchFieldViewHolder extends RecyclerView.ViewHolder {
    public TextView headingTextView;
    public TextView searchCustomerTextView;
    public EditText searchCustomerEditText;
    public Button searchCustomerBtn;

    public CreateInvoiceSearchFieldViewHolder(@NonNull View itemView) {
        super(itemView);
        headingTextView = itemView.findViewById(R.id.create_parcel_text_view);
        searchCustomerTextView = itemView.findViewById(R.id.find_user_text_view);
        searchCustomerEditText = itemView.findViewById(R.id.find_user_edit_text);
        searchCustomerBtn = itemView.findViewById(R.id.choose_btn);
    }
}