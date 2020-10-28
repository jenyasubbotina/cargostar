package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.packaging.PackagingType;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.viewmodel.HeaderViewModel;
import uz.alexits.cargostar.viewmodel.PackagingDataViewModel;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.CalculatorAdapter;
import uz.alexits.cargostar.view.callback.CreateParcelCallback;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;

public class CalculatorFragment extends Fragment implements CreateParcelCallback {
    private Context context;
    private FragmentActivity activity;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private EditText parcelSearchEditText;
    private ImageView parcelSearchImageView;
    private ImageView profileImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;
    //main content views

    //location view model
    private LocationDataViewModel locationDataViewModel;

    //country spinner items
    private ArrayAdapter<Country> countryArrayAdapter;
    private Spinner srcCountrySpinner;
    private Spinner destCountrySpinner;
    private RelativeLayout srcCountryField;
    private RelativeLayout destCountryField;

    //city spinner items
    private ArrayAdapter<City> srcCityArrayAdapter;
    private ArrayAdapter<City> destCityArrayAdapter;
    private RelativeLayout srcCityField;
    private RelativeLayout destCityField;
    private Spinner srcCitySpinner;
    private Spinner destCitySpinner;

    //package type items
    private ArrayAdapter<PackagingType> packagingTypeArrayAdapter;
    private Spinner packagingTypeSpinner;
    private RelativeLayout packagingTypeField;

    private CalculatorAdapter calculatorAdapter;

    private CardView firstCard;
    private CardView secondCard;
    private RadioButton firstCardRadioBtn;
    private RadioButton secondCardRadioBtn;
    private ImageView firstCardImageView;
    private ImageView secondCardImageView;

    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private Button addBtn;

    private TextView totalQuantityTextView;
    private TextView totalWeightTextView;
    private TextView totalDimensionsTextView;
    private TextView expressCost;
    private TextView economyExpressCost;
    private Button calculateBtn;

    //calc items
    private List<Cargo> itemList;
    private RecyclerView itemRecyclerView;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();
        itemList = new ArrayList<>();

        SyncWorkRequest.fetchPackagingData(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_calculator, container, false);

        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        parcelSearchEditText = activity.findViewById(R.id.search_edit_text);
        parcelSearchImageView = activity.findViewById(R.id.search_btn);
        profileImageView = activity.findViewById(R.id.profile_image_view);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //main content views
        srcCountryField = root.findViewById(R.id.source_country_field);
        destCountryField = root.findViewById(R.id.destination_country_field);

        srcCityField = root.findViewById(R.id.source_city_field);
        destCityField = root.findViewById(R.id.destination_city_field);

        srcCountrySpinner = root.findViewById(R.id.source_country_spinner);
        destCountrySpinner = root.findViewById(R.id.destination_country_spinner);

        srcCitySpinner = root.findViewById(R.id.source_city_spinner);
        destCitySpinner = root.findViewById(R.id.destination_city_spinner);

        packagingTypeSpinner = root.findViewById(R.id.package_type_spinner);
        packagingTypeField = root.findViewById(R.id.package_type_field);

        weightEditText = root.findViewById(R.id.weight_edit_text);
        lengthEditText = root.findViewById(R.id.length_edit_text);
        widthEditText = root.findViewById(R.id.width_edit_text);
        heightEditText = root.findViewById(R.id.height_edit_text);
        addBtn = root.findViewById(R.id.add_item_btn);

        totalQuantityTextView = root.findViewById(R.id.total_quantity_value_text_view);
        totalWeightTextView = root.findViewById(R.id.total_weight_value_text_view);
        totalDimensionsTextView = root.findViewById(R.id.total_dimensions_value_text_view);
        calculateBtn = root.findViewById(R.id.calculate_btn);

        expressCost = root.findViewById(R.id.express_sum_text_view);
        economyExpressCost = root.findViewById(R.id.economy_express_sum_text_view);

        countryArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srcCountrySpinner.setAdapter(countryArrayAdapter);
        destCountrySpinner.setAdapter(countryArrayAdapter);

        srcCityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        destCityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        srcCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srcCitySpinner.setAdapter(srcCityArrayAdapter);
        destCitySpinner.setAdapter(destCityArrayAdapter);

        packagingTypeArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packagingTypeSpinner.setAdapter(packagingTypeArrayAdapter);

        firstCard = root.findViewById(R.id.first_card);
        secondCard = root.findViewById(R.id.second_card);
        firstCardImageView = root.findViewById(R.id.first_card_logo);
        secondCardImageView = root.findViewById(R.id.second_card_logo);
        firstCardRadioBtn = root.findViewById(R.id.first_card_radio_btn);
        secondCardRadioBtn = root.findViewById(R.id.second_card_radio_btn);

