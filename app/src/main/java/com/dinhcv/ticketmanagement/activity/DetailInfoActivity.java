package com.dinhcv.ticketmanagement.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.print.sdk.PrinterInstance;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.TicketManagermentApplication;
import com.dinhcv.ticketmanagement.activity.AlertDialog.DisplayImageDialogBuilder;
import com.dinhcv.ticketmanagement.utils.Debug;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;
import com.dinhcv.ticketmanagement.utils.Utils;

public class DetailInfoActivity extends AppCompatActivity {

    private PrinterInstance mPrinter;
    private ImageView image1;
    private ImageView image2;

    private String filePath = null;
    private String filePath1 = null;
    private boolean isChange = false;
    private int id;
    private LinearLayout ll_timeTotal;
    private Button btn_printTicket;
    private Button btn_printBill;
    private Button btn_acceptCarout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        getParameter();

        TicketManagermentApplication app = (TicketManagermentApplication) getApplication();
        mPrinter = app.getIPrinter();

        initView();
    }

    private void getParameter() {
        id = getIntent().getIntExtra(PreferenceKey.EXTRA_VEHICLE_ID, -1);
    }

    private void initView() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.detail_info_title));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        ll_timeTotal = (LinearLayout) findViewById(R.id.ll_timeTotal);
        btn_printTicket = (Button) findViewById(R.id.btn_printTicket);
        btn_printBill = (Button) findViewById(R.id.btn_printBill);
        btn_acceptCarout = (Button) findViewById(R.id.btn_acceptCarout);

        TextView tv_lisence = (TextView) findViewById(R.id.tv_lisence);
        TextView tv_barcode = (TextView) findViewById(R.id.tv_barcode);
        TextView tv_timeIn = (TextView) findViewById(R.id.tv_timeIn);
        TextView tv_timeOut = (TextView) findViewById(R.id.tv_timeOut);
        TextView tv_status = (TextView) findViewById(R.id.tv_status);
        TextView tv_timeTotal = (TextView) findViewById(R.id.tv_timeTotal);
        TextView tv_revenue = (TextView) findViewById(R.id.tv_revenue);
        TextView tv_timeIn1 = (TextView) findViewById(R.id.tv_timeIn1);
        TextView tv_timeOut1 = (TextView) findViewById(R.id.tv_timeOut1);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        if (!Utils.isPantech() && !Utils.isOppo()) {
            Debug.normal("Rotation image 90");
            image1.setRotation(90);
            image2.setRotation(90);
        }
        btn_printTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mPrinter != null) {
//                    PrintUtils.printTicket(DetailInfoActivity.this.getResources(), mPrinter, mTicketInfo);
//
//                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailInfoActivity.this);
//                    alert.setTitle(R.string.warning_title);
//                    alert.setMessage(R.string.print_bill_success);
//                    alert.setIcon(R.drawable.dialog_info_icon);
//                    alert.setPositiveButton(R.string.ok, null);
//                    alert.show();
//                } else {
//                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailInfoActivity.this);
//                    alert.setTitle(R.string.error_title);
//                    alert.setMessage(R.string.none_found);
//                    alert.setIcon(R.drawable.dialog_warning_icon);
//                    alert.setPositiveButton(R.string.ok, null);
//                    alert.show();
//                }
            }
        });

        btn_printBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPrinter != null) {
                    //PrintUtils.printBill(DetailInfoActivity.this.getResources(), mPrinter, mTicketInfo);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailInfoActivity.this);
                    alert.setTitle(R.string.error_title);
                    alert.setMessage(R.string.none_found);
                    alert.setIcon(R.drawable.dialog_warning_icon);
                    alert.setPositiveButton(R.string.ok, null);
                    alert.show();
                }
            }
        });

        btn_acceptCarout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SaveCarOutTicketTask().execute();

            }
        });

