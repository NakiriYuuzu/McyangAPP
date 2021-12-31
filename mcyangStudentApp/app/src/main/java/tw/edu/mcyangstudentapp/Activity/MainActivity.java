package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;

import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RequestModel.RequestHelper;

public class MainActivity extends AppCompatActivity {

    MaterialCardView btn_Sign, btn_SignOut, btn_Qa, btn_Question, btn_Group, btn_EndClass;

    RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        initView();

        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();
    }

    private void initView() {
        requestHelper = new RequestHelper(this);

        btn_Sign = findViewById(R.id.main_btn_Sign);
        btn_Group = findViewById(R.id.main_btn_Group);
        btn_Qa = findViewById(R.id.main_btn_Qa);
        btn_Question = findViewById(R.id.main_btn_Question);
        btn_SignOut = findViewById(R.id.main_btn_SignOut);
        btn_EndClass = findViewById(R.id.main_btn_EndClass);
    }
}