        itemRecyclerView = root.findViewById(R.id.calculationsRecyclerView);
        calculatorAdapter = new CalculatorAdapter(context, itemList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        itemRecyclerView.setLayoutManager(layoutManager);
        itemRecyclerView.setAdapter(calculatorAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
        });
        editImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, ProfileActivity.class));
        });
        createUserImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CreateUserActivity.class));
        });
        notificationsImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, NotificationsActivity.class));
        });
        calculatorImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CalculatorActivity.class));
        });

        weightEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(weightEditText, hasFocus);
        });

        lengthEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(lengthEditText, hasFocus);
        });

        widthEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(widthEditText, hasFocus);
        });

        heightEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(heightEditText, hasFocus);
        });

        firstCard.setOnClickListener(v -> {
            firstCardRadioBtn.setChecked(true);
        });

        secondCard.setOnClickListener(v -> {
            secondCardRadioBtn.setChecked(true);
        });

        firstCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                secondCardRadioBtn.setChecked(false);
            }
        });

        secondCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstCardRadioBtn.setChecked(false);
            }
        });

        srcCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final Country selectedCountry = (Country) adapterView.getSelectedItem();

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        srcCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final int selectedPosition = srcCountrySpinner.getSelectedItemPosition();

                populateCityAdapter(srcCityArrayAdapter, selectedCountry.getId());

//                if (selectedPosition == 0) {
//                    sourceCitySpinner.setAdapter(uzbekistanCityAdapter);
//                    if (destinationCountrySpinner.getSelectedItem().equals(countryList[0])) {
//                        firstCardRadioBtn.setChecked(true);
//                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
//                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                        secondCardImageView.setVisibility(View.INVISIBLE);
//                        secondCard.setVisibility(View.INVISIBLE);
//                        return;
//                    }
//                    firstCardRadioBtn.setChecked(true);
//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
//                    secondCardRadioBtn.setVisibility(View.VISIBLE);
//                    secondCardImageView.setVisibility(View.VISIBLE);
//                    secondCard.setVisibility(View.VISIBLE);
//                    return;
//                }
//                if (destinationCountrySpinner.getSelectedItem().equals(countryList[0])) {
//                    firstCardRadioBtn.setChecked(true);
//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                    secondCardImageView.setVisibility(View.INVISIBLE);
//                    secondCard.setVisibility(View.INVISIBLE);
//                }
//                if (selectedPosition == 1) {
//                    sourceCitySpinner.setAdapter(kazakhstanCityAdapter);
//                    return;
//                }
//                if (selectedPosition == 2) {
//                    sourceCitySpinner.setAdapter(kirgizstanCityAdapter);
//                    return;
//                }
//                if (selectedPosition == 3) {
//                    sourceCitySpinner.setAdapter(russiaCityAdapter);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final Country selectedCountry = (Country) adapterView.getSelectedItem();

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        destCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final int selectedPosition = destCountrySpinner.getSelectedItemPosition();

                populateCityAdapter(destCityArrayAdapter, selectedCountry.getId());

