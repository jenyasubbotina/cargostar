package uz.alexits.cargostar.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;

public class CreateInvoiceTwoCardViewsViewHolder extends RecyclerView.ViewHolder {
    //service provider
    public CardView firstCard;
    public CardView secondCard;
    public RadioButton firstCardRadioBtn;
    public RadioButton secondCardRadioBtn;
    public ImageView firstCardImageView;
    public ImageView secondCardImageView;
    public String firstCardValue;
    public String secondCardValue;

    public CreateInvoiceTwoCardViewsViewHolder(@NonNull View itemView) {
        super(itemView);
        firstCard = itemView.findViewById(R.id.first_card);
        secondCard = itemView.findViewById(R.id.second_card);
        firstCardImageView = itemView.findViewById(R.id.first_card_logo);
        secondCardImageView = itemView.findViewById(R.id.second_card_logo);
        firstCardRadioBtn = itemView.findViewById(R.id.first_card_radio_btn);
        secondCardRadioBtn = itemView.findViewById(R.id.second_card_radio_btn);
    }

    public void bindCards() {
        firstCard.setOnClickListener(v -> {
            firstCardRadioBtn.setChecked(true);
        });
        secondCard.setOnClickListener(v -> {
            secondCardRadioBtn.setChecked(true);
        });
    }

    public void unbindCards() {
        firstCard.setOnClickListener(null);
        secondCard.setOnClickListener(null);
    }

    public void bindRadioBtns(final CreateInvoiceCallback callback) {
        firstCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                secondCardRadioBtn.setChecked(false);
                callback.onRadioBtnsSelected(firstCardRadioBtn, b);
            }
        });
        secondCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstCardRadioBtn.setChecked(false);
                callback.onRadioBtnsSelected(secondCardRadioBtn, b);
            }
        });
    }

    public void unbindRadioBtns() {
        firstCard.setOnClickListener(null);
        firstCardRadioBtn.setOnCheckedChangeListener(null);
    }
}
