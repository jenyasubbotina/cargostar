package uz.alexits.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.fragment.MainFragmentDirections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent() != null) {
            final String pushIntentKey = getIntent().getStringExtra(IntentConstants.INTENT_PUSH_KEY);

            if (pushIntentKey != null) {
                if (pushIntentKey.equalsIgnoreCase(IntentConstants.REQUEST_PUBLIC_REQUESTS)) {
                    Navigation.findNavController(this, R.id.main_fragment_container).navigate(R.id.action_mainFragment_to_publicRequestsFragment);
                    return;
                }
                if (pushIntentKey.equalsIgnoreCase(IntentConstants.REQUEST_MY_REQUESTS)) {
                    Navigation.findNavController(this, R.id.main_fragment_container).navigate(R.id.action_mainFragment_to_myRequestsFragment);
                    return;
                }
                if (pushIntentKey.equalsIgnoreCase(IntentConstants.REQUEST_CURRENT_TRANSPORTATIONS)) {
                    Navigation.findNavController(this, R.id.main_fragment_container).navigate(R.id.action_mainFragment_to_currentTransportationsFragment);
                    return;
                }
            }
            if (getIntent().getIntExtra(IntentConstants.INTENT_REQUEST_KEY, -1) == IntentConstants.REQUEST_FIND_REQUEST) {
                final MainFragmentDirections.ActionMainFragmentToInvoiceDataFragment action =
                        MainFragmentDirections.actionMainFragmentToInvoiceDataFragment();
                action.setRequestId(getIntent().getLongExtra(Constants.KEY_REQUEST_ID, -1L));
                action.setInvoiceId(getIntent().getLongExtra(Constants.KEY_INVOICE_ID, -1L));
                action.setCourierId(getIntent().getLongExtra(Constants.KEY_COURIER_ID, -1L));
                action.setClientId(getIntent().getLongExtra(Constants.KEY_CLIENT_ID, -1L));
                action.setSenderCountryId(getIntent().getLongExtra(Constants.KEY_SENDER_COUNTRY_ID, -1L));
                action.setSenderRegionId(getIntent().getLongExtra(Constants.KEY_SENDER_REGION_ID, -1L));
                action.setSenderCityName(getIntent().getStringExtra(Constants.KEY_SENDER_CITY_NAME));
                action.setRecipientCountryId(getIntent().getLongExtra(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L));
                action.setRecipientCityName(getIntent().getStringExtra(Constants.KEY_RECIPIENT_CITY_NAME));
                action.setProviderId(getIntent().getLongExtra(Constants.KEY_PROVIDER_ID, -1L));
                Navigation.findNavController(this, R.id.main_fragment_container).navigate(action);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}