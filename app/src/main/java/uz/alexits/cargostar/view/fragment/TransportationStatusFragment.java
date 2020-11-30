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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.SignatureActivity;
import uz.alexits.cargostar.view.adapter.PartialAdapter;
import uz.alexits.cargostar.view.callback.PartialCallback;
import uz.alexits.cargostar.viewmodel.TransportationStatusViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class TransportationStatusFragment extends Fragment implements PartialCallback {
    private Context context;
    private FragmentActivity activity;

    private TextView sourceTextView;
    private TextView currentPointTextView;
    private TextView destinationTextView;
    private Button submitStatusBtn;

    private ProgressBar progressBar;

    private ImageView checkImageView;
    private AnimatedVectorDrawable vectorDrawable;
    private AnimatedVectorDrawableCompat vectorDrawableCompat;

    private RecyclerView transportationListRecyclerView;
    private PartialAdapter partialAdapter;

    private static volatile TransportationStatus inTransitTransportationStatus = null;
    private static volatile TransportationStatus onItsWayTransportationStatus = null;
    private static volatile TransportationStatus deliveredTransportationStatus = null;
    private static volatile TransportationStatus leftCountryTransportationStatus = null;

    private static volatile long senderId = -1;
    private static volatile long senderCountryId = -1;
    private static volatile long senderRegionId = -1;
    private static volatile long senderCityId = -1;
    private static volatile long recipientCountryId = -1;
    private static volatile long recipientCityId = -1;
    private static volatile int deliveryType = -1;
    private static volatile int consignmentQuantity = -1;
    private static volatile String comment = null;

    private static volatile Transportation currentTransportation = null;

    private static volatile long nextPoint = -1;
    private static volatile TransportationStatus nextStatus = null;
    private static volatile Country destinationCountry = null;

    public TransportationStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        if (getArguments() != null) {
            final long requestId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getRequestId();
            final long invoiceId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getInvoiceId();
            final long transportationId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationId();
            final long courierId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCourierId();
            final long providerId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getProviderId();
            final long partialId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPartialId();
            final long statusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusId();
            final long paymentStatusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPaymentStatusId();
            final long currentTransitPointId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCurrentTransitPointId();

            final String statusName = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusName();
            final String trackingCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTrackingCode();
            final String qrCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getQrCode();
            final String partyQRCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPartyQrCode();
            final String cityFrom = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityFrom();
            final String cityTo = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityTo();
            final String instructions = TransportationStatusFragmentArgs.fromBundle(getArguments()).getInstructions();
            final String arrivalDate = TransportationStatusFragmentArgs.fromBundle(getArguments()).getArrivalDate();
            final String direction = TransportationStatusFragmentArgs.fromBundle(getArguments()).getDirection();

            currentTransportation = new Transportation(
                    transportationId,
                    providerId,
                    courierId,
                    invoiceId,
                    requestId,
                    statusId,
                    paymentStatusId,
                    currentTransitPointId,
                    partialId,
                    arrivalDate,
                    trackingCode,
                    qrCode,
                    partyQRCode,
                    instructions,
                    direction,
                    1,
                    new Date(),
                    new Date());
            currentTransportation.setCityFrom(cityFrom);
            currentTransportation.setCityTo(cityTo);
            currentTransportation.setTransportationStatusName(statusName);
        }
        SyncWorkRequest.fetchTransportationData(context, currentTransportation.getId());
        SyncWorkRequest.fetchTransportationRoute(context, currentTransportation.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_transportation_status, container, false);

        sourceTextView = root.findViewById(R.id.source_text_view);
        destinationTextView = root.findViewById(R.id.destination_text_view);
        submitStatusBtn = root.findViewById(R.id.submit_status_btn);
        checkImageView = root.findViewById(R.id.check_image_view);
        progressBar = root.findViewById(R.id.progress_bar);
        transportationListRecyclerView = root.findViewById(R.id.transportation_list_recycler_view);
        currentPointTextView = root.findViewById(R.id.current_point_text_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        partialAdapter = new PartialAdapter(context, this);

        transportationListRecyclerView.setHasFixedSize(true);
        transportationListRecyclerView.setLayoutManager(layoutManager);
        transportationListRecyclerView.setAdapter(partialAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submitStatusBtn.setOnClickListener(v -> {
            if (currentTransportation == null) {
                Toast.makeText(context, "Подождите, данные синхронизируются", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nextStatus == null) {
                Toast.makeText(context, "Ошибка: отсутствует след статус перевозки", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nextPoint <= 0) {
                Toast.makeText(context, "Ошибка: отсутствует след транзитная точка", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nextStatus.getId() == deliveredTransportationStatus.getId()) {
                startActivityForResult(new Intent(context, SignatureActivity.class), IntentConstants.REQUEST_RECIPIENT_SIGNATURE);
                return;
            }
            updateTransportationStatus(context, currentTransportation.getId(), nextStatus.getId(), nextPoint, currentTransportation.getPartialId());
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TransportationStatusViewModel statusViewModel = new ViewModelProvider(this).get(TransportationStatusViewModel.class);

        statusViewModel.setTransportationId(currentTransportation.getId());
        statusViewModel.setRequestId(currentTransportation.getRequestId());
        statusViewModel.setCurrentTransitPointId(currentTransportation.getCurrentTransitionPointId());

        if (currentTransportation.getPartialId() <= 0) {
            final List<Transportation> transportationList = new ArrayList<>();
            transportationList.add(currentTransportation);
            partialAdapter.setTransportationList(transportationList);
            partialAdapter.notifyDataSetChanged();
        }
        else {
            statusViewModel.setPartialId(currentTransportation.getPartialId());
        }

        sourceTextView.setText(currentTransportation.getCityFrom());
        destinationTextView.setText(currentTransportation.getCityTo());

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
                        if (currentTransportation.getTransportationStatusId() == null) {
                            return;
                        }
                        if (inTransitTransportationStatus == null) {
                            return;
                        }
                        if (onItsWayTransportationStatus == null) {
                            return;
                        }
                        if (deliveredTransportationStatus == null) {
                            return;
                        }
                        if (leftCountryTransportationStatus == null) {
                            return;
                        }
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

        statusViewModel.getDestinationCountry().observe(getViewLifecycleOwner(), destCountry -> {
           if (destCountry != null) {
               destinationCountry = destCountry;
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

        statusViewModel.getPartialTransportationList().observe(getViewLifecycleOwner(), partialTransportationList -> {
            if (partialTransportationList != null) {
                Log.i(TAG, "partialTransport: " + partialTransportationList);
                partialAdapter.setTransportationList(partialTransportationList);
                partialAdapter.notifyDataSetChanged();
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
                updateStatusDelivered(context, currentTransportation.getInvoiceId(), recipientSignatureFilePath, currentTransportation.getId(), nextStatus.getId(), nextPoint, currentTransportation.getPartialId());
                return;
            }
            Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTransportationStatus(@NonNull final Context context,
                                            final long transportationId,
                                            final long nextStatusId,
                                            final long nextPointId,
                                            final long partialId) {
        final UUID updateTransportationStatusId = SyncWorkRequest.updateTransportationStatus(context,
                transportationId, nextStatusId, nextPointId, partialId);

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
                                       final long nextPointId,
                                       final long partialId) {
        final UUID sendSignatureAndUpdateDelivered = SyncWorkRequest.sendRecipientSignatureAndUpdateStatusDelivered(
                context,
                invoiceId,
                recipientSignatureFilePath,
                transportationId,
                nextStatusId,
                nextPointId,
                partialId);

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

    @Override
    public void onPartialSelected(Transportation currentItem) {
        final TransportationStatusFragmentDirections.ActionParcelStatusFragmentToParcelDataFragment action =
                TransportationStatusFragmentDirections.actionParcelStatusFragmentToParcelDataFragment();
        action.setRequestId(currentItem.getRequestId() != null ? currentItem.getRequestId() : -1L);
        action.setInvoiceId(currentItem.getInvoiceId() != null ? currentItem.getInvoiceId() : -1L);
        action.setIsPublic(false);
        action.setIsRequest(false);
        action.setCourierId(currentItem.getCourierId() != null ? currentItem.getCourierId() : -1L);
        action.setProviderId(currentItem.getProviderId() != null ? currentItem.getProviderId() : -1L);
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
    }

    private static final String TAG = TransportationStatusFragment.class.toString();
}