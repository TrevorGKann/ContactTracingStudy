package com.trevor.CoCoT;

import static android.os.SystemClock.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BLEtools extends AppCompatActivity {

    Context context;
    LogFiler logFiler;


    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private BluetoothLeScanner bluetoothLeScanner;
    private AdvertiseSettings advertiseSettings;
    private AdvertiseData data;
    private List<ScanFilter> scanningFilters;
    private ScanFilter scanningFilter;
    private ParcelUuid uuid;
    private ParcelUuid dataUuid;
    private ScanSettings scanSettings;
    private boolean mScanning;
    private ScanCallback mScanCallback;
    private HashMap<Integer, Integer> phonesSeen;

    TextView rssiTargetTextView;

    int participantID;

    public BLEtools(BluetoothManager bluetoothManager, Context context) {
        this.bluetoothManager = bluetoothManager;
        this.context = context;

        logFiler = LogFileAccesser.getLogFiler(context);
    }

    protected BLEtools setParticipantID(int participantID) {
        this.participantID = participantID;
        return this;
    }

    protected BLEtools setRssiTargetTextView(TextView targetTextView) {
        this.rssiTargetTextView = targetTextView;
        return this;
    }

    @SuppressLint("MissingPermission")
    protected int setupBLE() {

        //get handlers
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
            Toast.makeText(context, "Turned on Bluetooth", Toast.LENGTH_SHORT).show();
            sleep(100);
        }
        bluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();


        //check if BLE is supported
        if (mBluetoothAdapter == null || bluetoothLeScanner == null) {
            Toast.makeText(context, "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
            return -1;
        }
        if (!mBluetoothAdapter.isMultipleAdvertisementSupported()) {
            Toast.makeText(context, "Your device does not support this functionality", Toast.LENGTH_SHORT).show();
            return -1;
        }


        //configure broadcast
        advertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(constants.ADVERTISE_MODE)
                .setConnectable(constants.ADVERTISE_CONNECTABLE)
                .setTxPowerLevel(constants.ADVERTISE_TX_POWER_LEVEL)
//                .setTimeout(constants.ADVERTISE_TIMEOUT_MILLISECONDS)
                .build();

        uuid = new ParcelUuid(UUID.fromString(constants.SERVICE_STRING));
        dataUuid = new ParcelUuid(UUID.fromString("0000950d-0000-1000-8000-00805f9b34fb"));
        /*
         * no idea why this particular data UUID works and the other one doesn't.
         * but you can't have the original uuid in the data advertisement and you can't leave it out
         * otherwise the filtering doesn't work.
         */


        data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(uuid)
                .addServiceData(dataUuid, String.format("%d", participantID).getBytes())
                .build();


        //configure scanning
        scanningFilter = new ScanFilter.Builder()
                .setServiceUuid(uuid)
                .build();
        scanningFilters = new ArrayList<>();
        scanningFilters.add(scanningFilter);

        scanSettings = new ScanSettings.Builder()
                .setScanMode(constants.SCAN_MODE)
                .build();

        phonesSeen = new HashMap<>();

        return 1;
    }

    @SuppressLint("MissingPermission")
    protected int StartBLEAdvertisements() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            Toast.makeText(context, "Turned on Bluetooth", Toast.LENGTH_SHORT).show();
            sleep(100);
        }
        bluetoothLeAdvertiser.startAdvertising(advertiseSettings, data, mAdvertiseCallback);
        return 1;
    }

    @SuppressLint("MissingPermission")
    protected void stopBLEAdvertise() {
        if (bluetoothLeAdvertiser != null) {
            bluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        }

    }

    private final AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.d(constants.LOG_TAG, "Advertise server started.");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.e(constants.LOG_TAG, "Peripheral advertising failed: " + errorCode);
        }
    };

    @SuppressLint("MissingPermission")
    protected int startBLEListener() {
        bluetoothLeScanner.startScan(scanningFilters, scanSettings, new BLEScannerCallback());
        mScanning = true;
        Log.d(constants.LOG_TAG, "Started BLE scanning");

        return 1;
    }

    private void stopBLEListener() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
                && mScanning && mBluetoothAdapter != null
                && mBluetoothAdapter.isEnabled() && bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(mScanCallback);
            Log.d(constants.LOG_TAG, "Stopped BLE scanning");
        }

        mScanCallback = null;
        mScanning = false;

//        wakeLock.release();
    }

    protected boolean isBLERegistered(){
        return bluetoothManager != null;
    }

    protected void stopBLE(){
        stopBLEListener();
        stopBLEAdvertise();
        clearPhonesSeenArray();
    }

    protected void clearPhonesSeenArray(){
        if (phonesSeen!=null){
            phonesSeen = null;
        }
    }

    private final class BLEScannerCallback extends ScanCallback {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            individualResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                individualResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(constants.LOG_TAG, "BLE Scan Failed with code " + errorCode);
        }

        private void individualResult(ScanResult result){
            BluetoothDevice device = result.getDevice();
            String MAC = device.getAddress();
            String receivedData = new String(result.getScanRecord().getServiceData(dataUuid));
            int otherParticipantID = Integer.parseInt(receivedData);
            int rssi = result.getRssi();
            recordBLEReceived(MAC, otherParticipantID, rssi);
        }
    }

    private void recordBLEReceived(String MAC, int otherParticipantID, int rssi){
        String logString = context.getResources().getString(R.string.BLELog,
                otherParticipantID, rssi);
        if (phonesSeen != null) {phonesSeen.put(otherParticipantID, rssi);}

        logFiler.logLine(logString);
        writeRSSIsToScreen();
    }

    public void writeRSSIsToScreen(){
        String rssiText = getSeenPhonesList();
        if (rssiText != null) {
            rssiTargetTextView.setText(rssiText);
        }
    }

    public String getSeenPhonesList(){
        final int MAX_DISPLAY = 5;
        StringBuilder writeText = new StringBuilder();
        if (phonesSeen == null) return "";
        int displayCount = 0;
        for (Integer otherID : phonesSeen.keySet()){
            int otherRSSI = phonesSeen.get(otherID);
            writeText.append(otherID).append(" at ").append(otherRSSI).append("\n");
            displayCount++;
            if (displayCount >= MAX_DISPLAY) break;
        }
        return writeText.toString();
    }
}
