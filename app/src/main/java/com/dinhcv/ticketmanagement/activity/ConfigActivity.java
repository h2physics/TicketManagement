package com.dinhcv.ticketmanagement.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.activity.AlertDialog.AboutDialogBuilder;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Park.Park;
import com.dinhcv.ticketmanagement.model.structure.Park.ParkRequest;
import com.dinhcv.ticketmanagement.model.structure.Price.Price;
import com.dinhcv.ticketmanagement.model.structure.User.UserInformation;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
import com.dinhcv.ticketmanagement.utils.DataUtils;

import static android.view.View.GONE;

public class ConfigActivity extends AppCompatActivity {
    private static final String LOG_TAG = ConfigActivity.class.getSimpleName();

    //View element
    private TextView edt_time1, edt_time2, edt_money1, edt_money2;
    private Button btn_save, btn_cancel, btn_log, btn_about;
    private EditText edt_parking, edt_address, edt_hotline, edt_website;
    private TextView tvConfigPriceLabel;
    private TextView tvNextBlockLabel;
    private LinearLayout llNextBlock;
    private LinearLayout llTypePrice;
    private TextView tvTypePriceValue;
    private TextView tvLimitedMoneyLabel;
    private LinearLayout llLimitedMoney;
    private EditText edt_limited_money;

    //Variable
    private UserInformation user;
    private Park park;
    private Price price;
    private int typeMoney;
    private ParkRequest parkRequest;

