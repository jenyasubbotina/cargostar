package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import uz.alexits.cargostar.R;

public class PartyStatusFragment extends Fragment
//        implements PartyCallback
{
    private Context context;
    private FragmentActivity activity;

    private RecyclerView parcelsRecyclerView;
//    private List<Shipping> shippingList;

    private Button submitStatusBtn;

    private ImageView checkImageView;
    private AnimatedVectorDrawable vectorDrawable;
    private AnimatedVectorDrawableCompat vectorDrawableCompat;

    public PartyStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        activity = getActivity();

//        final Country uzbekistan = new Country("Uzbekistan");
//        final City tashkent = new City("Tashkent");
//        final City samarkand = new City("Samarkand");
//
//        final Address senderAddress = new Address();
//        senderAddress.setCountry(uzbekistan);
//        senderAddress.setRegion("Tashkent");
//        senderAddress.setCity(tashkent);
//        senderAddress.setAddress("Mirabad str., 76");
//        senderAddress.setPostIndex("100440");
//
//        final Address receiverAddress = new Address();
//        receiverAddress.setCountry(uzbekistan);
//        receiverAddress.setRegion("Samarkand");
//        receiverAddress.setCity(samarkand);
//        receiverAddress.setAddress("Navoi str., 12");
//        receiverAddress.setPostIndex("230180");
//
//        final Shipping inTransitShipping = new Shipping("P1293819208309");
//        inTransitShipping.setSenderAddress(senderAddress);
//        inTransitShipping.setRecipientAddress(receiverAddress);
//
//        final Shipping onTheWayShipping = new Shipping("P129189048392");
//        onTheWayShipping.setSenderAddress(senderAddress);
//        onTheWayShipping.setRecipientAddress(receiverAddress);
//
//        final Shipping lostShipping = new Shipping("P878798239392");
//        lostShipping.setSenderAddress(senderAddress);
//        lostShipping.setRecipientAddress(receiverAddress);
//
//        final Shipping deliveredShipping = new Shipping("P293928982922");
//        deliveredShipping.setSenderAddress(senderAddress);
//        deliveredShipping.setRecipientAddress(receiverAddress);
//
//        shippingList = new ArrayList<>();
//        shippingList.add(inTransitShipping);
//        shippingList.add(onTheWayShipping);
//        shippingList.add(lostShipping);
//        shippingList.add(deliveredShipping);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_party_status, container, false);

        parcelsRecyclerView = root.findViewById(R.id.parcels_recycler_view);
        submitStatusBtn = root.findViewById(R.id.submit_status_btn);
        checkImageView = root.findViewById(R.id.check_image_view);

//        final PartyParcelAdapter adapter = new PartyParcelAdapter(context, shippingList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        parcelsRecyclerView.setLayoutManager(layoutManager);
//        parcelsRecyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

//    @Override
//    public void onPartySelected(Shipping currentItem) {
//        NavHostFragment.findNavController(this).navigate(R.id.action_partyStatusFragment_to_parcelStatusFragment);
//    }
}