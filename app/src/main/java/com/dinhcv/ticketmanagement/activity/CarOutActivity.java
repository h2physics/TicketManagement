package com.dinhcv.ticketmanagement.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.print.sdk.PrinterInstance;
import com.bumptech.glide.Glide;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.TicketManagermentApplication;
import com.dinhcv.ticketmanagement.activity.AlertDialog.DisplayImageDialogBuilder;
import com.dinhcv.ticketmanagement.lib.RotateTransformation;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Price.Price;
import com.dinhcv.ticketmanagement.model.structure.User.UserInformation;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
import com.dinhcv.ticketmanagement.printer.PrintUtils;
import com.dinhcv.ticketmanagement.utils.DataUtils;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;
import com.dinhcv.ticketmanagement.utils.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class CarOutActivity extends AppCompatActivity {
    private static final String LOG_TAG = CarInActivity.class.getSimpleName();

    private TextView tv_lisence;
    private PrinterInstance mPrinter;
    private LinearLayout imgInfoLayout;
    private TextView tv_barcode;
    private TextView tv_timeIn;
    private TextView tv_timeOut;
    private TextView tv_revenue;
    private LinearLayout ll_timeTotal;
    private TextView tv_timeTotal;
    private ImageButton btn_printBill;
    private TextView tv_timeIn1;
    private TextView tv_timeOut1;

    private ImageView image1;
    private ImageView image2;

    private String filePath;
    private String filePath1;
    private String licenseCode;
    private String mTimeOut;
    private double mFee;
    private String mCarOutImage;
    private final String CAR_TYPE = "xe_ra";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_out);
        initView();
        getParameter();

        TicketManagermentApplication app = (TicketManagermentApplication) getApplication();
        mPrinter = app.getIPrinter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void getParameter() {

        licenseCode = getIntent().getStringExtra(PreferenceKey.EXTRA_BARCODE);
        Log.e(LOG_TAG, "License code: " + licenseCode);

    }

    private void initView() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.carout_title));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        imgInfoLayout = (LinearLayout) findViewById(R.id.image_info_layout);
        tv_lisence = (TextView) findViewById(R.id.tv_lisence);
        tv_barcode = (TextView) findViewById(R.id.tv_barcode);
        tv_timeIn = (TextView) findViewById(R.id.tv_timeIn);
        tv_timeOut = (TextView) findViewById(R.id.tv_timeOut);
        tv_revenue = (TextView) findViewById(R.id.tv_revenue);
        ll_timeTotal = (LinearLayout) findViewById(R.id.ll_timeTotal);
        tv_timeTotal = (TextView) findViewById(R.id.tv_timeTotal);
        btn_printBill = (ImageButton) findViewById(R.id.btn_printBill);

        tv_timeIn1 = (TextView) findViewById(R.id.tv_timeIn1);
        tv_timeOut1 = (TextView) findViewById(R.id.tv_timeOut1);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
    }

    private void initData() {
        Uri uri = ManagerContract.VehicleEntry.buildUriWithLicenseCode(licenseCode);
        Cursor cursor = getApplicationContext().getContentResolver().query(uri,
                null,
                ManagerContract.VehicleEntry.COLUMN_BARCODE + "=?",
                new String[]{licenseCode},
                null,
                null);

       // UserInformation user = DataUtils.queryCurrentUserInfo(getApplicationContext());

        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            String licensePlate = cursor.getString(PreferenceKey.INDEX_VEHICLE_LICENSE_PLATE);
            String timeIn = cursor.getString(PreferenceKey.INDEX_VEHICLE_TIME_IN);
            Date dateOut = new Date();
            mTimeOut = Utils.convertDateInToString(dateOut);
            Date dateIn = Utils.convertStringToDateWithOtherFormat(timeIn);
            String totalTime = Utils.getTotalTime(dateIn, dateOut);
            Price price = DataUtils.queryCurrentPrice(getApplicationContext(), ManagerContract.PriceEntry.CONTENT_URI);
            if (price.getType() == 1){
                mFee = Utils.getTotalPriceFollowedType1(
                        Integer.parseInt(price.getTime_block_1()),
                        price.getCost_block_1(),
                        timeIn,
                        mTimeOut);
            } else {
                mFee = Utils.getTotalPriceFollowedType2(
                        Integer.parseInt(price.getTime_block_1()),
                        Integer.parseInt(price.getTime_block_2()),
                        price.getCost_block_1(),
                        price.getCost_block_2(),
                        price.getTotal_price(),
                        timeIn,
                        mTimeOut);
            }

            tv_lisence.setText(licensePlate);
            tv_barcode.setText(licenseCode);
            tv_revenue.setText(String.valueOf(mFee));
            tv_timeIn.setText(timeIn);
            tv_timeIn1.setText(timeIn);
            tv_timeOut.setText(mTimeOut);
            tv_timeOut1.setText(mTimeOut);
            tv_timeTotal.setText(totalTime);
            String pathImageCarIn = cursor.getString(PreferenceKey.INDEX_VEHICLE_IMAGE_CAR_IN);
            String url = "http://mp.vteam.info/" + pathImageCarIn;
            Log.e(LOG_TAG, "Image car in: " + url);
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .transform(new RotateTransformation(this, 90f))
                    .into(image1);
            image1.setImageResource(R.drawable.no_thumbnail);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                BitMatrix bitMatrix = multiFormatWriter.encode(licenseCode, BarcodeFormat.CODE_128,(width/2),200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                Date now = new Date();
                String fileName = CAR_TYPE + Utils.convertDateToString(now) + ".PNG";
                File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Ticketmanager");
                if (!folder.exists()){
                    folder.mkdirs();
                }
                File file = new File(Environment.getExternalStorageDirectory() + "/Ticketmanager/" + fileName);
                mCarOutImage = file.getAbsolutePath();
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                ((TextView) findViewById(R.id.tv_barcode_display)).setText(licenseCode);
                image2.setImageBitmap(bitmap);
            }
            catch (WriterException e){
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            btn_printBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new SaveCarOutTicketTask().execute(mTimeOut, mCarOutImage, String.valueOf(mFee));
                }
            });
        }

