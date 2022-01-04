package tw.edu.pu.Activity.Sign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import tw.edu.pu.Activity.MainActivity;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.R;

public class Sign_Second_Activity extends AppCompatActivity {

    ShapeableImageView btnBack;
    MaterialTextView btnOpen, btnClose;
    MaterialButton btnEnter;
    RecyclerView recyclerView;

    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_second);

        initView();
        initButton();
    }

    private void initButton() {
        btnOpen.setOnClickListener(v -> beaconController.start_BroadcastBeacon());

        btnClose.setOnClickListener(v -> beaconController.stop_BroadcastBeacon());

        btnEnter.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        //FixME Beacon init need major and minor
        beaconController = new BeaconController(this);
        beaconController.init_BroadcastBeacon();

        btnBack = findViewById(R.id.signSecond_btn_Back);
        btnOpen = findViewById(R.id.signSecond_btn_BeaconOn);
        btnClose = findViewById(R.id.signSecond_btn_BeaconOff);
        btnEnter = findViewById(R.id.signSecond_btn_Enter);
        recyclerView = findViewById(R.id.signSecond_recycleView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconController.stop_BroadcastBeacon();
    }

    @Override
    protected void onStop() {
        super.onStop();
        beaconController.stop_BroadcastBeacon();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stop_BroadcastBeacon();
    }
}