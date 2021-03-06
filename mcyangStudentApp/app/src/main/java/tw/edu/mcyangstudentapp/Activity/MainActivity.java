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
import com.google.firebase.messaging.FirebaseMessaging;

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
    boolean func_Sign, func_Qa, func_Question, func_EndClass, func_Answer;

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
        Log.e("onCreate: ", shareData.getStudentNames());

        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();

        checkLogin();
        initButton();
        initFirebase();
    }

    private void initFirebase() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> shareData.saveToken(s));
    }

    @SuppressLint("SetTextI18n")
    private void checkLogin() {
        String studentID = getIntent().getStringExtra("studentID");
        isAfterLogin = getIntent().getBooleanExtra("check", isAfterLogin);
        tvNames.setText(shareData.getStudentNames() + "??????");

        if (studentID == null)
            return;

        if (studentID.equals(shareData.getStudentID())) {
            if (isAfterLogin) {
                checkStatus();
            }
        } else {
            shareData.cleanData();
            shareData.saveStudentID(studentID);
            beforeSign();
        }

        Log.e("SID: ", shareData.getStudentID() + " | " + studentID + " | " + shareData.getStudentNames() + " | CourseID: " + shareData.getMajor());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkStatus() {
        if (shareData.getMajor() != null)
            viewHelper.showAlertBuilder("????????????", "???????????????????????????????????????????????????????????????", "??????", "??????", new CustomViewHelper.AlertListener() {
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
        tvNames.setText(shareData.getStudentNames());
        btn_Group.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        func_Sign = true;
        btn_Sign.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        func_Qa = false;
        btn_Qa.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        func_Answer = false;
        btn_Answer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        func_Question = false;
        btn_Question.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        func_EndClass = false;
        btn_EndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
    }

    private void afterSign() {
        tvNames.setText(shareData.getStudentNames());
        if (shareData.getMajor() != null) {
            func_Sign = false;
            btn_Sign.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grey));
            func_Qa = true;
            btn_Qa.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            func_Answer = true;
            btn_Answer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            func_Question = true;
            btn_Question.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            func_EndClass = true;
            btn_EndClass.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            btn_Group.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initButton() {
        btn_Sign.setOnClickListener(v -> {
            if (func_Sign) {
                if (requestHelper.checkAll_Enabled()) {
                    Intent ii = new Intent(MainActivity.this, SignActivity.class);
                    startActivity(ii);
                }
            } else {
                Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        btn_Qa.setOnClickListener(v -> {
            if (func_Qa) {
                if (requestHelper.checkAll_Enabled()) {
                    Intent ii = new Intent(getApplicationContext(), RaceActivity.class);
                    startActivity(ii);
                }
            } else {
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            }

        });

        btn_Question.setOnClickListener(v -> {
            if (func_Question) {
                if (requestHelper.checkAll_Enabled()) {
                    viewHelper.showAlertBuilder("????????????", "???????????????????????????????", "??????", "??????", new CustomViewHelper.AlertListener() {
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
                                Toast.makeText(getApplicationContext(), "30??????????????????", Toast.LENGTH_SHORT).show();

                            dialogInterface.dismiss();
                        }

                        @Override
                        public void onNegative(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }

            } else
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        });

        btn_Answer.setOnClickListener(v -> {
            if (func_Answer) {
                if (requestHelper.checkAll_Enabled()) {
                    Intent ii = new Intent(getApplicationContext(), AnswerActivity.class);
                    startActivity(ii);
                }
            } else {
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        btn_EndClass.setOnClickListener(v -> {
            if (func_EndClass) {
                viewHelper.showAlertBuilder("????????????", "????????????????????????", "??????", "??????", new CustomViewHelper.AlertListener() {
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
            } else {
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        btn_Group.setOnClickListener(v -> {
            if (requestHelper.checkAll_Enabled()) {
                Intent ii = new Intent(getApplicationContext(), GroupActivity.class);
                startActivity(ii);
            }
        });

        btn_LearningRecord.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), LearningRecordActivity.class);
            startActivity(ii);
        });

        btn_SignOut.setOnClickListener(v -> {
            shareData.saveLoginAccount("");
            shareData.saveLoginPassword("");
            finish();
        });
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
    }

    @Override
    protected void onStop() {
        super.onStop();
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