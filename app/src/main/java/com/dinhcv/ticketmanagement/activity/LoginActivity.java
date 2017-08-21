package com.dinhcv.ticketmanagement.activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.model.structure.AccessToken.AccessTokenBody;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;
import com.dinhcv.ticketmanagement.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private EditText edt_user;
    private EditText edt_password;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onStart() {
        super.onStart();

        isLogin = PreferenceUtils.getIsLogin(getApplicationContext());
        Log.e(LOG_TAG, "Is login: " + isLogin);
        if (!isLogin){
            initView();
        }else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
//            new UpdateLoginTimeTask().execute();
        }
    }

    private void initView(){
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.login_title));
        toolBar.setLogo(R.mipmap.ic_launcher);
        toolBar.showOverflowMenu();

        edt_user = (EditText) findViewById(R.id.edt_user);
        edt_password = (EditText) findViewById(R.id.edt_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = edt_user.getText().toString().trim();
                String pass = edt_password.getText().toString().trim();

                // verify
                boolean isOk = verifyData(user, pass);
                if (!isOk){
                    return;
                }

                // login
                new LoginTask().execute(user, pass);

            }
        });

    }



    private boolean verifyData(String user, String pass){

        if (user.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);

            alert.setTitle(   R.string.error_title );
            alert.setMessage( R.string.account_is_null );
            alert.setIcon(    R.drawable.dialog_warning_icon );
            alert.setPositiveButton(R.string.ok, null );
            alert.show();
            return false;
        }

        if (pass.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);

            alert.setTitle(   R.string.error_title );
            alert.setMessage( R.string.password_is_null );
            alert.setIcon(    R.drawable.dialog_warning_icon );
            alert.setPositiveButton(R.string.ok, null );
            alert.show();
            return false;
        }

        return true;
    }

    class LoginTask extends AsyncTask<String, Void, Boolean > {

        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog( LoginActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String pass = params[1];

            AccessTokenBody accessTokenBody = new AccessTokenBody(getString(R.string.client_id),
                                                                  getString(R.string.client_secret),
                                                                  getString(R.string.grant_type),
                                                                  username,
                                                                  pass);

            NetworkUtils.getAccessTokenFromServer(getApplicationContext(), accessTokenBody, new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    NetworkUtils.getUserInfoFromServer(getApplicationContext(), new NetworkExecuteCompleted() {
                        @Override
                        public void onSuccess() {
                            PreferenceUtils.setIsLogin(getApplicationContext(), true);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError() {
                            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                            alert.setTitle(   R.string.login_error_title );
                            alert.setMessage( R.string.undefined_error );
                            alert.setIcon(    R.drawable.dialog_warning_icon );
                            alert.setPositiveButton(R.string.ok, null );
                            alert.show();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailed() {
                            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                            alert.setTitle(   R.string.login_error_title );
                            alert.setMessage( R.string.undefined_error );
                            alert.setIcon(    R.drawable.dialog_warning_icon );
                            alert.setPositiveButton(R.string.ok, null );
                            alert.show();
                            progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onError() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle(   R.string.login_error_title );
                    alert.setMessage( R.string.login_error );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailed() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle(   R.string.login_error_title );
                    alert.setMessage( R.string.internet_error );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }
            });
            return true;

        }

        @Override
        protected void onPostExecute(Boolean isLogin) {
            super.onPostExecute(isLogin);
        }
    }

    private class UpdateLoginTimeTask extends AsyncTask<Void, Void, Void>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog( LoginActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            NetworkUtils.updateUserTimeLogin(getApplicationContext(), new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    PreferenceUtils.setIsLogin(getApplicationContext(), true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle(   R.string.login_error_title );
                    alert.setMessage( R.string.undefined_error );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailed() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle(   R.string.login_error_title );
                    alert.setMessage( R.string.undefined_error );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }
            });
            return null;
        }
    }


}
