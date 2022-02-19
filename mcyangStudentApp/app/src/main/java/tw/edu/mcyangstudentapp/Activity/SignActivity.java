package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SignModel;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.SignAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.SignViewModel;

public class SignActivity extends AppCompatActivity {

    private static final String TAG = "SignActivity: ";

    ArrayList<SignModel> signModels;
    ArrayList<String> sign_ID;

    ShapeableImageView btnBack;
    MaterialTextView tvNoData;
    RecyclerView recyclerView;

    BeaconController beaconController;
    ShareData shareData;
    SignAdapter signAdapter;
    SignViewModel signViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
        initButton();
        startScanning();
        initRecycleView();
    }

    private void syncViewModel() {
        signViewModel = new ViewModelProvider(this).get(SignViewModel.class);
        signViewModel.getSignListObserver().observe(this, signModels -> {
            if (signModels != null)
                signAdapter.updateSignAdapter(signModels, sign_ID);
        });
    }

    private boolean isBeaconExisted(String major) {
        boolean isExist = true;
        if (signModels.size() > 0)
            for (int i = 0; i < signModels.size(); i++)
                if (signModels.get(i).getMajor().equals(major)) {
                    isExist = false;
                    break;
                }

        return isExist;
    }

    private void startScanning() {
        beaconController.startScanning((beacons, region) -> {
            Log.e(TAG, "startScanning: " + beacons.size());
            if (beacons.size() > 0) {
                for (Beacon beacon : beacons)
                    if (isBeaconExisted(beacon.getId2().toString())) {
                        signModels.add(new SignModel(beacon.getId2().toString(), beacon.getId3().toString()));
                        sign_ID.add(beacon.getId3().toString());
                    }

                syncViewModel();
                signViewModel.setSignList(signModels);
                //shareData.sign_saveData(signModels);
                tvNoData.setVisibility(View.GONE);
            }
        });
    }

    private void initRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        signAdapter = new SignAdapter(this, signModels, sign_ID);
        recyclerView.setAdapter(signAdapter);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> {
            finish();
            beaconController.stopScanning();
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.sign_btn_back);
        recyclerView = findViewById(R.id.sign_recycleView);
        tvNoData = findViewById(R.id.sign_textView_NoFound);

        signModels = new ArrayList<>();
        sign_ID = new ArrayList<>();

        shareData = new ShareData(this);

        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_SIGN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconController.stopScanning();
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