package com.trevor.CoCoT;

import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanSettings;

import java.util.UUID;

public class constants {
    //public static String SERVICE_STRING = "7D2EA28A-F7BD-485A-BD9D-92AD6ECFE93E";//from demo
    public static String SERVICE_STRING = "BEABEEBE-ABEE-BEAB-EEBE-ABEEBEABEEBE";//test
    //public static String SERVICE_STRING = "0000fd6f-0000-1000-8000-00805f9b34fb";
    public static UUID SERVICE_UUID = UUID.fromString(SERVICE_STRING);


    public static final long SCAN_PERIOD = 5000;
    public static final String LOG_TAG = "BLE_App";
    public static final int ADVERTISE_MODE = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY;
    public static final int SCAN_MODE = ScanSettings.SCAN_MODE_LOW_LATENCY;
    public static final int ADVERTISE_TX_POWER_LEVEL = AdvertiseSettings.ADVERTISE_TX_POWER_HIGH;
    public static final int ADVERTISE_TIMEOUT_MILLISECONDS = 100000;
    public static final int SCAN_TIMEOUT_MILLI_S = 250;
    public static final boolean ADVERTISE_CONNECTABLE = false;


    public static final int MICROSECONDS_TO_SECONDS = 1000000;
    public static final int SAMPLE_FREQUENCY = 120;
    public static final int SENSOR_LATENCY = MICROSECONDS_TO_SECONDS / SAMPLE_FREQUENCY;

    public static final String SERVER_NAME = "maritlage.andrew.cmu.edu";
    public static final String SERVER_PORT = "5000";
    public static final String SERVER_EXTENSION = "/upload";
    public static final String SERVER_URL = "https://" + SERVER_NAME + ":" + SERVER_PORT + SERVER_EXTENSION;

    public static final int MAX_LOG_LINES = 5000;


    static final String CHANNEL_ID = "CoCoT";
    static final int NOTIFICATION_ID = 1;
}
