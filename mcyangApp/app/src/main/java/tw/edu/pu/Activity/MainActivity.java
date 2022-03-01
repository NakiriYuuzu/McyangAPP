package tw.edu.pu.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import tw.edu.pu.Activity.Answer.AnswerActivity;
import tw.edu.pu.Activity.Group.GroupActivity;
import tw.edu.pu.Activity.Race.RaceActivity;
import tw.edu.pu.Activity.Sign.SignActivity;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.Helper.CustomViewHelper;
import tw.edu.pu.R;
import tw.edu.pu.Helper.RequestHelper;
import tw.edu.pu.StoredData.ShareData;

public class MainActivity extends AppCompatActivity {

    boolean isScanning = false;
    boolean isAfterLogin = false;

    MaterialCardView btnCreate, btnSign, btnGroup, btnRace, btnAnswer, btnEndClass, btnSignOut, btnReceive;
    MaterialTextView tv_TeacherNames, tv_beaconBTN;
    ConstraintLayout constraintLayout;

    List<String> studentRequest;

    VolleyApi volleyApi;
    ShareData shareData;
    RequestHelper requestHelper;
    CustomViewHelper customViewHelper;
    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();

        tv_TeacherNames.setText(shareData.getUserNames());
        btnGroup.setEnabled(false);
        if (getIntent().getBooleanExtra("check", isAfterLogin))
            checkStatus();

        initButton();
    }

    private void checkStatus() {
        if (shareData.getMajor() != null)
            customViewHelper.showAlertBuilder("結束課程", "偵測到上個課程未結束，請問是否要結束它？", new CustomViewHelper.AlertListener() {
                @Override
                public void onPositive(DialogInterface dialogInterface, int i) {
                    shareData.cleanData();
                    beforeSign();
                    dialogInterface.dismiss();
                }

                @Override
                public void onNegative(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        else
            beforeSign();
    }

    private boolean is_Existed(String id2) {
        boolean isExist = true;
        if (studentRequest.size() > 0)
            for (int i = 0; i < studentRequest.size(); i++)
                if (id2.equals(studentRequest.get(i))) {
                    new Handler().postDelayed(() -> {
                        if (studentRequest.size() > 0)
                            studentRequest.remove(0);
                    }, 10000);
                    isExist = false;
                    break;
                }

        return isExist;
    }

    private void beforeSign() {
        btnSign.setEnabled(true);
        btnSign.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
        btnEndClass.setEnabled(false);
        btnEndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        btnRace.setEnabled(false);
        btnRace.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        btnAnswer.setEnabled(false);
        btnAnswer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
    }

    private void afterSign() {
        if (shareData.getMajor() != null) {
            btnSign.setEnabled(false);
            btnSign.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
            btnEndClass.setEnabled(true);
            btnEndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            btnRace.setEnabled(true);
            btnRace.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            btnAnswer.setEnabled(true);
            btnAnswer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
        }
    }

    private void startScanning() {
        beaconController.startScanning((beacons, region) -> {
            Log.e("startScanning: ", beacons.size() + "");
            if (beacons.size() > 0) {
                String major = beacons.iterator().next().getId2().toString();
                if (is_Existed(major)) {
                    studentRequest.add(major);
                    toastStudent(major);
                }
            }
        });
    }

    private void toastStudent(String major) {
        volleyApi.getApi(DefaultSetting.URL_STUDENT + major, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject;
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);
                    jsonObject = new JSONObject(result);
                    String studentNames = jsonObject.getString("S_Name");
                    customViewHelper.showSnackBar(constraintLayout, studentNames + "同學：提問中...");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                customViewHelper.showSnackBar(constraintLayout, "同學提問中，無法查詢該同學的名稱！");
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

        btnAnswer.setOnClickListener(v -> {
            Intent ii = new Intent(this, AnswerActivity.class);
            startActivity(ii);
        });

        btnRace.setOnClickListener(v -> {
            Intent ii = new Intent(this, RaceActivity.class);
            startActivity(ii);
        });


        btnEndClass.setOnClickListener(v -> customViewHelper.showAlertBuilder("結束課程", "是否要結束課程？", new CustomViewHelper.AlertListener() {
            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                shareData.cleanData();
                beforeSign();
                dialogInterface.dismiss();
            }

            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }));

        btnReceive.setOnClickListener(v -> {
            if (isScanning) {
                isScanning = false;
                beaconController.stopScanning();
                tv_beaconBTN.setText(R.string.main_btn_beaconBtn_off);
                btnReceive.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            } else {
                isScanning = true;
                startScanning();
                tv_beaconBTN.setText(R.string.main_btn_beaconBtn_on);
                btnReceive.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            }
        });

        // FIXME: Add when sign out turn off auto login
        btnSignOut.setOnClickListener(v -> finish());
    }

    private void initView() {
        constraintLayout = findViewById(R.id.main_fragment_container);
        btnCreate = findViewById(R.id.main_btn_Create);
        btnSign = findViewById(R.id.main_btn_Sign);
        btnGroup = findViewById(R.id.main_btn_Group);
        btnRace = findViewById(R.id.main_btn_OpenQA);
        btnAnswer = findViewById(R.id.main_btn_Answer);
        btnEndClass = findViewById(R.id.main_btn_EndClass);
        btnSignOut = findViewById(R.id.main_btn_SignOut);
        btnReceive = findViewById(R.id.main_btn_beaconBtn);
        tv_TeacherNames = findViewById(R.id.main_Users);
        tv_beaconBTN = findViewById(R.id.main_tv_beacon);

        studentRequest = new ArrayList<>();

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
        requestHelper = new RequestHelper(this);
        customViewHelper = new CustomViewHelper(this);
        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_MAIN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        afterSign();
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