package tw.edu.pu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import tw.edu.pu.Activity.Group.GroupActivity;
import tw.edu.pu.Activity.Race.RaceActivity;
import tw.edu.pu.Activity.Sign.SignActivity;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.RequestModel.RequestHelper;
import tw.edu.pu.StoredData.ShareData;

public class MainActivity extends AppCompatActivity {

    MaterialCardView btnCreate, btnSign, btnGroup, btnRace, btnEndClass, btnSignOut;

    VolleyApi volleyApi;
    ShareData shareData;
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
                toastStudent(beacons.iterator().next().getId2().toString());
            }
        });
    }

    private void toastStudent(String major) {
        volleyApi.getApi(DefaultSetting.URL_STUDENT + major, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    String studentNames = jsonObject.getString("S_Name");
                    Toast.makeText(MainActivity.this, studentNames + "同學：提問中...", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(MainActivity.this, "同學提問中，無法查詢該同學的名稱！", Toast.LENGTH_SHORT).show();
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
            shareData.cleanData();
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

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
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