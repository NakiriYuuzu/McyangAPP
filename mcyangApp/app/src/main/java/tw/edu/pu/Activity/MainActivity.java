package tw.edu.pu.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.R;
import tw.edu.pu.RequestModel.RequestHelper;

public class MainActivity extends AppCompatActivity {

    BeaconController beaconController;
    RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        initView();

        requestHelper.requestBasicPermission();
        requestHelper.requestBluetooth();
        beaconController.init_BroadcastBeacon();

    }

    private void initView() {
        beaconController = new BeaconController(this);
        requestHelper = new RequestHelper(this);
    }
}