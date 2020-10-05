package com.example.cargostar.view.adapter;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cargostar.R;
import com.example.cargostar.model.actor.AddressBook;
import com.example.cargostar.model.location.TransitPoint;
import com.example.cargostar.view.UiUtils;
import com.example.cargostar.view.callback.CreateParcelCallback;
import com.example.cargostar.view.viewholder.CalcItemViewHolder;
import com.example.cargostar.view.viewholder.CreateParcelButtonViewHolder;
import com.example.cargostar.view.viewholder.CreateParcelEditTextSpinnerViewHolder;
import com.example.cargostar.view.viewholder.CreateParcelHeadingViewHolder;
import com.example.cargostar.view.viewholder.CreateParcelSingleImageEditTextViewHolder;
import com.example.cargostar.view.viewholder.CreateParcelSingleSpinnerViewHolder;
import com.example.cargostar.view.viewholder.CreateParcelTwoEditTextsViewHolder;
import com.example.cargostar.view.viewholder.CreateParcelTwoImageEditTextsViewHolder;
import com.example.cargostar.view.viewholder.ParcelDataStrokeViewHolder;
import java.util.List;

public class CreateParcelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final CreateParcelCallback callback;
    private List<CreateParcelData> itemList;
    private List<AddressBook> addressBookEntries;

    public CreateParcelAdapter(@NonNull final Context context, final CreateParcelCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setAddressBookEntries(List<AddressBook> addressBookEntries) {
        this.addressBookEntries = addressBookEntries;
    }

    public void setItemList(List<CreateParcelData> itemList) {
        this.itemList = itemList;
    }

    public List<AddressBook> getAddressBookEntries() {
        return addressBookEntries;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;
        if (viewType == CreateParcelData.TYPE_HEADING) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_parcel_heading_text_view, parent, false);
            return new CreateParcelHeadingViewHolder(root);
        }
        else if (viewType == CreateParcelData.TYPE_TWO_EDIT_TEXTS || viewType == CreateParcelData.TYPE_SINGLE_EDIT_TEXT) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_parcel_two_edit_texts, parent, false);
            return new CreateParcelTwoEditTextsViewHolder(root);
        }
        else if (viewType == CreateParcelData.TYPE_EDIT_TEXT_SPINNER) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_parcel_edit_text_spinner, parent, false);
            return new CreateParcelEditTextSpinnerViewHolder(root);
        }
        else if (viewType == CreateParcelData.TYPE_TWO_IMAGE_EDIT_TEXTS || viewType == CreateParcelData.TYPE_SINGLE_IMAGE_EDIT_TEXT) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_parcel_two_image_edit_texts, parent, false);
            return new CreateParcelTwoImageEditTextsViewHolder(root);
        }
        else if (viewType == CreateParcelData.TYPE_ONE_IMAGE_EDIT_TEXT) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_parcel_one_image_edit_text, parent, false);
            return new CreateParcelSingleImageEditTextViewHolder(root);
        }
        else if (viewType == CreateParcelData.TYPE_BUTTON) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_parcel_button, parent, false);
            return new CreateParcelButtonViewHolder(root);
        }
        else if (viewType == CreateParcelData.TYPE_CALCULATOR_ITEM) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_calculator, parent, false);
            return new CalcItemViewHolder(root);
        }
        else if (viewType == CreateParcelData.TYPE_SINGLE_SPINNER) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_parcel_single_spinner, parent, false);
            return new CreateParcelSingleSpinnerViewHolder(root);
        }
        else {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_parcel_data_stoke, parent, false);
            return new ParcelDataStrokeViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i(CreateParcelAdapter.class.toString(), "onBindViewHolder: " + position);

        if (getItemViewType(position) == CreateParcelData.TYPE_HEADING) {
            final CreateParcelHeadingViewHolder viewHolder = (CreateParcelHeadingViewHolder) holder;
            viewHolder.headingTextView.setText(itemList.get(position).firstKey);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_TWO_EDIT_TEXTS) {
            final CreateParcelTwoEditTextsViewHolder viewHolder = (CreateParcelTwoEditTextsViewHolder) holder;
            viewHolder.firstTextView.setText(itemList.get(position).firstKey);
            viewHolder.secondTextView.setText(itemList.get(position).secondKey);

            if (!TextUtils.isEmpty(itemList.get(position).firstValue)) {
                viewHolder.firstEditText.setText(itemList.get(position).firstValue);
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(itemList.get(position).secondValue)) {
                viewHolder.secondEditText.setText(itemList.get(position).secondValue);
                viewHolder.secondEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.secondEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (itemList.get(position).firstEnabled) {
                if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_EMAIL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_TEXT) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_NUMBER) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_PHONE) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.firstEditText.setEnabled(false);
            }
            if (itemList.get(position).secondEnabled) {
                if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_EMAIL) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_TEXT) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_NUMBER) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_PHONE) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.secondEditText.setEnabled(false);
            }
            viewHolder.firstEditText.setOnFocusChangeListener((v, hasFocus) -> {
                UiUtils.onFocusChanged(viewHolder.firstEditText, hasFocus);
            });
            viewHolder.secondEditText.setOnFocusChangeListener((v, hasFocus) -> {
                UiUtils.onFocusChanged(viewHolder.secondEditText, hasFocus);
            });
            viewHolder.bindWatchers(position, callback);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_SINGLE_EDIT_TEXT) {
            final CreateParcelTwoEditTextsViewHolder viewHolder = (CreateParcelTwoEditTextsViewHolder) holder;
            viewHolder.secondTextView.setVisibility(View.INVISIBLE);
            viewHolder.secondEditText.setVisibility(View.INVISIBLE);
            viewHolder.firstTextView.setText(itemList.get(position).firstKey);

            if (!TextUtils.isEmpty(itemList.get(position).firstValue)) {
                viewHolder.firstEditText.setText(itemList.get(position).firstValue);
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (itemList.get(position).firstEnabled) {
                Log.i(CreateParcelAdapter.class.toString(), "position: " + position + " firstInputType" + itemList.get(position).firstInputType);
                if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_EMAIL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_TEXT) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_NUMBER) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_PHONE) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.firstEditText.setEnabled(false);
            }
            viewHolder.firstEditText.setOnFocusChangeListener((v, hasFocus) -> {
                UiUtils.onFocusChanged(viewHolder.firstEditText, hasFocus);
            });
            viewHolder.bindWatchers(position, callback);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_EDIT_TEXT_SPINNER) {
            final CreateParcelEditTextSpinnerViewHolder viewHolder = (CreateParcelEditTextSpinnerViewHolder) holder;
            viewHolder.firstTextView.setText(itemList.get(position).firstKey);
            viewHolder.editText.setText(itemList.get(position).firstValue);
            viewHolder.secondTextView.setText(itemList.get(position).secondKey);

            if (!TextUtils.isEmpty(itemList.get(position).firstValue)) {
                viewHolder.editText.setText(itemList.get(position).firstValue);
                viewHolder.editText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.editText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (itemList.get(position).firstEnabled) {
                if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_EMAIL) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_TEXT) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_NUMBER) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
                else if (itemList.get(position).firstInputType == CreateParcelData.INPUT_TYPE_PHONE) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.editText.setEnabled(false);
            }
            viewHolder.editText.setOnFocusChangeListener((v, hasFocus) -> {
                UiUtils.onFocusChanged(viewHolder.editText, hasFocus);
            });
            viewHolder.bindWatcher(position, callback);
