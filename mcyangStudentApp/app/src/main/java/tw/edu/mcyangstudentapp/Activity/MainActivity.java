package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import tw.edu.mcyangstudentapp.Activity.Answer.AnswerActivity;
import tw.edu.mcyangstudentapp.Activity.Group.GroupActivity;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.Helper.RequestHelper;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class MainActivity extends AppCompatActivity {
    boolean beaconChecked = false;

    MaterialCardView btn_Sign, btn_SignOut, btn_Qa, btn_Question, btn_Group, btn_EndClass, btn_Answer, btn_LearningRecord;

    ShareData shareData;
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

        btn_Group.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), GroupActivity.class);
            startActivity(ii);
        });

        btn_Question.setOnClickListener(v -> {
            if (!beaconChecked) {
                beaconChecked = true;
                beaconController.start_BroadcastBeacon();
                Log.e("initButton: ", "01");

                new Handler().postDelayed(() -> {
                    beaconController.stop_BroadcastBeacon();
                    Log.e("initButton: ", "02");

                    new Handler().postDelayed(() -> {
                        beaconChecked = false;
                        Log.e("initButton: ", "03");
                    }, 30000);

                }, 5000);
            } else
                Toast.makeText(this, "30秒後在嘗試。", Toast.LENGTH_SHORT).show();
        });

        btn_Answer.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), AnswerActivity.class);
            startActivity(ii);
        });

        btn_LearningRecord.setOnClickListener(v -> {
            Intent ii = new Intent(getApplicationContext(), LearningRecordActivity.class);
            startActivity(ii);
        });

        btn_EndClass.setOnClickListener(v -> shareData.cleanData());

        btn_SignOut.setOnClickListener(v -> finish());
    }

    private void initView() {
        requestHelper = new RequestHelper(this);
        requestHelper.requestGPSPermission();
        requestHelper.checkGPS_Enabled();
        requestHelper.requestBluetooth();

        shareData = new ShareData(this);

        beaconController = new BeaconController(this);
        beaconController.init_BroadcastBeacon();

        btn_Sign = findViewById(R.id.main_btn_Sign);
        btn_Group = findViewById(R.id.main_btn_Group);
        btn_Qa = findViewById(R.id.main_btn_Qa);
        btn_Question = findViewById(R.id.main_btn_Question);
        btn_SignOut = findViewById(R.id.main_btn_SignOut);
        btn_EndClass = findViewById(R.id.main_btn_EndClass);
        btn_Answer = findViewById(R.id.main_btn_Answer);
        btn_LearningRecord = findViewById(R.id.main_btn_LearningRecord);
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