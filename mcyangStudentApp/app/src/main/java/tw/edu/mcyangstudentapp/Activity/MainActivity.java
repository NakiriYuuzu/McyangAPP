package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import tw.edu.mcyangstudentapp.Activity.Answer.AnswerActivity;
import tw.edu.mcyangstudentapp.Activity.Group.GroupActivity;
import tw.edu.mcyangstudentapp.Activity.LearningRecord.LearningRecordActivity;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.Helper.CustomViewHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.Helper.RequestHelper;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class MainActivity extends AppCompatActivity {
    boolean beaconChecked = false;
    boolean isAfterLogin = false;

    MaterialCardView btn_Sign, btn_SignOut, btn_Qa, btn_Question, btn_Group, btn_EndClass, btn_Answer, btn_LearningRecord;
    MaterialTextView tvNames;

    ShareData shareData;
    RequestHelper requestHelper;
    BeaconController beaconController;
    CustomViewHelper viewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();

        btn_Group.setEnabled(false);

        checkLogin();
        initButton();
    }

    private void checkLogin() {
        String studentID = getIntent().getStringExtra("studentID");
        isAfterLogin = getIntent().getBooleanExtra("check", isAfterLogin);

        if (studentID == null)
            return;

        if (studentID.equals(shareData.getStudentID())) {
            if (isAfterLogin) {
                checkStatus();
                tvNames.setText(shareData.getStudentNames());
            }
        } else {
            shareData.cleanData();
            shareData.saveStudentID(studentID);
            tvNames.setText(shareData.getStudentNames());
            beforeSign();
        }

        Log.e("SID: ", shareData.getStudentID() + " | " + studentID + " | " + shareData.getStudentNames());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkStatus() {
        if (shareData.getMajor() != null)
            viewHelper.showAlertBuilder("結束課程", "偵測到上個課程未結束，請問要是否要結束它？", "確認", "取消", new CustomViewHelper.AlertListener() {
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

    private void beforeSign() {
        btn_Sign.setEnabled(true);
        btn_Sign.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        btn_Qa.setEnabled(false);
        btn_Qa.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        btn_Answer.setEnabled(false);
        btn_Answer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        btn_Question.setEnabled(false);
        btn_Question.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        btn_EndClass.setEnabled(false);
        btn_EndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
    }

    private void afterSign() {
        if (shareData.getMajor() != null) {
            btn_Sign.setEnabled(false);
            btn_Sign.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
            btn_Qa.setEnabled(true);
            btn_Qa.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            btn_Answer.setEnabled(true);
            btn_Answer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            btn_Question.setEnabled(true);
            btn_Question.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            btn_EndClass.setEnabled(true);
            btn_EndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initButton() {
        btn_Sign.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), SignActivity.class);
            startActivity(ii);
        });

        btn_Qa.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), RaceActivity.class);
            startActivity(ii);
        });

        btn_Group.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), GroupActivity.class);
            startActivity(ii);
        });

        btn_Question.setOnClickListener(v ->
                viewHelper.showAlertBuilder("我要提問", "是否要向老師提出問題?", "確認", "取消", new CustomViewHelper.AlertListener() {
                    @Override
                    public void onPositive(DialogInterface dialogInterface, int i) {
                        if (!beaconChecked) {
                            beaconChecked = true;
                            beaconController.init_BroadcastBeacon();
                            beaconController.start_BroadcastBeacon();
                            Log.e("initButton: ", "01");

                            new Handler().postDelayed(() -> {
                                beaconController.stop_BroadcastBeacon();
                                Log.e("initButton: ", "02");

                                new Handler().postDelayed(() -> {
                                    beaconChecked = false;
                                    Log.e("initButton: ", "03");
                                }, 30000);

                            }, 10000);
                        } else
                            Toast.makeText(getApplicationContext(), "30秒後在嘗試。", Toast.LENGTH_SHORT).show();

                        dialogInterface.dismiss();
                    }

                    @Override
                    public void onNegative(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }));

        btn_Answer.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), AnswerActivity.class);
            startActivity(ii);
        });

        btn_LearningRecord.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), LearningRecordActivity.class);
            startActivity(ii);
        });

        btn_EndClass.setOnClickListener(v ->
                viewHelper.showAlertBuilder("結束課程", "是否要結束課程？", "確認", "取消", new CustomViewHelper.AlertListener() {
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
                })
        );

        btn_SignOut.setOnClickListener(v -> finish());
    }

    private void initView() {
        requestHelper = new RequestHelper(this);
        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();

        shareData = new ShareData(this);
        viewHelper = new CustomViewHelper(this);

        beaconController = new BeaconController(this);

        btn_Sign = findViewById(R.id.main_btn_Sign);
        btn_Group = findViewById(R.id.main_btn_Group);
        btn_Qa = findViewById(R.id.main_btn_Qa);
        btn_Question = findViewById(R.id.main_btn_Question);
        btn_SignOut = findViewById(R.id.main_btn_SignOut);
        btn_EndClass = findViewById(R.id.main_btn_EndClass);
        btn_Answer = findViewById(R.id.main_btn_Answer);
        btn_LearningRecord = findViewById(R.id.main_btn_LearningRecord);
        tvNames = findViewById(R.id.main_tv_studentNames);
    }

    @Override
    protected void onResume() {
        super.onResume();
        afterSign();
        tvNames.setText(shareData.getStudentNames());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (beaconChecked)
            beaconController.stop_BroadcastBeacon();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconChecked)
            beaconController.stop_BroadcastBeacon();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beaconChecked)
            beaconController.stop_BroadcastBeacon();
    }
}