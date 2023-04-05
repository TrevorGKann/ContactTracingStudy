package com.trevor.CoCoT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

@SuppressLint("DefaultLocale")
public class MainActivity extends AppCompatActivity {

    TextView gyroText, accelText, magText, BLEText, participantIDText, fingerprintText;
    ProgressBar startupBar;
    Button startStopButton, manageStorageButton;

    int participantID;

    private LogFiler logFiler;
    BLEtools bletools;
    SensorHandler sensorHandler;

    PowerManager.WakeLock partialWakeLock;
    PowerManager.WakeLock proximityWakeLock;

    private ExperimentState experimentState;

    Notification notification;

    String[] allPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
    };


    ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                boolean allGranted = true;
                for (String permission : allPermissions) {
                    if (!result.get(permission)) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    Log.v(constants.LOG_TAG, "All permissions granted");
                } else {
                    Log.v(constants.LOG_TAG, "Not all permissions granted");
                }
            });

    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    Log.v(constants.LOG_TAG, "Permission granted");
                } else {
                    Log.v(constants.LOG_TAG, "Permission not granted");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        experimentState = ExperimentState.IDLE;
        collectVisibleAttributes();

        logFiler = LogFileAccesser.getLogFiler(getApplicationContext());
        bletools = new BLEtools((BluetoothManager) getSystemService(BLUETOOTH_SERVICE), getApplicationContext());
        sensorHandler = new SensorHandler((SensorManager) getSystemService(Context.SENSOR_SERVICE), getApplicationContext());

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SensorRecording::partialWakelockTag");
        proximityWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "SensorRecording::proximityWakelockTag");

        NotificationFactory.registerNotificationChannel(this);
        requestAllPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (experimentState == ExperimentState.RUNNING) {
            showExperimentNotification();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorHandler.unbindSensors();
        bletools.stopBLE();
        logFiler.closeLogFile(false);
        partialWakeLock.release();
        proximityWakeLock.release();
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void requestAllPermissions() {
        requestMultiplePermissionsLauncher.launch(allPermissions);
    }


    private void collectVisibleAttributes() {
        gyroText = findViewById(R.id.GyroText);
        accelText = findViewById(R.id.AccelText);
        magText = findViewById(R.id.magText);
        BLEText = findViewById(R.id.BLE_text);
        participantIDText = findViewById(R.id.participant_ID_text);
        fingerprintText = findViewById(R.id.fingerprintText);

        fingerprintText.setText("Fingerprint: " + FingerprintGenerator.getFingerPrint());

        startupBar = findViewById(R.id.startupBar);
        startupBar.setMax(10);
        startupBar.setVisibility(View.INVISIBLE);

        startStopButton = findViewById(R.id.start_stop_button);
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (experimentState) {
                    case IDLE:
                        startExperiment();
                        break;
                    case RUNNING:
                        stopExperiment();
                        break;
                }
            }
        });

        manageStorageButton = findViewById(R.id.storageManagerButton);
        manageStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] files = getApplicationContext().fileList();
                for (String filenames : files) {
                    Log.v(constants.LOG_TAG, "File found: " + filenames);
                }
            }
        });
    }


    private void bindSensors(){
        sensorHandler.bindAccelerometer(accelText);
        sensorHandler.bindGyroscope(gyroText);
        sensorHandler.bindMagnetometer(magText);
        sensorHandler.bindGravitometer();
    }

    private void lockVisibleAttributes(){
        participantIDText.setFocusable(false);
        participantIDText.setClickable(false);
        startStopButton.setClickable(false);
        participantIDText.setFocusableInTouchMode(false);
    }

    private void setExperimentState(ExperimentState state){
        switch (state){
            case IDLE:
                experimentState = ExperimentState.IDLE;
                startStopButton.setText(R.string.startStopButtonText_start);

                startStopButton.setClickable(true);
                participantIDText.setClickable(true);
                participantIDText.setFocusable(true);
                participantIDText.setFocusableInTouchMode(true);
                break;
            case RUNNING:
                experimentState = ExperimentState.RUNNING;
                startStopButton.setText(R.string.startStopButtonText_stop);

                startStopButton.setClickable(true);
                participantIDText.setFocusable(false);
                participantIDText.setClickable(false);
                participantIDText.setFocusableInTouchMode(false);
                break;
        }
    }

    private void startExperiment() {
        if (TextUtils.isEmpty(participantIDText.getText().toString())) {
            new AlertDialog.Builder(this)
                    .setTitle("Valid ID Required")
                    .setMessage("Please enter a valid participant ID")
                    .setPositiveButton("OK", null)
                    .create().show();
            return;
        }
        participantID = Integer.parseInt(participantIDText.getText().toString());

        lockVisibleAttributes();

        startupBar.setVisibility(View.VISIBLE);
        startupBar.setProgress(0);

        Log.d(constants.LOG_TAG, "Binding sensors");
        bindSensors();
        startupBar.incrementProgressBy(1);

        Log.d(constants.LOG_TAG, "Setting up BLE parameters");
        bletools.setParticipantID(participantID)
                .setRssiTargetTextView(BLEText);
        int bleSetupCode = bletools.setupBLE();
        if (bleSetupCode == -1) {
            Log.e(constants.LOG_TAG, "failed to setup BLE");
            failedToStartExperiment();
            return;
        }
        startupBar.incrementProgressBy(1);

        Log.d(constants.LOG_TAG, "Starting BLE advertisements");
        int bleStartReturnCode = bletools.StartBLEAdvertisements();
        if (bleStartReturnCode == -1) {
            Log.e(constants.LOG_TAG, "failed to start BLE advertisement");
            failedToStartExperiment();
            return;
        }
        startupBar.incrementProgressBy(1);

        Log.d(constants.LOG_TAG, "Starting BLE listener");
        bleStartReturnCode = bletools.startBLEListener();
        if (bleStartReturnCode == -1) {
            Log.e(constants.LOG_TAG, "failed to start BLE advertisement");
            failedToStartExperiment();
            return;
        }
        startupBar.incrementProgressBy(1);

        Log.d(constants.LOG_TAG, "creating log file");
        logFiler.setParticipantID(participantID);
        logFiler.createLogBuffer();
        startupBar.incrementProgressBy(1);

        Log.d(constants.LOG_TAG, "creating log file");
        logFiler.writeLogFileHeader();
        startupBar.incrementProgressBy(1);

        new AlertDialog.Builder(this)
                .setTitle("Starting Experiment")
                .setMessage("Please do not lock your screen for the duration of the experiment")
                .setPositiveButton("OK", null)
                .create().show();
        showExperimentNotification();

        startupBar.setProgress(startupBar.getMax());
        startupBar.setVisibility(View.INVISIBLE);


        setExperimentState(ExperimentState.RUNNING);

        //set window manager flag to keep_screen_on
        partialWakeLock.acquire();
        proximityWakeLock.acquire();
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.v(constants.LOG_TAG, "Experiment started");
    }

    private void failedToStartExperiment() {

        sensorHandler.unbindSensors();

        bletools.stopBLE();

        logFiler.closeLogFile(false);

        startupBar.setProgress(startupBar.getMax());
        startupBar.setVisibility(View.INVISIBLE);

        setExperimentState(ExperimentState.IDLE);
    }

    private void stopExperiment() {
        lockVisibleAttributes();
        startupBar.setVisibility(View.VISIBLE);
        startupBar.setProgress(0);

        sensorHandler.unbindSensors();
        startupBar.incrementProgressBy(1);

        bletools.stopBLE();
        startupBar.incrementProgressBy(1);


        logFiler.closeLogFile(true);
        startupBar.incrementProgressBy(1);

        //Start uploading text file
        startupBar.setVisibility(View.INVISIBLE);

        stopNotification();

        partialWakeLock.release();
        proximityWakeLock.release();

        uploadLogFile();

        setExperimentState(ExperimentState.IDLE);
    }

    private int uploadLogFile() {
        Toast.makeText(getApplicationContext(), R.string.uploadLogFile_feedback_upload, Toast.LENGTH_SHORT).show();
        logFiler.uploadUnuploadedLogFiles();
        return 1;
    }

    private void configureNotification(){
if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationFactory.registerNotificationChannel(this);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            if (notification==null){
                notification = NotificationFactory.createExperimentRunningNotification(getApplicationContext());
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showExperimentNotification(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationFactory.registerNotificationChannel(this);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            configureNotification();
            notificationManagerCompat.notify(constants.NOTIFICATION_ID, notification);
        }
    }

    private void stopNotification(){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.cancel(constants.NOTIFICATION_ID);
        notification = null;
    }
}
