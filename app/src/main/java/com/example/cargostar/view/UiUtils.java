package com.example.cargostar.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cargostar.R;
import com.example.cargostar.view.activity.CalculatorActivity;
import com.example.cargostar.view.activity.CreateUserActivity;
import com.example.cargostar.view.activity.MainActivity;
import com.example.cargostar.view.activity.NotificationsActivity;
import com.example.cargostar.view.activity.ProfileActivity;

public class UiUtils {
//    public static void initAppBar(final Activity activity,
//                                  final int toEditProfile,
//                                  final int toProfile,
//                                  final int toCreateUser,
//                                  final int toCalculator,
//                                  final int toNotifications) {
//        final ImageView editUserImageView = activity.findViewById(toEditProfile);
//        final ImageView profileImageView = activity.findViewById(toProfile);
//        final ImageView createUserImageView = activity.findViewById(toCreateUser);
//        final ImageView calculatorImageView = activity.findViewById(toCalculator);
//        final ImageView notificationsImageView = activity.findViewById(toNotifications);
//
//        if (toEditProfile != -1) {
//            editUserImageView.setOnClickListener(v -> {
//                activity.startActivityForResult(new Intent(activity, ProfileActivity.class), MainActivity.REQUEST_EXIT);
//            });
//        }
//        if (toProfile != -1) {
//            profileImageView.setOnClickListener(v -> {
//                activity.startActivity(new Intent(activity, MainActivity.class));
//            });
//        }
//        if (toCreateUser != -1) {
//            createUserImageView.setOnClickListener(v -> {
//                activity.startActivity(new Intent(activity, CreateUserActivity.class));
//            });
//        }
//        if (toCalculator != -1) {
//            calculatorImageView.setOnClickListener(v -> {
//                activity.startActivity(new Intent(activity, CalculatorActivity.class));
//            });
//        }
//        if (toNotifications != -1) {
//            notificationsImageView.setOnClickListener(v -> {
//                activity.startActivity(new Intent(activity, NotificationsActivity.class));
//            });
//        }
//    }

    public static void onFocusChanged(final EditText editText, final boolean hasFocus) {
        if (hasFocus) {
            editText.setBackgroundResource(R.drawable.edit_text_active);
            return;
        }
        if (editText.getText().length() > 0) {
            editText.setBackgroundResource(R.drawable.edit_text_active);
            return;
        }
        editText.setBackgroundResource(R.drawable.edit_text_locked);
    }
}
