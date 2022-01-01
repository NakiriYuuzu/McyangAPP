package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.SignAdapter;

public class SignActivity extends AppCompatActivity {

    ShapeableImageView btnBack;
    RecyclerView recyclerView;

    BeaconController beaconController;
    SignAdapter signAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        initButton();
        initRecycleView();
    }

    private void initRecycleView() {
        ArrayList<String> left = new ArrayList<>();
        left.add("選擇");
        left.add("選擇");
        ArrayList<String> right = new ArrayList<>();
        right.add("0001");
        right.add("0002");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        signAdapter = new SignAdapter(this, left, right);
        recyclerView.setAdapter(signAdapter);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.sign_btn_back);
        recyclerView = findViewById(R.id.sign_recycleView);

        beaconController = new BeaconController(this);

        beaconController.beaconInit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stopScanning();
    }
}