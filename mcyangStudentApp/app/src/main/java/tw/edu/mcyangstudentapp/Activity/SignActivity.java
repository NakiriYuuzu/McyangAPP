package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.imageview.ShapeableImageView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collections;

import tw.edu.mcyangstudentapp.ActivityModel.SignModel;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.SignAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.SignViewModel;

public class SignActivity extends AppCompatActivity implements LifecycleOwner {

    private static final String TAG = "SignActivity: ";

    ArrayList<SignModel> signModels;

    ShapeableImageView btnBack;
    RecyclerView recyclerView;

    BeaconController beaconController;
    SignViewModel viewModel;
    ShareData shareData;
    SignAdapter signAdapter;
    SignModel signModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        initButton();
        startScanning();
        initRecycleView();
    }

    Observer<ArrayList<SignModel>> signListUpdateObserver = signModels -> signAdapter.updateSignList(signModels);

//    private void initViewModel() {
//        viewModel = new ViewModelProvider(this).get(SignViewModel.class);
//        viewModel.getUserMutableLiveData().observe(this, signListUpdateObserver);
//    }

    private void startScanning() {
        Log.e(TAG, "startScanning: ");

        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                for (Beacon beaconList : beacons) {
                    ArrayList<Beacon> list = new ArrayList<>();
                    ArrayList<SignModel> signList = new ArrayList<>();

                    list.add(beaconList);
                    if (list.size() > 0) {
                        Collections.sort(list, (o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance()));

                        for (Beacon b : list) {
                            signModel.setMinor(b.getId2().toString());
                            signModel.setMajor(b.getId3().toString());
                            signList.add(new SignModel(b.getId2().toString(), b.getId3().toString()));
                        }

                        shareData.sign_saveData(signList);
                    }
                    //initViewModel();
                }
            }
        });
    }

    private void initRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        signAdapter = new SignAdapter(this, signModels);
        recyclerView.setAdapter(signAdapter);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.sign_btn_back);
        recyclerView = findViewById(R.id.sign_recycleView);

        signModel = new SignModel();

        shareData = new ShareData(this);
        signModels = shareData.sign_getData();

        beaconController = new BeaconController(this);
        beaconController.beaconInit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stopScanning();
    }
}