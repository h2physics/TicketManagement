package com.dinhcv.ticketmanagement.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.print.sdk.PrinterInstance;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.TicketManagermentApplication;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Statistic.Statistic;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import com.dinhcv.ticketmanagement.printer.PrintUtils;
import com.dinhcv.ticketmanagement.utils.DataUtils;
import com.dinhcv.ticketmanagement.utils.Debug;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;
import com.dinhcv.ticketmanagement.utils.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StatisticActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = StatisticActivity.class.getSimpleName();

    private Date mFromDate;
    private Date mToDate;
    private Timestamp mFromDateInTimeStamp;
    private Timestamp mToDateInTimeStamp;
    private Date TODAY;
    private Calendar mCalendar;

    private int mAnaYear;
    private int mAnaMonth;
    private int mAnaDay;

    private EditText edtStartHour;
    private EditText edtEndHour;

    private int mStartHour = 0;
    private int mStartMinute = 0;
    private int mEndHour = 23;
    private int mEndMinute = 59;

    private TextView edt_dateFrom;
    private TextView edt_dateTo;
    private TextView tv_carInOnDay;
    private TextView tv_carOutOnDay;
    private TextView tv_revenueOnDay;
    private TextView tv_carinTotal;
    private TextView tv_caroutTotal;
    private TextView tv_carTotal;
    private Button btn_search;
    private TextView tv_revenueTotal;
    private ImageButton imbPrintStatistic;

    private boolean isTodayFirst = false;
    private long totalRevenue = 0;
    private int mCarTotal = 0;
    private int mCarinOnDay = 0;
    private int mCaroutOnDay = 0;
    private long mPriceOnDay = 0;
    private int mCarinTotal = 0;
    private int mCaroutTotal = 0;
    private long mTotalPrice = 0;
    private boolean isSearched = false;
    private PrinterInstance mPrinter;

    private LoaderManager.LoaderCallbacks callbacks;

    private static final int VEHICLE_IN_LOADER_ID = 3002;
    private static final int VEHICLE_OUT_LOADER_ID = 3004;
    private static final int VEHICLE_WITH_TIME_LOADER_ID = 3003;
    private final String[] VEHICLE_PROJECTION = {
            ManagerContract.VehicleEntry.COLUMN_ID,
            ManagerContract.VehicleEntry.COLUMN_TYPE,
            ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE,
            ManagerContract.VehicleEntry.COLUMN_BARCODE,
            ManagerContract.VehicleEntry.COLUMN_TIME_IN,
            ManagerContract.VehicleEntry.COLUMN_TIME_OUT,
            ManagerContract.VehicleEntry.COLUMN_STATUS,
            ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_IN,
            ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_OUT,
            ManagerContract.VehicleEntry.COLUMN_PRICE,
            ManagerContract.VehicleEntry.COLUMN_PARKING_ID

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        initDateData();
        initView();
        TicketManagermentApplication app = (TicketManagermentApplication) getApplication();
        mPrinter = app.getIPrinter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        callbacks = this;
        initData();
        new VehicleTask().execute(currentDate().toString(), currentDateUntilNow().toString());
        Log.e(LOG_TAG, "Current: " + currentDate().toString() + " - Now: " + currentDateUntilNow().toString() + " - Gap: " + ((currentDate().getTime() - currentDateUntilNow().getTime())/(1000*60*60)));
    }

    private void initView() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.statistic_title));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        tv_carinTotal = (TextView) findViewById(R.id.tv_carinTotal);
        tv_caroutTotal = (TextView) findViewById(R.id.tv_caroutTotal);
        tv_revenueTotal = (TextView) findViewById(R.id.tv_revenueTotal);
        tv_carTotal = (TextView) findViewById(R.id.tv_carTotal);
        tv_carInOnDay = (TextView) findViewById(R.id.tv_carInOnDay);
        tv_carOutOnDay = (TextView) findViewById(R.id.tv_carOutOnDay);
        tv_revenueOnDay = (TextView) findViewById(R.id.tv_revenueOnDay);
        edt_dateFrom = (TextView) findViewById(R.id.edt_dateFrom);
        edt_dateTo = (TextView) findViewById(R.id.edt_dateTo);
        edt_dateFrom.setKeyListener(null);
        edt_dateTo.setKeyListener(null);
        btn_search = (Button) findViewById(R.id.btn_search);
        imbPrintStatistic = (ImageButton) findViewById(R.id.btn_printStatistic);

        edtStartHour = (EditText) findViewById(R.id.edt_timeFrom);
        edtEndHour = (EditText) findViewById(R.id.edt_timeTo);
        edtStartHour.setKeyListener(null);
        edtEndHour.setKeyListener(null);

    }

    private void handleSearchCar() {

        if (!checkTimeInvalid()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(StatisticActivity.this);
            dialog.setTitle(getString(R.string
                    .error_title))
                    .setMessage(getString(R.string.set_time_invalid))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.ok), null)
                    .show();
            return;
        } else {
            mFromDateInTimeStamp = new Timestamp(mFromDate.getTime());
            mToDateInTimeStamp = new Timestamp(mToDate.getTime());
            Log.e(LOG_TAG, "From date: " + mFromDateInTimeStamp + " - To date: " + mToDateInTimeStamp);

            getSupportLoaderManager().destroyLoader(VEHICLE_IN_LOADER_ID);
            getSupportLoaderManager().restartLoader(VEHICLE_IN_LOADER_ID, null, callbacks);
            getSupportLoaderManager().destroyLoader(VEHICLE_OUT_LOADER_ID);
            getSupportLoaderManager().restartLoader(VEHICLE_OUT_LOADER_ID, null, callbacks);
        }

    }

    private void initData() {
        String fromDate = Utils.convertOnlyDateToString(mFromDate);
        String toDate = Utils.convertOnlyDateToString(mToDate);
        mFromDateInTimeStamp = new Timestamp(mFromDate.getTime());
        mToDateInTimeStamp = new Timestamp(mToDate.getTime());

        edtStartHour.setText(Utils.convertTimeToString(mStartHour, mStartMinute));
        edtEndHour.setText(Utils.convertTimeToString(mEndHour, mEndMinute));
        edt_dateFrom.setText(fromDate);
        edt_dateTo.setText(toDate);

        edt_dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open dialog

                handleFromDatePicker();
            }
        });

        edt_dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open dialog

                handleToDatePicker();
            }
        });

        edtStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open dialog

                handleStartTimePicker();
            }
        });

        edtEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open dialog

                handleEndTimePicker();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // handle search
                handleSearchCar();
            }
        });

        imbPrintStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Statistic statistic = new Statistic(
                        tv_carTotal.getText().toString().trim(),
                        tv_carInOnDay.getText().toString().trim(),
                        tv_carOutOnDay.getText().toString().trim(),
                        tv_revenueOnDay.getText().toString().trim(),
                        Utils.convertDateInToString(mFromDate),
                        Utils.convertDateInToString(mToDate),
                        tv_carinTotal.getText().toString().trim(),
                        tv_caroutTotal.getText().toString().trim(),
                        tv_revenueTotal.getText().toString().trim()
                );
                new PrintStatistic().execute(statistic);
            }
        });
    }



    private void emptyLoad() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(StatisticActivity.this);

        dialog.setTitle(R.string.warning_title);
        dialog.setMessage(R.string.list_is_null);
        dialog.setIcon(R.drawable.dialog_warning_icon);
        dialog.setPositiveButton(R.string.ok, null);
        dialog.show();

    }

    /**
     * Show from date picker dialog
     */
    private void handleFromDatePicker() {

        new DatePickerDialog(StatisticActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar.set(Calendar.HOUR_OF_DAY, mStartHour);
                mCalendar.set(Calendar.MINUTE, mStartMinute);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
                mFromDate = mCalendar.getTime();
                mAnaYear = year;
                mAnaMonth = monthOfYear;
                mAnaDay = dayOfMonth;
                boolean isOk = checkTimeInvalid();
                if (isOk) edt_dateFrom.setText(Utils.convertOnlyDateToString(mFromDate));
            }
        }, mAnaYear, mAnaMonth, mAnaDay).show();

    }

    /**
     * Show from date picker dialog
     */
    private void handleToDatePicker() {

        new DatePickerDialog(StatisticActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar.set(Calendar.HOUR_OF_DAY, mEndHour);
                mCalendar.set(Calendar.MINUTE, mEndMinute);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
                mToDate = mCalendar.getTime();
                mAnaYear = year;
                mAnaMonth = monthOfYear;
                mAnaDay = dayOfMonth;
                boolean isOk = checkTimeInvalid();
                if (isOk) edt_dateTo.setText(Utils.convertOnlyDateToString(mToDate));
            }
        }, mAnaYear, mAnaMonth, mAnaDay).show();

    }


    /**
     * Show date picker dialog
     */
    private void handleStartTimePicker() {

        new TimePickerDialog(StatisticActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mCalendar = Calendar.getInstance();
                mCalendar.setTime(mFromDate);
                mCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                mCalendar.set(Calendar.MINUTE, selectedMinute);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
                mFromDate = mCalendar.getTime();
                boolean isOk = checkTimeInvalid();
                if (isOk) edtStartHour.setText(Utils.convertTimeToString(selectedHour, selectedMinute));
            }
        }, mStartHour, mStartMinute, true).show();

    }

    /**
     * Show date picker dialog
     */
    private void handleEndTimePicker() {

        new TimePickerDialog(StatisticActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                mCalendar = Calendar.getInstance();
                mCalendar.setTime(mToDate);
                mCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                mCalendar.set(Calendar.MINUTE, selectedMinute);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
                mToDate = mCalendar.getTime();

                boolean isOk = checkTimeInvalid();
                if (isOk) edtEndHour.setText(Utils.convertTimeToString(selectedHour, selectedMinute));

            }
        }, mEndHour, mEndMinute, true).show();

    }


    private void initDateData() {

        Calendar now = Calendar.getInstance();
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        mAnaYear = now.get(Calendar.YEAR);
        mAnaMonth = now.get(Calendar.MONTH);
        mAnaDay = now.get(Calendar.DAY_OF_MONTH);

        TODAY = now.getTime();
        Debug.normal("Today: " + now);

        // set start date
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mCalendar.set(Calendar.HOUR_OF_DAY, mStartHour);
        mCalendar.set(Calendar.MINUTE, mStartMinute);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        mFromDate = mCalendar.getTime();

        // set end date
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, mEndHour);
        cal.set(Calendar.MINUTE, mEndMinute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        mToDate = cal.getTime();

    }

    private Timestamp currentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return new Timestamp(calendar.getTime().getTime());
    }

    private Timestamp currentDateUntilNow(){
        Calendar calendar = Calendar.getInstance();
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * Check time invalid
     */
    private boolean checkTimeInvalid() {

        long fromDate = mFromDate.getTime();
        long toDate = mToDate.getTime();

        long sub = toDate - fromDate;
        if (sub < 0) {
            return false;
        }
        return true;
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

    private class VehicleTask extends AsyncTask<String, Void, Void>{
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( StatisticActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            Cursor cursorTotalVehicleIn = DataUtils.queryVehicleInPark(getApplicationContext());
            if (cursorTotalVehicleIn != null){
                mCarTotal = cursorTotalVehicleIn.getCount();
            }
            Cursor cursorVehicleIn = DataUtils.queryVehicleInWithDate(getApplicationContext(), params[0], params[1]);
            if (cursorVehicleIn != null){
                mCarinOnDay = cursorVehicleIn.getCount();
            }

            Cursor cursorVehicleOut = DataUtils.queryVehicleOutWithDate(getApplicationContext(), params[0], params[1]);
            if (cursorVehicleOut != null){
                mCaroutOnDay = cursorVehicleOut.getCount();
            }
            while (cursorVehicleOut.moveToNext()){
                mPriceOnDay += (long) cursorVehicleOut.getDouble(PreferenceKey.INDEX_VEHICLE_PRICE);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tv_carInOnDay.setText(String.valueOf(mCarinOnDay));
            tv_carOutOnDay.setText(String.valueOf(mCaroutOnDay));
            tv_revenueOnDay.setText(String.valueOf(mPriceOnDay) + " vnd");
            tv_carTotal.setText(String.valueOf(mCarTotal));
            getSupportLoaderManager().initLoader(VEHICLE_IN_LOADER_ID, null, callbacks);
            getSupportLoaderManager().initLoader(VEHICLE_OUT_LOADER_ID, null, callbacks);
            progressDialog.dismiss();
        }
    }

    private class PrintStatistic extends AsyncTask<Statistic, Void, Statistic>{
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( StatisticActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Statistic doInBackground(Statistic... params) {
            Log.e(LOG_TAG, "Statistic: " + params[0].toString());
            return params[0];
        }

        @Override
        protected void onPostExecute(Statistic statistic) {
            super.onPostExecute(statistic);
            if (mPrinter == null) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(StatisticActivity.this);
                alert.setTitle(R.string.warning_title);
                alert.setMessage(R.string.cannot_print_statistic);
                alert.setIcon(R.drawable.dialog_info_icon);
                alert.setPositiveButton(R.string.ok, null);
                alert.show();
                progressDialog.dismiss();
            } else {
                PrintUtils.printStatistic(getApplicationContext(), StatisticActivity.this.getResources(), mPrinter, statistic);
                Log.e(LOG_TAG, "Statistic: " + statistic.toString());
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case VEHICLE_IN_LOADER_ID: {
                Uri uri = ManagerContract.VehicleEntry.CONTENT_URI;
                Log.e(LOG_TAG, "From: " + mFromDateInTimeStamp.toString() + " - To: " + mToDateInTimeStamp.toString());

                return new CursorLoader(this,
                        uri,
                        VEHICLE_PROJECTION,
                        ManagerContract.VehicleEntry.COLUMN_TIME_IN + " BETWEEN ? AND ?",
                        new String[]{mFromDateInTimeStamp.toString(), mToDateInTimeStamp.toString()},
                        null);
            }
            case VEHICLE_OUT_LOADER_ID: {
                Uri uri = ManagerContract.VehicleEntry.CONTENT_URI;
                return new CursorLoader(this,
                        uri,
                        VEHICLE_PROJECTION,
                        ManagerContract.VehicleEntry.COLUMN_TIME_OUT + " BETWEEN ? AND ?",
                        new String[]{mFromDateInTimeStamp.toString(), mToDateInTimeStamp.toString()},
                        null);
            }
            default:
                throw new UnsupportedOperationException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == VEHICLE_IN_LOADER_ID){
            if (data != null){
                mCarinTotal = data.getCount();
                tv_carinTotal.setText(String.valueOf(mCarinTotal));
            } else {
                mCarinTotal = 0;
            }
        } else if (loader.getId() == VEHICLE_OUT_LOADER_ID){
            if (data != null){
                mCaroutTotal = data.getCount();
                tv_caroutTotal.setText(String.valueOf(mCaroutTotal));
                while (data.moveToNext()){
                    mTotalPrice += (long) data.getDouble(PreferenceKey.INDEX_VEHICLE_PRICE);
                    tv_revenueTotal.setText(String.valueOf(mTotalPrice) + " vnd");
                }
            } else {
                mCaroutTotal = 0;
                mTotalPrice = 0;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == VEHICLE_IN_LOADER_ID){
            mCarinTotal = 0;
        } else if (loader.getId() == VEHICLE_OUT_LOADER_ID){
            mTotalPrice = 0;
            mCaroutTotal = 0;
        }

    }
}
