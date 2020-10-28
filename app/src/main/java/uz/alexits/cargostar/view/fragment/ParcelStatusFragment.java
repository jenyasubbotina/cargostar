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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.model.TransportationStatus;
import uz.alexits.cargostar.viewmodel.ParcelStatusViewModel;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.fragment.ParcelStatusFragmentArgs;
import uz.alexits.cargostar.view.fragment.ParcelStatusFragmentDirections;

public class ParcelStatusFragment extends Fragment {
    private Context context;
    private FragmentActivity activity;

    private ConstraintLayout parcelItem;

    private TextView parcelIdTextView;
    private TextView parcelIdItemTextView;
    private TextView fromTextView;
    private TextView toTextView;
    private TextView sourceTextView;
    private TextView currentPointTextView;
    private TextView destinationTextView;
    private Button submitStatusBtn;

    private ImageView checkImageView;
    private AnimatedVectorDrawable vectorDrawable;
    private AnimatedVectorDrawableCompat vectorDrawableCompat;

    private long requestId = -1;
    private long currentLocationId = -1;
    private final TransportationStatus[] statusArray = new TransportationStatus[] {TransportationStatus.IN_TRANSIT, TransportationStatus.ON_THE_WAY, TransportationStatus.DELIVERED, TransportationStatus.LOST};

    public ParcelStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();
        if (getArguments() != null) {
            requestId = ParcelStatusFragmentArgs.fromBundle(getArguments()).getParcelId();
        }
        Log.i(ParcelStatusFragment.class.toString(), "requestId=" + requestId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_parcel_status, container, false);

        parcelItem = root.findViewById(R.id.parcel_item);
        parcelIdTextView = root.findViewById(R.id.parcel_id_text_view);
        parcelIdItemTextView = root.findViewById(R.id.parcel_id_item_text_view);
        fromTextView = root.findViewById(R.id.from_text_view);
        toTextView = root.findViewById(R.id.to_text_view);
        sourceTextView = root.findViewById(R.id.source_text_view);
        currentPointTextView = root.findViewById(R.id.current_point_text_view);
        destinationTextView = root.findViewById(R.id.destination_text_view);
        submitStatusBtn = root.findViewById(R.id.submit_status_btn);
        checkImageView = root.findViewById(R.id.check_image_view);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parcelItem.setOnClickListener(v -> {
            final ParcelStatusFragmentDirections.ActionParcelStatusFragmentToParcelDataFragment action =
                    ParcelStatusFragmentDirections.actionParcelStatusFragmentToParcelDataFragment();
            action.setParcelId(requestId);
            action.setRequestOrParcel(Constants.INTENT_PARCEL);
            NavHostFragment.findNavController(this).navigate(action);
        });

        submitStatusBtn.setOnClickListener(v -> {
            final Drawable drawable = checkImageView.getDrawable();
            checkImageView.setVisibility(View.VISIBLE);

            if (drawable instanceof AnimatedVectorDrawableCompat) {
                vectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
                vectorDrawableCompat.start();
                return;
            }
            if (drawable instanceof AnimatedVectorDrawable) {
                vectorDrawable = (AnimatedVectorDrawable) drawable;
                vectorDrawable.start();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ParcelStatusViewModel parcelStatusViewModel = new ViewModelProvider(this).get(ParcelStatusViewModel.class);

        parcelStatusViewModel.selectParcel(requestId).observe(getViewLifecycleOwner(), parcel -> {
            currentLocationId = parcel.getReceipt().getCurrentLocation();

            parcelIdTextView.setText(String.valueOf(parcel.getReceipt().getId()));
            parcelIdItemTextView.setText(String.valueOf(parcel.getReceipt().getId()));
            fromTextView.setText(parcel.getReceipt().getSenderAddress().getCity());
            toTextView.setText(parcel.getReceipt().getRecipientAddress().getCity());
            sourceTextView.setText(parcel.getRoute().get(0).getName());
            destinationTextView.setText(parcel.getRoute().get(parcel.getRoute().size() - 1).getName());

            final TransportationStatus currentStatus = parcel.getReceipt().getTransportationStatus();
            if (currentStatus == null) {
                submitStatusBtn.setText(TransportationStatus.IN_TRANSIT.toString());
                return;
            }
            if (currentStatus == TransportationStatus.IN_TRANSIT) {
                submitStatusBtn.setText(TransportationStatus.ON_THE_WAY.toString());
                return;
            }
            if (currentStatus == TransportationStatus.ON_THE_WAY) {
                submitStatusBtn.setText(TransportationStatus.DELIVERED.toString());
                return;
            }
            if (currentStatus == TransportationStatus.DELIVERED || currentStatus == TransportationStatus.LOST) {
                submitStatusBtn.setVisibility(View.INVISIBLE);
            }
        });

        parcelStatusViewModel.selectTransitPoint(currentLocationId).observe(getViewLifecycleOwner(), transitPoint -> {
           if (transitPoint != null) {
               currentPointTextView.setText(transitPoint.getName());
           }
        });
    }
}