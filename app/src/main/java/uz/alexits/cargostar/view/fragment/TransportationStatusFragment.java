package uz.alexits.cargostar.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.UUID;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.SignatureActivity;
import uz.alexits.cargostar.viewmodel.TransportationStatusViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class TransportationStatusFragment extends Fragment {
    private Context context;
    private FragmentActivity activity;

    private ConstraintLayout transportationItem;
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

    private long invoiceId;
    private long requestId;
    private long courierId;
    private long providerId;

    private long senderId;
    private long senderCountryId;
    private long senderRegionId;
    private long senderCityId;
    private long recipientCountryId;
    private long recipientCityId;
    private int deliveryType;
    private int consignmentQuantity;
    private String comment;

    private static TransportationStatus inTransitTransportationStatus;
    private static TransportationStatus onItsWayTransportationStatus;
    private static TransportationStatus deliveredTransportationStatus;
    private static TransportationStatus leftCountryTransportationStatus;

    private Country destinationCountry;

    public TransportationStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        if (getArguments() != null) {
            requestId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getRequestId();
            invoiceId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getInvoiceId();
            transportationId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationId();
            courierId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCourierId();
            providerId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getProviderId();

            final long statusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusId();
            final String statusName = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusName();
            final long paymentStatusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPaymentStatusId();
            final String trackingCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTrackingCode();
            final String qrCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getQrCode();
            final String partyQRCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPartyQrCode();
            final long currentTransitPointId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCurrentTransitPointId();
            final String cityFrom = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityFrom();
            final String cityTo = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityTo();
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
        final View root = inflater.inflate(R.layout.fragment_transportation_status, container, false);

        transportationItem = root.findViewById(R.id.transportation_item);
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

        submitStatusBtn.setOnClickListener(v -> {
            if (currentTransportation == null || nextStatus == null) {
                Toast.makeText(context, "Идет синхронинзация данных", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentTransportation.getTransportationStatusId() == deliveredTransportationStatus.getId()) {
                startActivityForResult(new Intent(context, SignatureActivity.class), IntentConstants.REQUEST_SENDER_SIGNATURE);
                return;
            }
            updateTransportationStatus(context, transportationId, nextStatus.getId(), nextPoint);

        });

        transportationItem.setOnClickListener(v -> {
            final TransportationStatusFragmentDirections.ActionParcelStatusFragmentToParcelDataFragment action =
                    TransportationStatusFragmentDirections.actionParcelStatusFragmentToParcelDataFragment();
            action.setRequestId(requestId);
            action.setInvoiceId(invoiceId);
            action.setIsPublic(false);
            action.setIsRequest(false);
            action.setCourierId(courierId);
            action.setProviderId(providerId);
            action.setClientId(senderId);
            action.setSenderCountryId(senderCountryId);
            action.setSenderRegionId(senderRegionId);
            action.setSenderCityId(senderCityId);
            action.setRecipientCountryId(recipientCountryId);
            action.setRecipientCityId(recipientCityId);
            action.setDeliveryType(deliveryType);
            action.setComment(comment);
            action.setConsignmentQuantity(consignmentQuantity);
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TransportationStatusViewModel statusViewModel = new ViewModelProvider(this).get(TransportationStatusViewModel.class);

        statusViewModel.setTransportationId(transportationId);
        statusViewModel.setRequestId(requestId);

        statusViewModel.getCurrentTransportation().observe(getViewLifecycleOwner(), transportation -> {
            if (transportation != null) {
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
            Route currentPath = null;
            Route nextPath = null;

            if (currentTransportation != null) {
                if (route != null && !route.isEmpty()) {
                    for (int i = 0; i < route.size(); i++) {
                        if (route.get(i).getTransitPointId().equals(currentTransportation.getCurrentTransitionPointId())) {
                            currentPath = route.get(i);

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
                        if (currentTransportation.getTransportationStatusName() != null) {
                            if (nextPath != null) {
                                if (currentTransportation.getTransportationStatusId() == onItsWayTransportationStatus.getId()) {
                                    statusViewModel.setNextTransportationStatus(inTransitTransportationStatus);
                                    statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                                    return;
                                }
                                if (currentTransportation.getTransportationStatusId() == inTransitTransportationStatus.getId()) {
                                    statusViewModel.setNextTransportationStatus(onItsWayTransportationStatus);
                                    statusViewModel.setNextTransitPointId(nextPath.getTransitPointId());
                                }
                                return;
                            }
                            if (currentTransportation.getTransportationStatusId() == onItsWayTransportationStatus.getId()) {
                                statusViewModel.setNextTransportationStatus(inTransitTransportationStatus);
                                statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                                return;
                            }
                            if (currentTransportation.getTransportationStatusId() == inTransitTransportationStatus.getId()) {
                                if (destinationCountry != null) {
                                    if (destinationCountry.getId() == 191) {
                                        //delivered
                                        statusViewModel.setNextTransportationStatus(deliveredTransportationStatus);
                                        statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                                        return;
                                    }
                                    //left country
                                    statusViewModel.setNextTransportationStatus(leftCountryTransportationStatus);
                                    statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                                }
                            }
                            if (currentTransportation.getTransportationStatusId() == deliveredTransportationStatus.getId()
                                    || currentTransportation.getTransportationStatusId() == leftCountryTransportationStatus.getId()) {
                                statusViewModel.setNextTransportationStatus(null);
                                statusViewModel.setNextTransitPointId(currentPath.getTransitPointId());
                            }
                        }
                    }
                }
            }
        });

        statusViewModel.getNextStatus().observe(getViewLifecycleOwner(), nextTransportationStatus -> {
            nextStatus = nextTransportationStatus;

            if (nextStatus != null) {
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
                nextPoint = nextTransitPoint;
            }
        });

        statusViewModel.getDestinationCountry().observe(getViewLifecycleOwner(), destinationCountry -> {
           if (destinationCountry != null) {
               this.destinationCountry = destinationCountry;
           }
        });

        statusViewModel.getCurrentRequest().observe(getViewLifecycleOwner(), currentRequest -> {
            if (currentRequest != null) {
                senderId = currentRequest.getClientId() != null ? currentRequest.getClientId() : 0;
                senderCountryId = currentRequest.getSenderCountryId() != null ? currentRequest.getSenderCountryId() : 0;
                senderRegionId = currentRequest.getSenderRegionId() != null ? currentRequest.getSenderRegionId() : 0;
                senderCityId = currentRequest.getSenderCityId() != null ? currentRequest.getSenderCityId() : 0;
                recipientCountryId = currentRequest.getRecipientCountryId() != null ? currentRequest.getRecipientCountryId() : 0;
                recipientCityId = currentRequest.getRecipientCityId() != null ? currentRequest.getRecipientCityId() : 0;
                deliveryType = currentRequest.getDeliveryType();
                comment = currentRequest.getComment();
                consignmentQuantity = currentRequest.getConsignmentQuantity();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentConstants.REQUEST_RECIPIENT_SIGNATURE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Отправьте подпись получателя", Toast.LENGTH_SHORT).show();
                return;
            }
            if (resultCode == Activity.RESULT_OK && data != null) {
                final String recipientSignatureFilePath = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
                updateStatusDelivered(context, invoiceId, recipientSignatureFilePath, transportationId, nextStatus.getId(), nextPoint);
                return;
            }
            Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTransportationStatus(@NonNull final Context context,
                                            final long transportationId,
                                            final long nextStatusId,
                                            final long nextPointId) {
        final UUID updateTransportationStatusId = SyncWorkRequest.updateTransportationStatus(context,
                transportationId, nextStatusId, nextPointId);

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
    }

    private void updateStatusDelivered(@NonNull final Context context,
                                       final long invoiceId,
                                       final String recipientSignatureFilePath,
                                       final long transportationId,
                                       final long nextStatusId,
                                       final long nextPointId) {
        final UUID sendSignatureAndUpdateDelivered = SyncWorkRequest.sendRecipientSignatureAndUpdateStatusDelivered(
                context,
                invoiceId,
                recipientSignatureFilePath,
                transportationId,
                nextStatusId,
                nextPointId);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(sendSignatureAndUpdateDelivered).observe(getViewLifecycleOwner(), workInfo -> {
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
    }

    private static final String TAG = TransportationStatusFragment.class.toString();
}