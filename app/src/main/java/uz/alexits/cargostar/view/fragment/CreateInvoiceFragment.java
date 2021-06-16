package uz.alexits.cargostar.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.entities.calculation.PackagingAndPrice;
import uz.alexits.cargostar.entities.calculation.PackagingType;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.SignatureActivity;
import uz.alexits.cargostar.view.adapter.ConsignmentAdapter;
import uz.alexits.cargostar.view.adapter.CustomArrayAdapter;
import uz.alexits.cargostar.view.adapter.PackagingAndPriceRadioAdapter;
import uz.alexits.cargostar.view.callback.ConsignmentCallback;
import uz.alexits.cargostar.view.callback.TariffCallback;
import uz.alexits.cargostar.view.dialog.ScanQrDialog;
import uz.alexits.cargostar.viewmodel.CreateInvoiceViewModel;
import uz.alexits.cargostar.viewmodel.factory.CreateInvoiceViewModelFactory;

public class CreateInvoiceFragment extends Fragment implements ConsignmentCallback, TariffCallback {
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

    /* content views */
    private EditText senderEmailEditText;
    private EditText senderSignatureEditText;
    private ImageView senderSignatureResultImageView;
    private ImageView senderSignatureImageView;
    private EditText senderFirstNameEditText;
    private EditText senderLastNameEditText;
    private EditText senderMiddleNameEditText;
    private EditText senderPhoneEditText;
    private EditText senderAddressEditText;
    private EditText senderZipEditText;
    private EditText senderCompanyEditText;
    private EditText senderTntEditText;
    private EditText senderFedexEditText;
    private EditText recipientEmailEditText;
    private EditText recipientFirstNameEditText;
    private EditText recipientLastNameEditText;
    private EditText recipientMiddleNameEditText;
    private EditText recipientAddressEditText;
    private EditText recipientZipEditText;
    private EditText recipientPhoneEditText;
    private EditText recipientCargoEditText;
    private EditText recipientTntEditText;
    private EditText recipientFedexEditText;
    private EditText recipientCompanyEditText;
    private EditText payerEmailEditText;
    private EditText payerFirstNameEditText;
    private EditText payerLastNameEditText;
    private EditText payerMiddleNameEditText;
    private EditText payerAddressEditText;
    private EditText payerZipEditText;
    private EditText payerPhoneEditText;
    private EditText discountEditText;
    private EditText payerCargoEditText;
    private EditText payerTntEditText;
    private EditText payerTntTaxIdEditText;
    private EditText payerFedexEditText;
    private EditText payerFedexTaxIdEditText;
    private EditText payerInnEditText;
    private EditText payerCompanyEditText;
    private EditText contractNumberEditText;
    private EditText instructionsEditText;
    private EditText senderCityNameEditText;
    private EditText recipientCityNameEditText;
    private EditText payerCityNameEditText;
    private EditText cargoDescriptionEditText;
    private EditText cargoNameEditText;
    private EditText cargoPriceEditText;
    private TextView lengthTextView;
    private TextView widthTextView;
    private TextView heightTextView;
    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private TextView totalQuantityTextView;
    private TextView totalWeightTextView;
    private TextView totalDimensionsTextView;
    private RecyclerView tariffPriceRecyclerView;

    private View cargostarCardView;
    private View tntCardView;
    private View fedexCardView;
    private RadioButton cargostarRadioBtn;
    private RadioButton tntRadioBtn;
    private RadioButton fedexRadioBtn;
    private ImageView cargostarImageView;
    private ImageView tntImageView;
    private ImageView fedexImageView;

    private RadioGroup packageTypeRadioGroup;
    private RadioButton docTypeRadioBtn;
    private RadioButton boxTypeRadioBtn;
    private RadioGroup paymentMethodRadioGroup;
    private RadioButton onlineRadioBtn;
    private RadioButton cashRadioBtn;
    private RadioButton terminalRadioBtn;
    private RadioButton transferRadioBtn;
    private RadioButton corporateRadioBtn;
    private RadioGroup payerIsSomeoneRadioGroup;
    private RadioButton senderIsPayerRadioBtn;
    private RadioButton recipientIsPayerRadioBtn;

    private ProgressBar progressBar;

    private Spinner packagingTypeSpinner;
    private Spinner senderCountrySpinner;
    private Spinner recipientCountrySpinner;
    private Spinner payerCountrySpinner;
    private Spinner recipientAddressBookSpinner;
    private Spinner payerAddressBookSpinner;

    private Button addBtn;
    private Button calculateBtn;
    private Button createInvoiceBtn;

    private CustomArrayAdapter<PackagingType> packagingTypeAdapter;
    private CustomArrayAdapter<Country> countryAdapter;
    private CustomArrayAdapter<AddressBook> addressBookAdapter;
    private PackagingAndPriceRadioAdapter packagingAndPriceAdapter;
    private ConsignmentAdapter consignmentAdapter;

    private CreateInvoiceViewModel createInvoiceViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CreateInvoiceViewModelFactory createInvoiceFactory = new CreateInvoiceViewModelFactory(requireContext());
        createInvoiceViewModel = new ViewModelProvider(getViewModelStore(), createInvoiceFactory).get(CreateInvoiceViewModel.class);
        createInvoiceViewModel.setAction(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getAction());

