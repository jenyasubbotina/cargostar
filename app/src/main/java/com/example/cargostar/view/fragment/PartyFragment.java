package com.example.cargostar.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cargostar.R;
import com.example.cargostar.view.adapter.PartyParcelAdapter;
import com.example.cargostar.view.callback.PartyCallback;

import java.util.List;

public class PartyFragment extends Fragment
//        implements PartyCallback
{
    private Context context;
    private FragmentActivity activity;
    private TextView partyIdTextView;
    private TextView partyQuantityTextView;

    private RecyclerView parcelsRecyclerView;
//    private List<Shipping> shippingList;

    public PartyFragment() {
        // Required empty public constructor
        // get current party with shippingList in it
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        if (getArguments() != null) {
            Log.i(PartyFragment.class.toString(), "args: " + getArguments());
        }

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
        final View root = inflater.inflate(R.layout.fragment_party, container, false);

        partyIdTextView = root.findViewById(R.id.party_id_text_view);
        partyQuantityTextView = root.findViewById(R.id.quantity_text_view);
        parcelsRecyclerView = root.findViewById(R.id.parcels_recycler_view);
//        final PartyParcelAdapter adapter = new PartyParcelAdapter(context, shippingList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        parcelsRecyclerView.setLayoutManager(layoutManager);
//        parcelsRecyclerView.setAdapter(adapter);

        return root;
    }

//    @Override
//    public void onPartySelected(Shipping currentItem) {
//        NavHostFragment.findNavController(this).navigate(R.id.action_partyFragment_to_parcelDataFragment);
//    }
}