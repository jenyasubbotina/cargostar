package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.view.viewholder.CalcItemViewHolder;
import uz.alexits.cargostar.view.viewholder.CreateInvoiceButtonViewHolder;
import uz.alexits.cargostar.view.viewholder.CreateInvoiceEditTextSpinnerViewHolder;
import uz.alexits.cargostar.view.viewholder.CreateInvoiceHeadingViewHolder;
import uz.alexits.cargostar.view.viewholder.CreateInvoiceSingleSpinnerViewHolder;
import uz.alexits.cargostar.view.viewholder.CreateInvoiceTwoEditTextsViewHolder;
import uz.alexits.cargostar.view.viewholder.CreateInvoiceTwoImageEditTextsViewHolder;
import uz.alexits.cargostar.view.viewholder.CreateInvoiceTwoSpinnersViewHolder;
import uz.alexits.cargostar.view.viewholder.ParcelDataStrokeViewHolder;
import uz.alexits.cargostar.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateInvoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final CreateInvoiceCallback callback;

    private List<CreateInvoiceData> itemList;

    private final ArrayAdapter<Country> countryArrayAdapter;

    private final ArrayAdapter<Region> senderRegionArrayAdapter;
    private final ArrayAdapter<Region> recipientRegionArrayAdapter;
    private final ArrayAdapter<Region> payerRegionArrayAdapter;

    private final ArrayAdapter<City> senderCityArrayAdapter;
    private final ArrayAdapter<City> recipientCityArrayAdapter;
    private final ArrayAdapter<City> payerCityArrayAdapter;

    private final ArrayAdapter<PackagingType> packagingTypeArrayAdapter;

    private List<Country> countryList;

    private List<Region> senderRegionList;
    private List<Region> recipientRegionList;
    private List<Region> payerRegionList;

    private List<City> senderCityList;
    private List<City> recipientCityList;
    private List<City> payerCityList;

    private List<PackagingType> packagingTypeList;

    private List<AddressBook> addressBookEntries;

    private int position;

    public CreateInvoiceAdapter(@NonNull final Context context, final CreateInvoiceCallback callback) {
        this.context = context;
        this.callback = callback;

        this.countryList = new ArrayList<>();
        this.countryArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, countryList);

        this.senderRegionList = new ArrayList<>();
        this.recipientRegionList = new ArrayList<>();
        this.payerRegionList = new ArrayList<>();

        this.senderCityList = new ArrayList<>();
        this.recipientCityList = new ArrayList<>();
        this.payerCityList = new ArrayList<>();

        this.packagingTypeList = new ArrayList<>();

        this.senderRegionArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, senderRegionList);
        this.recipientRegionArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, recipientRegionList);
        this.payerRegionArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, payerRegionList);

        this.senderCityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, senderCityList);
        this.recipientCityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, recipientCityList);
        this.payerCityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, payerCityList);

        this.packagingTypeArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, packagingTypeList);

        this.position = -1;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public List<Region> getSenderRegionList() {
        return senderRegionList;
    }

    public List<Region> getRecipientRegionList() {
        return recipientRegionList;
    }

    public List<Region> getPayerRegionList() {
        return payerRegionList;
    }

    public List<City> getSenderCityList() {
        return senderCityList;
    }

    public List<City> getRecipientCityList() {
        return recipientCityList;
    }

    public List<City> getPayerCityList() {
        return payerCityList;
    }

    public List<AddressBook> getAddressBookEntries() {
        return addressBookEntries;
    }

    public void setAddressBookEntries(final List<AddressBook> addressBookEntries) {
        this.addressBookEntries = addressBookEntries;
    }

    public void setCountryList(final List<Country> countryList) {
        this.countryList = countryList;
        countryArrayAdapter.clear();
        countryArrayAdapter.addAll(countryList);
        countryArrayAdapter.notifyDataSetChanged();
    }

    public void setSenderRegionList(final List<Region> senderRegionList) {
        this.senderRegionList = senderRegionList;
        senderRegionArrayAdapter.clear();
        senderRegionArrayAdapter.addAll(senderRegionList);
        senderRegionArrayAdapter.notifyDataSetChanged();
    }

    public void setRecipientRegionList(final List<Region> recipientRegionList) {
        this.recipientRegionList = recipientRegionList;
        recipientRegionArrayAdapter.clear();
        recipientRegionArrayAdapter.addAll(recipientRegionList);
        recipientRegionArrayAdapter.notifyDataSetChanged();
    }

    public void setPayerRegionList(final List<Region> payerRegionList) {
        this.payerRegionList = payerRegionList;
        payerRegionArrayAdapter.clear();
        payerRegionArrayAdapter.addAll(payerRegionList);
        payerRegionArrayAdapter.notifyDataSetChanged();
    }

    public void setSenderCityList(final List<City> senderCityList) {
        this.senderCityList = senderCityList;
        senderCityArrayAdapter.clear();
        senderCityArrayAdapter.addAll(senderCityList);
        senderCityArrayAdapter.notifyDataSetChanged();
    }

    public void setRecipientCityList(final List<City> recipientCityList) {
        this.recipientCityList = recipientCityList;
        recipientCityArrayAdapter.clear();
        recipientCityArrayAdapter.addAll(recipientCityList);
        recipientCityArrayAdapter.notifyDataSetChanged();
    }

    public void setPayerCityList(final List<City> payerCityList) {
        this.payerCityList = payerCityList;
        payerCityArrayAdapter.clear();
        payerCityArrayAdapter.addAll(payerCityList);
        payerCityArrayAdapter.notifyDataSetChanged();
    }

    public void setPackagingTypeList(final List<PackagingType> packagingTypeList) {
        this.packagingTypeList = packagingTypeList;
        packagingTypeArrayAdapter.clear();
        packagingTypeArrayAdapter.addAll(packagingTypeList);
        packagingTypeArrayAdapter.notifyDataSetChanged();
    }

    public void setItemList(List<CreateInvoiceData> itemList) {
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;

        if (viewType == CreateInvoiceData.TYPE_HEADING) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_invoice_heading_text_view, parent, false);
            return new CreateInvoiceHeadingViewHolder(root);
        }
        else if (viewType == CreateInvoiceData.TYPE_TWO_EDIT_TEXTS || viewType == CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_invoice_two_edit_texts, parent, false);
            return new CreateInvoiceTwoEditTextsViewHolder(root);
        }
        else if (viewType == CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_invoice_edit_text_spinner, parent, false);
            return new CreateInvoiceEditTextSpinnerViewHolder(root);
        }
        else if (viewType == CreateInvoiceData.TYPE_TWO_SPINNERS) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_invoice_two_spinners, parent, false);
            return new CreateInvoiceTwoSpinnersViewHolder(root);
        }
        else if (viewType == CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_invoice_two_image_edit_texts, parent, false);
            return new CreateInvoiceTwoImageEditTextsViewHolder(root);
        }
        else if (viewType == CreateInvoiceData.TYPE_BUTTON) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_invoice_button, parent, false);
            return new CreateInvoiceButtonViewHolder(root);
        }
        else if (viewType == CreateInvoiceData.TYPE_CALCULATOR_ITEM) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_calculator, parent, false);
            return new CalcItemViewHolder(root);
        }
        else if (viewType == CreateInvoiceData.TYPE_SINGLE_SPINNER) {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_create_invoice_single_spinner, parent, false);
            return new CreateInvoiceSingleSpinnerViewHolder(root);
        }
        else {
            root = LayoutInflater.from(context).inflate(R.layout.view_holder_parcel_data_stoke, parent, false);
            return new ParcelDataStrokeViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        this.position = position;

        if (getItemViewType(position) == CreateInvoiceData.TYPE_HEADING) {
            final CreateInvoiceHeadingViewHolder viewHolder = (CreateInvoiceHeadingViewHolder) holder;
            viewHolder.headingTextView.setText(itemList.get(position).firstKey);
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_EDIT_TEXTS) {
            final CreateInvoiceTwoEditTextsViewHolder viewHolder = (CreateInvoiceTwoEditTextsViewHolder) holder;
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
                if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_EMAIL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_TEXT) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_NUMBER) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_PHONE) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.firstEditText.setEnabled(false);
            }
            if (itemList.get(position).secondEnabled) {
                if (itemList.get(position).secondInputType == CreateInvoiceData.INPUT_TYPE_EMAIL) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).secondInputType == CreateInvoiceData.INPUT_TYPE_TEXT) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).secondInputType == CreateInvoiceData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).secondInputType == CreateInvoiceData.INPUT_TYPE_NUMBER) {
                    viewHolder.secondEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else if (itemList.get(position).secondInputType == CreateInvoiceData.INPUT_TYPE_PHONE) {
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
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT) {
            final CreateInvoiceTwoEditTextsViewHolder viewHolder = (CreateInvoiceTwoEditTextsViewHolder) holder;
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
                if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_EMAIL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_TEXT) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_NUMBER) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_PHONE) {
                    viewHolder.firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.firstEditText.setEnabled(false);
            }
            viewHolder.firstEditText.setOnFocusChangeListener((v, hasFocus) -> {
                UiUtils.onFocusChanged(viewHolder.firstEditText, hasFocus);
            });
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER) {
            final CreateInvoiceEditTextSpinnerViewHolder viewHolder = (CreateInvoiceEditTextSpinnerViewHolder) holder;
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
                if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_EMAIL) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_TEXT) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_NUMBER_DECIMAL) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_NUMBER) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
                else if (itemList.get(position).firstInputType == CreateInvoiceData.INPUT_TYPE_PHONE) {
                    viewHolder.firstTextView.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
            else {
                viewHolder.editText.setEnabled(false);
            }
            viewHolder.editText.setOnFocusChangeListener((v, hasFocus) -> {
                UiUtils.onFocusChanged(viewHolder.editText, hasFocus);
            });
            /* country spinner */
            if (position == 8 || position == 17 || position == 25) {
                countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.spinner.setAdapter(countryArrayAdapter);
            }
            /* packaging type spinner */
            else if (position == 46) {
                packagingTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.spinner.setAdapter(packagingTypeArrayAdapter);
            }
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_SPINNERS) {
            final CreateInvoiceTwoSpinnersViewHolder viewHolder = (CreateInvoiceTwoSpinnersViewHolder) holder;
            viewHolder.regionTextView.setText(itemList.get(position).firstKey);
            viewHolder.cityTextView.setText(itemList.get(position).secondKey);

            /* sender location data */
            if (position == 9) {
                senderRegionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                senderCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.regionSpinner.setAdapter(senderRegionArrayAdapter);
                viewHolder.citySpinner.setAdapter(senderCityArrayAdapter);
            }
            /* recipient location data */
            else if (position == 18) {
                recipientRegionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                recipientCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.regionSpinner.setAdapter(recipientRegionArrayAdapter);
                viewHolder.citySpinner.setAdapter(recipientCityArrayAdapter);
            }
            /* payer location data */
            else if (position == 26) {
                payerRegionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                payerCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.regionSpinner.setAdapter(payerRegionArrayAdapter);
                viewHolder.citySpinner.setAdapter(payerCityArrayAdapter);
            }
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_STROKE) {
            final ParcelDataStrokeViewHolder viewHolder = (ParcelDataStrokeViewHolder) holder;
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS) {
            final CreateInvoiceTwoImageEditTextsViewHolder viewHolder = (CreateInvoiceTwoImageEditTextsViewHolder) holder;
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
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_BUTTON) {
            final CreateInvoiceButtonViewHolder viewHolder = (CreateInvoiceButtonViewHolder) holder;
            viewHolder.button.setText(itemList.get(position).firstKey);
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_CALCULATOR_ITEM) {
            final CalcItemViewHolder viewHolder = (CalcItemViewHolder) holder;
            viewHolder.indexTextView.setText(itemList.get(position).index);
            viewHolder.packageTypeTextView.setText(itemList.get(position).packageType);
            viewHolder.weightTextView.setText(itemList.get(position).weight);
            viewHolder.dimensionsTextView.setText(itemList.get(position).dimensions);
        }
        //address book spinner
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_SINGLE_SPINNER) {
            final CreateInvoiceSingleSpinnerViewHolder viewHolder = (CreateInvoiceSingleSpinnerViewHolder) holder;
            viewHolder.textView.setText(itemList.get(position).firstKey);

            final SpinnerAdapter<AddressBook> addressBookSpinnerAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, addressBookEntries);
            addressBookSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            viewHolder.spinner.setAdapter(addressBookSpinnerAdapter);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_EDIT_TEXTS) {
            final CreateInvoiceTwoEditTextsViewHolder viewHolder = (CreateInvoiceTwoEditTextsViewHolder) holder;
            viewHolder.bindWatchers(position, callback);
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT) {
            final CreateInvoiceTwoEditTextsViewHolder viewHolder = (CreateInvoiceTwoEditTextsViewHolder) holder;
            viewHolder.bindWatchers(position, callback);
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER) {
            final CreateInvoiceEditTextSpinnerViewHolder viewHolder = (CreateInvoiceEditTextSpinnerViewHolder) holder;
            viewHolder.bindWatcher(position, callback);
            viewHolder.bindSpinner(context, position, callback);
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_SPINNERS) {
            final CreateInvoiceTwoSpinnersViewHolder viewHolder = (CreateInvoiceTwoSpinnersViewHolder) holder;
            /* sender location data */
            if (position == 9 || position == 18 || position == 26) {
                viewHolder.bindFirstSpinner(context, position, callback);
                viewHolder.bindSecondSpinner(context, position, callback);
            }
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS) {
            final CreateInvoiceTwoImageEditTextsViewHolder viewHolder = (CreateInvoiceTwoImageEditTextsViewHolder) holder;
            viewHolder.bindImageViews(callback);
            viewHolder.bindWatchers(position, callback);
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_BUTTON) {
            final CreateInvoiceButtonViewHolder viewHolder = (CreateInvoiceButtonViewHolder) holder;
            viewHolder.bindBtn(callback);
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_CALCULATOR_ITEM) {
            final CalcItemViewHolder viewHolder = (CalcItemViewHolder) holder;
            viewHolder.bind(position, callback);
        }
        //address book spinner
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_SINGLE_SPINNER) {
            final CreateInvoiceSingleSpinnerViewHolder viewHolder = (CreateInvoiceSingleSpinnerViewHolder) holder;
            viewHolder.bindSpinner(callback);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_EDIT_TEXTS) {
            final CreateInvoiceTwoEditTextsViewHolder viewHolder = (CreateInvoiceTwoEditTextsViewHolder) holder;
            viewHolder.unbindWatchers();
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT) {
            final CreateInvoiceTwoEditTextsViewHolder viewHolder = (CreateInvoiceTwoEditTextsViewHolder) holder;
            viewHolder.unbindWatchers();
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER) {
            final CreateInvoiceEditTextSpinnerViewHolder viewHolder = (CreateInvoiceEditTextSpinnerViewHolder) holder;
            viewHolder.unbindWatcher();
            viewHolder.unbindSpinner();
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_SPINNERS) {
            final CreateInvoiceTwoSpinnersViewHolder viewHolder = (CreateInvoiceTwoSpinnersViewHolder) holder;
            /* sender location data */
            if (position == 9 || position == 18 || position == 26){
                viewHolder.unbindFirstSpinner();
                viewHolder.unbindSecondSpinner();
            }
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS) {
            final CreateInvoiceTwoImageEditTextsViewHolder viewHolder = (CreateInvoiceTwoImageEditTextsViewHolder) holder;
            viewHolder.unbindImageViews();
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_BUTTON) {
            final CreateInvoiceButtonViewHolder viewHolder = (CreateInvoiceButtonViewHolder) holder;
            viewHolder.unbindBtn();
        }
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_CALCULATOR_ITEM) {
            final CalcItemViewHolder viewHolder = (CalcItemViewHolder) holder;
            viewHolder.unbind();
        }
        //address book spinner
        else if (getItemViewType(position) == CreateInvoiceData.TYPE_SINGLE_SPINNER) {
            final CreateInvoiceSingleSpinnerViewHolder viewHolder = (CreateInvoiceSingleSpinnerViewHolder) holder;
            viewHolder.unbindSpinner();
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).type == CreateInvoiceData.TYPE_HEADING) {
            return CreateInvoiceData.TYPE_HEADING;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_TWO_EDIT_TEXTS) {
            return CreateInvoiceData.TYPE_TWO_EDIT_TEXTS;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER) {
            return CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT) {
            return CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_STROKE) {
            return CreateInvoiceData.TYPE_STROKE;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS) {
            return CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_BUTTON) {
            return CreateInvoiceData.TYPE_BUTTON;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_CALCULATOR_ITEM) {
            return CreateInvoiceData.TYPE_CALCULATOR_ITEM;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_SINGLE_SPINNER) {
            return CreateInvoiceData.TYPE_SINGLE_SPINNER;
        }
        if (itemList.get(position).type == CreateInvoiceData.TYPE_TWO_SPINNERS) {
            return CreateInvoiceData.TYPE_TWO_SPINNERS;
        }
        return -1;
    }

    private static final String TAG = CreateInvoiceAdapter.class.toString();
}