//                if (selectedPosition == 0) {
//                    destinationCitySpinner.setAdapter(uzbekistanCityAdapter);
//                    if (sourceCountrySpinner.getSelectedItem().equals(countryList[0])) {
//                        firstCardRadioBtn.setChecked(true);
//                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
//                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                        secondCardImageView.setVisibility(View.INVISIBLE);
//                        secondCard.setVisibility(View.INVISIBLE);
//                        return;
//                    }
//                    firstCardRadioBtn.setChecked(true);
//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                    secondCardImageView.setVisibility(View.INVISIBLE);
//                    secondCard.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                if (sourceCountrySpinner.getSelectedItem().equals(countryList[0])) {
//                    firstCardRadioBtn.setChecked(true);
//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
//                    secondCardRadioBtn.setVisibility(View.VISIBLE);
//                    secondCardImageView.setVisibility(View.VISIBLE);
//                    secondCard.setVisibility(View.VISIBLE);
//                }
//                if (selectedPosition == 1) {
//                    destinationCitySpinner.setAdapter(kazakhstanCityAdapter);
//                    return;
//                }
//                if (selectedPosition == 2) {
//                    destinationCitySpinner.setAdapter(kirgizstanCityAdapter);
//                    return;
//                }
//                if (selectedPosition == 3) {
//                    destinationCitySpinner.setAdapter(russiaCityAdapter);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        srcCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        srcCityField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        destCityField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        packagingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        packagingTypeField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addBtn.setOnClickListener(v -> {
//            final String packageType = packageTypeSpinner.getSelectedItem().toString();
//            final int weight = Integer.parseInt(weightEditText.getText().toString());
//            final int length = Integer.parseInt(lengthEditText.getText().toString());
//            final int width = Integer.parseInt(widthEditText.getText().toString());
//            final int height = Integer.parseInt(heightEditText.getText().toString());
//            final String sourceCountryStr = sourceCountrySpinner.getSelectedItem().toString();
//            final String sourceCityStr = sourceCitySpinner.getSelectedItem().toString();
//            final String destinationCountryStr = destinationCountrySpinner.getSelectedItem().toString();
//            final String destinationCityStr = destinationCitySpinner.getSelectedItem().toString();
//
//            final Country sourceCountry = new Country(sourceCountryStr);
//            final City sourceCity = new City(sourceCityStr);
//            final Country destinationCountry = new Country(destinationCountryStr);
//            final City destinationCity = new City(destinationCityStr);
//
//            final Address sourceAddress = new Address();
//            sourceAddress.setCountry(sourceCountry);
//            sourceAddress.setCity(sourceCity);
//
//            final Address destinationAddress = new Address();
//            destinationAddress.setCountry(destinationCountry);
//            destinationAddress.setCity(destinationCity);
//
//            final Cargo newItem = new Cargo(de);
//            newItem.set(sourceAddress);
//            newItem.setDestination(destinationAddress);
//            newItem.setLength(length);
//            newItem.setWidth(width);
//            newItem.setHeight(height);
//            newItem.setWeight(weight);
//            newItem.setPackageType(packageType);
//
//            itemList.add(newItem);
//            calculatorAdapter.notifyItemInserted(itemList.size() - 1);
        });

        calculateBtn.setOnClickListener(v -> {
            int totalQuantity = itemList.size() + 1;
            int totalWeight = 0;
            int totalLength = 0;
            int totalWidth = 0;
            int totalHeight = 0;

            for (final Cargo item : itemList) {
                totalWeight += item.getWeight();
                totalLength += item.getLength();
                totalWidth += item.getWidth();
                totalHeight += item.getHeight();
            }
            final String totalDimensions = totalLength + "x" + totalWidth + "x" + totalHeight;
            totalQuantityTextView.setText(String.valueOf(totalQuantity));
            totalWeightTextView.setText(String.valueOf(totalWeight));
            totalDimensionsTextView.setText(totalDimensions);
//            expressCost.setText();
//            economyExpressCost.setText();
        });
    }

    private void populateCityAdapter(final ArrayAdapter<City> arrayAdapter, final long countryId) {
        arrayAdapter.clear();

        locationDataViewModel.getCitiesByCountryId(countryId).observe(getViewLifecycleOwner(), cityList -> {
            Log.i(TAG, "country selected cities by id " + countryId + " :" + cityList);
            for (final City city : cityList) {
                arrayAdapter.add(city);
            }
            arrayAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //header view model
        final HeaderViewModel headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);

        headerViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        headerViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        headerViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            headerViewModel.selectRequest(parcelId).observe(getViewLifecycleOwner(), receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (receiptWithCargoList.getReceipt() == null) {
//                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                final Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_FIND_PARCEL);
                mainIntent.putExtra(Constants.INTENT_REQUEST_VALUE, parcelId);
                startActivity(mainIntent);
            });
        });

        //country/city spinners
       locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

       locationDataViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
           for (final Country country : countryList) {
               countryArrayAdapter.add(country);
           }
           countryArrayAdapter.notifyDataSetChanged();
       });

        //providers / packaging
        final PackagingDataViewModel packagingDataViewModel = new ViewModelProvider(this).get(PackagingDataViewModel.class);

        packagingDataViewModel.getPackagingTypeList().observe(getViewLifecycleOwner(), packagingTypeList -> {
            for (final PackagingType packagingType : packagingTypeList) {
                packagingTypeArrayAdapter.add(packagingType);
            }
            packagingTypeArrayAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onAddBtnClicked() {

    }

    @Override
    public void onCameraImageClicked(int position) {

    }

    @Override
    public void onSenderSignatureClicked() {

    }

    @Override
    public void onRecipientSignatureClicked() {

    }

    @Override
    public void afterFirstEditTextChanged(int position, Editable editable) {

    }

    @Override
    public void afterSecondEditTextChanged(int position, Editable editable) {

    }

    @Override
    public void onSpinnerItemChanged(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onDeleteItemClicked(final int position) {
        itemList.remove(position);
        calculatorAdapter.notifyItemRemoved(position);
    }

    private static final String TAG = CalculatorFragment.class.toString();
}