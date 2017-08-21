package com.dinhcv.ticketmanagement.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
import com.dinhcv.ticketmanagement.utils.DataUtils;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;

import java.util.List;

public class CarInParkActivity extends AppCompatActivity{
    private static final String LOG_TAG = CarInParkActivity.class.getSimpleName();

    private static final int PAGE_SIZE = 10;
    private ListView mListview;
    private TextView tv_carTotal;
    private Boolean mBusy = false;
    private int mPageNext = 0;
    private boolean mIsLoad = false;
    private List<Vehicle> listCarInPark;
    private static final int VEHICLE_LOADER_ID = 1001;
    private LoaderManager.LoaderCallbacks callbacks;
    private VehicleAdapter mAdapter;
   // private Cursor mCursor;

    public static final String[] VEHICLE_PROJECTION = {
            ManagerContract.VehicleEntry.COLUMN_STATUS,
            ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE,
            ManagerContract.VehicleEntry.COLUMN_TIME_IN
    };

    public static final int INDEX_VEHICLE_STATUS = 0;
    public static final int INDEX_VEHICLE_LICENSE_PLATE = 1;
    public static final int INDEX_VEHICLE_TIME_IN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_in_park);

        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Cursor cursor = DataUtils.queryVehicleInPark(getApplicationContext());
        if (cursor.getCount() >= 0){
            initData(cursor);
        }
    }

    private void initView(){

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        toolBar.setTitle(getString(R.string.car_in_parking));
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();

        tv_carTotal = (TextView) findViewById(R.id.tv_carTotal);
        mListview = (ListView) findViewById(R.id.lv_ticket);
        //listCarInPark = new ArrayList<>();

       // registerEvent();


    }

    private void initData(Cursor c){
        //load from beginning
        mPageNext = 0;
        mIsLoad = true;
        //callbacks = CarInParkActivity.this;
        mAdapter = new VehicleAdapter(this);

        if (c.getCount() == 0){
            emptyLoad();
        } else {
            mAdapter.swapCursor(c);
            mListview.setAdapter(mAdapter);
        }

        tv_carTotal.setText(c.getCount() + "");

        registerEvent();
    }

    private void registerEvent(){
        //load more when scrolling end of table
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //load more data
                //Debug.normal("Ticket count: " + countTicket());
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if(lastInScreen == totalItemCount) {
                    if (!mBusy) {
                      //  new LoadTicketDataTask().execute(mPageNext, PAGE_SIZE);
                    }

                }
            }
        });
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        switch (id){
//            case VEHICLE_LOADER_ID: {
//                Uri uri = ManagerContract.VehicleEntry.CONTENT_URI;
//
//                return new CursorLoader(this,
//                        uri,
//                        VEHICLE_PROJECTION,
//                        null,
//                        null,
//                        null);
//            }
//            default:
//                throw new RuntimeException("Loader is not implemented: " + id);
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        if (data.getCount() > 0){
//            mAdapter.swapCursor(data);
////            while (data.moveToNext()){
////                Vehicle vehicle = new Vehicle();
////                vehicle.setStatus(data.getString(INDEX_VEHICLE_STATUS));
////                vehicle.setLicensePlate(data.getString(INDEX_VEHICLE_LICENSE_PLATE));
////                vehicle.setTimeIn(data.getString(INDEX_VEHICLE_TIME_IN));
////                listCarInPark.add(vehicle);
////            }
//            mListview.setAdapter(mAdapter);
//            tv_carTotal.setText(String.valueOf(getCountOfCarInPark(data)));
//        } else {
//            emptyLoad();
//            tv_carTotal.setText("0");
//        }
//    }

//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mAdapter.swapCursor(null);
//    }

    public class VehicleAdapter extends BaseAdapter {
        private Cursor mCursor;
        private Context mContext;

        VehicleAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            if (mCursor != null){
                return mCursor.getCount();
            } else {
                return 0;
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
                convertView = inflater.inflate(R.layout.activity_car_in_park_row, null, false);
            }

            mCursor.moveToPosition(position);


            final TextView tv_no = ((TextView) convertView.findViewById(R.id.tv_no));
            final ImageView img_carType = (ImageView) convertView.findViewById(R.id.img_carType);
            final TextView tv_lisence_plate= ((TextView) convertView.findViewById(R.id.tv_lisence_plate));
            final TextView tv_time = ((TextView) convertView.findViewById(R.id.tv_time));

            int count = parent.getChildCount();

            //set other background by odd/even
            convertView.setBackgroundResource((count % 2 == 0) ?
                    R.drawable.table_background_row_even_selector :
                    R.drawable.table_background_row_odd_selector);

            //set data
            tv_no.setText(String.valueOf(position + 1));

            int status = mCursor.getInt(PreferenceKey.INDEX_VEHICLE_STATUS);
            if (status == 1){
                img_carType.setImageResource(R.drawable.carin);
            }else if (status == 0){
                img_carType.setImageResource(R.drawable.carout);
            }
            String licensePlate = mCursor.getString(PreferenceKey.INDEX_VEHICLE_LICENSE_PLATE);
            tv_lisence_plate.setText(licensePlate);
            String timeIn = mCursor.getString(PreferenceKey.INDEX_VEHICLE_TIME_IN);
            String timeOut = mCursor.getString(PreferenceKey.INDEX_VEHICLE_TIME_OUT);
            String time = "Vào: " + timeIn + "\n" + "Ra: " + timeOut;
            tv_time.setText(time);

            return convertView;
        }

        public void swapCursor(Cursor cursor){
            mCursor = cursor;
            notifyDataSetChanged();
        }
    }

//    private class VehicleTask extends AsyncTask<Void, Void, Void>{
//        ProgressDialog progressDialog = null;
//        @Override
//        protected void onPreExecute() {
//            progressDialog = new ProgressDialog( CarInParkActivity.this );
//
//            progressDialog.setTitle("Loading...");
//
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            NetworkUtils.getVehicleListFromServer(getApplicationContext(), new NetworkExecuteCompleted() {
//                @Override
//                public void onSuccess() {
//                    Cursor cursor = DataUtils.queryVehicle(getApplicationContext(), ManagerContract.VehicleEntry.CONTENT_URI);
//                    if (cursor.getCount() >= 0){
//                        initData(cursor);
//                    }
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onError() {
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onFailed() {
//                    progressDialog.dismiss();
//                }
//            });
//            return null;
//        }
//    }

//    private class LoadTicketDataTask extends AsyncTask<Integer, Void, Cursor> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            if (mBusy || (!mIsLoad)) {
//                this.cancel(false);
//            }else {
//                mBusy = true;
//            }
//        }
//
//        @Override
//        protected Cursor doInBackground(Integer... params) {
//
//            if (isCancelled()) {
//                Debug.normal("Skip load ticket when system busy");
//                return Collections.emptyList();
//            }
//
//            //range of loading page
//            int pageIndex = params[0];
//            int pageSize = params[1];
//
//            Debug.normal("Load ticket: pageIndex(%d) pageSize(%d)", pageIndex, pageSize);
//
//            //load data
//            List<TicketInfo> found = mTicketModel.getTicketInParkingList(pageIndex, pageSize);
//            if ((found == null) || (found.size() < pageSize)) {
//                Debug.normal("Load to end position of database, Can not load more");
//                mIsLoad = false;
//            }
//
//            return found;
//        }
//
//        @Override
//        protected void onPostExecute(List<TicketInfo> ticketInfos) {
//            super.onPostExecute(ticketInfos);
//            if (isFinishing()) {
//                return;
//            }
//
//            mBusy = false;
//
//            if ((ticketInfos == null) || (ticketInfos.isEmpty())) {
//                Debug.normal("Cannot load more ticketInfos");
//                emptyLoad();
//                return;
//            }
//
//            //next page index
//            mPageNext++;
//
//            mTicketList.addAll(ticketInfos);
//            //add to view
//            addTicketToListView(mTicketList);
//        }
//    }
////
////
//    private void addTicketToListView(List<TicketInfo> ticketInfoList){
//
//        if ((ticketInfoList == null) || (ticketInfoList.isEmpty())){
//            Debug.normal("Room list is null");
//            mListview.setAdapter(null);
//            return;
//        }
//
//        // get listview current position - used to maintain scroll position
//        int currentPosition = mListview.getLastVisiblePosition();
//
//        TicketAdapter listAdapter = new TicketAdapter(getApplicationContext(), ticketInfoList);
//        mListview.setAdapter(listAdapter);
//
//        // Setting new scroll position
//        mListview.setSelectionFromTop( currentPosition + 1, 0);
//    }

    private void emptyLoad() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CarInParkActivity.this);

        dialog.setTitle(R.string.warning_title);
        dialog.setMessage(R.string.list_is_null);
        dialog.setIcon(R.drawable.dialog_info_icon);
        dialog.setPositiveButton(R.string.ok, null);
        dialog.show();

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
