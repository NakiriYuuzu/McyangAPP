package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.altbeacon.beacon.Beacon;

import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.R;

public class RaceActivity extends AppCompatActivity {

    private static final String TAG = "RaceActivity: ";
    boolean firstChecked = false;
    boolean checked = false;
    String raceID;

    MaterialCardView btnStart;
    MaterialTextView tvHint;
    ShapeableImageView btnBack, imgBtn;
    MaterialButton btnNext, btnEnd;

    VolleyApi volleyApi;
    BeaconController beaconController;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        initView();
        initButton();
    }

    private void initButton() {
        btnStart.setOnClickListener(v -> {
            volleyApi.postApi(DefaultSetting.URL_RACE_LIST, new VolleyApi.VolleyGet() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onFailed(VolleyError error) {

                }
            }, (VolleyApi.VolleyPost) () -> {
                Map<String, String> params = new HashMap<>();
                params.put("", "");
                return params;
            });
        });

        btnNext.setOnClickListener(v -> finish());

        btnEnd.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        imgBtn = findViewById(R.id.race_img_Btn);
        tvHint = findViewById(R.id.race_tv_Btn);
        btnStart = findViewById(R.id.race_btn_Race);
        btnNext = findViewById(R.id.race_btn_Next);
        btnBack = findViewById(R.id.race_btn_Back);
        btnEnd = findViewById(R.id.race_btn_End);

        volleyApi = new VolleyApi(this);

        beaconController = new BeaconController(this);
        beaconController.beaconInit();
        beaconScanning();
    }

    private void beaconScanning() {
        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                for (Beacon beacon : beacons)
                    raceID = beacon.getId3().toString();

                if (!firstChecked) {
                    firstChecked = true;
                    switchAndNotify();
                }
            }
        });
    }

    private void switchAndNotify() {
        handler.postDelayed(runnable = () -> {
            btnStart.setBackgroundResource(R.drawable.button_yellow);
            tvHint.setText("");

            handler.postDelayed(runnable, 10000);
        }, 10000);
    }

}