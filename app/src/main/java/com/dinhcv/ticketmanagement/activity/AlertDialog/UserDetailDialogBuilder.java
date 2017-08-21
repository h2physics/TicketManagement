package com.dinhcv.ticketmanagement.activity.AlertDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.model.structure.User.UserInformation;
import com.dinhcv.ticketmanagement.utils.DataUtils;

/**
 * Created by YukiNoHara on 6/9/2017.
 */

public class UserDetailDialogBuilder {
    private static final String LOG_TAG = UserDetailDialogBuilder.class.getSimpleName();

    private EditText edt_account;
    private EditText edt_password;
    private EditText edt_repassword;
    private EditText edt_name;
    private RadioButton radio1;
    private RadioButton radio2;
    private RadioGroup radioGroup;
    private Dialog mDialog = null;
    private int mId = 0;

    public interface OnRereadingStatusListener{
        void onRereadingStatus(Context context, String account, String name, int permission);
    }

    public Dialog create(final Context cnt, UserInformation user, final OnRereadingStatusListener listener)
    {
        //create the actual dialog
        mDialog = new DialogBase( cnt );

        mDialog.setTitle(R.string.user_infor);
        //override layout
        mDialog.setContentView(R.layout.dialog_add_account);
        UserInformation userCurrent = DataUtils.queryCurrentUserInfo(cnt);

        radioGroup = (RadioGroup) mDialog.findViewById(R.id.rdoGroup);
        edt_account = (EditText) mDialog.findViewById(R.id.edt_account);
        edt_password = (EditText) mDialog.findViewById(R.id.edt_password);
        edt_repassword = (EditText) mDialog.findViewById(R.id.edt_repassword);
        edt_name = (EditText) mDialog.findViewById(R.id.edt_name);
        radio1 = (RadioButton) mDialog.findViewById(R.id.radio_1);
        radio2 = (RadioButton) mDialog.findViewById(R.id.radio_2);
        if (userCurrent.getRole().getRoleId() == 2){
            radio2.setVisibility(View.GONE);
        }
        edt_password.setVisibility(View.GONE);
        edt_repassword.setVisibility(View.GONE);

        mDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        edt_account.setText(user.getEmail());
        edt_name.setText(user.getName());
        if (user.getRole().getRoleId() == 2){
            radio2.setChecked(true);
        } else if (user.getRole().getRoleId() == 3){
            radio1.setChecked(true);
        }

        final Button btnSave = (Button)mDialog.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edt_account.getText().toString().trim();
                if (!checkValidateEmail(account)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(cnt);
                    alert.setTitle(   R.string.login_error_title );
                    alert.setMessage( R.string.email_error );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    return;
                }

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(selectedId);
                int flag = radioGroup.indexOfChild(radioButton);
                int role = flag + 2;
                Log.e(LOG_TAG, "Selected radio id: " + flag);

                if ( account.isEmpty() ) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(mDialog.getContext());

                    alert.setTitle(   R.string.error_title );
                    alert.setMessage( R.string.account_is_null );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    return;
                }

                String name = edt_name.getText().toString().trim();
                if ( name.isEmpty() ) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(mDialog.getContext());

                    alert.setTitle(   R.string.error_title );
                    alert.setMessage( R.string.name_is_null );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    return;
                }

                if (listener != null) {
                    listener.onRereadingStatus(cnt, account, name, role);
                    mDialog.dismiss();
                }
            }
        });

        return mDialog;
    }

    public boolean checkValidateEmail(String email){
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(EMAIL_REGEX);
    }

}