//        if (mTicketInfo.getStatus() == 1){
//            ll_timeTotal.setVisibility(View.GONE);
//        }else {
//            ll_timeTotal.setVisibility(View.VISIBLE);
//            tv_timeTotal.setText(Utils.getTotalTime(mTicketInfo.getTimeIn(), mTicketInfo.getTimeOut()));
//        }
//
//        tv_lisence.setText(mTicketInfo.getLisencePlate());
//        tv_barcode.setText(mTicketInfo.getLisenceCode());
//        String timein = Utils.convertDateInToString(mTicketInfo.getTimeIn());
//        tv_timeIn.setText(timein);
//        tv_timeIn1.setText(timein);
//
//        filePath = mTicketInfo.getCarInImagePath();
//        Debug.normal("File path 1: " + filePath);
//        File imgFile = new File(filePath);
//
//        if (imgFile.exists()) {
//
//            Debug.normal("File image exist" + imgFile.getAbsolutePath());
//            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
//
//            image1.setImageBitmap(myBitmap);
//        }
//
//        String timeout = "-----";
//        String revenue = "----- vnd";
//        String status = getResources().getString(R.string.in_packing);
//
//        if (mTicketInfo.getStatus() == 2) {
//            btn_acceptCarout.setVisibility(View.INVISIBLE);
//            btn_printBill.setVisibility(View.VISIBLE);
//
//            filePath1 = mTicketInfo.getCarOutImagePath();
//            if (filePath1 != null) {
//
//                Debug.normal("File path 2: " + filePath1);
//                File imgFile1 = new File(filePath1);
//
//                if (imgFile1.exists()) {
//
//                    Debug.normal("File image exist" + imgFile1.getAbsolutePath());
//                    Bitmap myBitmap1 = BitmapFactory.decodeFile(filePath1);
//
//                    image2.setImageBitmap(myBitmap1);
//                }
//            }
//            status = getResources().getString(R.string.out_packing);
//            timeout = Utils.convertDateInToString(mTicketInfo.getTimeOut());
//
//            revenue = Utils.convertFeeToString(mTicketInfo.getFee()) + " Vnd";
//        } else {
//            btn_printBill.setVisibility(View.INVISIBLE);
//        }
//
//        tv_timeOut.setText(timeout);
//        tv_timeOut1.setText(timeout);
//        tv_status.setText(status);
//        tv_revenue.setText(revenue);

        handleEvent();
    }

    private void handleEvent() {
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage(filePath);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath1 != null) {
                    displayImage(filePath1);
                }
            }
        });

    }

    private void displayImage(String filePath) {

        Dialog dialog = new DisplayImageDialogBuilder().create(DetailInfoActivity.this, filePath);
        dialog.show();
    }


    private class SaveCarOutTicketTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(DetailInfoActivity.this);

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

//            mTicketInfo.setStatus(2);
//            mTicketInfo.setTicketType(2);
//            mTicketInfo.setTimeOut(new Date());
//
//            long fee = Utils.getRevenueTotal(mTicketInfo.getTimeIn(), mTicketInfo.getTimeOut(), settingBlock);
//            Debug.normal("FEEEEEEEEEEEEEEEEEEEEEEEEEEEEE: " + fee);
//            mTicketInfo.setFee(fee);
//
//            boolean save = mTicketModel.saveTicket(mTicketInfo);
//            if (!save) {
//                Debug.error("Can not save car out ticket ");
//                return false;
//            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isOk) {
            super.onPostExecute(isOk);

            progressDialog.dismiss();

            if (isOk) {

                if (mPrinter == null) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailInfoActivity.this);
                    alert.setTitle(R.string.warning_title);
                    alert.setMessage(R.string.cannot_print);
                    alert.setIcon(R.drawable.dialog_info_icon);
                    alert.setPositiveButton(R.string.ok, null);
                    alert.show();

                } else {
                    //PrintUtils.printBill(DetailInfoActivity.this.getResources(), mPrinter, mTicketInfo);

                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailInfoActivity.this);
                    alert.setTitle(R.string.warning_title);
                    alert.setMessage(R.string.print_bill_success);
                    alert.setIcon(R.drawable.dialog_info_icon);
                    alert.setPositiveButton(R.string.ok, null);
                    alert.show();


                }

                // Update UI
                initView();
                isChange = true;
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(DetailInfoActivity.this);
                alert.setTitle(R.string.error_title);
                alert.setMessage(R.string.carout_save_error);
                alert.setIcon(R.drawable.dialog_warning_icon);
                alert.setPositiveButton(R.string.ok, null);
                alert.show();

            }

        }
    }

    @Override
    public void onBackPressed() {

        Debug.normal("Click on back press");

        Intent intent = new Intent();
        if (isChange) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();

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
                Intent intent = new Intent();
                if (isChange) {
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);


    }

}
