package com.dinhcv.ticketmanagement.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.print.sdk.PrinterInstance;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.TicketManagermentApplication;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Park.Park;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
import com.dinhcv.ticketmanagement.printer.PrintUtils;
import com.dinhcv.ticketmanagement.utils.DataUtils;
import com.dinhcv.ticketmanagement.utils.Debug;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;
import com.dinhcv.ticketmanagement.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;

public class CarInActivity extends AppCompatActivity {
    private static final String LOG_TAG = CarInActivity.class.getSimpleName();

    private PrinterInstance mPrinter;
    private String mImagePath = null;
    private ImageView image;
    private Button btn_recapture;
    LinearLayout ll_carInPark;
    TextView tv_lisence;
    TextView tv_timeIn;
    TextView tv_timeIn1;
    ImageButton btn_printTicket;

    private boolean isPrinted = false;
    private int lisenceCode = 0;
    private Vehicle mVehicle;
    private Park mCurrentPark;
    private List<Vehicle> listVehicleInPark;
    private File compressedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_in);

        getParameter();

        TicketManagermentApplication app = (TicketManagermentApplication) getApplication();
        mPrinter = app.getIPrinter();
        listVehicleInPark = new ArrayList<>();

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void getParameter(){
        mVehicle = (Vehicle) getIntent().getSerializableExtra(PreferenceKey.EXTRA_VEHICLE);
        Log.e(LOG_TAG, "Vehicle info: " + mVehicle.getLicensePlate() + " - " + mVehicle.getImageCarIn() + " - " + mVehicle.getTimeIn());


    }

    private void initView(){
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.carin_title));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        ll_carInPark = (LinearLayout) findViewById(R.id.ll_carInPark);
        tv_lisence = (TextView) findViewById(R.id.tv_lisence);
        tv_timeIn = (TextView) findViewById(R.id.tv_timeIn);
        tv_timeIn1 = (TextView) findViewById(R.id.tv_timeIn1);
        btn_printTicket = (ImageButton) findViewById(R.id.btn_printTicket);
        btn_recapture = (Button) findViewById(R.id.btn_recapture);
        if (isPrinted){
            btn_recapture.setEnabled(false);
        }

        image = (ImageView) findViewById(R.id.image);
    }

    private void initData() {
        mCurrentPark = DataUtils.getCurrentPark(getApplicationContext(), ManagerContract.ParkEntry.CONTENT_URI);
        tv_lisence.setText(mVehicle.getLicensePlate());
        String timein = mVehicle.getTimeIn();
        tv_timeIn.setText(timein);
        tv_timeIn1.setText(timein);

        String filePath = mVehicle.getImageCarIn();
        mImagePath = filePath;

        Log.e(LOG_TAG, "IMAGE PATH: " + filePath);
        String path = Environment.getExternalStorageDirectory() +"/Ticketmanager/";
        File imgFile = new File(filePath);
        compressedImage = new Compressor.Builder(this)
                .setMaxWidth(320)
                .setMaxHeight(200)
                .setQuality(15)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .setDestinationDirectoryPath(path)
                .build()
                .compressToFile(imgFile);

        Bitmap compressedBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
        mVehicle.setImageCarIn(compressedImage.getAbsolutePath());
        imgFile.delete();

        if(compressedImage.exists()){
            image.setImageBitmap(compressedBitmap);
        }

        btn_printTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPrinter != null) {
                    if (!isPrinted) {
                        Cursor cursor = DataUtils.queryVehicle(getApplicationContext(), ManagerContract.VehicleEntry.CONTENT_URI);
//                        lisenceCode = Utils.getLicenseCode(CarInActivity.this);
//                        String barcode = Utils.convertToStringAroundNumber(lisenceCode);
                        while (cursor.moveToNext()){
                            listVehicleInPark.add(new Vehicle(
                                    cursor.getInt(PreferenceKey.INDEX_VEHICLE_ID),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_TYPE),
                                    cursor.getInt(PreferenceKey.INDEX_VEHICLE_PARK_ID),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_LICENSE_PLATE),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_BARCODE),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_TIME_IN),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_TIME_OUT),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_STATUS),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_IMAGE_CAR_IN),
                                    cursor.getString(PreferenceKey.INDEX_VEHICLE_IMAGE_CAR_OUT),
                                    cursor.getDouble(PreferenceKey.INDEX_VEHICLE_PRICE)
                            ));
                        }
                        String barcode = Utils.getBarcode(listVehicleInPark);
                        Utils.setLicenseCode(CarInActivity.this, lisenceCode);
                        Log.e(LOG_TAG, "Barcode: " + barcode);

                        mVehicle.setBarcode(barcode);
                        mVehicle.setTypeVehicle(getResources().getString(R.string.typeVehicle_car));
                        if (mCurrentPark != null){
                            mVehicle.setParkId(mCurrentPark.getId());
                        } else {
                            mVehicle.setParkId(1);
                        }
                        new SaveCarInTicketTask().execute();
                    }else {
                        PrintUtils.printTicket(getApplicationContext(), CarInActivity.this.getResources(), mPrinter, mVehicle);
                    }

                }else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(CarInActivity.this);
                    alert.setTitle(R.string.error_title);
                    alert.setMessage(R.string.none_found);
                    alert.setIcon(R.drawable.dialog_warning_icon);
                    alert.setPositiveButton(R.string.ok, null);
                    alert.show();
                }

            }
        });


        btn_recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarInActivity.this, ReCaptureActivity.class);
                startActivityForResult(intent, 900);
            }
        });

    }

    private void getDropboxIMGSize(String file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        Debug.normal("Image Width: "+ imageWidth +" Image height: "+ imageHeight);

    }


    private class SaveCarInTicketTask extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog( CarInActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);

            if (mVehicle.getBarcode() != null){

                if (mPrinter != null) {
                    Vehicle ve = new Vehicle();
                    ve.setTypeVehicle(mVehicle.getTypeVehicle());
                    ve.setLicensePlate(mVehicle.getLicensePlate());
                    ve.setBarcode(mVehicle.getBarcode());
                    ve.setParkId(1);
                    ve.setImageCarIn(mVehicle.getImageCarIn());
                    NetworkUtils.updateVehicleIn(getApplicationContext(), ve, new NetworkExecuteCompleted() {
                        @Override
                        public void onSuccess() {
                            isPrinted = true;
                            PrintUtils.printTicket(getApplicationContext(), CarInActivity.this.getResources(), mPrinter, mVehicle);

                            AlertDialog.Builder alert = new AlertDialog.Builder(CarInActivity.this);
                            alert.setTitle(   R.string.notify_string );
                            alert.setMessage( R.string.car_in_successfully );
                            alert.setIcon(    R.drawable.dialog_warning_icon );
                            alert.setPositiveButton(R.string.ok, null );
                            alert.show();
                            compressedImage.delete();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError() {
                            AlertDialog.Builder alert = new AlertDialog.Builder(CarInActivity.this);
                            alert.setTitle(   R.string.notify_string );
                            alert.setMessage( R.string.car_in_error );
                            alert.setIcon(    R.drawable.dialog_warning_icon );
                            alert.setPositiveButton(R.string.ok, null );
                            alert.show();
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onFailed() {
                            AlertDialog.Builder alert = new AlertDialog.Builder(CarInActivity.this);
                            alert.setTitle(   R.string.notify_string );
                            alert.setMessage( R.string.car_in_failed );
                            alert.setIcon(    R.drawable.dialog_warning_icon );
                            alert.setPositiveButton(R.string.ok, null );
                            alert.show();
                            progressDialog.dismiss();
                        }
                    });

                }else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(CarInActivity.this);
                    alert.setTitle(   R.string.error_title );
                    alert.setMessage( R.string.none_found );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }
            }else {
                AlertDialog.Builder alert = new AlertDialog.Builder(CarInActivity.this);
                alert.setTitle(   R.string.error_title );
                alert.setMessage( R.string.carout_save_error );
                alert.setIcon(    R.drawable.dialog_warning_icon );
                alert.setPositiveButton(R.string.ok, null );
                alert.show();
                progressDialog.dismiss();
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 900) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra(ReCaptureActivity.FILE_IMAGE_PATH);
                if (result != null){
                    mImagePath = result;

                    Debug.normal("File path: "+mImagePath);
                    File imgFile = new File(mImagePath);

                    if(imgFile.exists()){

                        getDropboxIMGSize(mImagePath);

                        Debug.normal("File image exist" +imgFile.getAbsolutePath());
                        Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);

                        image.setImageBitmap(myBitmap);
                        Log.e(LOG_TAG, "Vehicle: " + mVehicle.getLicensePlate());
                        mVehicle.setImageCarIn(mImagePath);

                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


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
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}
