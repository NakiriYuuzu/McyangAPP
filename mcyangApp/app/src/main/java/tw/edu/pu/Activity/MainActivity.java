package tw.edu.pu.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import tw.edu.pu.Activity.Sign.SignActivity;
import tw.edu.pu.R;
import tw.edu.pu.RequestModel.RequestHelper;

public class MainActivity extends AppCompatActivity {

    MaterialCardView btnCreate, btnSign, btnGroup, btnRace, btnEndClass, btnSignOut;
    RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();
        initButton();
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
    }
}