package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceButtonViewHolder extends RecyclerView.ViewHolder {
    public Button button;

    public CreateInvoiceButtonViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button);

    }

    public void bindBtn(final CreateInvoiceCallback callback) {
        button.setOnClickListener(v -> {
            callback.onAddBtnClicked();
        });
    }

    public void unbindBtn() {
        button.setOnClickListener(null);
    }
}
