package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.card.MaterialCardView;

import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RequestModel.RequestHelper;

public class MainActivity extends AppCompatActivity {

    MaterialCardView btn_Sign, btn_SignOut, btn_Qa, btn_Question, btn_Group, btn_EndClass;

    RequestHelper requestHelper;
    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initButton();
    }

    private void initButton() {
        btn_Sign.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), SignActivity.class);
            startActivity(ii);
        });

        btn_Qa.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), RaceActivity.class);
            startActivity(ii);
        });

        btn_Question.setOnClickListener(v -> beaconController.start_BroadcastBeacon());

        btn_SignOut.setOnClickListener(v -> finish());
    }

    private void initView() {
        requestHelper = new RequestHelper(this);
        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();

        beaconController = new BeaconController(this);
        beaconController.init_BroadcastBeacon();

        btn_Sign = findViewById(R.id.main_btn_Sign);
        btn_Group = findViewById(R.id.main_btn_Group);
        btn_Qa = findViewById(R.id.main_btn_Qa);
        btn_Question = findViewById(R.id.main_btn_Question);
        btn_SignOut = findViewById(R.id.main_btn_SignOut);
        btn_EndClass = findViewById(R.id.main_btn_EndClass);
    }
}