        if (getArguments() != null) {
            if (CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRequestId() > 0) {
                createInvoiceViewModel.setCurrentRequest(
                        new Request(
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRequestId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderFirstName(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderMiddleName(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderLastName(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderEmail(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderPhone(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderAddress(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderCountryId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderCityName(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCountryId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCityName(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getComment(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderUserId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getCourierId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getProviderId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getInvoiceId(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderCityName(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCityName(),
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getConsignmentQuantity(),
                                null,
                                CreateInvoiceFragmentArgs.fromBundle(getArguments()).getDeliveryType()));
            }
            createInvoiceViewModel.setInvoiceId(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getInvoiceId());
            createInvoiceViewModel.setClientId(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderId());
            createInvoiceViewModel.setCurrentRecipientId(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientId());
            createInvoiceViewModel.setRecipientCountryId(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCountryId());
            createInvoiceViewModel.setCurrentPayerId(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerId());
            createInvoiceViewModel.setProviderId(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getProviderId());
            createInvoiceViewModel.setSelectedType(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getDeliveryType());
            createInvoiceViewModel.setPackagingId(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPackagingId());
            createInvoiceViewModel.setTotalPrice(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getTotalPrice());
            createInvoiceViewModel.setPaymentMethod(CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPaymentMethod());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_create_invoice, container, false);

        /* header views */
        profileImageView = requireActivity().findViewById(R.id.profile_image_view);
        fullNameTextView = requireActivity().findViewById(R.id.full_name_text_view);
        branchTextView = requireActivity().findViewById(R.id.branch_text_view);
        courierIdTextView = requireActivity().findViewById(R.id.courier_id_text_view);
        requestSearchEditText = requireActivity().findViewById(R.id.search_edit_text);
        requestSearchImageView = requireActivity().findViewById(R.id.search_btn);
        editImageView = requireActivity().findViewById(R.id.edit_image_view);
        createUserImageView = requireActivity().findViewById(R.id.create_user_image_view);
        calculatorImageView = requireActivity().findViewById(R.id.calculator_image_view);
        notificationsImageView = requireActivity().findViewById(R.id.notifications_image_view);
        badgeCounterTextView = requireActivity().findViewById(R.id.badge_counter_text_view);

        /* content views */
        progressBar = root.findViewById(R.id.progress_bar);
        senderEmailEditText = root.findViewById(R.id.sender_email_edit_text);
        senderSignatureEditText = root.findViewById(R.id.sender_signature_edit_text);
        senderSignatureResultImageView = root.findViewById(R.id.sender_signature_result_image_view);
        senderSignatureImageView = root.findViewById(R.id.sender_signature_image_view);
        senderFirstNameEditText = root.findViewById(R.id.sender_first_name_edit_text);
        senderLastNameEditText = root.findViewById(R.id.sender_last_name_edit_text);
        senderMiddleNameEditText = root.findViewById(R.id.sender_middle_name_edit_text);
        senderPhoneEditText = root.findViewById(R.id.sender_phone_edit_text);
        senderAddressEditText = root.findViewById(R.id.sender_address_edit_text);
        senderZipEditText = root.findViewById(R.id.sender_zip_edit_text);
        senderCompanyEditText = root.findViewById(R.id.sender_company_edit_text);
        senderTntEditText = root.findViewById(R.id.sender_tnt_edit_text);
        senderFedexEditText = root.findViewById(R.id.sender_fedex_edit_text);
        discountEditText = root.findViewById(R.id.sender_discount_edit_text);
        recipientEmailEditText = root.findViewById(R.id.recipient_email_edit_text);
        recipientFirstNameEditText = root.findViewById(R.id.recipient_first_name_edit_text);
        recipientLastNameEditText = root.findViewById(R.id.recipient_last_name_edit_text);
        recipientMiddleNameEditText = root.findViewById(R.id.recipient_middle_name_edit_text);
        recipientAddressEditText = root.findViewById(R.id.recipient_address_edit_text);
        recipientZipEditText = root.findViewById(R.id.recipient_zip_edit_text);
        recipientPhoneEditText = root.findViewById(R.id.recipient_phone_edit_text);
        recipientCargoEditText = root.findViewById(R.id.recipient_cargo_edit_text);
        recipientTntEditText = root.findViewById(R.id.recipient_tnt_edit_text);
        recipientFedexEditText = root.findViewById(R.id.recipient_fedex_edit_text);
        recipientCompanyEditText = root.findViewById(R.id.recipient_company_edit_text);
        payerIsSomeoneRadioGroup = root.findViewById(R.id.payer_is_someone_radio_group);
        senderIsPayerRadioBtn = root.findViewById(R.id.payer_is_sender_radio_btn);
        recipientIsPayerRadioBtn = root.findViewById(R.id.payer_is_recipient_radio_btn);
        payerEmailEditText = root.findViewById(R.id.payer_email_edit_text);
        payerFirstNameEditText = root.findViewById(R.id.payer_first_name_edit_text);
        payerLastNameEditText = root.findViewById(R.id.payer_last_name_edit_text);
        payerMiddleNameEditText = root.findViewById(R.id.payer_middle_name_edit_text);
        payerAddressEditText = root.findViewById(R.id.payer_address_edit_text);
        payerZipEditText = root.findViewById(R.id.payer_zip_edit_text);
        payerPhoneEditText = root.findViewById(R.id.payer_phone_edit_text);
        payerCargoEditText = root.findViewById(R.id.payer_cargo_edit_text);
        payerTntEditText = root.findViewById(R.id.payer_tnt_edit_text);
        payerTntTaxIdEditText = root.findViewById(R.id.payer_tnt_tax_id_edit_text);
        payerFedexEditText = root.findViewById(R.id.payer_fedex_edit_text);
        payerFedexTaxIdEditText = root.findViewById(R.id.payer_fedex_tax_id_edit_text);
        payerInnEditText = root.findViewById(R.id.payer_inn_edit_text);
        payerCompanyEditText = root.findViewById(R.id.payer_company_edit_text);
        contractNumberEditText = root.findViewById(R.id.contract_number_edit_text);
        totalQuantityTextView = root.findViewById(R.id.total_quantity_value_text_view);
        totalWeightTextView = root.findViewById(R.id.total_weight_value_text_view);
        totalDimensionsTextView = root.findViewById(R.id.total_dimensions_value_text_view);
        calculateBtn = root.findViewById(R.id.calculate_btn);
        instructionsEditText = root.findViewById(R.id.instructions_edit_text);
        cargostarCardView = root.findViewById(R.id.cargostar_card_view);
        tntCardView = root.findViewById(R.id.tnt_card_view);
        fedexCardView = root.findViewById(R.id.fedex_card_view);
        cargostarImageView = root.findViewById(R.id.cargostar_image_view);
        tntImageView = root.findViewById(R.id.tnt_image_view);
        fedexImageView = root.findViewById(R.id.fedex_image_view);
        cargostarRadioBtn = root.findViewById(R.id.cargostar_radio_btn);
        tntRadioBtn = root.findViewById(R.id.tnt_radio_btn);
        fedexRadioBtn = root.findViewById(R.id.fedex_radio_btn);
        packageTypeRadioGroup = root.findViewById(R.id.package_type_radio_group);
        docTypeRadioBtn = root.findViewById(R.id.doc_type_radio_btn);
        boxTypeRadioBtn = root.findViewById(R.id.box_type_radio_btn);
        cargoNameEditText = root.findViewById(R.id.cargo_name_edit_text);
        cargoPriceEditText = root.findViewById(R.id.cargo_price_edit_text);
        cargoDescriptionEditText = root.findViewById(R.id.cargo_description_edit_text);
        packagingTypeSpinner = root.findViewById(R.id.packaging_type_spinner);
        lengthTextView = root.findViewById(R.id.length_text_view);
        widthTextView = root.findViewById(R.id.width_text_view);
        heightTextView = root.findViewById(R.id.height_text_view);
        weightEditText = root.findViewById(R.id.weight_edit_text);
        lengthEditText = root.findViewById(R.id.length_edit_text);
        widthEditText = root.findViewById(R.id.width_edit_text);
        heightEditText = root.findViewById(R.id.height_edit_text);
        addBtn = root.findViewById(R.id.add_item_btn);
        paymentMethodRadioGroup = root.findViewById(R.id.payment_method_radio_group);
        onlineRadioBtn = root.findViewById(R.id.online_radio_btn);
        cashRadioBtn = root.findViewById(R.id.cash_radio_btn);
        terminalRadioBtn = root.findViewById(R.id.terminal_radio_btn);
        transferRadioBtn = root.findViewById(R.id.transfer_radio_btn);
        corporateRadioBtn = root.findViewById(R.id.corporate_radio_btn);
        createInvoiceBtn = root.findViewById(R.id.create_receipt_btn);
        tariffPriceRecyclerView = root.findViewById(R.id.tariff_price_recycler_view);
        final RecyclerView itemRecyclerView = root.findViewById(R.id.calculations_recycler_view);
        senderCountrySpinner = root.findViewById(R.id.sender_country_spinner);
        recipientCountrySpinner = root.findViewById(R.id.recipient_country_spinner);
        payerCountrySpinner = root.findViewById(R.id.payer_country_spinner);
        senderCityNameEditText = root.findViewById(R.id.sender_city_edit_text);
        recipientCityNameEditText = root.findViewById(R.id.recipient_city_edit_text);
        payerCityNameEditText = root.findViewById(R.id.payer_city_edit_text);
        recipientAddressBookSpinner = root.findViewById(R.id.recipient_address_book_spinner);
        payerAddressBookSpinner = root.findViewById(R.id.payer_address_book_spinner);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        final LinearLayoutManager tariffPriceLayoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        tariffPriceLayoutManager.setOrientation(RecyclerView.VERTICAL);

        itemRecyclerView.setLayoutManager(layoutManager);
        tariffPriceRecyclerView.setLayoutManager(tariffPriceLayoutManager);

        countryAdapter = new CustomArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        addressBookAdapter = new CustomArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingTypeAdapter = new CustomArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        consignmentAdapter = new ConsignmentAdapter(requireContext(), this, 2);
        packagingAndPriceAdapter = new PackagingAndPriceRadioAdapter(requireContext(), this);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressBookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packagingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        senderCountrySpinner.setAdapter(countryAdapter);
        recipientCountrySpinner.setAdapter(countryAdapter);
        payerCountrySpinner.setAdapter(countryAdapter);
        recipientAddressBookSpinner.setAdapter(addressBookAdapter);
        payerAddressBookSpinner.setAdapter(addressBookAdapter);
        packagingTypeSpinner.setAdapter(packagingTypeAdapter);
        itemRecyclerView.setAdapter(consignmentAdapter);
        tariffPriceRecyclerView.setAdapter(packagingAndPriceAdapter);

        /* if edit current invoice */
        if (createInvoiceViewModel.getInvoiceId() > 0) {
            createInvoiceBtn.setText(R.string.update_invoice);
        }
        else {
            createInvoiceBtn.setText(R.string.create_invoice);
        }
        if (createInvoiceViewModel.getProviderId() > 0) {
            if (createInvoiceViewModel.getProviderId() == 4) {
                fedexRadioBtn.setChecked(true);
            }
            else if (createInvoiceViewModel.getProviderId() == 5) {
                tntRadioBtn.setChecked(true);
            }
            else if (createInvoiceViewModel.getProviderId() == 6) {
                cargostarRadioBtn.setChecked(true);
            }
            cargostarCardView.setEnabled(false);
            tntCardView.setEnabled(false);
            fedexCardView.setEnabled(false);
            cargostarRadioBtn.setEnabled(false);
            tntRadioBtn.setEnabled(false);
            fedexRadioBtn.setEnabled(false);
            cargostarImageView.setEnabled(false);
            tntImageView.setEnabled(false);
            fedexImageView.setEnabled(false);
        }
        if (createInvoiceViewModel.getType() > 0) {
            docTypeRadioBtn.setEnabled(false);
            boxTypeRadioBtn.setEnabled(false);
        }
        if (createInvoiceViewModel.getPackagingId() > 0) {
            packagingTypeSpinner.setEnabled(false);
        }
        if (createInvoiceViewModel.getPaymentMethod() > 0) {
            switch (createInvoiceViewModel.getPaymentMethod()) {
                case 1:
                    onlineRadioBtn.setChecked(true);
                    break;
                case 2:
                    cashRadioBtn.setChecked(true);
                    break;
                case 3:
                    terminalRadioBtn.setChecked(true);
                    break;
                case 4:
                    transferRadioBtn.setChecked(true);
                    break;
                case 5:
                    corporateRadioBtn.setChecked(true);
                    break;
            }
            cashRadioBtn.setEnabled(false);
            corporateRadioBtn.setEnabled(false);
            transferRadioBtn.setEnabled(false);
            onlineRadioBtn.setEnabled(false);
            terminalRadioBtn.setEnabled(false);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        senderEmailEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderEmailEditText));
        senderSignatureEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderSignatureEditText));
        senderFirstNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderFirstNameEditText));
        senderLastNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderLastNameEditText));
        senderMiddleNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderMiddleNameEditText));
        senderPhoneEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderPhoneEditText));
        senderAddressEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderAddressEditText));
        senderCityNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderCityNameEditText));
        senderZipEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderZipEditText));
        senderCompanyEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderCompanyEditText));
        senderTntEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderTntEditText));
        senderFedexEditText.addTextChangedListener(UiUtils.getOnTextChanged(senderFedexEditText));

        recipientEmailEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientEmailEditText));
        recipientFirstNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientFirstNameEditText));
        recipientLastNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientLastNameEditText));
        recipientMiddleNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientMiddleNameEditText));
        recipientCityNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientCityNameEditText));
        recipientAddressEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientAddressEditText));
        recipientZipEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientZipEditText));
        recipientPhoneEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientPhoneEditText));
        recipientCargoEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientCargoEditText));
        recipientTntEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientTntEditText));
        recipientFedexEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientFedexEditText));
        recipientCompanyEditText.addTextChangedListener(UiUtils.getOnTextChanged(recipientCompanyEditText));

        payerEmailEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerEmailEditText));
        payerFirstNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerFirstNameEditText));
        payerLastNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerLastNameEditText));
        payerMiddleNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerMiddleNameEditText));
        payerAddressEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerAddressEditText));
        payerZipEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerZipEditText));
        payerCityNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerCityNameEditText));
        payerPhoneEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerPhoneEditText));
        discountEditText.addTextChangedListener(UiUtils.getOnTextChanged(discountEditText));
        payerCargoEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerCargoEditText));
        payerTntEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerTntEditText));
        payerTntTaxIdEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerTntTaxIdEditText));
        payerFedexEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerFedexEditText));
        payerFedexTaxIdEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerFedexTaxIdEditText));
        payerInnEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerInnEditText));
        payerCompanyEditText.addTextChangedListener(UiUtils.getOnTextChanged(payerCompanyEditText));
        contractNumberEditText.addTextChangedListener(UiUtils.getOnTextChanged(contractNumberEditText));
        instructionsEditText.addTextChangedListener(UiUtils.getOnTextChanged(instructionsEditText));

        weightEditText.addTextChangedListener(UiUtils.getOnTextChanged(weightEditText));
        lengthEditText.addTextChangedListener(UiUtils.getOnTextChanged(lengthEditText));
        widthEditText.addTextChangedListener(UiUtils.getOnTextChanged(widthEditText));
        heightEditText.addTextChangedListener(UiUtils.getOnTextChanged(heightEditText));
        cargoNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(cargoNameEditText));
        cargoDescriptionEditText.addTextChangedListener(UiUtils.getOnTextChanged(cargoDescriptionEditText));
        cargoPriceEditText.addTextChangedListener(UiUtils.getOnTextChanged(cargoPriceEditText));

        /* header navigation */
        profileImageView.setOnClickListener(v -> UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.mainFragment));
        createUserImageView.setOnClickListener(v -> UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.createUserFragment));
        notificationsImageView.setOnClickListener(v -> UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.notificationsFragment));
        calculatorImageView.setOnClickListener(v -> UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.calculatorFragment));
        editImageView.setOnClickListener(v -> UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.profileFragment));

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
            createInvoiceViewModel.searchRequest(Long.parseLong(requestId));
        });

        senderSignatureImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(requireContext(), SignatureActivity.class).putExtra(Constants.ADDRESSEE_IS_ACCEPTED, true), IntentConstants.REQUEST_SENDER_SIGNATURE);
        });

        cargostarCardView.setOnClickListener(v -> cargostarRadioBtn.setChecked(true));
        tntCardView.setOnClickListener(v -> tntRadioBtn.setChecked(true));
        fedexCardView.setOnClickListener(v -> fedexRadioBtn.setChecked(true));

        cargostarRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                tntRadioBtn.setChecked(false);
                fedexRadioBtn.setChecked(false);
                createInvoiceViewModel.setSelectedProviderId(6L);
            }
        });

        tntRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cargostarRadioBtn.setChecked(false);
                fedexRadioBtn.setChecked(false);
                createInvoiceViewModel.setSelectedProviderId(5L);
            }
        });

        fedexRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cargostarRadioBtn.setChecked(false);
                tntRadioBtn.setChecked(false);
                createInvoiceViewModel.setSelectedProviderId(4L);
            }
        });

        packageTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == docTypeRadioBtn.getId()) {
                //docs
                createInvoiceViewModel.setSelectedType(1);
                //make consignment fields invisible
                lengthTextView.setVisibility(View.INVISIBLE);
                widthTextView.setVisibility(View.INVISIBLE);
                heightTextView.setVisibility(View.INVISIBLE);
                lengthEditText.setVisibility(View.INVISIBLE);
                widthEditText.setVisibility(View.INVISIBLE);
                heightEditText.setVisibility(View.INVISIBLE);
            }
            else if (checkedId == boxTypeRadioBtn.getId()) {
                //boxes
                createInvoiceViewModel.setSelectedType(2);
                //make consignment fields visible
                lengthTextView.setVisibility(View.VISIBLE);
                widthTextView.setVisibility(View.VISIBLE);
                heightTextView.setVisibility(View.VISIBLE);
                lengthEditText.setVisibility(View.VISIBLE);
                widthEditText.setVisibility(View.VISIBLE);
                heightEditText.setVisibility(View.VISIBLE);
            }
            else {
                createInvoiceViewModel.setSelectedType(0);
            }
        });

        packagingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                createInvoiceViewModel.setSelectedPackagingType((PackagingType) adapterView.getSelectedItem());
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

        senderCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                createInvoiceViewModel.setSrcCountry((Country) adapterView.getSelectedItem());
                createInvoiceViewModel.setDestCountry((Country) recipientCountrySpinner.getSelectedItem());

                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        senderCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recipientCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                createInvoiceViewModel.setSrcCountry((Country) senderCountrySpinner.getSelectedItem());
                createInvoiceViewModel.setDestCountry((Country) adapterView.getSelectedItem());

                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        recipientCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payerCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        payerCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final Country payerCountry = (Country) parent.getSelectedItem();
                createInvoiceViewModel.setPayerCountry(payerCountry);

                if (payerCountry.getId() != 191 && recipientIsPayerRadioBtn.isChecked()) {
                    paymentMethodRadioGroup.setVisibility(View.GONE);
                    onlineRadioBtn.setVisibility(View.GONE);
                    cashRadioBtn.setVisibility(View.GONE);
                    terminalRadioBtn.setVisibility(View.GONE);
                    transferRadioBtn.setVisibility(View.GONE);
                    corporateRadioBtn.setVisibility(View.GONE);
                }
                else {
                    paymentMethodRadioGroup.setVisibility(View.VISIBLE);
                    onlineRadioBtn.setVisibility(View.VISIBLE);
                    cashRadioBtn.setVisibility(View.VISIBLE);
                    terminalRadioBtn.setVisibility(View.VISIBLE);
                    transferRadioBtn.setVisibility(View.VISIBLE);
                    corporateRadioBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recipientAddressBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        recipientAddressBookSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                createInvoiceViewModel.updateRecipientData((AddressBook) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payerAddressBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        payerAddressBookSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                createInvoiceViewModel.updatePayerData((AddressBook) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextWatcher senderEmailWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() >= 10) {
                    createInvoiceViewModel.setSenderEmail(s.toString());
                }
            }
        };
        senderEmailEditText.addTextChangedListener(senderEmailWatcher);

        payerIsSomeoneRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == senderIsPayerRadioBtn.getId()) {
                final Country country = (Country) senderCountrySpinner.getSelectedItem();

                if (country == null) {
                    Log.e(TAG, "senderCountry is null");
                    return;
                }
                setPayerUiFields(new AddressBook(
                        1, 0,
                        country.getId(),
                        senderCityNameEditText.getText().toString().trim(),
                        senderAddressEditText.getText().toString().trim(),
                        senderZipEditText.getText().toString().trim(),
                        senderFirstNameEditText.getText().toString().trim(),
                        senderMiddleNameEditText.getText().toString().trim(),
                        senderLastNameEditText.getText().toString().trim(),
                        senderEmailEditText.getText().toString().trim(),
                        senderPhoneEditText.getText().toString().trim(),
                        null,
                        senderTntEditText.getText().toString().trim(),
                        senderFedexEditText.getText().toString().trim(),
                        senderCompanyEditText.getText().toString().trim(),null, null, null, 0));

                if (country.getId() != 191) {
                    paymentMethodRadioGroup.setVisibility(View.GONE);
                    onlineRadioBtn.setVisibility(View.GONE);
                    cashRadioBtn.setVisibility(View.GONE);
                    terminalRadioBtn.setVisibility(View.GONE);
                    transferRadioBtn.setVisibility(View.GONE);
                    corporateRadioBtn.setVisibility(View.GONE);
                }
                else {
                    paymentMethodRadioGroup.setVisibility(View.VISIBLE);
                    onlineRadioBtn.setVisibility(View.VISIBLE);
                    cashRadioBtn.setVisibility(View.VISIBLE);
                    terminalRadioBtn.setVisibility(View.VISIBLE);
                    transferRadioBtn.setVisibility(View.VISIBLE);
                    corporateRadioBtn.setVisibility(View.VISIBLE);
                }
                return;
            }
            if (checkedId == recipientIsPayerRadioBtn.getId()) {
                final Country country = (Country) recipientCountrySpinner.getSelectedItem();

                if (country == null) {
                    Log.e(TAG, "recipientCountry is null");
                    return;
                }
                setPayerUiFields(new AddressBook(
                        1, 0,
                        country.getId(),
                        recipientCityNameEditText.getText().toString().trim(),
                        recipientAddressEditText.getText().toString().trim(),
                        recipientZipEditText.getText().toString().trim(),
                        recipientFirstNameEditText.getText().toString().trim(),
                        recipientMiddleNameEditText.getText().toString().trim(),
                        recipientLastNameEditText.getText().toString().trim(),
                        recipientEmailEditText.getText().toString().trim(),
                        recipientPhoneEditText.getText().toString().trim(),
                        recipientCargoEditText.getText().toString().trim(),
                        recipientTntEditText.getText().toString().trim(),
                        recipientFedexEditText.getText().toString().trim(),
                        recipientCompanyEditText.getText().toString().trim(), null, null, null, 0));
            }
        });

        paymentMethodRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == onlineRadioBtn.getId()) {
                createInvoiceViewModel.setPaymentMethod(1);
                return;
            }
            if (checkedId == cashRadioBtn.getId()) {
                createInvoiceViewModel.setPaymentMethod(2);
                return;
            }
            if (checkedId == terminalRadioBtn.getId()) {
                createInvoiceViewModel.setPaymentMethod(3);
                return;
            }
            if (checkedId == transferRadioBtn.getId()) {
                createInvoiceViewModel.setPaymentMethod(4);
                return;
            }
            if (checkedId == corporateRadioBtn.getId()) {
                createInvoiceViewModel.setPaymentMethod(5);
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
            createInvoiceViewModel.addConsignment(
                    packagingType.getId(),
                    !TextUtils.isEmpty(length) ? Double.parseDouble(length) : 0,
                    !TextUtils.isEmpty(width) ? Double.parseDouble(width) : 0,
                    !TextUtils.isEmpty(height) ? Double.parseDouble(height) : 0,
                    !TextUtils.isEmpty(weight) ? Double.parseDouble(weight) : 0);
        });

        calculateBtn.setOnClickListener(v -> createInvoiceViewModel.calculateTotalPrice());

        createInvoiceBtn.setOnClickListener(v -> {
            final String senderEmail = senderEmailEditText.getText().toString().trim();
            final String senderSignature = senderSignatureEditText.getText().toString().trim();
            final String senderFirstName = senderFirstNameEditText.getText().toString().trim();
            final String senderLastName = senderLastNameEditText.getText().toString().trim();
            final String senderMiddleName = senderMiddleNameEditText.getText().toString().trim();
            final String senderPhone = senderPhoneEditText.getText().toString().trim();
            final String senderAddress = senderAddressEditText.getText().toString().trim();
            final String senderCityName = senderCityNameEditText.getText().toString().trim();
            final String senderZip = senderZipEditText.getText().toString().trim();
            final String senderCompanyName = senderCompanyEditText.getText().toString().trim();
            final String senderTnt = senderTntEditText.getText().toString().trim();
            final String senderFedex = senderFedexEditText.getText().toString().trim();

            final String recipientEmail = recipientEmailEditText.getText().toString().trim();
            final String recipientFirstName = recipientFirstNameEditText.getText().toString().trim();
            final String recipientLastName = recipientLastNameEditText.getText().toString().trim();
            final String recipientMiddleName = recipientMiddleNameEditText.getText().toString().trim();
            final String recipientAddress = recipientAddressEditText.getText().toString().trim();
            final String recipientCityName = recipientCityNameEditText.getText().toString().trim();
            final String recipientZip = recipientZipEditText.getText().toString().trim();
            final String recipientPhone = recipientPhoneEditText.getText().toString().trim();
            final String recipientCargo = recipientCargoEditText.getText().toString().trim();
            final String recipientTnt = recipientTntEditText.getText().toString().trim();
            final String recipientFedex = recipientFedexEditText.getText().toString().trim();
            final String recipientCompanyName = recipientCompanyEditText.getText().toString().trim();

            final String payerEmail = payerEmailEditText.getText().toString().trim();
            final String payerFirstName = payerFirstNameEditText.getText().toString().trim();
            final String payerLastName = payerLastNameEditText.getText().toString().trim();
            final String payerMiddleName = payerMiddleNameEditText.getText().toString().trim();
            final String payerAddress = payerAddressEditText.getText().toString().trim();
            final String payerCityName = payerCityNameEditText.getText().toString().trim();
            final String payerZip = payerZipEditText.getText().toString().trim();
            final String payerPhone = payerPhoneEditText.getText().toString().trim();
            final String discount = discountEditText.getText().toString().trim();

            final String payerCargo = payerCargoEditText.getText().toString().trim();
            final String payerTnt = payerTntEditText.getText().toString().trim();
            final String payerTntTax = payerTntTaxIdEditText.getText().toString().trim();
            final String payerFedex = payerFedexEditText.getText().toString().trim();
            final String payerFedexTax = payerFedexTaxIdEditText.getText().toString().trim();
            final String payerInn = payerInnEditText.getText().toString().trim();
            final String payerCompany = payerCompanyEditText.getText().toString().trim();
            final String contractNumber = contractNumberEditText.getText().toString().trim();

            final String instructions = instructionsEditText.getText().toString().trim();

            if (packageTypeRadioGroup.getCheckedRadioButtonId() != docTypeRadioBtn.getId()
                    && packageTypeRadioGroup.getCheckedRadioButtonId() != boxTypeRadioBtn.getId()) {
                Toast.makeText(requireContext(), "Для создания заявки выберите тип посылки (Документы/Коробка)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(senderEmail)) {
                Log.e(TAG, "sendInvoice(): senderEmail is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите email отправителя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(senderSignature)) {
                Log.e(TAG, "sendInvoice(): senderSignature is empty");
                Toast.makeText(requireContext(), "Для создания заявки добавьте подпись отправителя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(senderFirstName)) {
                Log.e(TAG, "senderFirstName empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите имя отправителя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(senderLastName)) {
                Log.e(TAG, "senderLastName empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите фамилию отправителя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(senderPhone)) {
                Log.e(TAG, "sendInvoice(): senderPhone is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите номер телефона отправителя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(senderAddress)) {
                Log.e(TAG, "senderAddress empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите адрес отправителя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(senderZip)) {
                Log.e(TAG, "senderZip empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите почтовый индекс отправителя", Toast.LENGTH_SHORT).show();
                return;
            }
            //recipient data
            if (TextUtils.isEmpty(recipientFirstName)) {
                Log.e(TAG, "sendInvoice(): recipientFirstName is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите имя получателя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(recipientLastName)) {
                Log.e(TAG, "sendInvoice(): recipientLastName is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите фамилию получателя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(recipientPhone)) {
                Log.e(TAG, "sendInvoice(): recipientPhone is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите номер телефона получателя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(recipientAddress)) {
                Log.e(TAG, "sendInvoice(): recipientAddress is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите адрес получателя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(recipientZip)) {
                Log.e(TAG, "sendInvoice(): recipientZip is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите почтовый индекс получателя", Toast.LENGTH_SHORT).show();
                return;
            }

            //payer data
            if (TextUtils.isEmpty(payerFirstName)) {
                Log.e(TAG, "sendInvoice(): payerFirstName is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите имя плательщика", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(payerLastName)) {
                Log.e(TAG, "sendInvoice(): payerLastName is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите фамилию плательщика", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(payerPhone)) {
                Log.e(TAG, "sendInvoice(): payerPhone is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите номер телефона плательщика", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(payerAddress)) {
                Log.e(TAG, "sendInvoice(): payerAddress is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите адрес плательщика", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(payerZip)) {
                Log.e(TAG, "sendInvoice(): payerZip is empty");
                Toast.makeText(requireContext(), "Для создания заявки укажите почтовый индекс плательщика", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(payerCompany)) {
                if (TextUtils.isEmpty(payerInn)) {
                    Log.e(TAG, "sendInvoice(): payerInn is empty");
                    Toast.makeText(requireContext(), "Для создания заявки укажите ИНН плательщика", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contractNumber)) {
                    Log.e(TAG, "sendInvoice(): contractNumber is empty");
                    Toast.makeText(requireContext(), "Для создания заявки укажите номер контракта плательщика", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (createInvoiceViewModel.getPackagingId() <= 0 && packagingAndPriceAdapter.getSelectedPackagingId() <= 0) {
                Toast.makeText(requireContext(), "Выберите тариф", Toast.LENGTH_SHORT).show();
                return;
            }
            if (createInvoiceViewModel.getTotalPrice() <= 0 && packagingAndPriceAdapter.getSelectedPrice() <= 0) {
                Toast.makeText(requireContext(), "Выберите тариф", Toast.LENGTH_SHORT).show();
                return;
            }
            if (createInvoiceViewModel.getPaymentMethod() <=0 && (paymentMethodRadioGroup.getCheckedRadioButtonId() != onlineRadioBtn.getId()
                    && paymentMethodRadioGroup.getCheckedRadioButtonId() != cashRadioBtn.getId()
                    && paymentMethodRadioGroup.getCheckedRadioButtonId() != terminalRadioBtn.getId()
                    && paymentMethodRadioGroup.getCheckedRadioButtonId() != transferRadioBtn.getId()
                    && paymentMethodRadioGroup.getCheckedRadioButtonId() != corporateRadioBtn.getId())) {
                Toast.makeText(requireContext(), "Метод оплаты не выбран", Toast.LENGTH_SHORT).show();
                return;
            }
            createInvoiceViewModel.createInvoice(
                    senderFirstName,
                    senderMiddleName,
                    senderLastName,
                    senderEmail,
                    senderPhone,
                    ((Country) senderCountrySpinner.getSelectedItem()).getId(),
                    senderCityName,
                    senderAddress,
                    senderZip,
                    senderTnt,
                    senderFedex,
                    senderCompanyName,
                    senderSignature,
                    recipientFirstName,
                    recipientMiddleName,
                    recipientLastName,
                    recipientEmail,
                    recipientPhone,
                    ((Country) recipientCountrySpinner.getSelectedItem()).getId(),
                    recipientCityName,
                    recipientAddress,
                    recipientZip,
                    recipientCargo,
                    recipientTnt,
                    recipientFedex,
                    recipientCompanyName,
                    payerFirstName,
                    payerMiddleName,
                    payerLastName,
                    payerEmail,
                    payerPhone,
                    ((Country) payerCountrySpinner.getSelectedItem()).getId(),
                    payerCityName,
                    payerAddress,
                    payerZip,
                    payerCargo,
                    payerTnt,
                    payerFedex,
                    payerTntTax,
                    payerFedexTax,
                    payerCompany,
                    TextUtils.isEmpty(discount) ? 0.0 : Double.parseDouble(discount),
                    payerInn,
                    contractNumber,
                    instructions);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final boolean[] activityCreated = {true, true, true};

        /* header */
        createInvoiceViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        createInvoiceViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        createInvoiceViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        createInvoiceViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        /* content view data */
        createInvoiceViewModel.getSelectedProvider().observe(getViewLifecycleOwner(), provider -> {
            createInvoiceViewModel.setSelectedProvider(provider);
        });

        createInvoiceViewModel.getProviderIdList().observe(getViewLifecycleOwner(), providerList -> {
            if (providerList == null || providerList.isEmpty()) {
                //other -> other = provider radio group is empty
                cargostarRadioBtn.setVisibility(View.INVISIBLE);
                cargostarImageView.setVisibility(View.INVISIBLE);
                cargostarCardView.setVisibility(View.INVISIBLE);

                tntRadioBtn.setVisibility(View.INVISIBLE);
                tntImageView.setVisibility(View.INVISIBLE);
                tntCardView.setVisibility(View.INVISIBLE);

                fedexRadioBtn.setVisibility(View.INVISIBLE);
                fedexImageView.setVisibility(View.INVISIBLE);
                fedexCardView.setVisibility(View.INVISIBLE);
                return;
            }
            if (providerList.size() == 1) {
                final long providerId = providerList.get(0);
                if (providerId <= 0) {
                    return;
                }
                if (providerId == 6L) {
                    cargostarRadioBtn.setVisibility(View.VISIBLE);
                    cargostarImageView.setVisibility(View.VISIBLE);
                    cargostarCardView.setVisibility(View.VISIBLE);

                    tntRadioBtn.setVisibility(View.GONE);
                    tntImageView.setVisibility(View.GONE);
                    tntCardView.setVisibility(View.GONE);

                    fedexRadioBtn.setVisibility(View.GONE);
                    fedexImageView.setVisibility(View.GONE);
                    fedexCardView.setVisibility(View.GONE);
                    return;
                }
                if (providerId == 5L) {
                    cargostarRadioBtn.setVisibility(View.GONE);
                    cargostarImageView.setVisibility(View.GONE);
                    cargostarCardView.setVisibility(View.GONE);

                    tntRadioBtn.setVisibility(View.VISIBLE);
                    tntImageView.setVisibility(View.VISIBLE);
                    tntCardView.setVisibility(View.VISIBLE);

                    fedexRadioBtn.setVisibility(View.GONE);
                    fedexImageView.setVisibility(View.GONE);
                    fedexCardView.setVisibility(View.GONE);
                }
                return;
            }
            if (providerList.size() == 2) {
                cargostarRadioBtn.setVisibility(View.GONE);
                cargostarImageView.setVisibility(View.GONE);
                cargostarCardView.setVisibility(View.GONE);

                tntRadioBtn.setVisibility(View.VISIBLE);
                tntImageView.setVisibility(View.VISIBLE);
                tntCardView.setVisibility(View.VISIBLE);

                fedexRadioBtn.setVisibility(View.VISIBLE);
                fedexImageView.setVisibility(View.VISIBLE);
                fedexCardView.setVisibility(View.VISIBLE);
            }
        });

        createInvoiceViewModel.getPackagingTypeList().observe(getViewLifecycleOwner(), packagingTypeList -> {
            packagingTypeAdapter.clear();
            packagingTypeAdapter.addAll(packagingTypeList);
        });

        createInvoiceViewModel.getConsignmentList().observe(getViewLifecycleOwner(), consignmentList -> {
            consignmentAdapter.setItemList(consignmentList);
            consignmentAdapter.notifyDataSetChanged();
        });

        createInvoiceViewModel.getReceivedConsignmentList().observe(getViewLifecycleOwner(), consignmentList -> {
            createInvoiceViewModel.setCurrentConsignmentList(consignmentList);
            consignmentAdapter.setItemList(consignmentList);
            consignmentAdapter.notifyDataSetChanged();
        });

        createInvoiceViewModel.getCalculationResult().observe(getViewLifecycleOwner(), calculationResult -> {
            totalQuantityTextView.setText(String.valueOf(calculationResult.getTotalQuantity()));
            totalWeightTextView.setText(String.valueOf(new BigDecimal(Double.toString(calculationResult.getTotalVolume())).setScale(2, RoundingMode.HALF_UP).doubleValue()));
            totalDimensionsTextView.setText(String.valueOf(new BigDecimal(Double.toString(calculationResult.getTotalWeight())).setScale(2, RoundingMode.HALF_UP).doubleValue()));
        });

        createInvoiceViewModel.getPackagingAndPriceList().observe(getViewLifecycleOwner(), packagingAndPrices -> {
            packagingAndPriceAdapter.setResultList(packagingAndPrices);
        });

        createInvoiceViewModel.getPackagingList().observe(getViewLifecycleOwner(), packagingList -> {
            createInvoiceViewModel.setSelectedPackagingList(packagingList);
        });

        createInvoiceViewModel.getZoneSettingsList().observe(getViewLifecycleOwner(), zoneSettingsList -> {
            Log.i(TAG, "zoneSettingsList: " + zoneSettingsList);
            createInvoiceViewModel.setSelectedZoneSettingsList(zoneSettingsList);
        });

        createInvoiceViewModel.getVat().observe(getViewLifecycleOwner(), vat -> createInvoiceViewModel.setSelectedVat(vat));

        createInvoiceViewModel.getCountryList().observe(getViewLifecycleOwner(), countries -> {
            if (countries != null) {
                countryAdapter.addAll(countries);

                if (createInvoiceViewModel.getAction() == CreateInvoiceViewModel.CREATE_INVOICE_ACTION_DEFAULT && activityCreated[0]) {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                            activityCreated[0] = false;

                            senderCountrySpinner.setSelection(i);
                            recipientCountrySpinner.setSelection(i);
                            payerCountrySpinner.setSelection(i);
                            return;
                        }
                    }
                }
                if (createInvoiceViewModel.getInvoiceId() <= 0) {
                    for (int i = 0; i < countryAdapter.getCount(); i++) {
                        if (createInvoiceViewModel.getRecipientCountryId() == countryAdapter.getItemList().get(i).getId()) {
                            recipientCountrySpinner.setSelection(i);
                            return;
                        }
                    }
                }
            }
        });

        createInvoiceViewModel.getAddressBook().observe(getViewLifecycleOwner(), addressBook -> {
            addressBookAdapter.clear();
            if (addressBook != null) {
                final List<AddressBook> list = new ArrayList<>(addressBook);
                list.add(0, new AddressBook(
                        0, 0, 0, null, getString(R.string.address), null,
                        getString(R.string.first_name), null, getString(R.string.last_name), null, null,
                        null, null, null,
                        null, null, null, null, 0));
                addressBookAdapter.addAll(list);
            }
        });

        createInvoiceViewModel.getCurrentRequest().observe(getViewLifecycleOwner(), request -> {
            if (request != null) {
                if (request.getProviderId() == 4) {
                    cargostarRadioBtn.setChecked(true);
                }
                else if (request.getProviderId() == 5) {
                    tntRadioBtn.setChecked(true);
                }
                else if (request.getProviderId() == 6) {
                    fedexRadioBtn.setChecked(true);
                }
                if (request.getDeliveryType() == 1) {
                    docTypeRadioBtn.setChecked(true);
                }
                else if (request.getDeliveryType() == 2) {
                    boxTypeRadioBtn.setChecked(true);
                }
                instructionsEditText.setText(!TextUtils.isEmpty(request.getComment()) ? request.getComment() : null);
            }
        });

        createInvoiceViewModel.getSenderData().observe(getViewLifecycleOwner(), sender -> {
            if (sender != null) {
                senderEmailEditText.setText(!TextUtils.isEmpty(sender.getEmail()) ? sender.getEmail() : null);
                senderFirstNameEditText.setText(!TextUtils.isEmpty(sender.getFirstName()) ? sender.getFirstName() : null);
                senderLastNameEditText.setText(!TextUtils.isEmpty(sender.getLastName()) ? sender.getLastName() : null);
                senderMiddleNameEditText.setText(!TextUtils.isEmpty(sender.getMiddleName()) ? sender.getMiddleName() : null);
                senderAddressEditText.setText(!TextUtils.isEmpty(sender.getAddress()) ? sender.getAddress() : null);
                senderZipEditText.setText(!TextUtils.isEmpty(sender.getZip()) ? sender.getZip() : null);
                senderPhoneEditText.setText(!TextUtils.isEmpty(sender.getPhone()) ? sender.getPhone() : null);
                discountEditText.setText(sender.getDiscount() > 0 ? String.valueOf(sender.getDiscount()) : null);
                senderTntEditText.setText(!TextUtils.isEmpty(sender.getTntAccountNumber()) ? sender.getTntAccountNumber() : null);
                senderFedexEditText.setText(!TextUtils.isEmpty(sender.getFedexAccountNumber()) ? sender.getFedexAccountNumber() : null);
                senderCompanyEditText.setText(!TextUtils.isEmpty(sender.getCompany()) ? sender.getCompany() : null);
                senderCityNameEditText.setText(!TextUtils.isEmpty(sender.getCityName()) ? sender.getCityName() : null);
                senderSignatureEditText.setText(!TextUtils.isEmpty(sender.getSignatureUrl()) ? sender.getSignatureUrl() : null);

                if (!TextUtils.isEmpty(sender.getSignatureUrl())) {
                    senderSignatureResultImageView.setImageResource(R.drawable.ic_image_green);
                }
                else {
                    senderSignatureResultImageView.setImageResource(R.drawable.ic_image_red);
                }
                senderSignatureResultImageView.setVisibility(View.VISIBLE);

                for (int i = 0; i < countryAdapter.getCount(); i++) {
                    if (countryAdapter.getItemList().get(i).getId() == sender.getCountryId()) {
                        senderCountrySpinner.setSelection(i);
                        return;
                    }
                }
            }
        });

        createInvoiceViewModel.getClientDataFromAddressBook().observe(getViewLifecycleOwner(), sender -> {
            if (sender != null) {
                senderFirstNameEditText.setText(!TextUtils.isEmpty(sender.getFirstName()) ? sender.getFirstName() : null);
                senderLastNameEditText.setText(!TextUtils.isEmpty(sender.getLastName()) ? sender.getLastName() : null);
                senderMiddleNameEditText.setText(!TextUtils.isEmpty(sender.getMiddleName()) ? sender.getMiddleName() : null);
                senderAddressEditText.setText(!TextUtils.isEmpty(sender.getAddress()) ? sender.getAddress() : null);
                senderZipEditText.setText(!TextUtils.isEmpty(sender.getZip()) ? sender.getZip() : null);
                senderPhoneEditText.setText(!TextUtils.isEmpty(sender.getPhone()) ? sender.getPhone() : null);
                discountEditText.setText(sender.getDiscount() > 0 ? String.valueOf(sender.getDiscount()) : null);
                senderTntEditText.setText(!TextUtils.isEmpty(sender.getTntAccountNumber()) ? sender.getTntAccountNumber() : null);
                senderFedexEditText.setText(!TextUtils.isEmpty(sender.getFedexAccountNumber()) ? sender.getFedexAccountNumber() : null);
                senderCompanyEditText.setText(!TextUtils.isEmpty(sender.getCompany()) ? sender.getCompany() : null);
                senderCityNameEditText.setText(!TextUtils.isEmpty(sender.getCityName()) ? sender.getCityName() : null);
                senderSignatureEditText.setText(!TextUtils.isEmpty(sender.getSignatureUrl()) ? sender.getSignatureUrl() : null);

                if (!TextUtils.isEmpty(sender.getSignatureUrl())) {
                    senderSignatureResultImageView.setImageResource(R.drawable.ic_image_green);
                }
                else {
                    senderSignatureResultImageView.setImageResource(R.drawable.ic_image_red);
                }
                senderSignatureResultImageView.setVisibility(View.VISIBLE);

                for (int i = 0; i < countryAdapter.getCount(); i++) {
                    if (countryAdapter.getItemList().get(i).getId() == sender.getCountryId()) {
                        senderCountrySpinner.setSelection(i);
                        return;
                    }
                }
            }
        });

        createInvoiceViewModel.getRecipientData().observe(getViewLifecycleOwner(), this::setRecipientUiFields);

        createInvoiceViewModel.getPayerData().observe(getViewLifecycleOwner(), this::setPayerUiFields);

        createInvoiceViewModel.getRecipientAddressBook().observe(getViewLifecycleOwner(), this::setRecipientUiFields);

        createInvoiceViewModel.getPayerAddressBook().observe(getViewLifecycleOwner(), this::setPayerUiFields);

        createInvoiceViewModel.getCreateInvoiceResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                createInvoiceBtn.setEnabled(true);
                createInvoiceBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                NavHostFragment.findNavController(this).navigate(R.id.action_createInvoiceFragment_to_mainFragment);
                return;
            }
            if (workInfo.getState() == WorkInfo.State.CANCELLED || workInfo.getState() == WorkInfo.State.FAILED) {
                createInvoiceBtn.setEnabled(true);
                createInvoiceBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                Log.e(TAG, "createInvoice(): ");
                Toast.makeText(requireContext(), "Произошла ошибка при создании накладной", Toast.LENGTH_SHORT).show();
                return;
            }
            if (workInfo.getState() == WorkInfo.State.BLOCKED || workInfo.getState() == WorkInfo.State.ENQUEUED) {
                Toast.makeText(requireContext(), "Кэширование накладной...", Toast.LENGTH_SHORT).show();
                createInvoiceBtn.setEnabled(true);
                createInvoiceBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            if (workInfo.getState() == WorkInfo.State.RUNNING) {
                createInvoiceBtn.setEnabled(false);
                createInvoiceBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDeleteItemClicked(final int position) {
        createInvoiceViewModel.removeConsignment(position);
    }

    @Override
    public void onScanItemClicked(int position) {
        ScanQrDialog dialogFragment = ScanQrDialog.newInstance(position, IntentConstants.REQUEST_SCAN_QR_CARGO);
        dialogFragment.setTargetFragment(CreateInvoiceFragment.this, IntentConstants.REQUEST_SCAN_QR_CARGO);
        dialogFragment.show(getParentFragmentManager().beginTransaction(), TAG);
    }

    private void setRecipientUiFields(final AddressBook recipient) {
        if (recipient != null && recipient.getId() > 0) {
            recipientEmailEditText.setText(!TextUtils.isEmpty(recipient.getEmail()) ? recipient.getEmail() : null);
            recipientFirstNameEditText.setText(!TextUtils.isEmpty(recipient.getFirstName()) ? recipient.getFirstName() : null);
            recipientLastNameEditText.setText(!TextUtils.isEmpty(recipient.getLastName()) ? recipient.getLastName() : null);
            recipientMiddleNameEditText.setText(!TextUtils.isEmpty(recipient.getMiddleName()) ? recipient.getMiddleName() : null);
            recipientAddressEditText.setText(!TextUtils.isEmpty(recipient.getAddress()) ? recipient.getAddress() : null);
            recipientZipEditText.setText(!TextUtils.isEmpty(recipient.getZip()) ? recipient.getZip() : null);
            recipientPhoneEditText.setText(!TextUtils.isEmpty(recipient.getPhone()) ? recipient.getPhone() : null);
            recipientCargoEditText.setText(!TextUtils.isEmpty(recipient.getCargostarAccountNumber()) ? recipient.getCargostarAccountNumber() : null);
            recipientTntEditText.setText(!TextUtils.isEmpty(recipient.getTntAccountNumber()) ? recipient.getTntAccountNumber() : null);
            recipientFedexEditText.setText(!TextUtils.isEmpty(recipient.getFedexAccountNumber()) ? recipient.getFedexAccountNumber() : null);
            recipientCompanyEditText.setText(!TextUtils.isEmpty(recipient.getCompany()) ? recipient.getCompany() : null);
            recipientCityNameEditText.setText(!TextUtils.isEmpty(recipient.getCityName()) ? recipient.getCityName() : null);

            for (int i = 0; i < countryAdapter.getCount(); i++) {
                if (countryAdapter.getItemList().get(i).getId() == recipient.getCountryId()) {
                    recipientCountrySpinner.setSelection(i);
                    return;
                }
            }
        }
        else {
            recipientEmailEditText.setText(null);
            recipientFirstNameEditText.setText(null);
            recipientLastNameEditText.setText(null);
            recipientMiddleNameEditText.setText(null);
            recipientAddressEditText.setText(null);
            recipientZipEditText.setText(null);
            recipientPhoneEditText.setText(null);
            recipientCargoEditText.setText(null);
            recipientTntEditText.setText(null);
            recipientFedexEditText.setText(null);
            recipientCompanyEditText.setText(null);
            recipientCityNameEditText.setText(null);

            for (int i = 0; i < countryAdapter.getCount(); i++) {
                if (countryAdapter.getItemList().get(i).getName().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    recipientCountrySpinner.setSelection(i);
                    return;
                }
            }
        }
    }

    private void setPayerUiFields(final AddressBook payer) {
        if (payer != null && payer.getId() > 0) {
            payerEmailEditText.setText(!TextUtils.isEmpty(payer.getEmail()) ? payer.getEmail() : null);
            payerFirstNameEditText.setText(!TextUtils.isEmpty(payer.getFirstName()) ? payer.getFirstName() : null);
            payerLastNameEditText.setText(!TextUtils.isEmpty(payer.getLastName()) ? payer.getLastName() : null);
            payerMiddleNameEditText.setText(!TextUtils.isEmpty(payer.getMiddleName()) ? payer.getMiddleName() : null);
            payerAddressEditText.setText(!TextUtils.isEmpty(payer.getAddress()) ? payer.getAddress() : null);
            payerZipEditText.setText(!TextUtils.isEmpty(payer.getZip()) ? payer.getZip() : null);
            payerPhoneEditText.setText(!TextUtils.isEmpty(payer.getPhone()) ? payer.getPhone() : null);
            payerCargoEditText.setText(!TextUtils.isEmpty(payer.getCargostarAccountNumber()) ? payer.getCargostarAccountNumber() : null);
            payerTntEditText.setText(!TextUtils.isEmpty(payer.getTntAccountNumber()) ? payer.getTntAccountNumber() : null);
            payerFedexEditText.setText(!TextUtils.isEmpty(payer.getFedexAccountNumber()) ? payer.getFedexAccountNumber() : null);
            payerCompanyEditText.setText(!TextUtils.isEmpty(payer.getCompany()) ? payer.getCompany() : null);
            payerCityNameEditText.setText(!TextUtils.isEmpty(payer.getCityName()) ? payer.getCityName() : null);
            payerInnEditText.setText(!TextUtils.isEmpty(payer.getInn()) ? payer.getInn() : null);
            contractNumberEditText.setText(!TextUtils.isEmpty(payer.getContractNumber()) ? payer.getContractNumber() : null);

            for (int i = 0; i < countryAdapter.getCount(); i++) {
                if (countryAdapter.getItemList().get(i).getId() == payer.getCountryId()) {
                    payerCountrySpinner.setSelection(i);
                    return;
                }
            }
        }
        else {
            payerEmailEditText.setText(null);
            payerFirstNameEditText.setText(null);
            payerLastNameEditText.setText(null);
            payerMiddleNameEditText.setText(null);
            payerAddressEditText.setText(null);
            payerZipEditText.setText(null);
            payerPhoneEditText.setText(null);
            payerCargoEditText.setText(null);
            payerTntEditText.setText(null);
            payerFedexEditText.setText(null);
            payerCompanyEditText.setText(null);
            payerCityNameEditText.setText(null);
            payerInnEditText.setText(null);
            contractNumberEditText.setText(null);

            for (int i = 0; i < countryAdapter.getCount(); i++) {
                if (countryAdapter.getItemList().get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    payerCountrySpinner.setSelection(i);
                    return;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentConstants.REQUEST_SCAN_QR_CARGO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                final String qr = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
                final int position = data.getIntExtra(Constants.KEY_QR_POSITION, -1);

                if (position >= 0) {
                    consignmentAdapter.getItemList().get(position).setQr(qr);
                    consignmentAdapter.notifyItemChanged(position);
                }
            }
            return;
        }
        if (requestCode == IntentConstants.REQUEST_SENDER_SIGNATURE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                senderSignatureEditText.setText(null);
                senderSignatureResultImageView.setImageResource(R.drawable.ic_image_red);
                senderSignatureResultImageView.setVisibility(View.VISIBLE);
                return;
            }
            if (resultCode == Activity.RESULT_OK && data != null) {
                senderSignatureEditText.setText(data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                senderSignatureResultImageView.setImageResource(R.drawable.ic_image_green);
                senderSignatureResultImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onTariffSelected(final int position, final PackagingAndPrice item) {
        createInvoiceViewModel.setCurrentPackagingId(item.getPackaging().getId());
        createInvoiceViewModel.setTotalPrice(item.getPrice());
        packagingAndPriceAdapter.setLastCheckedPosition(position);
        packagingAndPriceAdapter.notifyDataSetChanged();
    }
    private static final String TAG = CreateInvoiceFragment.class.toString();
}
