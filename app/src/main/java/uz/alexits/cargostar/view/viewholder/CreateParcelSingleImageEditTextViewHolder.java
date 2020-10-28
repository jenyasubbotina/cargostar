package uz.alexits.cargostar.view.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.callback.CreateParcelCallback;

public class CreateParcelSingleImageEditTextViewHolder extends RecyclerView.ViewHolder {
    public TextView firstTextView;
    public TextView secondTextView;
    public RelativeLayout firstField;
    public EditText firstEditText;
    public EditText secondEditText;
    public ImageView firstImageView;
    public ImageView firstResultImageView;

    public CreateParcelSingleImageEditTextViewHolder(@NonNull View itemView) {
        super(itemView);
        firstTextView = itemView.findViewById(R.id.first_text_view);
        secondTextView = itemView.findViewById(R.id.second_text_view);
        firstField = itemView.findViewById(R.id.first_field);
        firstEditText = itemView.findViewById(R.id.first_edit_text);
        secondEditText = itemView.findViewById(R.id.second_edit_text);
        firstImageView = itemView.findViewById(R.id.first_image_view);
        firstResultImageView = itemView.findViewById(R.id.first_result_image_view);
    }

    public void bindImageView(final int position, final CreateParcelCallback callback) {
        firstImageView.setOnClickListener(v -> {
            callback.onCameraImageClicked(position);
        });
    }

    public void bindWatchers(final int position, final CreateParcelCallback callback) {
        firstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                callback.afterFirstEditTextChanged(position, editable);
            }
        });

        secondEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                callback.afterSecondEditTextChanged(position, editable);
            }
        });
    }
}
