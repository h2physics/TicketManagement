package com.dinhcv.ticketmanagement.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.activity.AlertDialog.UserCreateDialogBuilder;
import com.dinhcv.ticketmanagement.activity.AlertDialog.UserDetailDialogBuilder;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.User.UserInformation;
import com.dinhcv.ticketmanagement.model.structure.User.UserRequest;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
import com.dinhcv.ticketmanagement.utils.DataUtils;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private static final String LOG_TAG = UserListActivity.class.getSimpleName();

    //Element View
    private ListView mListView;
    private Dialog dialogDetail;
    private TextView tv_userTotal;
    private ImageButton btn_addUser;

    //Variable
    private List<UserInformation> mList;
    private UserInformation user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mList = new ArrayList<>();

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = DataUtils.queryCurrentUserInfo(getApplicationContext());
        new UserListTask().execute();
    }

    private void initView(){

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.user_manager_title));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        btn_addUser = (ImageButton) findViewById(R.id.btn_addUser);
        tv_userTotal = (TextView) findViewById(R.id.tv_userTotal);
        mListView = (ListView) findViewById(R.id.lv_user);
        btn_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    /**
     * Get account data
     */
    private void initData() {
        if ((mList != null) && (mList.size() >0)) {
            tv_userTotal.setText(String.valueOf(mList.size()));
            AccountAdapter accountAdapter = new AccountAdapter(UserListActivity.this, mList);
            mListView.setAdapter(accountAdapter);
        }

        btn_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }


    public class AccountAdapter extends BaseAdapter {
        private List<UserInformation> mListAccount;
        private Context mContext;

        AccountAdapter(Context context, List<UserInformation> list) {
            this.mContext = context;
            mListAccount = list;
        }

        @Override
        public int getCount() {
            return mListAccount.size();
        }

        @Override
        public Object getItem(int position) {
            return mListAccount.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_user_list_row, null, false);
            }

            final UserInformation userMember = mListAccount.get(position);
            if (userMember == null) {
                return null;
            }


            final TextView tv_no = ((TextView) convertView.findViewById(R.id.tv_no));
            final TextView tv_account = ((TextView) convertView.findViewById(R.id.tv_account));
            final TextView tv_name= ((TextView) convertView.findViewById(R.id.tv_name));
            final TextView tv_role = ((TextView) convertView.findViewById(R.id.tv_role));
            final Button btn_delete = ((Button) convertView.findViewById(R.id.btn_delete));

            int count = parent.getChildCount();

            //set other background by odd/even
            convertView.setBackgroundResource((count % 2 == 0) ?
                    R.drawable.table_background_row_even_selector :
                    R.drawable.table_background_row_odd_selector);

            //set data
            tv_no.setText(String.valueOf(position + 1));
            tv_account.setText(userMember.getEmail());
            tv_name.setText(userMember.getName());
            String permission = getResources().getString(R.string.look_car);
            if (userMember.getRole().getRoleId() == 2){
                permission = getResources().getString(R.string.manager);
            } else if(userMember.getRole().getRoleId() == 1){
                permission = getResources().getString(R.string.admin);
            }
            tv_role.setText(permission);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // gotoAccountDetail(user);
                }
            });

            //handle event

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UserListActivity.this);
                    alert.setTitle(R.string.delete_confirm_title);
                    alert.setMessage(R.string.delete_confirm);
                    alert.setIcon(R.drawable.dialog_warning_icon);
                    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteAccount(userMember);
                        }
                    });
                    alert.setNegativeButton(R.string.cancel, null);
                    alert.show();
                }
            });


            return convertView;
        }
    }

    private class UserListTask extends AsyncTask<String, Void, Void>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( UserListActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            NetworkUtils.getUserBeingManagedFromServer(getApplicationContext(), new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    mList = DataUtils.getUserList(getApplicationContext(), ManagerContract.UserInfoBeingManagedEntry.CONTENT_URI);
                    initData();
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {
                    Log.e(LOG_TAG, "On error");
                }

                @Override
                public void onFailed() {
                    Log.e(LOG_TAG, "On failed");
                }
            });
            return null;
        }
    }

    private class DeleteUserTask extends AsyncTask<String, Void, Void>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( UserListActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            NetworkUtils.deleteUser(getApplicationContext(), params[0], new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    mList = DataUtils.getUserList(getApplicationContext(), ManagerContract.UserInfoBeingManagedEntry.CONTENT_URI);
                    initData();
                    AlertDialog.Builder alert = new AlertDialog.Builder(UserListActivity.this);
                    alert.setTitle(R.string.warning_title);
                    alert.setMessage(R.string.delete_successfully);
                    alert.setIcon(R.drawable.dialog_warning_icon);
                    alert.setPositiveButton(R.string.ok, null);
                    alert.show();
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {
                    progressDialog.dismiss();
                }

                @Override
                public void onFailed() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UserListActivity.this);
                    alert.setTitle(R.string.error_title);
                    alert.setMessage(R.string.delete_failed);
                    alert.setIcon(R.drawable.dialog_warning_icon);
                    alert.setPositiveButton(R.string.ok, null);
                    alert.show();
                    progressDialog.dismiss();
                }
            });
            return null;
        }
    }

    private void gotoAccountDetail(UserInformation user) {
        dialogDetail = new UserDetailDialogBuilder().create(UserListActivity.this, user, new UserDetailDialogBuilder.OnRereadingStatusListener() {
            @Override
            public void onRereadingStatus(Context context, String account, String name, int permission) {

            }
        });

        dialogDetail.show();
    }

    /**
     * Delete account
     *
     */
    private void deleteAccount(UserInformation userMember) {
        if (user.getRole().getRoleId() == 1){
            if (userMember.getRole().getRoleId() == 1){
                AlertDialog.Builder alert = new AlertDialog.Builder(UserListActivity.this);
                alert.setTitle(R.string.error_title);
                alert.setMessage(R.string.cannot_delete_co_worker);
                alert.setIcon(R.drawable.dialog_warning_icon);
                alert.setPositiveButton(R.string.ok, null);
                alert.show();
                return;
            }
        }

        if (user.getEmail().equals(userMember.getEmail())){
            AlertDialog.Builder alert = new AlertDialog.Builder(UserListActivity.this);
            alert.setTitle(R.string.error_title);
            alert.setMessage(R.string.cannot_delete);
            alert.setIcon(R.drawable.dialog_warning_icon);
            alert.setPositiveButton(R.string.ok, null);
            alert.show();
            return;
        }

        new DeleteUserTask().execute(String.valueOf(userMember.getId()));
    }

    private void createAccount() {
        dialogDetail = new UserCreateDialogBuilder().create(UserListActivity.this, new UserCreateDialogBuilder.OnRereadingStatusListener() {
            @Override
            public void onRereadingStatus(final Context context, String account, String password, String name, int permission) {
                UserRequest userRequest = new UserRequest(account, password, name, permission, user.getParkId());
                NetworkUtils.postCreateUser(context, userRequest, new NetworkExecuteCompleted() {
                    @Override
                    public void onSuccess() {
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                        alert.setTitle(   R.string.warning_title );
                        alert.setMessage( R.string.register_status_success );
                        alert.setIcon(    R.drawable.dialog_info_icon );
                        alert.setPositiveButton(R.string.ok, null );
                        alert.show();
                        new UserListTask().execute();
                    }

                    @Override
                    public void onError() {
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                        alert.setTitle(   R.string.warning_title );
                        alert.setMessage( R.string.register_status_error );
                        alert.setIcon(    R.drawable.dialog_info_icon );
                        alert.setPositiveButton(R.string.ok, null );
                        alert.show();
                        new UserListTask().execute();
                    }

                    @Override
                    public void onFailed() {
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                        alert.setTitle(   R.string.warning_title );
                        alert.setMessage( R.string.register_status_failed );
                        alert.setIcon(    R.drawable.dialog_info_icon );
                        alert.setPositiveButton(R.string.ok, null );
                        alert.show();
                        new UserListTask().execute();
                    }
                });

            }
        });

        dialogDetail.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.back_menu:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);


    }
}