//            viewHolder.bindSpinner(adapter, position, callback);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_STROKE) {
            final ParcelDataStrokeViewHolder viewHolder = (ParcelDataStrokeViewHolder) holder;
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_TWO_IMAGE_EDIT_TEXTS) {
            final CreateParcelTwoImageEditTextsViewHolder viewHolder = (CreateParcelTwoImageEditTextsViewHolder) holder;
            viewHolder.firstTextView.setText(itemList.get(position).firstKey);
            viewHolder.secondTextView.setText(itemList.get(position).secondKey);

            if (!TextUtils.isEmpty(itemList.get(position).firstValue)) {
                viewHolder.firstEditText.setText(itemList.get(position).firstValue);
                viewHolder.firstResultImageView.setImageResource(R.drawable.ic_image_green);
                viewHolder.firstResultImageView.setVisibility(View.VISIBLE);
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.firstResultImageView.setImageResource(R.drawable.ic_image_red);
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_locked);
                viewHolder.firstResultImageView.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(itemList.get(position).secondValue)) {
                viewHolder.secondEditText.setText(itemList.get(position).secondValue);
                viewHolder.secondResultImageView.setImageResource(R.drawable.ic_image_green);
                viewHolder.secondResultImageView.setVisibility(View.VISIBLE);
                viewHolder.secondEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.secondResultImageView.setImageResource(R.drawable.ic_image_red);
                viewHolder.secondEditText.setBackgroundResource(R.drawable.edit_text_locked);
                viewHolder.secondResultImageView.setVisibility(View.INVISIBLE);
            }
            viewHolder.bindImageViews(callback);
            viewHolder.bindWatchers(position, callback);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_ONE_IMAGE_EDIT_TEXT) {
            final CreateParcelSingleImageEditTextViewHolder viewHolder = (CreateParcelSingleImageEditTextViewHolder) holder;
            viewHolder.firstTextView.setText(itemList.get(position).firstKey);
            viewHolder.firstImageView.setImageResource(R.drawable.ic_camera);
            viewHolder.secondTextView.setText(itemList.get(position).secondKey);

            if (!TextUtils.isEmpty(itemList.get(position).firstValue)) {
                viewHolder.firstEditText.setText(itemList.get(position).firstValue);
                viewHolder.firstResultImageView.setBackgroundResource(R.drawable.ic_doc_green);
                viewHolder.firstResultImageView.setVisibility(View.VISIBLE);
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.firstEditText.setBackgroundResource(R.drawable.edit_text_locked);
                viewHolder.firstResultImageView.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(itemList.get(position).secondValue)) {
                viewHolder.secondEditText.setText(itemList.get(position).secondValue);
                viewHolder.secondEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                viewHolder.secondEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (itemList.get(position).secondEnabled) {
                if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_EMAIL) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_TEXT) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_NUMBER) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
                else if (itemList.get(position).secondInputType == CreateParcelData.INPUT_TYPE_PHONE) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.secondEditText.setEnabled(false);
            }
            viewHolder.secondEditText.setOnFocusChangeListener((v, hasFocus) -> {
                UiUtils.onFocusChanged(viewHolder.secondEditText, hasFocus);
            });
            viewHolder.bindImageView(position, callback);
            viewHolder.bindWatchers(position, callback);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_SINGLE_IMAGE_EDIT_TEXT) {
            final CreateParcelTwoImageEditTextsViewHolder viewHolder = (CreateParcelTwoImageEditTextsViewHolder) holder;
            viewHolder.firstTextView.setText(itemList.get(position).firstKey);
            viewHolder.firstImageView.setImageResource(R.drawable.ic_camera);

            if (!TextUtils.isEmpty(itemList.get(position).firstValue)) {
                viewHolder.firstEditText.setText(itemList.get(position).firstValue);
                viewHolder.firstResultImageView.setImageResource(R.drawable.ic_image_red);
            }
            else {
                viewHolder.firstResultImageView.setImageResource(R.drawable.ic_image_green);
            }
            viewHolder.secondTextView.setVisibility(View.INVISIBLE);
            viewHolder.secondResultImageView.setVisibility(View.INVISIBLE);
            viewHolder.secondImageView.setVisibility(View.INVISIBLE);
            viewHolder.secondEditText.setVisibility(View.INVISIBLE);
            viewHolder.secondField.setVisibility(View.INVISIBLE);
            viewHolder.bindWatchers(position, callback);
            viewHolder.bindImageView(position, callback);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_BUTTON) {
            final CreateParcelButtonViewHolder viewHolder = (CreateParcelButtonViewHolder) holder;
            viewHolder.button.setText(itemList.get(position).firstKey);
            viewHolder.bindBtn(callback);
        }
        else if (getItemViewType(position) == CreateParcelData.TYPE_CALCULATOR_ITEM) {
            final CalcItemViewHolder viewHolder = (CalcItemViewHolder) holder;
            viewHolder.indexTextView.setText(itemList.get(position).index);
            viewHolder.packageTypeTextView.setText(itemList.get(position).packageType);
            viewHolder.sourceTextView.setText(itemList.get(position).source);
            viewHolder.destinationTextView.setText(itemList.get(position).destination);
            viewHolder.weightTextView.setText(itemList.get(position).weight);
            viewHolder.dimensionsTextView.setText(itemList.get(position).dimensions);
            Log.i(CreateParcelAdapter.class.toString(), "position=" + position);
            viewHolder.bind(position, callback);
        }
        //address book spinner
        else if (getItemViewType(position) == CreateParcelData.TYPE_SINGLE_SPINNER) {
            final CreateParcelSingleSpinnerViewHolder viewHolder = (CreateParcelSingleSpinnerViewHolder) holder;
            viewHolder.textView.setText(itemList.get(position).firstKey);

            final SpinnerAdapter<AddressBook> addressBookSpinnerAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, addressBookEntries);
            addressBookSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            viewHolder.spinner.setAdapter(addressBookSpinnerAdapter);
            viewHolder.bindSpinner(callback);
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).type == CreateParcelData.TYPE_HEADING) {
            return CreateParcelData.TYPE_HEADING;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_TWO_EDIT_TEXTS) {
            return CreateParcelData.TYPE_TWO_EDIT_TEXTS;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_EDIT_TEXT_SPINNER) {
            return CreateParcelData.TYPE_EDIT_TEXT_SPINNER;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_SINGLE_EDIT_TEXT) {
            return CreateParcelData.TYPE_SINGLE_EDIT_TEXT;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_STROKE) {
            return CreateParcelData.TYPE_STROKE;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_TWO_IMAGE_EDIT_TEXTS) {
            return CreateParcelData.TYPE_TWO_IMAGE_EDIT_TEXTS;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_SINGLE_IMAGE_EDIT_TEXT) {
            return CreateParcelData.TYPE_SINGLE_IMAGE_EDIT_TEXT;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_ONE_IMAGE_EDIT_TEXT) {
            return CreateParcelData.TYPE_ONE_IMAGE_EDIT_TEXT;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_BUTTON) {
            return CreateParcelData.TYPE_BUTTON;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_CALCULATOR_ITEM) {
            return CreateParcelData.TYPE_CALCULATOR_ITEM;
        }
        if (itemList.get(position).type == CreateParcelData.TYPE_SINGLE_SPINNER) {
            return CreateParcelData.TYPE_SINGLE_SPINNER;
        }
        return -1;
    }
}
