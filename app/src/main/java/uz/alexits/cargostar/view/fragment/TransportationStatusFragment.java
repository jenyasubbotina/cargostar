package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.UUID;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.viewmodel.TransportationStatusViewModel;
import uz.alexits.cargostar.viewmodel.TransportationViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class TransportationStatusFragment extends Fragment {
    private Context context;
    private FragmentActivity activity;

    private ConstraintLayout parcelItem;

    private TextView transportationIdTextView;
    private TextView transportationIdItemTextView;
    private TextView srcCityTextView;
    private TextView destCityTextView;
    private TextView sourceTextView;
    private TextView currentPointTextView;
    private TextView destinationTextView;
    private Button submitStatusBtn;

    private ProgressBar progressBar;

    private ImageView checkImageView;
    private AnimatedVectorDrawable vectorDrawable;
    private AnimatedVectorDrawableCompat vectorDrawableCompat;

    private long transportationId = -1;
    private Transportation currentTransportation;

    private TransportationStatus nextStatus;
    private Long nextPoint;

    private static TransportationStatus inTransitTransportationStatus;
    private static TransportationStatus onItsWayTransportationStatus;
    private static TransportationStatus deliveredTransportationStatus;
    private static TransportationStatus leftCountryTransportationStatus;
//    private final TransportationStatus[] statusArray = new TransportationStatus[] {TransportationStatus.IN_TRANSIT, TransportationStatus.ON_THE_WAY, TransportationStatus.DELIVERED, TransportationStatus.LOST};

    public TransportationStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        if (getArguments() != null) {
            transportationId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationId();
            final long invoiceId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getInvoiceId();
            final long statusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusId();
            final String statusName = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusName();
            final long paymentStatusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPaymentStatusId();
            final String trackingCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTrackingCode();
            final String qrCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getQrCode();
            final String partyQRCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPartyQrCode();
            final long currentTransitPointId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCurrentTransitPointId();
            final String cityFrom = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityFrom();
            final String cityTo = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityTo();
            final long courierId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCourierId();
            final long providerId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getProviderId();
            final String instructions = TransportationStatusFragmentArgs.fromBundle(getArguments()).getInstructions();
            final String arrivalDate = TransportationStatusFragmentArgs.fromBundle(getArguments()).getArrivalDate();
            final String direction = TransportationStatusFragmentArgs.fromBundle(getArguments()).getDirection();
        }
        Log.i(TransportationStatusFragment.class.toString(), "currentTransportationId=" + transportationId);

        SyncWorkRequest.fetchTransportationData(context, transportationId);
        SyncWorkRequest.fetchTransportationRoute(context, transportationId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_parcel_status, container, false);

        parcelItem = root.findViewById(R.id.parcel_item);
        transportationIdTextView = root.findViewById(R.id.parcel_id_text_view);
        transportationIdItemTextView = root.findViewById(R.id.parcel_id_item_text_view);
        srcCityTextView = root.findViewById(R.id.from_text_view);
        destCityTextView = root.findViewById(R.id.to_text_view);
        sourceTextView = root.findViewById(R.id.source_text_view);
        currentPointTextView = root.findViewById(R.id.current_point_text_view);
        destinationTextView = root.findViewById(R.id.destination_text_view);
        submitStatusBtn = root.findViewById(R.id.submit_status_btn);
        checkImageView = root.findViewById(R.id.check_image_view);
        progressBar = root.findViewById(R.id.progress_bar);

        transportationIdTextView.setText(String.valueOf(transportationId));
        transportationIdItemTextView.setText(String.valueOf(transportationId));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parcelItem.setOnClickListener(v -> {
            final TransportationStatusFragmentDirections.ActionParcelStatusFragmentToParcelDataFragment action =
                    TransportationStatusFragmentDirections.actionParcelStatusFragmentToParcelDataFragment();
            action.setRequestId(transportationId);
            action.setRequestOrParcel(IntentConstants.INTENT_PARCEL);
            NavHostFragment.findNavController(this).navigate(action);
        });

        submitStatusBtn.setOnClickListener(v -> {
            if (currentTransportation == null || nextStatus == null) {
                Toast.makeText(context, "Идет синхронинзация данных", Toast.LENGTH_SHORT).show();
                return;
            }
            final UUID updateTransportationStatusId = SyncWorkRequest.updateTransportationStatus(context,
                    currentTransportation.getId(), nextStatus.getId(), nextPoint);

            WorkManager.getInstance(context).getWorkInfoByIdLiveData(updateTransportationStatusId).observe(getViewLifecycleOwner(), workInfo -> {
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

                    Toast.makeText(context, "Произошла ошибка при обновлении статуса", Toast.LENGTH_SHORT).show();

                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                checkImageView.setVisibility(View.INVISIBLE);
                submitStatusBtn.setEnabled(false);
            });
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TransportationStatusViewModel statusViewModel = new ViewModelProvider(this).get(TransportationStatusViewModel.class);

        statusViewModel.setTransportationId(transportationId);

        statusViewModel.getCurrentTransportation().observe(getViewLifecycleOwner(), transportation -> {
            if (transportation != null) {
                Log.i(TAG, "currentTransportation: " + transportation);
                currentTransportation = transportation;
                statusViewModel.setCurrentTransitPointId(currentTransportation.getCurrentTransitionPointId());

                srcCityTextView.setText(currentTransportation.getCityFrom());
                destCityTextView.setText(currentTransportation.getCityTo());
                sourceTextView.setText(currentTransportation.getCityFrom());
                destinationTextView.setText(currentTransportation.getCityTo());
            }
        });

        statusViewModel.getInTransitStatus().observe(getViewLifecycleOwner(), inTransitStatus -> {
            inTransitTransportationStatus = inTransitStatus;
        });

        statusViewModel.getOnItsWayStatus().observe(getViewLifecycleOwner(), onItsWayStatus -> {
            onItsWayTransportationStatus = onItsWayStatus;
        });

        statusViewModel.getDeliveredStatus().observe(getViewLifecycleOwner(), deliveredStatus -> {
            deliveredTransportationStatus = deliveredStatus;
        });

        statusViewModel.getLeftCountryStatus().observe(getViewLifecycleOwner(), leftCountryStatus -> {
            leftCountryTransportationStatus = leftCountryStatus;
        });

        statusViewModel.getCurrentTransitPoint().observe(getViewLifecycleOwner(), transitPoint -> {
            if (transitPoint != null) {
                statusViewModel.setCurrentCityId(transitPoint.getCityId());
            }
        });

        statusViewModel.getCurrentCity().observe(getViewLifecycleOwner(), currentCity -> {
            if (currentCity != null) {
                currentPointTextView.setText(currentCity.getName());
            }
        });

        statusViewModel.getRoute().observe(getViewLifecycleOwner(), route -> {
            Log.i(TAG, "route: " + route);
            Route currentPath = null;
            Route nextPath = null;

            if (currentTransportation != null) {
                if (route != null && !route.isEmpty()) {
                    Log.i(TAG, "route is not empty: ");
                    for (int i = 0; i < route.size(); i++) {
                        if (route.get(i).getTransitPointId().equals(currentTransportation.getCurrentTransitionPointId())) {
                            currentPath = route.get(i);
                            Log.i(TAG, "currentPath: " + currentPath);

                            if (i == route.size() - 1) {
                                nextPath = null;
                            }
                            else {
                                nextPath = route.get(i + 1);
                            }
                            break;
                        }
                    }
                    if (currentPath != null) {
                        Log.i(TAG, "currentPath: " + currentPath);
                        if (currentTransportation.getTransportationStatusName() != null) {
                            if (nextPath != null) {
                                Log.i(TAG, "nextPath: " + nextPath);
                                if (currentTransportation.getTransportationStatusId() == onItsWayTransportationStatus.getId()) {
                                    statusViewModel.setNextTransportationStatus(inTransitTransportationStatus);
                                    statusViewModel.setNextTransitPointId(nextPath.getTransitPointId());
                                    return;
                                }
                                if (currentTransportation.getTransportationStatusId() == inTransitTransportationStatus.getId()) {
                                    statusViewModel.setNextTransportationStatus(onItsWayTransportationStatus);
                                    statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                                }
                                return;
                            }
                            if (currentTransportation.getTransportationStatusId() == onItsWayTransportationStatus.getId()) {
                                //delivered
                                statusViewModel.setNextTransportationStatus(deliveredTransportationStatus);
                                statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                                return;
                            }
                            if (currentTransportation.getTransportationStatusId() == inTransitTransportationStatus.getId()) {
                                statusViewModel.setNextTransportationStatus(onItsWayTransportationStatus);
                                statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                            }
                            if (currentTransportation.getTransportationStatusId() == deliveredTransportationStatus.getId()) {
                                statusViewModel.setNextTransportationStatus(null);
                            }
                        }
                    }
                }
            }
        });
        statusViewModel.getNextStatus().observe(getViewLifecycleOwner(), nextTransportationStatus -> {
            nextStatus = nextTransportationStatus;

            if (nextStatus != null) {
                Log.i(TAG, "next status: " + nextTransportationStatus);
                submitStatusBtn.setText(nextStatus.getName());
                submitStatusBtn.setBackgroundResource(R.drawable.btn_gradient_orange);
                submitStatusBtn.setEnabled(true);
                return;
            }
            submitStatusBtn.setEnabled(false);
            submitStatusBtn.setBackgroundResource(R.drawable.bg_gradient_grey);
        });

        statusViewModel.getNextTransitPoint().observe(getViewLifecycleOwner(), nextTransitPoint -> {
            if (nextTransitPoint != null) {
                Log.i(TAG, "next point: " + nextTransitPoint);
                nextPoint = nextTransitPoint;
            }
        });
    }

    private static final String TAG = TransportationStatusFragment.class.toString();
}