    public static final int INDEX_PARK_ID = 0;
    public static final int INDEX_PARK_NAME = 1;
    public static final int INDEX_PARK_ADDRESS = 2;
    public static final int INDEX_PARK_HOTLINE = 3;
    public static final int INDEX_PARK_WEBSITE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = DataUtils.queryCurrentUserInfo(getApplicationContext());
        new ParkTask().execute(String.valueOf(user.getId()));
    }

    private void initView(){
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.config));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        edt_time1 = (EditText) findViewById(R.id.edt_time1);
        edt_money1 = (EditText) findViewById(R.id.edt_money1);
        edt_time2 = (EditText) findViewById(R.id.edt_time2);
        edt_money2 = (EditText) findViewById(R.id.edt_money2);

        edt_parking = (EditText) findViewById(R.id.edt_parking);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_hotline = (EditText) findViewById(R.id.edt_hotline);
        edt_website = (EditText) findViewById(R.id.edt_website);

        tvConfigPriceLabel = (TextView) findViewById(R.id.tv_config_price_type);
        tvNextBlockLabel = (TextView) findViewById(R.id.tv_next_block_label);
        llNextBlock = (LinearLayout)findViewById(R.id.ll_next_block);
        llTypePrice = (LinearLayout) findViewById(R.id.ll_type_price);
        tvTypePriceValue = (TextView) findViewById(R.id.tv_type_price_value);
        tvLimitedMoneyLabel = (TextView) findViewById(R.id.tv_limited_money_label);
        llLimitedMoney = (LinearLayout) findViewById(R.id.ll_limited_money);
        edt_limited_money = (EditText) findViewById(R.id.edt_limited_money);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_log = (Button) findViewById(R.id.btn_log);
        btn_about = (Button) findViewById(R.id.btn_about);

    }

    private void initData(){

        if (price.getType() == 2){
            showTypeMoney2();
            tvTypePriceValue.setText(getString(R.string.type_price_value_2));
            edt_time1.setText(price.getTime_block_1());
            edt_time2.setText(price.getTime_block_2());
            edt_money1.setText(String.valueOf(price.getCost_block_1()));
            edt_money2.setText(String.valueOf(price.getCost_block_2()));
            edt_limited_money.setText(String.valueOf(price.getTotal_price()));
        } else if (price.getType() == 1){
            hideTypeMoney2();
            tvTypePriceValue.setText(getString(R.string.type_price_value_1));
            edt_time1.setText(String.valueOf(price.getTime_block_1()));
            edt_money1.setText(String.valueOf(price.getCost_block_1()));
        }

        edt_parking.setText(park.getParkingName());
        edt_address.setText(park.getParkingAddress());
        edt_hotline.setText(park.getParkingHotline());
        edt_website.setText(park.getParkingWebsite());

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ConfigActivity.this, LogActivity.class);
                startActivity(intent);

            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new AboutDialogBuilder().create(ConfigActivity.this);
                dialog.show();

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBlock();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        llTypePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeMoneyDialog();
            }
        });


    }

    private void showTypeMoneyDialog() {
        final CharSequence[] type = new CharSequence[]{getString(R.string.type_price_value_1), getString(R.string.type_price_value_2)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.type_price));
        builder.setItems(type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0: {
                        Log.e(LOG_TAG, "type 1");
                        hideTypeMoney2();
                        price.setType(1);
                        tvTypePriceValue.setText(getString(R.string.type_price_value_1));
                        break;
                    }
                    case 1: {
                        Log.e(LOG_TAG, "type 2");
                        showTypeMoney2();
                        price.setType(2);
                        tvTypePriceValue.setText(getString(R.string.type_price_value_2));
                        break;
                    }
                }
            }
        });
        builder.show();
    }

    private void hideTypeMoney2(){
        tvNextBlockLabel.setVisibility(GONE);
        llNextBlock.setVisibility(GONE);
        tvLimitedMoneyLabel.setVisibility(GONE);
        llLimitedMoney.setVisibility(GONE);
    }

    private void showTypeMoney2(){
        tvNextBlockLabel.setVisibility(View.VISIBLE);
        llNextBlock.setVisibility(View.VISIBLE);
        tvLimitedMoneyLabel.setVisibility(View.VISIBLE);
        llLimitedMoney.setVisibility(View.VISIBLE);
    }


    private void updateBlock(){
        String parkName = edt_parking.getText().toString().trim();
        String parkAddress = edt_address.getText().toString().trim();
        String parkHotline = edt_hotline.getText().toString().trim();
        String parkWebsite = edt_website.getText().toString().trim();
        if (price.getType() == 1){
            String time1  = edt_time1.getText().toString().trim();
            String money1 = edt_money1.getText().toString().trim();
            if ((time1.isEmpty()) || (money1.isEmpty())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ConfigActivity.this);
                alert.setTitle(   R.string.error_title );
                alert.setMessage( R.string.block1_null );
                alert.setIcon(    R.drawable.dialog_warning_icon );
                alert.setPositiveButton(R.string.ok, null );
                alert.show();
                return;
            }
            price.setCost_block_1(Double.parseDouble(money1));
            price.setTime_block_1(time1);
            parkRequest = new ParkRequest(parkName, parkAddress, parkHotline, parkWebsite, price.getType(), price.getCost_block_1(), price.getTime_block_1());
            new UpdateParkTask().execute(String.valueOf(user.getParkId()));

        } else if (price.getType() == 2){
            String time1  = edt_time1.getText().toString().trim();
            String time2 = edt_time1.getText().toString().trim();
            String money1 = edt_money1.getText().toString().trim();
            String money2 = edt_money2.getText().toString().trim();
            String limitedMoney = edt_limited_money.getText().toString().trim();

            if ((time1.isEmpty()) || (money1.isEmpty())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ConfigActivity.this);
                alert.setTitle(   R.string.error_title );
                alert.setMessage( R.string.block1_null );
                alert.setIcon(    R.drawable.dialog_warning_icon );
                alert.setPositiveButton(R.string.ok, null );
                alert.show();
                return;
            }

            if ((time2.isEmpty()) || (money2.isEmpty())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ConfigActivity.this);
                alert.setTitle(   R.string.error_title );
                alert.setMessage( R.string.block2_null );
                alert.setIcon(    R.drawable.dialog_warning_icon );
                alert.setPositiveButton(R.string.ok, null );
                alert.show();
                return;
            }
            if (limitedMoney.isEmpty()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ConfigActivity.this);
                alert.setTitle(   R.string.error_title );
                alert.setMessage( R.string.limitedMoney);
                alert.setIcon(    R.drawable.dialog_warning_icon );
                alert.setPositiveButton(R.string.ok, null );
                alert.show();
                return;
            }
            price.setCost_block_1(Double.parseDouble(money1));
            price.setTime_block_1(time1);
            price.setCost_block_2(Double.parseDouble(money2));
            price.setTime_block_2(time2);
            price.setTotal_price(Double.parseDouble(limitedMoney));
            parkRequest = new ParkRequest(
                    parkName,
                    parkAddress,
                    parkHotline,
                    parkWebsite,
                    price.getType(),
                    price.getCost_block_1(),
                    price.getTime_block_1(),
                    price.getCost_block_2(),
                    price.getTime_block_2(),
                    price.getTotal_price());
            new UpdateParkTask().execute(String.valueOf(user.getParkId()));
        }

    }

    private class UpdateParkTask extends AsyncTask<String, Void, Void>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( ConfigActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            NetworkUtils.updateCurrentPark(getApplicationContext(), params[0], parkRequest, new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ConfigActivity.this);
                    alert.setTitle(   R.string.notify_string );
                    alert.setMessage( R.string.save_setting_block_success );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ConfigActivity.this);
                    alert.setTitle(   R.string.notify_string );
                    alert.setMessage( R.string.save_setting_block_error );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailed() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ConfigActivity.this);
                    alert.setTitle(   R.string.notify_string );
                    alert.setMessage( R.string.save_setting_block_error );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }
            });
            return null;
        }
    }

    private class ParkTask extends AsyncTask<String, Void, Void>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( ConfigActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            park = DataUtils.getCurrentPark(getApplicationContext(), ManagerContract.ParkEntry.CONTENT_URI);
            price = DataUtils.queryCurrentPrice(getApplicationContext(), ManagerContract.PriceEntry.CONTENT_URI);
            if (park != null && price != null){
                initData();
            }
            progressDialog.dismiss();
        }
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
