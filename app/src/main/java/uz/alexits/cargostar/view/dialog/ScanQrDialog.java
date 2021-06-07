package uz.alexits.cargostar.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.ScanQrActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ScanQrDialog extends DialogFragment {
    private int position;
    private int requestCode;

    public ScanQrDialog() { }

    public static ScanQrDialog newInstance(final int position, final int requestCode) {
        ScanQrDialog dialog = new ScanQrDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_QR_POSITION, position);
        args.putInt(Constants.REQUEST_CODE, requestCode);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt(Constants.KEY_QR_POSITION);
            requestCode = getArguments().getInt(Constants.REQUEST_CODE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View root = inflater.inflate(R.layout.dialog_scan_qr, null, false);

        final EditText qrCodeEditText = root.findViewById(R.id.qr_code_edit_text);
        final Button submitBtn = root.findViewById(R.id.submit_btn);
        final Button scanQrBtn = root.findViewById(R.id.scan_qr_btn);

        qrCodeEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        submitBtn.setOnClickListener(v -> {
            final String qrCode = qrCodeEditText.getText().toString().trim();

            if (TextUtils.isEmpty(qrCode)) {
                Toast.makeText(requireContext(), "Введите QR-code", Toast.LENGTH_SHORT).show();
                return;
            }
            if (getTargetFragment() == null) {
                Log.e(TAG, "onViewCreated(): internal error");
                Toast.makeText(requireContext(), "Произошла внутренняя ошибка", Toast.LENGTH_SHORT).show();
                return;
            }

            String scannedData = qrCode;
            int scanType = 0;

            if (qrCode.length() - 12 >= 0) {
                scannedData = qrCode.substring(qrCode.length() - 12);
                scanType = 1;
            }
            else {
                scanType = 2;
            }
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent()
                            .putExtra(Constants.SCAN_TYPE, scanType)
                            .putExtra(IntentConstants.INTENT_RESULT_VALUE, scannedData)
                            .putExtra(Constants.KEY_QR_POSITION, position));
            dismiss();
        });

        scanQrBtn.setOnClickListener(v -> {
            startActivityForResult(new Intent(requireContext(), ScanQrActivity.class), requestCode);
        });

        builder.setView(root);
        return builder.create();
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Log.w(TAG, "onActivityResult(): cancelled");
            dismiss();
            return;
        }
        if (data == null) {
            Log.w(TAG, "onActivityResult(): data is NULL");
            dismiss();
            return;
        }
        if (getTargetFragment() == null) {
            Log.w(TAG, "onActivityResult(): targetFragment is NULL");
            dismiss();
            return;
        }
        if (resultCode == RESULT_OK) {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent()
                            .putExtra(IntentConstants.INTENT_RESULT_VALUE, data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE))
                            .putExtra(Constants. SCAN_TYPE, data.getIntExtra(Constants.SCAN_TYPE, 0))
                            .putExtra(Constants.KEY_QR_POSITION, position));
            dismiss();
        }
    }

    private static final String TAG = ScanQrDialog.class.toString();
}