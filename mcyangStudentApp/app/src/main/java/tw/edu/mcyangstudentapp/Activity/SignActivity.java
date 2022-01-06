package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SignModel;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.SignAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.SignViewModel;

public class SignActivity extends AppCompatActivity {

    private static final String TAG = "SignActivity: ";
    private boolean checkedData = false;

    ArrayList<SignModel> globalSignList;

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
        initViewModel();
        initRecycleView();
    }

    private void initViewModel() {
        signViewModel = new ViewModelProvider(this).get(SignViewModel.class);
        signViewModel.getSignListObserver().observe(this, signModels -> {
            if (signModels != null)
                signAdapter.updateSignAdapter(signModels);
        });
    }

    private void startScanning() {
        Log.e(TAG, "startScanning: ");

        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                ArrayList<SignModel> signList = new ArrayList<>();

                for (Beacon beaconList : beacons)
                    signList.add(new SignModel(beaconList.getId2().toString(), beaconList.getId3().toString()));

                signViewModel.setSignList(signList);
                shareData.sign_saveData(signList);
                tvNoData.setVisibility(View.GONE);
                checkedData = true;

            } else {
                ArrayList<SignModel> signList = new ArrayList<>();
                if (checkedData) {
                    new Handler().postDelayed(() -> {
                        signViewModel.setSignList(signList);
                        Log.e(TAG, "Nothing Here");
                        tvNoData.setVisibility(View.VISIBLE);
                        checkedData = false;
                    }, 10000);
                } else {
                    signViewModel.setSignList(signList);
                    Log.e(TAG, "Nothing Here");
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        signAdapter = new SignAdapter(this, globalSignList);
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

        shareData = new ShareData(this);

        beaconController = new BeaconController(this);
        beaconController.beaconInit();
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