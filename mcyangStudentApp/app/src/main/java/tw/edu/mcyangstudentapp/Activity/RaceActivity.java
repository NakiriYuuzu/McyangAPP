package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.R;

public class RaceActivity extends AppCompatActivity {

    private static final String TAG = "RaceActivity: ";


    MaterialCardView btnStart;
    MaterialTextView tvHint;
    ShapeableImageView btnBack, imgBtn;
    MaterialButton btnNext, btnEnd;

    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        initView();
        initButton();
    }

    private void initButton() {
        btnStart.setOnClickListener(v -> beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                // TODO: remember to add api connect and send to backend
                beaconController.stopScanning();
                btnStart.setEnabled(false);

                // FIXME: if success btn to success else ...
                imgBtn.setImageResource(R.drawable.button_yellow);

            } else {

            }
        }));

        btnNext.setOnClickListener(v -> finish());

        btnEnd.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        beaconController = new BeaconController(this);
        beaconController.beaconInit();

        imgBtn = findViewById(R.id.race_img_Btn);
        tvHint = findViewById(R.id.race_tv_Btn);
        btnStart = findViewById(R.id.race_btn_Race);
        btnNext = findViewById(R.id.race_btn_Next);
        btnBack = findViewById(R.id.race_btn_Back);
        btnEnd = findViewById(R.id.race_btn_End);
    }
}