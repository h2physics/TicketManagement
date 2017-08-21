package com.dinhcv.ticketmanagement.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.print.sdk.PrinterInstance;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.TicketManagermentApplication;
import com.dinhcv.ticketmanagement.activity.AlertDialog.InputLisencePlateDialogBuilder;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Park.Park;
import com.dinhcv.ticketmanagement.model.structure.User.UserInformation;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import com.dinhcv.ticketmanagement.network.NetworkExecuteCompleted;
import com.dinhcv.ticketmanagement.network.NetworkUtils;
import com.dinhcv.ticketmanagement.utils.DataUtils;
import com.dinhcv.ticketmanagement.utils.Debug;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;
import com.dinhcv.ticketmanagement.utils.PreferenceUtils;
import com.dinhcv.ticketmanagement.utils.Utils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.dinhcv.ticketmanagement.lib.IntentIntegrator;
import com.dinhcv.ticketmanagement.lib.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String TAG = "AndroidCameraApi";
    public static final String WHICH_TICKET = "which.ticket";
    private ImageButton btn_carin;
    private ImageButton btn_carout;
    private ImageButton btn_carinParking;
    private ImageButton btn_statistic;
    private ImageButton btn_search;
    private ImageButton btn_addUser;
    private TextureView textureView;
    private LinearLayout ll_p1;
    private LinearLayout ll_p2;
    private LinearLayout ll_p3;
    private LinearLayout ll_p4;
    private Toolbar toolBar;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private Size mPreviewSize;
    private int mSensorOrientation;
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    private String CAR_IN = "xe_vao";
    private String CAR_OUT = "xe_ra";
    private String CAR_TYPE = "";
    private String FILE_PATH = "";
    private int CARTYPE = 1;
    private int STATUS = 1;


    // Intent request codes
    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 2;
    private Dialog dialogInput;
    //private m_setting_block settingBlock;

    private PrinterInstance mPrinter;
    private UserInformation user;

    public static final int INDEX_USER_ID = 0;
    public static final int INDEX_NAME = 1;
    public static final int INDEX_EMAIL = 2;
    public static final int INDEX_ROLE = 3;
    public static final int INDEX_PARK_ID = 4;
    public static final int INDEX_WORKING_SHIFT = 5;
    public static final int INDEX_COST_BLOCK_1 = 6;
    public static final int INDEX_COST_BLOCK_2 = 7;
    public static final int INDEX_TIME_BLOCK_1 = 8;
    public static final int INDEX_TIME_BLOCK_2 = 9;
    public static final int INDEX_IP = 10;
    public static final int INDEX_LOGIN_TIME = 11;
    public static final int INDEX_LOGOUT_TIME = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = DataUtils.queryCurrentUserInfo(getApplicationContext());
        Log.e(LOG_TAG, "User: " + user.toString());
        initData(user);
        new ParkTask().execute(user);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver();
    }

    private void checkConnectPrinter(){

        TicketManagermentApplication app = (TicketManagermentApplication) getApplication();
        mPrinter = app.getIPrinter();

        if (mPrinter == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(   R.string.warning_title );
            alert.setMessage( R.string.not_connect_print );
            alert.setIcon(    R.drawable.dialog_info_icon );
            alert.setPositiveButton(R.string.ok, null );
            alert.show();
        }

    }



    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

            if(cameraDevice != null){
                closeCamera();

                cameraDevice = null;
            }

            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };


    private void initView(){
        toolBar = (Toolbar) findViewById(R.id.toolbar_top);
        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        toolBar.setLogo(R.mipmap.ic_launcher);

        ll_p1 = (LinearLayout) findViewById(R.id.ll_p1);
        ll_p2 = (LinearLayout) findViewById(R.id.ll_p2);
        ll_p3 = (LinearLayout) findViewById(R.id.ll_p3);
        ll_p4 = (LinearLayout) findViewById(R.id.ll_p4);

        btn_carin = (ImageButton) findViewById(R.id.btn_carin);
        btn_carout = (ImageButton) findViewById(R.id.btn_carout);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_carinParking = (ImageButton) findViewById(R.id.btn_carinParking);
        btn_statistic = (ImageButton) findViewById(R.id.btn_statistic);
        btn_addUser = (ImageButton) findViewById(R.id.btn_addUser);


    }

    private void initData(UserInformation user){
        //permission = sharedPreferences.getInt(PreferenceKey.PREF_USER_PERMISSION, -1);
        Log.e(LOG_TAG, "Permission: " + user.getRole().getRoleId());
        if (user.getRole().getRoleId() == 3){
            toolBar.setTitle(getString(R.string.look_car));
        }else if (user.getRole().getRoleId() == 2){
            toolBar.setTitle(getString(R.string.manager));
        }else if (user.getRole().getRoleId() == 1){
            Log.e(LOG_TAG, "ADMIN");
            toolBar.setTitle(getString(R.string.admin));
        }
        setSupportActionBar(toolBar);
        toolBar.showOverflowMenu();
        textureView.setSurfaceTextureListener(textureListener);
        btn_carin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAR_TYPE = CAR_IN;
                CARTYPE = 1;
                STATUS = 1;
                takePicture();
            }
        });

        btn_carout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAR_TYPE = CAR_OUT;
                CARTYPE = 2;
                STATUS = 2;
                //takePicture();
                scanBarcode();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchCarActivity.class);
                startActivity(intent);
            }
        });

        btn_carinParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CarInParkActivity.class);
                startActivity(intent);
            }
        });

        btn_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });

        btn_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        });

        //int permission = Settings.getPermission();
        if (user.getRole().getRoleId() == 3){
            ll_p1.setVisibility(View.GONE);
            ll_p2.setVisibility(View.GONE);
            ll_p3.setVisibility(View.GONE);
            ll_p4.setVisibility(View.GONE);
        }
    }

    private class ParkTask extends AsyncTask<UserInformation, Void, Void>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( MainActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final UserInformation... params) {
            NetworkUtils.getParkFromServer(getApplicationContext(), String.valueOf(params[0].getParkId()), new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    Park park = DataUtils.getCurrentPark(getApplicationContext(), ManagerContract.ParkEntry.CONTENT_URI);
                    new VehicleTask().execute(String.valueOf(park.getId()));
                    progressDialog.dismiss();
                    Log.e(LOG_TAG, "Park success");
                }

                @Override
                public void onError() {
                    progressDialog.dismiss();
                    Log.e(LOG_TAG, "Park error");
                }

                @Override
                public void onFailed() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle(   R.string.warning_title );
                    alert.setMessage( R.string.load_park_failed );
                    alert.setIcon(    R.drawable.dialog_info_icon );
                    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Park park = DataUtils.getCurrentPark(getApplicationContext(), ManagerContract.ParkEntry.CONTENT_URI);
                            new VehicleTask().execute(String.valueOf(park.getId()));
                        }
                    });
                    alert.show();
                    progressDialog.dismiss();
                    Log.e(LOG_TAG, "Park failed");
                }
            });
            return null;
        }

    }

    private class VehicleTask extends AsyncTask<String, Void, Void>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( MainActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            NetworkUtils.getVehicleListFromServer(getApplicationContext(), params[0], new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    checkConnectPrinter();
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {
                    checkConnectPrinter();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailed() {
                    checkConnectPrinter();
                    progressDialog.dismiss();
                }
            });
            return null;
        }
    }

    private class UpdateLogoutTimeTask extends AsyncTask<Void, Void, Void>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( MainActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            NetworkUtils.updateUserTimeLogout(getApplicationContext(), new NetworkExecuteCompleted() {
                @Override
                public void onSuccess() {
                    startLoginActivity();
                    PreferenceUtils.setIsLogin(getApplicationContext(), false);
                    PreferenceUtils.setTokenReferences(getApplicationContext(), null);
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {
                    startLoginActivity();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailed() {
                    startLoginActivity();
                    PreferenceUtils.setIsLogin(getApplicationContext(), false);
                    PreferenceUtils.setTokenReferences(getApplicationContext(), null);
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle(   R.string.notify_string );
                    alert.setMessage( R.string.logout_failed );
                    alert.setIcon(    R.drawable.dialog_warning_icon );
                    alert.setPositiveButton(R.string.ok, null );
                    alert.show();
                    progressDialog.dismiss();
                }
            });
            return null;
        }
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(MainActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected void takePicture() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = null;
            try {
                characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 480;
            int height = 640;
            //if (jpegSizes != null && 0 < jpegSizes.length) {
            //  width = jpegSizes[0].getWidth();
            // height = jpegSizes[0].getHeight();
            //}
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            reader.acquireLatestImage ();
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));
            Date now = new Date();
            String fileName = CAR_TYPE + Utils.convertDateToString(now) + ".jpg";

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Ticketmanager");
            boolean success = true;
            if (!folder.exists()) {
                folder.mkdirs();
            }

            final File file = new File(Environment.getExternalStorageDirectory() +"/Ticketmanager/" + fileName);
            FILE_PATH = Environment.getExternalStorageDirectory() +"/Ticketmanager/" + fileName;
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireNextImage();

                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];

                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    //Toast.makeText(MainActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    //createCameraPreview();
                    if (CARTYPE == 1 ) {
                        dialogInput = new InputLisencePlateDialogBuilder().create(MainActivity.this, new InputLisencePlateDialogBuilder.OnRereadingStatusListener() {
                            @Override
                            public void onRereadingStatus(String lisence) {
                                if (lisence == null) {
                                    File file= new File(FILE_PATH);
                                    if(file.exists())
                                    {
                                        file.delete();
                                    }
                                    dialogInput.dismiss();
                                    createCameraPreview();
                                    return;
                                }

                                Debug.normal("String lisence: " + lisence);
                                // Save reinspect comment information
                                Log.e("String lisence", lisence);
                                saveCarInTicket(lisence, FILE_PATH);
                            }
                        });

                        dialogInput.show();
                    }else {
                        String filePath = FILE_PATH;
                        Debug.normal("GET FILE---------------------------");
                        Bitmap bmp = BitmapFactory.decodeFile(filePath);
                        Matrix matrix = new Matrix();
                        if (!Utils.isPantech()) {
                            matrix.postRotate(90);
                        }
                        Bitmap bMap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

                        Debug.normal("CreateBitmap FILE---------------------------");
                        String contents = null;

                        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
                        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

                        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        Reader reader = new MultiFormatReader();
                        Result resultImg = null;
                        try {
                            resultImg = reader.decode(bitmap);
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        } catch (ChecksumException e) {
                            e.printStackTrace();
                        } catch (FormatException e) {
                            e.printStackTrace();
                        }

                        if (resultImg != null) {
                            contents = resultImg.getText();
                            Debug.normal("Content BARCOD image: "+contents);

                            new SaveCarOutTicketTask().execute(contents);
                        }else {
                            Debug.normal("Cannot decode BARCOD image: ");
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle(   R.string.error_title );
                            alert.setMessage( R.string.carout_save_error );
                            alert.setIcon(    R.drawable.dialog_warning_icon );
                            alert.setPositiveButton(R.string.ok, null );
                            alert.show();

                            File file= new File(FILE_PATH);
                            if(file.exists())
                            {
                              //  file.delete();
                                Debug.normal("DElete image....");
                            }

                            createCameraPreview();
                        }

                    }

                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            Log.e(LOG_TAG, mPreviewSize.getWidth() + " - " + mPreviewSize.getHeight());
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            setUpCameraOutputs(textureView.getWidth(), textureView.getHeight());
            configureTransform(textureView.getWidth(), textureView.getHeight());
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            assert map != null;
            //imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());


                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);

                // Check if the flash is supported.
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
        }
    }

    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        int rota = (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
        Debug.normal("Orientation Rotation: "+rota);
        return rota ;

    }


    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }


    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == textureView || null == mPreviewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }

        matrix.postRotate(0, centerX, centerY);
        textureView.setTransform(matrix);
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void saveCarInTicket(String licensePlate, String filePath) {

        Date date = new Date();
        String timeIn = Utils.convertDateInToString(date);

        Vehicle vehicle = new Vehicle();
        vehicle.setImageCarIn(filePath);
        vehicle.setTimeIn(timeIn);
        vehicle.setLicensePlate(licensePlate);

        Intent intent = new Intent(MainActivity.this, CarInActivity.class);
        intent.putExtra(PreferenceKey.EXTRA_VEHICLE, vehicle);
        startActivity(intent);

    }


    private class SaveCarOutTicketTask extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog( MainActivity.this );

            progressDialog.setTitle("Loading...");

            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String licenseCode = params[0];
            Log.e("License", licenseCode);
            return licenseCode;
        }

        @Override
        protected void onPostExecute(String licenseCode) {
            super.onPostExecute(licenseCode);

            progressDialog.dismiss();

            Uri uri = ManagerContract.VehicleEntry.buildUriWithLicenseCode(licenseCode);
            Cursor cursor = getApplicationContext().getContentResolver().query(uri,
                    null,
                    ManagerContract.VehicleEntry.COLUMN_BARCODE + "=?",
                    new String[]{licenseCode},
                    null,
                    null);

            Log.e(LOG_TAG, cursor.getCount() + "");
            int status = cursor.getInt(PreferenceKey.INDEX_VEHICLE_STATUS);
            if (cursor.getCount() > 0 && status != 0){
                Intent intentCarOut = new Intent(MainActivity.this, CarOutActivity.class);
                intentCarOut.putExtra(PreferenceKey.EXTRA_BARCODE, licenseCode);
                startActivity(intentCarOut);
            }else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(   R.string.error_title );
                alert.setMessage( R.string.carout_save_error );
                alert.setIcon(    R.drawable.dialog_warning_icon );
                alert.setPositiveButton(R.string.ok, null );
                alert.show();

//                File file= new File(FILE_PATH);
//                if(file.exists())
//                {
//                    file.delete();
//                }

                onStart();
            }

        }
    }


    private void logout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage(R.string.confirm_logout);
        alert.setIcon(R.drawable.dialog_warning_icon);
        alert.setTitle(R.string.logout);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handleLogOutApp();
            }
        });

        alert.setNegativeButton(R.string.cancel, null);
        alert.show();
    }

    private void handleLogOutApp(){

        // clear current user

        // save log
        new UpdateLogoutTimeTask().execute();
    }

    private void startLoginActivity(){
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        logoutIntent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Can't scan barcode", Toast.LENGTH_SHORT).show();
            }
            else {
                new SaveCarOutTicketTask().execute(result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        MenuItem item = menu.findItem(R.id.config_menu);
        if (user.getRole().getRoleId()  == 3) {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.printer_menu:
                intent = new Intent(MainActivity.this, BluetoothConectActivity.class);
                startActivity(intent);
                return true;
            case R.id.config_menu:
                intent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(intent);
                return true;

            case R.id.logout_menu:
                // handle logout app
                logout();;
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
