package tw.edu.pu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import tw.edu.pu.Activity.Group.GroupActivity;
import tw.edu.pu.Activity.Race.RaceActivity;
import tw.edu.pu.Activity.Sign.SignActivity;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.R;
import tw.edu.pu.RequestModel.RequestHelper;

public class MainActivity extends AppCompatActivity {

    private boolean checked = true;

    MaterialCardView btnCreate, btnSign, btnGroup, btnRace, btnEndClass, btnSignOut;

    RequestHelper requestHelper;
    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();
        initButton();
        startScanning();
    }

    private void startScanning() {
        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                Log.e("", "startScanning: " + beacons.iterator().next().getDistance());
                beaconController.stopScanning();
                if (checked) {
                    checked =false;
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("同學提問")
                            .setMessage("同學：蔡淇鴻")
                            .setPositiveButton("確認", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                                new Handler().postDelayed(() -> checked = true, 5000);
                            }).setOnCancelListener(dialogInterface -> checked = true)
                            .show();
                }
            }
        });
    }

    private void initButton() {
        btnCreate.setOnClickListener(v -> {
            Intent ii = new Intent(this, CreateActivity.class);
            startActivity(ii);
        });

        btnSign.setOnClickListener(v -> {
            Intent ii = new Intent(this, SignActivity.class);
            startActivity(ii);
        });

        btnGroup.setOnClickListener(v -> {
            Intent ii = new Intent(this, GroupActivity.class);
            startActivity(ii);
        });

        btnRace.setOnClickListener(v -> {
            Intent ii = new Intent(this, RaceActivity.class);
            startActivity(ii);
        });


        btnEndClass.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            startActivity(ii);
        });

        // FIXME: Add when sign out turn off auto login
        btnSignOut.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnCreate = findViewById(R.id.main_btn_Create);
        btnSign = findViewById(R.id.main_btn_Sign);
        btnGroup = findViewById(R.id.main_btn_Group);
        btnRace = findViewById(R.id.main_btn_OpenQA);
        btnEndClass = findViewById(R.id.main_btn_EndClass);
        btnSignOut = findViewById(R.id.main_btn_SignOut);

        requestHelper = new RequestHelper(this);
        beaconController = new BeaconController(this);
        beaconController.beaconInit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        beaconController.stopScanning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stopScanning();
    }
}