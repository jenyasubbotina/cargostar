package uz.alexits.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.fragment.MainFragmentDirections;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_EXIT = 0x00;
    public static final int PICK_IMAGE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent() != null) {
            if (getIntent().getIntExtra(Constants.INTENT_REQUEST_KEY, -1) == Constants.REQUEST_PUBLIC_REQUESTS) {
                Navigation.findNavController(this, R.id.main_fragment_container).navigate(R.id.action_mainFragment_to_publicBidsFragment);
                return;
            }
            if (getIntent().getIntExtra(Constants.INTENT_REQUEST_KEY, -1) == Constants.REQUEST_MY_REQUESTS) {
                Navigation.findNavController(this, R.id.main_fragment_container).navigate(R.id.action_mainFragment_to_myBidsFragment);
                return;
            }
            if (getIntent().getIntExtra(Constants.INTENT_REQUEST_KEY, -1) == Constants.REQUEST_FIND_PARCEL) {
                final MainFragmentDirections.ActionMainFragmentToParcelDataFragment action =
                        MainFragmentDirections.actionMainFragmentToParcelDataFragment();
                action.setParcelId(getIntent().getLongExtra(Constants.INTENT_REQUEST_VALUE, -1));
                Navigation.findNavController(this, R.id.main_fragment_container).navigate(action);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(MainActivity.class.toString(), "onActivityResult: " + requestCode + " " + resultCode + " " + data);
        if (requestCode == PICK_IMAGE) {

        }
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
}