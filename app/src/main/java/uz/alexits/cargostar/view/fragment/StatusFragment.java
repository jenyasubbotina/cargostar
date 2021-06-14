package uz.alexits.cargostar.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.work.WorkInfo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.PartialAdapter;
import uz.alexits.cargostar.view.callback.PartialCallback;
import uz.alexits.cargostar.view.dialog.AddresseeDataDialog;
import uz.alexits.cargostar.viewmodel.StatusViewModel;
import uz.alexits.cargostar.viewmodel.factory.StatusViewModelFactory;

public class StatusFragment extends Fragment implements PartialCallback {
    //header views
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

    private TextView sourceTextView;
    private TextView currentPointTextView;
    private TextView destinationTextView;
    private Button submitStatusBtn;
    private ProgressBar progressBar;
    private ImageView checkImageView;
    private AnimatedVectorDrawable vectorDrawable;
    private AnimatedVectorDrawableCompat vectorDrawableCompat;
    private SeekBar pathSeekBar;
    private PartialAdapter partialAdapter;

    private StatusViewModel statusViewModel;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final StatusViewModelFactory statusFactory = new StatusViewModelFactory(requireContext());
        statusViewModel = new ViewModelProvider(getViewModelStore(), statusFactory).get(StatusViewModel.class);

        if (getArguments() != null) {
           statusViewModel.setCurrentTransportation(new Transportation(
                   StatusFragmentArgs.fromBundle(getArguments()).getTransportationId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getProviderId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getCourierId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getInvoiceId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getRequestId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getBrancheId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getPaymentStatusId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getCurrentTransitPointId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getPartialId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getArrivalDate(),
                   StatusFragmentArgs.fromBundle(getArguments()).getTrackingCode(),
                   StatusFragmentArgs.fromBundle(getArguments()).getQrCode(),
                   StatusFragmentArgs.fromBundle(getArguments()).getPartyQrCode(),
                   StatusFragmentArgs.fromBundle(getArguments()).getInstructions(),
                   StatusFragmentArgs.fromBundle(getArguments()).getDirection(),
                   StatusFragmentArgs.fromBundle(getArguments()).getCityFrom(),
                   StatusFragmentArgs.fromBundle(getArguments()).getCityTo(),
                   StatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusName(),
                   StatusFragmentArgs.fromBundle(getArguments()).getTransportationType(),
                   StatusFragmentArgs.fromBundle(getArguments()).getImportStatus()));
           statusViewModel.setCurrentTransitPointId(StatusFragmentArgs.fromBundle(getArguments()).getCurrentTransitPointId());
           statusViewModel.setCurrentStatus(new TransportationStatus(
                   StatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusId(),
                   StatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusName(),
                   null));

            statusViewModel.fetchRoute(StatusFragmentArgs.fromBundle(getArguments()).getTransportationId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_status, container, false);

        /* header views */
        fullNameTextView = requireActivity().findViewById(R.id.full_name_text_view);
        branchTextView = requireActivity().findViewById(R.id.branch_text_view);
        courierIdTextView = requireActivity().findViewById(R.id.courier_id_text_view);
        requestSearchEditText = requireActivity().findViewById(R.id.search_edit_text);
        requestSearchImageView = requireActivity().findViewById(R.id.search_btn);
        profileImageView = requireActivity().findViewById(R.id.profile_image_view);
        editImageView = requireActivity().findViewById(R.id.edit_image_view);
        createUserImageView = requireActivity().findViewById(R.id.create_user_image_view);
        calculatorImageView = requireActivity().findViewById(R.id.calculator_image_view);
        notificationsImageView = requireActivity().findViewById(R.id.notifications_image_view);
        badgeCounterTextView = requireActivity().findViewById(R.id.badge_counter_text_view);

        sourceTextView = root.findViewById(R.id.source_text_view);
        destinationTextView = root.findViewById(R.id.destination_text_view);
        submitStatusBtn = root.findViewById(R.id.submit_status_btn);
        checkImageView = root.findViewById(R.id.check_image_view);
        progressBar = root.findViewById(R.id.progress_bar);
        currentPointTextView = root.findViewById(R.id.current_point_text_view);

        pathSeekBar = root.findViewById(R.id.path_seek_bar);

        final RecyclerView transportationListRecyclerView = root.findViewById(R.id.transportation_list_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        transportationListRecyclerView.setHasFixedSize(true);
        transportationListRecyclerView.setLayoutManager(layoutManager);
        partialAdapter = new PartialAdapter(requireContext(), this);
        transportationListRecyclerView.setAdapter(partialAdapter);

        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MainActivity.class));
        });

        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        createUserImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.createUserFragment);
        });

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.notificationsFragment);
        });

        calculatorImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.calculatorFragment);
        });

        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.profileFragment);
        });

        pathSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pathSeekBar.setOnTouchListener((v, event) -> true);

        submitStatusBtn.setOnClickListener(v -> {
            if (statusViewModel.getNextStatus().getValue() == null) {
                Toast.makeText(requireContext(), "Подождите... данные синхронизируются", Toast.LENGTH_SHORT).show();
                return;
            }
            if (statusViewModel.getNextStatus().getValue().getId() == 6) {
                AddresseeDataDialog dialogFragment = AddresseeDataDialog.newInstance(statusViewModel.getInvoiceId());
                dialogFragment.setTargetFragment(StatusFragment.this, IntentConstants.REQUEST_RECIPIENT_SIGNATURE);
                dialogFragment.show(getParentFragmentManager().beginTransaction(), TAG);
                return;
            }
            statusViewModel.updateStatus();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        statusViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        statusViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        statusViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        statusViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        statusViewModel.getDestinationCountryId().observe(getViewLifecycleOwner(), countryId -> {
            if (countryId != null) {
                statusViewModel.setDestinationCountryId(countryId);
            }
        });

        statusViewModel.getCurrentTransportation().observe(getViewLifecycleOwner(), currentTransportation -> {
            if (currentTransportation != null) {
                sourceTextView.setText(currentTransportation.getCityFrom());
                destinationTextView.setText(currentTransportation.getCityTo());
            }
        });

        statusViewModel.getCurrentCity().observe(getViewLifecycleOwner(), currentCity -> {
            if (currentCity != null) {
                currentPointTextView.setText(getString(R.string.current_location, currentCity.getName()));
            }
        });

        statusViewModel.getPartialList().observe(getViewLifecycleOwner(), partialTransportationList -> {
            if (partialTransportationList != null) {
                partialAdapter.setTransportationList(partialTransportationList);
                partialAdapter.notifyDataSetChanged();
            }
        });

        statusViewModel.getCurrentStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                if (status.getName() == null) {
                    return;
                }
                if (status.getName().equalsIgnoreCase(getString(R.string.in_transit))) {
                    pathSeekBar.setThumb(ContextCompat.getDrawable(requireContext(), R.drawable.purple_bubble));
                    return;
                }
                if (status.getName().equalsIgnoreCase(getString(R.string.on_the_way))) {
                    pathSeekBar.setThumb(ContextCompat.getDrawable(requireContext(), R.drawable.blue_bubble));
                    return;
                }
                if (status.getName().equalsIgnoreCase(getString(R.string.delivered)) || status.getName().equalsIgnoreCase(getString(R.string.leaving_uzbekistan))) {
                    pathSeekBar.setThumb(ContextCompat.getDrawable(requireContext(), R.drawable.green_bubble));
                    submitStatusBtn.setText(status.getName());
                    submitStatusBtn.setEnabled(false);
                    submitStatusBtn.setBackgroundResource(R.drawable.bg_grey);
                }
            }
        });

        statusViewModel.getNextStatus().observe(getViewLifecycleOwner(), nextStatus -> {
            if (nextStatus != null) {
                submitStatusBtn.setText(nextStatus.getName());
                submitStatusBtn.setBackgroundResource(R.drawable.btn_gradient_orange);
                submitStatusBtn.setEnabled(true);
                return;
            }
            submitStatusBtn.setText(null);
            submitStatusBtn.setBackgroundResource(R.drawable.bg_gradient_grey);
            submitStatusBtn.setEnabled(false);
        });

        statusViewModel.getPathOffset().observe(getViewLifecycleOwner(), offset -> {
            pathSeekBar.setProgress(offset, true);
        });

        statusViewModel.getRoute().observe(getViewLifecycleOwner(), route -> {
            if (route != null && !route.isEmpty()) {
                //get Route and Current Transit Point
                for (int i = 0; i < route.size(); i++) {
                    if (route.get(i).getTransitPointId() == statusViewModel.getCurrentTransitPointId()) {
                        statusViewModel.setCurrentPath(route.get(i));
                        statusViewModel.setProgressOffset((int) ((float) (i + 1) / route.size() * 2 - 1) * 100);

                        if (i < route.size() - 1) {
                            statusViewModel.setNextPath(route.get(i + 1));
                            statusViewModel.setNextPointId(route.get((i + 1)).getTransitPointId());
                        }
                        return;
                    }
                }
            }
        });

        statusViewModel.getUpdateStatusResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                final Drawable drawable = checkImageView.getDrawable();
                checkImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    vectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    vectorDrawableCompat.start();
                    return;
                }
                if (drawable instanceof AnimatedVectorDrawable) {
                    vectorDrawable = (AnimatedVectorDrawable) drawable;
                    vectorDrawable.start();
                }
                return;
            }
            if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                progressBar.setVisibility(View.INVISIBLE);
                checkImageView.setVisibility(View.INVISIBLE);

                Toast.makeText(requireContext(), "Произошла ошибка при обновлении статуса", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            checkImageView.setVisibility(View.INVISIBLE);
            submitStatusBtn.setEnabled(false);
        });

        statusViewModel.getRequestDataFromCache().observe(getViewLifecycleOwner(), request -> {
            statusViewModel.setRequestData(request);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentConstants.REQUEST_RECIPIENT_SIGNATURE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Отправьте подпись получателя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (resultCode == Activity.RESULT_OK && data != null) {
                statusViewModel.sendRecipientSignatureAndUpdateStatus(
                        data.getLongExtra(Constants.KEY_INVOICE_ID, 0L),
                        data.getStringExtra(Constants.ADDRESSEE_FULL_NAME),
                        data.getStringExtra(Constants.ADDRESSEE_PHONE),
                        data.getStringExtra(Constants.ADDRESSEE_ADDRESS),
                        data.getStringExtra(Constants.ADDRESSEE_ORGANIZATION),
                        data.getStringExtra(Constants.ADDRESSEE_SIGNATURE),
                        data.getStringExtra(Constants.ADDRESSEE_SIGNATURE_DATE),
                        data.getStringExtra(Constants.ADDRESSEE_COMMENT),
                        data.getBooleanExtra(Constants.ADDRESSEE_IS_ACCEPTED, false));
                return;
            }
        }
        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartialSelected(final Transportation currentItem) {
        if (statusViewModel.getRequestData() == null) {
            Toast.makeText(requireContext(), "Подождите... данные синхронизируются", Toast.LENGTH_SHORT).show();
            return;
        }
        NavHostFragment.findNavController(StatusFragment.this)
                .navigate(StatusFragmentDirections.actionStatusFragmentToInvoiceFragment()
                        .setIsPublic(false)
                        .setIsRequest(false)
                        .setRequestId(currentItem.getRequestId())
                        .setInvoiceId(currentItem.getInvoiceId())
                        .setCourierId(currentItem.getCourierId())
                        .setProviderId(currentItem.getProviderId())
                        .setClientId(statusViewModel.getRequestData().getClientId())
                        .setSenderFirstName(statusViewModel.getRequestData().getSenderFirstName())
                        .setSenderLastName(statusViewModel.getRequestData().getSenderLastName())
                        .setSenderMiddleName(statusViewModel.getRequestData().getSenderMiddleName())
                        .setSenderEmail(statusViewModel.getRequestData().getSenderEmail())
                        .setSenderPhone(statusViewModel.getRequestData().getSenderPhone())
                        .setSenderAddress(statusViewModel.getRequestData().getSenderAddress())
                        .setSenderCountryId(statusViewModel.getRequestData().getSenderCountryId())
                        .setSenderCityName(statusViewModel.getRequestData().getSenderCityName())
                        .setRecipientCountryId(statusViewModel.getRequestData().getRecipientCountryId())
                        .setRecipientCityName(statusViewModel.getRequestData().getRecipientCityName())
                        .setDeliveryType(statusViewModel.getRequestData().getDeliveryType())
                        .setComment(statusViewModel.getRequestData().getComment())
                        .setConsignmentQuantity(statusViewModel.getRequestData().getConsignmentQuantity()));
    }

    private static final String TAG = StatusFragment.class.toString();
}