//        image1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                displayImage(filePath);
//            }
//        });

//        image2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (filePath1 != null){
//                    displayImage(filePath1);
//                }
//            }
//        });
    }

    private void displayImage(String filePath){
        Dialog dialog = new DisplayImageDialogBuilder().create(CarOutActivity.this, filePath);
        dialog.show();
    }

    private class SaveCarOutTicketTask extends AsyncTask<String, Void, Vehicle> {
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(CarOutActivity.this);

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Vehicle doInBackground(String... params) {
            Vehicle vehicle = DataUtils.queryVehicleByLicenseCode(getApplicationContext(), licenseCode);
            vehicle.setTimeOut(params[0]);
            vehicle.setImageCarOut(params[1]);
            vehicle.setPrice(Double.parseDouble(params[2]));
            return vehicle;
        }

        @Override
        protected void onPostExecute(final Vehicle vehicle) {
            super.onPostExecute(vehicle);

            if (vehicle.getTimeOut() != null) {

                if (mPrinter == null) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(CarOutActivity.this);
                    alert.setTitle(R.string.warning_title);
                    alert.setMessage(R.string.cannot_print);
                    alert.setIcon(R.drawable.dialog_info_icon);
                    alert.setPositiveButton(R.string.ok, null);
                    alert.show();

                } else {
                    NetworkUtils.updateVehicleOut(getApplicationContext(), vehicle, new NetworkExecuteCompleted() {
                        @Override
                        public void onSuccess() {
                            PrintUtils.printBill(getApplicationContext(), CarOutActivity.this.getResources(), mPrinter, vehicle);

                            AlertDialog.Builder alert = new AlertDialog.Builder(CarOutActivity.this);
                            alert.setTitle(   R.string.warning_title );
                            alert.setMessage( R.string.print_bill_success );
                            alert.setIcon(    R.drawable.dialog_info_icon );
                            alert.setPositiveButton(R.string.ok, null );
                            alert.show();
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onError() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailed() {
                            progressDialog.dismiss();
                        }
                    });


                }

                initView();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(CarOutActivity.this);
                alert.setTitle(R.string.error_title);
                alert.setMessage(R.string.carout_save_error);
                alert.setIcon(R.drawable.dialog_warning_icon);
                alert.setPositiveButton(R.string.ok, null);
                alert.show();
                progressDialog.dismiss();
            }

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
