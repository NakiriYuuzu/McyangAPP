package tw.edu.pu.Activity.Race;

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

public class RaceSecondActivity extends AppCompatActivity {

    ShapeableImageView btnBack;
    MaterialTextView btn_Open, btn_Close;
    MaterialButton btnNext, btnEnd;
    RecyclerView recyclerView;

    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_second);

        initView();
        initButton();
    }

    private void initButton() {
        btn_Open.setOnClickListener(v -> beaconController.start_BroadcastBeacon());

        btn_Close.setOnClickListener(v -> beaconController.stop_BroadcastBeacon());

        btnNext.setOnClickListener(v -> {
            Intent ii = new Intent(this, RaceActivity.class);
            startActivity(ii);
        });

        btnEnd.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        beaconController = new BeaconController(this);
        beaconController.init_BroadcastBeacon();

        btnBack = findViewById(R.id.raceSecond_btn_Back);
        btn_Open = findViewById(R.id.raceSecond_btn_BeaconOn);
        btn_Close = findViewById(R.id.raceSecond_btn_BeaconOff);
        btnNext = findViewById(R.id.raceSecond_btn_Next);
        btnEnd = findViewById(R.id.raceSecond_btn_Enter);
        recyclerView = findViewById(R.id.raceSecond_recycleView);
    }
}