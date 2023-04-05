package com.trevor.CoCoT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class request_permissions_view extends AppCompatActivity {

    Button request_permissions_button;

    String[] allPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//            Manifest.permission.BLUETOOTH_CONNECT,
//            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.BLUETOOTH_SCAN,
//            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
    };

    String[] safePermissions = {

    };
    String[] dangerousPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
//            Manifest.permission.BLUETOOTH,
//            Manifest.permission.BLUETOOTH_CONNECT,
//            Manifest.permission.BLUETOOTH_SCAN,
//            Manifest.permission.BLUETOOTH_ADVERTISE,
//            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
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
        setContentView(R.layout.activity_request_permissions_view);

        request_permissions_button = findViewById(R.id.request_permission_button);
        request_permissions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestButtonCallback();
            }
        });

        if (!checkPermissions()) {
            requestAllPermissions();
        } else {
            switchToMain();
        }

    }

    protected void requestButtonCallback () {
        if (!checkPermissions()) {
            requestPermissionsOneByOne();
        } else {
            switchToMain();
        }
    }

    protected void switchToMain(){
        Intent switchActivityToMain = new Intent(this, MainActivity.class);
        startActivity(switchActivityToMain);
    }

    public boolean checkPermissions() {
        for (String permission : allPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != 0) {
                return false;
            }
        }
        return true;
    }

    protected void requestAllPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationFactory.registerNotificationChannel(this);
        }
        requestMultiplePermissionsLauncher.launch(allPermissions);
    }

    protected void requestPermissionsOneByOne() {
        for (String permission : dangerousPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(permission);
                break;
            }
        }

        for (String permission : safePermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(permission);
            }
        }
    }
}