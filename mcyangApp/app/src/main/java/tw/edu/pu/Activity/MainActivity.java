package tw.edu.pu.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import tw.edu.pu.Activity.Answer.AnswerActivity;
import tw.edu.pu.Activity.Group.GroupActivity;
import tw.edu.pu.Activity.Race.RaceActivity;
import tw.edu.pu.Activity.Sign.SignActivity;
import tw.edu.pu.Activity.Sign.Sign_Second_Activity;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.Helper.CustomViewHelper;
import tw.edu.pu.Helper.RepeatHelper;
import tw.edu.pu.R;
import tw.edu.pu.Helper.RequestHelper;
import tw.edu.pu.StoredData.ShareData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    boolean onClicked = false;
    boolean isScanning = false;
    boolean isAfterLogin = false;

    boolean func_Race, func_Ans, func_Group, func_EndClass;

    MaterialCardView btnCreate, btnSign, btnGroup, btnRace, btnAnswer, btnEndClass, btnSignOut, btnReceive;
    MaterialTextView tv_TeacherNames, tv_beaconBTN;
    ConstraintLayout constraintLayout;

    List<String> studentRequest;

    VolleyApi volleyApi;
    ShareData shareData;
    RepeatHelper repeatHelper;
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
        checkLogin();
        initButton();
        initFirebase();
    }

    private void initFirebase() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> shareData.saveFirebaseToken(s));
        Log.e(TAG, "initFirebase: " + shareData.getFirebaseToken());
    }

    private void checkLogin() {
        String teacherID = getIntent().getStringExtra("teacherID");
        isAfterLogin = getIntent().getBooleanExtra("check", isAfterLogin);

        Log.e("SID: ", shareData.getID() + " | " + teacherID);

        if (teacherID == null) {
            return;
        }

        if (teacherID.equals(shareData.getID())) {
            if (isAfterLogin)
                checkStatus();
        } else {
            shareData.cleanData();
            shareData.saveID(teacherID);
            beforeSign();
        }
    }

    private void checkStatus() {
        if (shareData.getMajor() != null)
            customViewHelper.showAlertBuilder("結束課程", "偵測到上個課程未結束，請問是否要結束它？", new CustomViewHelper.AlertListener() {
                @Override
                public void onPositive(DialogInterface dialogInterface, int i) {
                    shareData.cleanData();
                    shareData.saveCourseID(null);
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
                    isExist = false;
                    break;
                }

        return isExist;
    }

    private void beforeSign() {
        btnSign.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
        func_Group = false;
        btnGroup.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        func_EndClass = false;
        btnEndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        func_Race = false;
        btnRace.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        func_Ans = false;
        btnAnswer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
    }

    private void afterSign() {
        if (shareData.getMajor() != null) {
            func_Group = true;
            btnGroup.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            func_EndClass = true;
            btnEndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            func_Race = true;
            btnRace.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            func_Ans = true;
            btnAnswer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
        }
    }

    private void startScanning() {
        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                String major = beacons.iterator().next().getId2().toString();
                Log.e("beacon", beacons.size() + "");
                if (is_Existed(major))
                    studentRequest.add(major);
            }
        });
    }

    private void toastStudent() {
        Log.e("ToastStudent: ", studentRequest.toString());
        if (studentRequest.size() > 0) {
            onClicked = true;
            volleyApi.getApi(DefaultSetting.URL_STUDENT + studentRequest.get(0), new VolleyApi.VolleyGet() {
                @Override
                public void onSuccess(String result) {
                    JSONObject jsonObject;
                    try {
                        byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                        result = new String(text);
                        jsonObject = new JSONObject(result);
                        String studentNames = jsonObject.getString("S_Name");
                        customViewHelper.showSnackBar(constraintLayout, studentNames + "同學：提問中...", (view, snackbar) -> {
                            if (studentRequest.size() > 0)
                                studentRequest.remove(0);
                            onClicked = false;
                            snackbar.dismiss();
                        });

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
    }

    private void initButton() {
        btnCreate.setOnClickListener(v -> {
            Intent ii = new Intent(this, CreateActivity.class);
            startActivity(ii);
        });


        btnSign.setOnClickListener(v -> {
            if (requestHelper.checkAll_Enabled()) {
                Intent ii = new Intent(this, SignActivity.class);
                startActivity(ii);
            }
        });

        btnGroup.setOnClickListener(v -> {
            if (func_Group) {
                if (requestHelper.checkAll_Enabled()) {
                    if (shareData.getCourseID() != null) {
                        customViewHelper.showAlertBuilder("繼續點名", "請問需要補點名嗎？", new CustomViewHelper.AlertListener() {
                            @Override
                            public void onPositive(DialogInterface dialogInterface, int i) {
                                shareData.saveMajor(shareData.getCourseID());
                                Intent ii = new Intent(getApplicationContext(), Sign_Second_Activity.class);
                                ii.putExtra("isResume", true);
                                startActivity(ii);
                                dialogInterface.dismiss();
                            }

                            @Override
                            public void onNegative(DialogInterface dialogInterface, int i) {
                                Intent ii = new Intent(getApplicationContext(), GroupActivity.class);
                                startActivity(ii);
                                dialogInterface.dismiss();
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(this, "您還未開啟點名，請先點名後在使用此功能。", Toast.LENGTH_SHORT).show();
            }
        });

        btnAnswer.setOnClickListener(v -> {
            if (func_Ans) {
                if (requestHelper.checkAll_Enabled()) {
                    if (shareData.getCourseID() != null) {
                        customViewHelper.showAlertBuilder("繼續點名", "請問需要補點名嗎？", new CustomViewHelper.AlertListener() {
                            @Override
                            public void onPositive(DialogInterface dialogInterface, int i) {
                                shareData.saveMajor(shareData.getCourseID());
                                Intent ii = new Intent(getApplicationContext(), Sign_Second_Activity.class);
                                ii.putExtra("isResume", true);
                                startActivity(ii);
                                dialogInterface.dismiss();
                            }

                            @Override
                            public void onNegative(DialogInterface dialogInterface, int i) {
                                Intent ii = new Intent(getApplicationContext(), AnswerActivity.class);
                                startActivity(ii);
                                dialogInterface.dismiss();
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(this, "您還未開啟點名，請先點名後在使用此功能。", Toast.LENGTH_SHORT).show();
            }
        });

        btnRace.setOnClickListener(v -> {
            if (func_Race) {
                if (requestHelper.checkAll_Enabled()) {
                    if (shareData.getCourseID() != null) {
                        customViewHelper.showAlertBuilder("繼續點名", "請問需要補點名嗎？", new CustomViewHelper.AlertListener() {
                            @Override
                            public void onPositive(DialogInterface dialogInterface, int i) {
                                shareData.saveMajor(shareData.getCourseID());
                                Intent ii = new Intent(getApplicationContext(), Sign_Second_Activity.class);
                                ii.putExtra("isResume", true);
                                startActivity(ii);
                                dialogInterface.dismiss();
                            }

                            @Override
                            public void onNegative(DialogInterface dialogInterface, int i) {
                                Intent ii = new Intent(getApplicationContext(), RaceActivity.class);
                                startActivity(ii);
                                dialogInterface.dismiss();
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(this, "您還未開啟點名，請先點名後在使用此功能。", Toast.LENGTH_SHORT).show();
            }
        });


        btnEndClass.setOnClickListener(v -> {
            if (func_EndClass) {
                customViewHelper.showAlertBuilder("結束課程", "是否要結束課程？", new CustomViewHelper.AlertListener() {
                    @Override
                    public void onPositive(DialogInterface dialogInterface, int i) {
                        shareData.cleanData();
                        shareData.saveCourseID(null);
                        beforeSign();
                        dialogInterface.dismiss();
                    }

                    @Override
                    public void onNegative(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, "您還未開啟點名，請先點名後在使用此功能。", Toast.LENGTH_SHORT).show();
            }
        });

        btnReceive.setOnClickListener(v -> {
            if (requestHelper.checkAll_Enabled()) {
                if (isScanning) {
                    isScanning = false;
                    beaconController.stopScanning();
                    repeatHelper.stop();
                    tv_beaconBTN.setText(R.string.main_btn_beaconBtn_off);
                    btnReceive.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                } else {
                    isScanning = true;
                    startScanning();
                    repeatHelper.start(2000);
                    tv_beaconBTN.setText(R.string.main_btn_beaconBtn_on);
                    btnReceive.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
                }
            }
        });

        // FIXME: Add when sign out turn off auto login
        btnSignOut.setOnClickListener(v -> {
            shareData.saveAccount("");
            shareData.savePassword("");
            finish();
        });
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

        repeatHelper = new RepeatHelper(() -> {
            if (!onClicked)
                toastStudent();
        });

        requestHelper = new RequestHelper(this);
        customViewHelper = new CustomViewHelper(this);
        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_MAIN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        afterSign();
        tv_TeacherNames.setText(shareData.getUserNames());
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