package uz.alexits.cargostar.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
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

    private SeekBar pathSeekBar;

    private RecyclerView transportationListRecyclerView;
    private PartialAdapter partialAdapter;

    private static volatile TransportationStatus inTransitTransportationStatus = null;
    private static volatile TransportationStatus onItsWayTransportationStatus = null;
    private static volatile TransportationStatus deliveredTransportationStatus = null;
    private static volatile TransportationStatus leftCountryTransportationStatus = null;

    private static volatile long transportationId = -1;
    private static volatile long currentPointId = -1;
    private static volatile long currentStatusId = -1;
    private static volatile String currentStatusName = null;
    private static volatile long partialId = -1;
    private static volatile String cityFrom = null;
    private static volatile String cityTo = null;

    private static volatile long requestId = -1;
    private static volatile long invoiceId = -1;
    private static volatile long brancheId = -1;
    private static volatile long courierId = -1;
    private static volatile long providerId = -1;
    private static volatile long paymentStatusId = -1;
    private static volatile String trackingCode = null;
    private static volatile String qrCode = null;
    private static volatile String partyQRCode = null;
    private static volatile String instructions = null;
    private static volatile String arrivalDate = null;
    private static volatile String direction = null;

    private static volatile long senderId = -1;
    private static volatile long senderCountryId = -1;
    private static volatile long senderRegionId = -1;
    private static volatile long senderCityId = -1;
    private static volatile long recipientCountryId = -1;
    private static volatile long recipientCityId = -1;
    private static volatile int deliveryType = -1;
    private static volatile int consignmentQuantity = -1;
    private static volatile String comment = null;

    private static volatile long nextPoint = -1;
    private static volatile TransportationStatus nextStatus = null;
    private static volatile Country destinationCountry = null;

    private static volatile int pointsQuantity = 0;
    private static volatile int currentPosition = 0;

    private TransportationStatusViewModel statusViewModel;

    public TransportationStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        transportationId = -1;
        currentPointId = -1;
        currentStatusId = -1;
        currentStatusName = null;
        partialId = -1;
        cityFrom = null;
        cityTo = null;

        requestId = -1;
        invoiceId = -1;
        brancheId = -1;
        courierId = -1;
        providerId = -1;
        paymentStatusId = -1;
        trackingCode = null;
        qrCode = null;
        partyQRCode = null;
        instructions = null;
        arrivalDate = null;
        direction = null;

        senderId = -1;
        senderCountryId = -1;
        senderRegionId = -1;
        senderCityId = -1;
        recipientCountryId = -1;
        recipientCityId = -1;
        deliveryType = -1;
        consignmentQuantity = -1;
        comment = null;

        nextPoint = -1;
        nextStatus = null;
        destinationCountry = null;
        pointsQuantity = 0;
        currentPosition = 0;

        if (getArguments() != null) {
            transportationId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationId();
            cityFrom = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityFrom();
            cityTo = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCityTo();
            partialId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPartialId();
            currentStatusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusId();
            currentPointId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCurrentTransitPointId();
            currentStatusName = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTransportationStatusName();

            requestId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getRequestId();
            invoiceId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getInvoiceId();
            brancheId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getBrancheId();
            courierId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getCourierId();
            providerId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getProviderId();

            paymentStatusId = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPaymentStatusId();

            trackingCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getTrackingCode();
            qrCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getQrCode();
            partyQRCode = TransportationStatusFragmentArgs.fromBundle(getArguments()).getPartyQrCode();

            instructions = TransportationStatusFragmentArgs.fromBundle(getArguments()).getInstructions();
            arrivalDate = TransportationStatusFragmentArgs.fromBundle(getArguments()).getArrivalDate();
            direction = TransportationStatusFragmentArgs.fromBundle(getArguments()).getDirection();

            SyncWorkRequest.fetchTransportationRoute(context, transportationId);
        }
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

        pathSeekBar = root.findViewById(R.id.path_seek_bar);

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

        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

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
            if (currentPointId <= 0) {
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
            updateTransportationStatus(context, -1L,null, transportationId, nextStatus.getId(), nextPoint, partialId);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        statusViewModel = new ViewModelProvider(this).get(TransportationStatusViewModel.class);

        statusViewModel.setTransportationId(transportationId);
        statusViewModel.setRequestId(requestId);
        statusViewModel.setCurrentPointId(currentPointId);

        if (partialId <= 0) {
            final List<Transportation> transportationList = new ArrayList<>();
            final Transportation transportation = new Transportation(
                    transportationId,
                    providerId,
                    courierId,
                    invoiceId,
                    requestId,
                    brancheId,
                    currentStatusId,
                    paymentStatusId,
                    currentPointId,
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
            transportation.setCityFrom(cityFrom);
            transportation.setCityTo(cityTo);
            transportation.setTransportationStatusName(currentStatusName);
            transportationList.add(transportation);
            partialAdapter.setTransportationList(transportationList);
            partialAdapter.notifyDataSetChanged();
        }
        else {
            statusViewModel.setPartialId(partialId);
        }

        sourceTextView.setText(cityFrom);
        destinationTextView.setText(cityTo);

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
                currentPointTextView.setText(getString(R.string.current_location, currentCity.getName()));
            }
        });

        statusViewModel.getRoute().observe(getViewLifecycleOwner(), route -> {
            Route currentPath = null;
            Route nextPath = null;

            if (currentStatusId <= 0) {
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

            if (currentPointId > 0) {
                if (route != null && !route.isEmpty()) {
                    //get Route and Current Transit Point
                    pointsQuantity = route.size();

                    for (int i = 0; i < route.size(); i++) {
                        if (route.get(i).getTransitPointId().equals(currentPointId)) {

                            currentPath = route.get(i);
                            currentPosition = i + 1;

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
                        if (currentStatusId == onItsWayTransportationStatus.getId()) {
                            statusViewModel.setNextStatus(inTransitTransportationStatus);
                            statusViewModel.setNextPoint(currentPath.getTransitPointId());
                            pathSeekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.blue_bubble));

                            final int temp = pointsQuantity * 2 - 1;
                            final float temp2 = (float) currentPosition / temp;
                            final float progress = temp2 * 100;
                            pathSeekBar.setProgress((int) progress, true);
                            Log.i(TAG, "currentStatus=" + currentStatusId + "setProgress=" + progress);

                            return;
                        }
                        if (currentStatusId == inTransitTransportationStatus.getId()) {
                            pathSeekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.purple_bubble));

                            if (nextPath != null) {
                                statusViewModel.setNextStatus(onItsWayTransportationStatus);
                                statusViewModel.setNextPoint(nextPath.getTransitPointId());
                            }
                            else {
                                if (destinationCountry != null) {
                                    if (destinationCountry.getId() == 191) {
                                        //delivered
                                        statusViewModel.setNextStatus(deliveredTransportationStatus);
                                    }
                                    else {//left country
                                        statusViewModel.setNextStatus(leftCountryTransportationStatus);
                                    }
                                    statusViewModel.setNextPoint(currentPath.getTransitPointId());
                                }
                            }
                            return;
                        }
                        if (currentStatusId == deliveredTransportationStatus.getId() || currentStatusId == leftCountryTransportationStatus.getId()) {
                            statusViewModel.setNextStatus(null);
                            statusViewModel.setNextPoint(currentPath.getTransitPointId());
                            pathSeekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.green_bubble));
                            pathSeekBar.setProgress(100, true);
                            Log.i(TAG, "currentStatus=" + currentStatusId + "setProgress=" + 100);
                        }
                    }
                }
                if (currentStatusId == deliveredTransportationStatus.getId() || currentStatusId == leftCountryTransportationStatus.getId()) {
                    pathSeekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.green_bubble));
                    pathSeekBar.setProgress(100, true);
                    Log.i(TAG, "currentStatus=" + currentStatusId + "setProgress=" + 100);
                    return;
                }
                if (currentStatusId == inTransitTransportationStatus.getId()) {
                    pathSeekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.purple_bubble));
                    pathSeekBar.setProgress(0, true);
                    Log.i(TAG, "currentStatus=" + currentStatusId + "setProgress=" + 0);
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
                partialAdapter.setTransportationList(partialTransportationList);
                partialAdapter.notifyDataSetChanged();
            }
        });

        statusViewModel.getCurrentTransportation().observe(getViewLifecycleOwner(), currentTransportation -> {
            if (currentTransportation != null) {
                if (currentTransportation.getTransportationStatusId() != null) {
                    currentStatusId = currentTransportation.getTransportationStatusId();
                }
                if (currentTransportation.getCurrentTransitionPointId() != null) {
                    currentPointId = currentTransportation.getCurrentTransitionPointId();
                    statusViewModel.setCurrentPointId(currentPointId);
                }
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
                updateTransportationStatus(context, invoiceId, recipientSignatureFilePath, transportationId, nextStatus.getId(), nextPoint, partialId);
                return;
            }
            Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTransportationStatus(@NonNull final Context context,
                                            final long invoiceId,
                                            final String recipientSignatureFilePath,
                                            final long transportationId,
                                            final long nextStatusId,
                                            final long nextPointId,
                                            final long partialId) {
        UUID updateStatusWork = null;

        if (invoiceId <= 0) {
            updateStatusWork = SyncWorkRequest.updateTransportationStatus(context, transportationId, nextStatusId, nextPointId, partialId);
        }
        else if (recipientSignatureFilePath == null) {
            return;
        }
        else {
            updateStatusWork = SyncWorkRequest.sendRecipientSignatureAndUpdateStatusDelivered(
                    context,
                    invoiceId,
                    recipientSignatureFilePath,
                    transportationId,
                    nextStatusId,
                    nextPointId,
                    partialId);
        }

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(updateStatusWork).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                if (currentPointId <= 0 || currentStatusId <= 0 || currentStatusName == null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    checkImageView.setVisibility(View.INVISIBLE);

                    Toast.makeText(context, "Произошла ошибка при обновлении статуса", Toast.LENGTH_SHORT).show();

                    return;
                }

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
        final TransportationStatusFragmentDirections.ActionParcelStatusFragmentToParcelDataFragment action = TransportationStatusFragmentDirections.actionParcelStatusFragmentToParcelDataFragment();

        action.setIsPublic(false);
        action.setIsRequest(false);
        action.setRequestId(currentItem.getRequestId() != null ? currentItem.getRequestId() : -1L);
        action.setInvoiceId(currentItem.getInvoiceId() != null ? currentItem.getInvoiceId() : -1L);
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

        UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
    }

    private static final String TAG = TransportationStatusFragment.class.toString();
}