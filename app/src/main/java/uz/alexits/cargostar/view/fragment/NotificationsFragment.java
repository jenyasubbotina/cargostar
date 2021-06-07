package uz.alexits.cargostar.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.viewholder.NotificationViewHolder;
import uz.alexits.cargostar.viewmodel.NotificationViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.NotificationAdapter;
import uz.alexits.cargostar.view.callback.NotificationCallback;
import uz.alexits.cargostar.viewmodel.factory.NotificationViewModelFactory;

public class NotificationsFragment extends Fragment implements NotificationCallback {
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView profileImageView;
    private TextView badgeCounterTextView;
    private ImageView notificationsImageView;

    //main content views
    private NotificationAdapter notificationAdapter;

    private NotificationViewModel notificationViewModel;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final NotificationViewModelFactory notificationFactory = new NotificationViewModelFactory(requireContext());
        notificationViewModel = new ViewModelProvider(getViewModelStore(), notificationFactory).get(NotificationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        //header views
        fullNameTextView = requireActivity().findViewById(R.id.full_name_text_view);
        branchTextView = requireActivity().findViewById(R.id.branch_text_view);
        courierIdTextView = requireActivity().findViewById(R.id.courier_id_text_view);
        requestSearchEditText = requireActivity().findViewById(R.id.search_edit_text);
        requestSearchImageView = requireActivity().findViewById(R.id.search_btn);
        editImageView = requireActivity().findViewById(R.id.edit_image_view);
        createUserImageView = requireActivity().findViewById(R.id.create_user_image_view);
        calculatorImageView = requireActivity().findViewById(R.id.calculator_image_view);
        profileImageView = requireActivity().findViewById(R.id.profile_image_view);
        notificationsImageView = requireActivity().findViewById(R.id.notifications_image_view);
        badgeCounterTextView = requireActivity().findViewById(R.id.badge_counter_text_view);

        //main content views
        final RecyclerView notificationRecyclerView = root.findViewById(R.id.notificationRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        notificationAdapter = new NotificationAdapter(requireContext(), this);
        notificationRecyclerView.setHasFixedSize(false);
        notificationRecyclerView.setLayoutManager(layoutManager);
        notificationRecyclerView.setAdapter(notificationAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views

        profileImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.mainFragment);
        });

        createUserImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.createUserFragment);
        });

        calculatorImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.calculatorFragment);
        });

        editImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.profileFragment);
        });

        notificationsImageView.setOnClickListener(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        notificationViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        notificationViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        notificationViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        notificationViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        //init notificationList
        notificationViewModel.selectAllNotifications().observe(getViewLifecycleOwner(), notificationList -> {
            notificationAdapter.setNotificationList(notificationList);
        });

    }

    @Override
    public void onNotificationSelected(Notification currentItem, NotificationViewHolder holder) {
        currentItem.setRead(true);
        notificationViewModel.readNotification(currentItem.getId());

        if (currentItem.getLink().equalsIgnoreCase(IntentConstants.REQUEST_PUBLIC_REQUESTS)) {
            NavHostFragment.findNavController(this).navigate(R.id.action_notificationsFragment_to_publicRequestsFragment);
            return;
        }
        if (currentItem.getLink().equalsIgnoreCase(IntentConstants.REQUEST_MY_REQUESTS)) {
            NavHostFragment.findNavController(this).navigate(R.id.action_notificationsFragment_to_myRequestsFragment);
            return;
        }
        if (currentItem.getLink().equalsIgnoreCase(IntentConstants.REQUEST_CURRENT_TRANSPORTATIONS)) {
            NavHostFragment.findNavController(this).navigate(NotificationsFragmentDirections.actionNotificationsFragmentToTransportationsFragment());
        }
    }

    private static final String TAG = NotificationsFragment.class.toString();
}