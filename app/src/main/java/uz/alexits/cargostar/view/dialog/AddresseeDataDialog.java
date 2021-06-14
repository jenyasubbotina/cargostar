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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.SignatureActivity;

public class AddresseeDataDialog extends DialogFragment {
    private long invoiceId;

    public AddresseeDataDialog() { }

    public static AddresseeDataDialog newInstance(final long invoiceId) {
        AddresseeDataDialog dialog = new AddresseeDataDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_INVOICE_ID, invoiceId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.invoiceId = getArguments().getLong(Constants.KEY_INVOICE_ID, 0);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View root = inflater.inflate(R.layout.dialog_addressee_data, null, false);

        final RadioGroup acceptanceRadioGroup = root.findViewById(R.id.acceptance_radio_group);
        final RadioButton acceptedRadioBtn = root.findViewById(R.id.accepted_radio_btn);
        final RadioButton refusedRadioBtn = root.findViewById(R.id.refused_radio_btn);

        final TextView fullNameTextView = root.findViewById(R.id.full_name_text_view);
        final TextView phoneTextView = root.findViewById(R.id.phone_text_view);
        final TextView addressTextView = root.findViewById(R.id.address_text_view);
        final TextView companyTextView = root.findViewById(R.id.company_text_view);

        final EditText fullNameEditText = root.findViewById(R.id.full_name_edit_text);
        final EditText phoneEditText = root.findViewById(R.id.phone_edit_text);
        final EditText addressEditText = root.findViewById(R.id.address_edit_text);
        final EditText companyEditText = root.findViewById(R.id.company_edit_text);
        final EditText commentEditText = root.findViewById(R.id.comment_edit_text);

        final Button signBtn = root.findViewById(R.id.sign_btn);

        fullNameEditText.addTextChangedListener(UiUtils.getOnTextChanged(fullNameEditText));
        phoneEditText.addTextChangedListener(UiUtils.getOnTextChanged(phoneEditText));
        addressEditText.addTextChangedListener(UiUtils.getOnTextChanged(addressEditText));
        companyEditText.addTextChangedListener(UiUtils.getOnTextChanged(companyEditText));
        commentEditText.addTextChangedListener(UiUtils.getOnTextChanged(commentEditText));

        acceptanceRadioGroup.setOnCheckedChangeListener((v, checkedId)-> {
            if (checkedId == acceptedRadioBtn.getId()) {
                fullNameTextView.setVisibility(View.VISIBLE);
                fullNameEditText.setVisibility(View.VISIBLE);
                phoneTextView.setVisibility(View.VISIBLE);
                phoneEditText.setVisibility(View.VISIBLE);
                addressTextView.setVisibility(View.VISIBLE);
                addressEditText.setVisibility(View.VISIBLE);
                companyTextView.setVisibility(View.VISIBLE);
                companyEditText.setVisibility(View.VISIBLE);
            }
            if (checkedId == refusedRadioBtn.getId()) {
                fullNameTextView.setVisibility(View.GONE);
                fullNameEditText.setVisibility(View.GONE);
                phoneTextView.setVisibility(View.GONE);
                phoneEditText.setVisibility(View.GONE);
                addressTextView.setVisibility(View.GONE);
                addressEditText.setVisibility(View.GONE);
                companyTextView.setVisibility(View.GONE);
                companyEditText.setVisibility(View.GONE);
            }
        });

        signBtn.setOnClickListener(v -> {
            if (acceptanceRadioGroup.getCheckedRadioButtonId() != acceptedRadioBtn.getId() && acceptanceRadioGroup.getCheckedRadioButtonId() != refusedRadioBtn.getId()) {
                Toast.makeText(requireContext(), "Выберите одну из опций: Получено/Отказано", Toast.LENGTH_SHORT).show();
                return;
            }
            final String fullName = fullNameEditText.getText().toString().trim();
            final String phone = phoneEditText.getText().toString().trim();
            final String address = addressEditText.getText().toString().trim();
            final String company = companyEditText.getText().toString().trim();
            final String comment = commentEditText.getText().toString().trim();

            if (acceptanceRadioGroup.getCheckedRadioButtonId() == refusedRadioBtn.getId() && TextUtils.isEmpty(comment)) {
                Toast.makeText(requireContext(), "При Отказе обязательно добавьте комментарий", Toast.LENGTH_SHORT).show();
                return;
            }
            if (acceptedRadioBtn.isChecked()) {
                startActivityForResult(new Intent(requireContext(), SignatureActivity.class)
                        .putExtra(Constants.KEY_INVOICE_ID, invoiceId)
                        .putExtra(Constants.ADDRESSEE_FULL_NAME, fullName)
                        .putExtra(Constants.ADDRESSEE_PHONE, phone)
                        .putExtra(Constants.ADDRESSEE_ADDRESS, address)
                        .putExtra(Constants.ADDRESSEE_ORGANIZATION, company)
                        .putExtra(Constants.ADDRESSEE_COMMENT, comment)
                        .putExtra(Constants.ADDRESSEE_IS_ACCEPTED, true), IntentConstants.REQUEST_RECIPIENT_SIGNATURE);
                return;
            }
            if (refusedRadioBtn.isChecked()) {
                startActivityForResult(new Intent(requireContext(), SignatureActivity.class)
                        .putExtra(Constants.KEY_INVOICE_ID, invoiceId)
                        .putExtra(Constants.ADDRESSEE_COMMENT, comment)
                        .putExtra(Constants.ADDRESSEE_IS_ACCEPTED, false), IntentConstants.REQUEST_RECIPIENT_SIGNATURE);
            }
        });

        builder.setView(root);
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (getTargetFragment() != null && data != null) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent()
                        .putExtra(Constants.KEY_INVOICE_ID, data.getLongExtra(Constants.KEY_INVOICE_ID, 0L))
                        .putExtra(Constants.ADDRESSEE_FULL_NAME, data.getStringExtra(Constants.ADDRESSEE_FULL_NAME))
                        .putExtra(Constants.ADDRESSEE_PHONE, data.getStringExtra(Constants.ADDRESSEE_PHONE))
                        .putExtra(Constants.ADDRESSEE_ADDRESS, data.getStringExtra(Constants.ADDRESSEE_ADDRESS))
                        .putExtra(Constants.ADDRESSEE_ORGANIZATION, data.getStringExtra(Constants.ADDRESSEE_ORGANIZATION))
                        .putExtra(Constants.ADDRESSEE_SIGNATURE, data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE))
                        .putExtra(Constants.ADDRESSEE_SIGNATURE_DATE, data.getStringExtra(Constants.ADDRESSEE_SIGNATURE_DATE))
                        .putExtra(Constants.ADDRESSEE_COMMENT, data.getStringExtra(Constants.ADDRESSEE_COMMENT))
                        .putExtra(Constants.ADDRESSEE_IS_ACCEPTED, data.getBooleanExtra(Constants.ADDRESSEE_IS_ACCEPTED, false)));
                dismiss();
            }
        }
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    private static final String TAG = AddresseeDataDialog.class.toString();
}