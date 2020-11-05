package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceTwoRadioBtnsViewHolder extends RecyclerView.ViewHolder {
    //service provider
    public RadioGroup radioGroup;
    public RadioButton firstRadioBtn;
    public RadioButton secondRadioBtn;

    public CreateInvoiceTwoRadioBtnsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.radioGroup = itemView.findViewById(R.id.package_type_radio_group);
        this.firstRadioBtn = itemView.findViewById(R.id.doc_type_radio_btn);
        this.secondRadioBtn = itemView.findViewById(R.id.box_type_radio_btn);
    }

    public void bindRadioGroup(final CreateInvoiceCallback callback) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            callback.onRadioGroupSelected(group, firstRadioBtn.getId(), secondRadioBtn.getId());
        });
    }

    public void unbindRadioGroup() {
        radioGroup.setOnCheckedChangeListener(null);
    }
}
