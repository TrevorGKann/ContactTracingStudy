package com.trevor.CoCoT;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

public class SensorHandler {

    Context context;

    private SensorManager sensorManager;
    private TextView gyroText, accelText, magText, BLEText, participantIDText;

    private LogFiler logFiler;


    public SensorHandler(SensorManager sensorManager, Context context){
        this.sensorManager = sensorManager;
        this.context = context;
        logFiler = LogFileAccesser.getLogFiler(context);
    }

    protected void unbindSensors() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(AccelerometerListener);
            sensorManager.unregisterListener(GyroscopeListener);
            sensorManager.unregisterListener(magnetometerListener);
            sensorManager.unregisterListener(gravityListener);
        }
    }

    protected void bindAccelerometer(TextView accelText){
        this.accelText = accelText;
        Log.d(constants.LOG_TAG, "Binding accel");
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(AccelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL, constants.SENSOR_LATENCY);
    }

    protected void bindGyroscope(TextView gyroText){
        this.gyroText = gyroText;
        Log.d(constants.LOG_TAG, "Binding gyro");
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(GyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL, constants.SENSOR_LATENCY);
    }

    protected void bindMagnetometer(TextView magText){
        this.magText = magText;
        Log.d(constants.LOG_TAG, "Binding magnetometer");
        Sensor magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(magnetometerListener, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL, constants.SENSOR_LATENCY);
    }

    protected void bindGravitometer(){
        Log.d(constants.LOG_TAG, "Binding gravity");
        Sensor gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(gravityListener, gravity, SensorManager.SENSOR_DELAY_NORMAL, constants.SENSOR_LATENCY);
    }

    private final SensorEventListener AccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xValue = event.values[0];
            float yValue = event.values[1];
            float zValue = event.values[2];

            String displayString = context.getResources().getString(R.string.FieldReport,
                    "Accelerometer", xValue, yValue, zValue);
            String logString = context.getResources().getString(R.string.FieldReportLog,
                    "Accelerometer", xValue, yValue, zValue);

            accelText.setText(displayString);
            logFiler.logLine(logString);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.v(constants.LOG_TAG, "Accelerometer accuracy changed: " + accuracy);
        }
    };

    private final SensorEventListener GyroscopeListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // The acceleration may be negative, so take their absolute value
            float xValue = event.values[0];
            float yValue = event.values[1];
            float zValue = event.values[2];

            String displayString = context.getResources().getString(R.string.FieldReport,
                    "Gyroscope", xValue, yValue, zValue);
            String logString = context.getResources().getString(R.string.FieldReportLog,
                    "Gyroscope", xValue, yValue, zValue);

            gyroText.setText(displayString);
            logFiler.logLine(logString);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.v(constants.LOG_TAG, "Gyroscope accuracy changed: " + accuracy);
        }
    };

    private final SensorEventListener magnetometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // The acceleration may be negative, so take their absolute value
            float xValue = event.values[0];
            float yValue = event.values[1];
            float zValue = event.values[2];

            String displayString = context.getResources().getString(R.string.FieldReport,
                    "Magnetic-field", xValue, yValue, zValue);
            String logString = context.getResources().getString(R.string.FieldReportLog,
                    "Magnetic-field", xValue, yValue, zValue);

            magText.setText(displayString);
            logFiler.logLine(logString);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.v(constants.LOG_TAG, "Magnetic-field accuracy changed: " + accuracy);
        }
    };

    private final SensorEventListener gravityListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // The acceleration may be negative, so take their absolute value
            float xValue = event.values[0];
            float yValue = event.values[1];
            float zValue = event.values[2];

//            String displayString = context.getResources().getString(R.string.FieldReport,
//                    "Gravity", xValue, yValue, zValue);
            String logString = context.getResources().getString(R.string.FieldReportLog,
                    "Gravity", xValue, yValue, zValue);

//            accelText.setText(displayString);
            logFiler.logLine(logString);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.v(constants.LOG_TAG, "Gravity accuracy changed: " + accuracy);
        }
    };
}
