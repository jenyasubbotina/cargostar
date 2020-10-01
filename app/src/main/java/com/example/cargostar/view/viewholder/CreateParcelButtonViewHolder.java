package com.example.cargostar.view.viewholder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cargostar.R;
import com.example.cargostar.view.callback.CreateParcelCallback;

public class CreateParcelButtonViewHolder extends RecyclerView.ViewHolder {
    public Button button;

    public CreateParcelButtonViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button);

    }

    public void bindBtn(final CreateParcelCallback callback) {
        button.setOnClickListener(v -> {
            callback.onAddBtnClicked();
        });
    }
}
