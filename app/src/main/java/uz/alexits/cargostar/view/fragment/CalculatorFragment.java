package uz.alexits.cargostar.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkInfo;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.entities.calculation.PackagingType;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.view.adapter.ConsignmentAdapter;
import uz.alexits.cargostar.view.adapter.PackagingAndPriceAdapter;
import uz.alexits.cargostar.view.callback.ConsignmentCallback;
import uz.alexits.cargostar.viewmodel.CalculatorViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.viewmodel.factory.CalculatorViewModelFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CalculatorFragment extends Fragment implements ConsignmentCallback {
    /* header views */
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView profileImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;

    /* swipe to sync */
    private SwipeRefreshLayout swipeRefreshLayout;

    /* main content views */
    /* country spinner items */
    private Spinner srcCountrySpinner;
    private Spinner destCountrySpinner;
    private ArrayAdapter<Country> countryArrayAdapter;

    /* packagingType items */
    private Spinner packagingTypeSpinner;
    private ArrayAdapter<PackagingType> packagingTypeArrayAdapter;

    /* packageType items */
    private RadioGroup packageTypeRadioGroup;
    private RadioButton docTypeRadioBtn;
    private RadioButton boxTypeRadioBtn;

    /* provider */
    private View cargostarCardView;
    private View tntCardView;
    private View fedexCardView;
    private RadioButton cargostarRadioBtn;
    private RadioButton tntRadioBtn;
    private RadioButton fedexRadioBtn;
    private ImageView cargostarImageView;
    private ImageView tntImageView;
    private ImageView fedexImageView;

    /* cargo list */
    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private Button addBtn;

    /* calculations */
    private TextView totalQuantityTextView;
    private TextView totalWeightTextView;
    private TextView totalDimensionsTextView;
    private Button calculateBtn;
    private ConsignmentAdapter consignmentAdapter;
    private PackagingAndPriceAdapter packagingAndPriceAdapter;

    private CalculatorViewModel calculatorViewModel;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CalculatorViewModelFactory calculatorFactory = new CalculatorViewModelFactory(requireContext());
        calculatorViewModel = new ViewModelProvider(getViewModelStore(), calculatorFactory).get(CalculatorViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_calculator, container, false);
        //header views
        fullNameTextView = requireActivity().findViewById(R.id.full_name_text_view);
        branchTextView = requireActivity().findViewById(R.id.branch_text_view);
        courierIdTextView = requireActivity().findViewById(R.id.courier_id_text_view);
        requestSearchEditText = requireActivity().findViewById(R.id.search_edit_text);
        requestSearchImageView = requireActivity().findViewById(R.id.search_btn);
        profileImageView = requireActivity().findViewById(R.id.profile_image_view);
        editImageView = requireActivity().findViewById(R.id.edit_image_view);
        createUserImageView = requireActivity().findViewById(R.id.create_user_image_view);
        notificationsImageView = requireActivity().findViewById(R.id.notifications_image_view);
        badgeCounterTextView = requireActivity().findViewById(R.id.badge_counter_text_view);
        calculatorImageView = requireActivity().findViewById(R.id.calculator_image_view);

        /* swipe to sync */
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);

        //main content views
        srcCountrySpinner = root.findViewById(R.id.source_country_spinner);
        destCountrySpinner = root.findViewById(R.id.destination_country_spinner);
        packagingTypeSpinner = root.findViewById(R.id.package_type_spinner);

        weightEditText = root.findViewById(R.id.weight_edit_text);
        lengthEditText = root.findViewById(R.id.length_edit_text);
        widthEditText = root.findViewById(R.id.width_edit_text);
        heightEditText = root.findViewById(R.id.height_edit_text);
        addBtn = root.findViewById(R.id.add_item_btn);

        totalQuantityTextView = root.findViewById(R.id.total_quantity_value_text_view);
        totalWeightTextView = root.findViewById(R.id.total_weight_value_text_view);
        totalDimensionsTextView = root.findViewById(R.id.total_dimensions_value_text_view);
        calculateBtn = root.findViewById(R.id.calculate_btn);

        packageTypeRadioGroup = root.findViewById(R.id.package_type_radio_group);
        docTypeRadioBtn = root.findViewById(R.id.doc_type_radio_btn);
        boxTypeRadioBtn = root.findViewById(R.id.box_type_radio_btn);

        cargostarCardView = root.findViewById(R.id.cargostar_card_view);
        tntCardView = root.findViewById(R.id.tnt_card_view);
        fedexCardView = root.findViewById(R.id.fedex_card_view);
        cargostarImageView = root.findViewById(R.id.cargostar_image_view);
        tntImageView = root.findViewById(R.id.tnt_image_view);
        fedexImageView = root.findViewById(R.id.fedex_image_view);
        cargostarRadioBtn = root.findViewById(R.id.cargostar_radio_btn);
        tntRadioBtn = root.findViewById(R.id.tnt_radio_btn);
        fedexRadioBtn = root.findViewById(R.id.fedex_radio_btn);

        final RecyclerView consignmentRecyclerView = root.findViewById(R.id.calculationsRecyclerView);
        final RecyclerView calculationResultRecyclerView = root.findViewById(R.id.tariff_price_recycler_view);

        final LinearLayoutManager tariffPriceLayoutManager = new LinearLayoutManager(requireContext());
        final LinearLayoutManager itemLayoutManager = new LinearLayoutManager(requireContext());
        tariffPriceLayoutManager.setOrientation(RecyclerView.VERTICAL);
        itemLayoutManager.setOrientation(RecyclerView.VERTICAL);

        consignmentAdapter = new ConsignmentAdapter(requireContext(), this, 1);
        countryArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingTypeArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingAndPriceAdapter = new PackagingAndPriceAdapter(requireContext());

        consignmentRecyclerView.setLayoutManager(itemLayoutManager);
        calculationResultRecyclerView.setLayoutManager(tariffPriceLayoutManager);

        consignmentRecyclerView.setAdapter(consignmentAdapter);
        calculationResultRecyclerView.setAdapter(packagingAndPriceAdapter);
        srcCountrySpinner.setAdapter(countryArrayAdapter);
        destCountrySpinner.setAdapter(countryArrayAdapter);
        packagingTypeSpinner.setAdapter(packagingTypeArrayAdapter);

        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packagingTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.profileFragment);
        });

        createUserImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.createUserFragment);
        });

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.notificationsFragment);
        });

        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        calculatorImageView.setOnClickListener(null);

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

        /* spinner items */
        srcCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                calculatorViewModel.setSrcCountry((Country) adapterView.getSelectedItem());
                calculatorViewModel.setDestCountry((Country) destCountrySpinner.getSelectedItem());

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        srcCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                calculatorViewModel.setSrcCountry((Country) srcCountrySpinner.getSelectedItem());
                calculatorViewModel.setDestCountry((Country) adapterView.getSelectedItem());

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        destCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* providers */
        cargostarCardView.setOnClickListener(v -> cargostarRadioBtn.setChecked(true));
        tntCardView.setOnClickListener(v -> tntRadioBtn.setChecked(true));
        fedexCardView.setOnClickListener(v -> fedexRadioBtn.setChecked(true));

        /* choose provider */
        cargostarRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                tntRadioBtn.setChecked(false);
                fedexRadioBtn.setChecked(false);
                calculatorViewModel.setSelectedProvider(6L);
            }
        });

        tntRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cargostarRadioBtn.setChecked(false);
                fedexRadioBtn.setChecked(false);
                calculatorViewModel.setSelectedProvider(5L);
            }
        });

        fedexRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cargostarRadioBtn.setChecked(false);
                tntRadioBtn.setChecked(false);
                calculatorViewModel.setSelectedProvider(4L);
            }
        });

        /* choose packaging type (1 / 2) */
        packageTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == docTypeRadioBtn.getId()) {
                //docs
                calculatorViewModel.setSelectedType(1);
                //make consignment fields invisible
                lengthEditText.setVisibility(View.INVISIBLE);
                widthEditText.setVisibility(View.INVISIBLE);
                heightEditText.setVisibility(View.INVISIBLE);
            }
            else if (checkedId == boxTypeRadioBtn.getId()) {
                //boxes
                calculatorViewModel.setSelectedType(2);
                //make consignment fields visible
                lengthEditText.setVisibility(View.VISIBLE);
                widthEditText.setVisibility(View.VISIBLE);
                heightEditText.setVisibility(View.VISIBLE);
            }
            else {
                calculatorViewModel.setSelectedType(0);
            }
        });

        packagingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculatorViewModel.setSelectedPackagingType((PackagingType) adapterView.getSelectedItem());
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        packagingTypeSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addBtn.setOnClickListener(v -> {
            final PackagingType packagingType = (PackagingType) packagingTypeSpinner.getSelectedItem();
            final String weight = weightEditText.getText().toString().trim();
            final String length = lengthEditText.getText().toString().trim();
            final String width = widthEditText.getText().toString().trim();
            final String height = heightEditText.getText().toString().trim();

            /* check for empty fields */
            if (packagingType == null) {
                Toast.makeText(requireContext(), "Выберите упаковку", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(weight)) {
                Toast.makeText(requireContext(), "Укажите вес", Toast.LENGTH_SHORT).show();
                return;
            }
            /* check for regex */
            if (!Regex.isFloatOrInt(weight)) {
                Toast.makeText(requireContext(), "Вес указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
                if (TextUtils.isEmpty(length)) {
                    Toast.makeText(requireContext(), "Укажите длину", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(width)) {
                    Toast.makeText(requireContext(), "Укажите ширину", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(height)) {
                    Toast.makeText(requireContext(), "Укажите высоту", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Regex.isFloatOrInt(length)) {
                    Toast.makeText(requireContext(), "Длина указана неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Regex.isFloatOrInt(width)) {
                    Toast.makeText(requireContext(), "Ширина указана неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Regex.isFloatOrInt(height)) {
                    Toast.makeText(requireContext(), "Высота указана неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            calculatorViewModel.addConsignment(
                    packagingType.getId(),
                    !TextUtils.isEmpty(length) ? Double.parseDouble(length) : 0,
                    !TextUtils.isEmpty(width) ? Double.parseDouble(width) : 0,
                    !TextUtils.isEmpty(height) ? Double.parseDouble(height) : 0,
                    !TextUtils.isEmpty(weight) ? Double.parseDouble(weight) : 0);
        });

        calculateBtn.setOnClickListener(v -> {
            if (srcCountrySpinner.getSelectedItem() == null) {
                Toast.makeText(requireContext(), "Укажите страну отправки", Toast.LENGTH_SHORT).show();
                return;
            }
            if (destCountrySpinner.getSelectedItem() == null) {
                Toast.makeText(requireContext(), "Укажите страну прибытия", Toast.LENGTH_SHORT).show();
                return;
            }
            if (packagingTypeSpinner.getSelectedItem() == null) {
                Toast.makeText(requireContext(), "Выберите упаковку", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cargostarRadioBtn.isChecked() && !tntRadioBtn.isChecked() && !fedexRadioBtn.isChecked()) {
                Toast.makeText(requireContext(), "Укажите поставщика услуг", Toast.LENGTH_SHORT).show();
                return;
            }
            if (packageTypeRadioGroup.getCheckedRadioButtonId() != docTypeRadioBtn.getId() && packageTypeRadioGroup.getCheckedRadioButtonId() != boxTypeRadioBtn.getId()) {
                Toast.makeText(requireContext(), "Укажите тип упаковки", Toast.LENGTH_SHORT).show();
                return;
            }
            calculatorViewModel.calculateTotalPrice();
        });

        requestSearchImageView.setOnClickListener(v -> {
            final String requestId = requestSearchEditText.getText().toString().trim();

            if (TextUtils.isEmpty(requestId)) {
                Toast.makeText(requireContext(), "Введите ID заявки", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isDigitsOnly(requestId)) {
                Toast.makeText(requireContext(), "Неверный формат", Toast.LENGTH_SHORT).show();
                return;
            }
            calculatorViewModel.searchRequest(Long.parseLong(requestId));
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            calculatorViewModel.fetchPackagingData();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        calculatorViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        calculatorViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        calculatorViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        calculatorViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                Toast.makeText(requireContext(), "Заявки не существует", Toast.LENGTH_SHORT).show();
                requestSearchEditText.setEnabled(true);
                return;
            }
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                requestSearchEditText.setEnabled(true);
                startActivity(new Intent(requireContext(), MainActivity.class)
                        .putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_REQUEST)
                        .putExtra(Constants.KEY_REQUEST_ID, workInfo.getOutputData().getLong(Constants.KEY_REQUEST_ID, 0L))
                        .putExtra(Constants.KEY_INVOICE_ID, workInfo.getOutputData().getLong(Constants.KEY_INVOICE_ID, 0L))
                        .putExtra(Constants.KEY_CLIENT_ID, workInfo.getOutputData().getLong(Constants.KEY_CLIENT_ID, 0L))
                        .putExtra(Constants.KEY_COURIER_ID, workInfo.getOutputData().getLong(Constants.KEY_COURIER_ID, 0L))
                        .putExtra(Constants.KEY_SENDER_COUNTRY_ID, workInfo.getOutputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, 0L))
                        .putExtra(Constants.KEY_SENDER_REGION_ID, workInfo.getOutputData().getLong(Constants.KEY_SENDER_REGION_ID, 0L))
                        .putExtra(Constants.KEY_SENDER_CITY_ID, workInfo.getOutputData().getLong(Constants.KEY_SENDER_CITY_ID, 0L))
                        .putExtra(Constants.KEY_RECIPIENT_COUNTRY_ID, workInfo.getOutputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, 0L))
                        .putExtra(Constants.KEY_RECIPIENT_CITY_ID, workInfo.getOutputData().getLong(Constants.KEY_RECIPIENT_CITY_ID, 0L))
                        .putExtra(Constants.KEY_PROVIDER_ID, workInfo.getOutputData().getLong(Constants.KEY_PROVIDER_ID, 0L)));
                return;
            }
            requestSearchEditText.setEnabled(false);
        });

        calculatorViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
            if (countryList != null) {
                countryArrayAdapter.clear();
                countryArrayAdapter.addAll(countryList);

                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        int finalI = i;
                        srcCountrySpinner.post(() -> srcCountrySpinner.setSelection(finalI, false));
                        int finalI1 = i;
                        destCountrySpinner.post(() -> destCountrySpinner.setSelection(finalI1, false));
                        return;
                    }
                }
            }
        });

        calculatorViewModel.getProviderList().observe(getViewLifecycleOwner(), providerList -> {
            if (providerList == null || providerList.isEmpty()) {
                //other -> other = provider radio group is empty
                cargostarRadioBtn.setChecked(false);
                cargostarRadioBtn.setVisibility(View.INVISIBLE);
                cargostarImageView.setVisibility(View.INVISIBLE);
                cargostarCardView.setVisibility(View.INVISIBLE);

                tntRadioBtn.setChecked(false);
                tntRadioBtn.setVisibility(View.INVISIBLE);
                tntImageView.setVisibility(View.INVISIBLE);
                tntCardView.setVisibility(View.INVISIBLE);

                fedexRadioBtn.setChecked(false);
                fedexRadioBtn.setVisibility(View.INVISIBLE);
                fedexImageView.setVisibility(View.INVISIBLE);
                fedexCardView.setVisibility(View.INVISIBLE);
                return;
            }
            if (providerList.size() == 1) {
                final String providerName = providerList.get(0).getNameEn();

                if (providerName == null) {
                    return;
                }
                if (providerName.equalsIgnoreCase(getString(R.string.cargostar))) {
                    cargostarRadioBtn.setChecked(false);
                    cargostarRadioBtn.setVisibility(View.VISIBLE);
                    cargostarImageView.setVisibility(View.VISIBLE);
                    cargostarCardView.setVisibility(View.VISIBLE);

                    tntRadioBtn.setChecked(false);
                    tntRadioBtn.setVisibility(View.GONE);
                    tntImageView.setVisibility(View.GONE);
                    tntCardView.setVisibility(View.GONE);

                    fedexRadioBtn.setChecked(false);
                    fedexRadioBtn.setVisibility(View.GONE);
                    fedexImageView.setVisibility(View.GONE);
                    fedexCardView.setVisibility(View.GONE);
                    return;
                }
                if (providerName.equalsIgnoreCase(getString(R.string.tnt))) {
                    cargostarRadioBtn.setChecked(false);
                    cargostarRadioBtn.setVisibility(View.GONE);
                    cargostarImageView.setVisibility(View.GONE);
                    cargostarCardView.setVisibility(View.GONE);

                    tntRadioBtn.setChecked(false);
                    tntRadioBtn.setVisibility(View.VISIBLE);
                    tntImageView.setVisibility(View.VISIBLE);
                    tntCardView.setVisibility(View.VISIBLE);

                    fedexRadioBtn.setChecked(false);
                    fedexRadioBtn.setVisibility(View.GONE);
                    fedexImageView.setVisibility(View.GONE);
                    fedexCardView.setVisibility(View.GONE);
                }
                return;
            }
            if (providerList.size() == 2) {
                cargostarRadioBtn.setChecked(false);
                cargostarRadioBtn.setVisibility(View.GONE);
                cargostarImageView.setVisibility(View.GONE);
                cargostarCardView.setVisibility(View.GONE);

                tntRadioBtn.setChecked(false);
                tntRadioBtn.setVisibility(View.VISIBLE);
                tntImageView.setVisibility(View.VISIBLE);
                tntCardView.setVisibility(View.VISIBLE);

                fedexRadioBtn.setChecked(false);
                fedexRadioBtn.setVisibility(View.VISIBLE);
                fedexImageView.setVisibility(View.VISIBLE);
                fedexCardView.setVisibility(View.VISIBLE);
            }
        });

        calculatorViewModel.getPackagingTypeList().observe(getViewLifecycleOwner(), packagingTypeList -> {
            packagingTypeArrayAdapter.clear();
            packagingTypeArrayAdapter.addAll(packagingTypeList);
            packagingTypeArrayAdapter.notifyDataSetChanged();
        });

        calculatorViewModel.getConsignmentList().observe(getViewLifecycleOwner(), consignmentList -> {
            consignmentAdapter.setItemList(consignmentList);
            consignmentAdapter.notifyDataSetChanged();
        });

        calculatorViewModel.getCalculationResult().observe(getViewLifecycleOwner(), calculationResult -> {
            totalQuantityTextView.setText(String.valueOf(calculationResult.getTotalQuantity()));
            totalWeightTextView.setText(String.valueOf(new BigDecimal(Double.toString(calculationResult.getTotalVolume())).setScale(2, RoundingMode.HALF_UP).doubleValue()));
            totalDimensionsTextView.setText(String.valueOf(new BigDecimal(Double.toString(calculationResult.getTotalWeight())).setScale(2, RoundingMode.HALF_UP).doubleValue()));
        });

        calculatorViewModel.getPackagingAndPriceList().observe(getViewLifecycleOwner(), packagingAndPrices -> {
            packagingAndPriceAdapter.setResultList(packagingAndPrices);
        });

        calculatorViewModel.getPackagingList().observe(getViewLifecycleOwner(), packagingList -> {
            calculatorViewModel.setSelectedPackagingList(packagingList);
        });

        calculatorViewModel.getZoneSettingsList().observe(getViewLifecycleOwner(), zoneSettingsList -> {
            calculatorViewModel.setSelectedZoneSettingsList(zoneSettingsList);
        });

        calculatorViewModel.getVat().observe(getViewLifecycleOwner(), vat -> calculatorViewModel.setSelectedVat(vat));

        calculatorViewModel.getFetchPackagingDataResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (workInfo.getState() == WorkInfo.State.FAILED) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), R.string.error_sync_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            if (workInfo.getState() == WorkInfo.State.CANCELLED) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                return;
            }
            swipeRefreshLayout.setRefreshing(true);
        });
    }

    @Override
    public void onDeleteItemClicked(final int position) {
        calculatorViewModel.removeConsignment(position);
    }

    @Override
    public void onScanItemClicked(int position) {

    }

    private static final String TAG = CalculatorFragment.class.toString();
}