package com.dinhcv.ticketmanagement.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
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

public class LogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = LogActivity.class.getSimpleName();

    private Date mFromDate;
    private Date mToDate;
    private Timestamp mFromDateInTimeStamp;
    private Timestamp mToDateInTimeStamp;
    private Calendar mCalendar;

    private int mAnaYear;
    private int mAnaMonth;
    private int mAnaDay;

    private TextView edt_dateFrom;
    private TextView edt_dateTo;
    private Button btn_search;

    private ListView mListview;
    private Cursor mCursor;
    private LogAdapter mAdapter;
    private LoaderManager.LoaderCallbacks callbacks;

    private static final int USER_LOADER_ID = 1000;
    private static final int USER_WITH_TIME_LOADER_ID = 1001;

    private static final String[] USER_PROJECTION = {
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_USER_ID,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_USERNAME,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_EMAIL,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_ROLE,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_PARK_ID,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_WORKING_SHIFT,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_COST_BLOCK_1,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_COST_BLOCK_2,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_TIME_BLOCK_1,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_TIME_BLOCK_2,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_IP,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_LOGIN_TIME,
            ManagerContract.UserInfoBeingManagedEntry.COLUMN_LOGOUT_TIME
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initDateData();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        callbacks = this;
        Log.e(LOG_TAG, "On start");
        initData();
        new UserListTask().execute();
    }

    private void initView(){

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.search_title));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        mListview = (ListView) findViewById(R.id.lv_ticket);
        btn_search = (Button) findViewById(R.id.btn_search);
        edt_dateFrom = (TextView) findViewById(R.id.edt_dateFrom);
        edt_dateTo = (TextView) findViewById(R.id.edt_dateTo);
        edt_dateFrom.setKeyListener(null);
        edt_dateTo.setKeyListener(null);
        edt_dateTo.setFocusable(false);
        edt_dateFrom.setFocusable(false);

        mAdapter = new LogAdapter(this);

    }

    private void handleSearchLog(){
        if (!checkTimeInvalid()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(LogActivity.this);
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
            getSupportLoaderManager().destroyLoader(USER_WITH_TIME_LOADER_ID);
            getSupportLoaderManager().restartLoader(USER_WITH_TIME_LOADER_ID, null, callbacks);
        }
    }

    private void initData(){
        String fromDate = Utils.convertOnlyDateToString(mFromDate);
        String toDate = Utils.convertOnlyDateToString(mToDate);
        Log.e(LOG_TAG, "From: " + mFromDate.toString() + " - To: " + mToDate.toString());
        edt_dateFrom.setText(fromDate);
        edt_dateTo.setText(toDate);
        mFromDateInTimeStamp = new Timestamp(mFromDate.getTime());
        mToDateInTimeStamp = new Timestamp(mToDate.getTime());

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


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // handle search
                handleSearchLog();
            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "OnCreateLoader with id: " + id);
        switch (id){
            case USER_LOADER_ID: {
                Uri uri = ManagerContract.UserInfoBeingManagedEntry.CONTENT_URI;
                return new CursorLoader(getApplicationContext(),
                        uri,
                        USER_PROJECTION,
                        null,
                        null,
                        null);
            }
            case USER_WITH_TIME_LOADER_ID: {
                Uri uri = ManagerContract.UserInfoBeingManagedEntry.CONTENT_URI;
                return new CursorLoader(getApplicationContext(),
                        uri,
                        USER_PROJECTION,
                        ManagerContract.UserInfoBeingManagedEntry.COLUMN_LOGIN_TIME + " BETWEEN ? AND ?",
                        new String[]{mFromDateInTimeStamp.toString(), mToDateInTimeStamp.toString()},
                        null);
            }
            default:
                throw new UnsupportedOperationException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(LOG_TAG, "OnLoadFinished");
        Log.e(LOG_TAG, "Cursor count: " + data.getCount());
        if (data != null){
            mAdapter.swapData(data);
            mListview.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(LOG_TAG, "OnLoadReset");
        mAdapter.swapData(null);
    }

    private class UserListTask extends AsyncTask<String, Void, Void>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( LogActivity.this );

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
                    getSupportLoaderManager().initLoader(USER_LOADER_ID, null, callbacks);
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

    public class LogAdapter extends BaseAdapter {
        private Context mContext;
        private Cursor mCursor;

        public LogAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            if(mCursor != null){
                return mCursor.getCount();
            } else {
                return -1;
            }
        }

        @Override
        public Object getItem(int position) {
            return mCursor.moveToPosition(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_log_row, null, false);
            }
            mCursor.moveToPosition(position);
            final TextView tv_no = ((TextView) convertView.findViewById(R.id.tv_no));
            final TextView tv_userId = (TextView) convertView.findViewById(R.id.tv_userId);
            final TextView tv_account= ((TextView) convertView.findViewById(R.id.tv_account));
            final TextView tv_timeIn = ((TextView) convertView.findViewById(R.id.tv_timeIn));
            final TextView tv_timeOut = ((TextView) convertView.findViewById(R.id.tv_timeOut));

            int count = parent.getChildCount();
            if (mCursor == null){
                return null;
            }

            //set other background by odd/even
            convertView.setBackgroundResource((count % 2 == 0) ?
                    R.drawable.table_background_row_even_selector :
                    R.drawable.table_background_row_odd_selector);

            //set data
            tv_no.setText(String.valueOf(position + 1));

            tv_userId.setText(String.valueOf(mCursor.getInt(PreferenceKey.INDEX_USER_INFO_ID)));
            tv_account.setText(mCursor.getString(PreferenceKey.INDEX_USER_INFO_USERNAME));
            String timeIn = mCursor.getString(PreferenceKey.INDEX_USER_INFO_LOGIN_TIME);
            if (timeIn != null){
                tv_timeIn.setText(timeIn);
            } else {
                tv_timeIn.setText("null");
            }

            String timeOut = mCursor.getString(PreferenceKey.INDEX_USER_INFO_LOGOUT_TIME);
            if (timeOut != null){
                tv_timeOut.setText(timeOut);
            } else {
                tv_timeOut.setText("null");
            }

            return convertView;
        }

        public void swapData(Cursor cursor){
            mCursor = cursor;
            notifyDataSetChanged();
        }
    }

    private void emptyLoad() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LogActivity.this);

        dialog.setTitle(R.string.error_title);
        dialog.setMessage(R.string.list_is_null);
        dialog.setIcon(R.drawable.dialog_warning_icon);
        dialog.setPositiveButton(R.string.ok, null);
        dialog.show();

    }


    /**
     * Show from date picker dialog
     */
    private void handleFromDatePicker() {

        new DatePickerDialog(LogActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar.set(Calendar.HOUR_OF_DAY, 0);
                mCalendar.set(Calendar.MINUTE, 0);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
                mFromDate = mCalendar.getTime();
                mAnaYear = year;
                mAnaMonth = monthOfYear;
                mAnaDay = dayOfMonth;
                edt_dateFrom.setText(Utils.convertOnlyDateToString(mFromDate));
            }
        }, mAnaYear, mAnaMonth, mAnaDay).show();

    }

    /**
     * Show from date picker dialog
     */
    private void handleToDatePicker() {

        new DatePickerDialog(LogActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar.set(Calendar.HOUR_OF_DAY, 23);
                mCalendar.set(Calendar.MINUTE, 59);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
                mToDate = mCalendar.getTime();
                mAnaYear = year;
                mAnaMonth = monthOfYear;
                mAnaDay = dayOfMonth;
                edt_dateTo.setText(Utils.convertOnlyDateToString(mToDate));
            }
        }, mAnaYear, mAnaMonth, mAnaDay).show();

    }


    private void initDateData(){

        Calendar now = Calendar.getInstance();
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        mAnaYear = now.get(Calendar.YEAR);
        mAnaMonth = now.get(Calendar.MONTH);
        mAnaDay = now.get(Calendar.DAY_OF_MONTH);

        Debug.normal("Today: "+now);

        // set start date
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        mFromDate = mCalendar.getTime();

        // set end date
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        mToDate = cal.getTime();

    }

    /**
     * Check time invalid
     */
    private boolean checkTimeInvalid(){

        long fromDate = mFromDate.getTime();
        long toDate = mToDate.getTime();

        long sub = toDate - fromDate ;
        if (sub < 0) {
            Debug.error("Error setting time");
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

}
