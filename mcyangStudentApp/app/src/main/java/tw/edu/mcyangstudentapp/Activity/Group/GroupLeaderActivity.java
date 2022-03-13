package tw.edu.mcyangstudentapp.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.altbeacon.beacon.Beacon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.Activity.MainActivity;
import tw.edu.mcyangstudentapp.ActivityModel.LeaderModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.RepeatHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.LeaderAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.LeaderViewModel;

public class GroupLeaderActivity extends AppCompatActivity {

    private static final String TAG = "GroupLeaderActivity: ";

    ArrayList<LeaderModel> leaderModels;

    MaterialTextView tvNotFound;
    ShapeableImageView btnBack;
    MaterialButton btnEnter;
    RecyclerView recyclerView;

    BeaconController beaconController;
    LeaderViewModel leaderViewModel;
    LeaderAdapter leaderAdapter;
    RepeatHelper repeatHelper;
    ShareData shareData;
    VolleyApi volleyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_leader);

        initView();
        initButton();
        initRecyclerView();
        startScanning();
    }

    private synchronized void syncViewModel() {
        leaderViewModel = new ViewModelProvider(this).get(LeaderViewModel.class);
        leaderViewModel.getLeaderObserver().observe(this, leaderList -> {
            if (leaderModels != null)
                leaderAdapter.updateLeaderAdapter(leaderModels);
        });

        leaderViewModel.setLeaderList(leaderModels);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderAdapter = new LeaderAdapter(this, leaderModels);
        recyclerView.setAdapter(leaderAdapter);
    }

    private void startScanning() {
        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                for (Beacon beacon : beacons) {
                    String id = beacon.getId2().toString();

                    if (id.equals(shareData.getMajor())) {
                        beaconController.stopScanning();
                        shareData.saveGroup_ID(beacon.getId3().toString());
                        sendData();
                    }
                }
            }
        });
    }

    private void sendData() {
        volleyApi.api(Request.Method.POST, DefaultSetting.URL_GROUP_TEAM_ID, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                repeatHelper.start(1000);
                tvNotFound.setText("已發送，等待老師審核。");
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e("GroupLeaderActivity: ", "volleyError!");
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            params.put("Group_number", "1");

            if (shareData.getGroup_ID() != null)
                params.put("Team_desc", shareData.getGroup_ID());

            params.put("Leader_id", shareData.getStudentID());
            return params;
        });
    }

    private synchronized void syncData() {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM + shareData.getGroup_ID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String team_id = jsonObject.getString("Team_id");
                        String leader_id = jsonObject.getString("Leader_id");
                        getStudentData(team_id, leader_id);
                    }

                } catch (JSONException e) {
                    Log.e("GroupLeaderActivity: ", "syncData: JSONError!");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e("GroupLeaderActivity: ", "syncData: volleyError!");
            }
        });
    }

    private void getStudentData(String team_id, String leader_id) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_STUDENT + leader_id, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {

                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String names = jsonObject.getString("S_Name");

                    if (isExisted(names))
                        leaderModels.add(new LeaderModel(team_id, names));

                    notFound();
                    syncViewModel();

                } catch (JSONException e) {
                    Log.e(TAG, "getStudentData: JSONError!");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e("GroupLeaderActivity: ", "getStudentData: volleyError!");
            }
        });
    }

    private void notFound() {
        if (leaderModels.size() > 0)
            tvNotFound.setVisibility(View.GONE);
        else
            tvNotFound.setVisibility(View.VISIBLE);
    }

    private boolean isExisted(String leaderName) {
        boolean isExist = true;
        if (leaderModels.size() > 0)
            for (LeaderModel leaderModel : leaderModels)
                if (leaderModel.getLeaderName().equals(leaderName)) {
                    isExist = false;
                    break;
                }
        return isExist;
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());

        btnEnter.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.groupLeader_btn_Back);
        btnEnter = findViewById(R.id.groupLeader_btn_Enter);
        tvNotFound = findViewById(R.id.groupLeader_textView_NoFound);
        recyclerView = findViewById(R.id.groupLeader_recycleView);

        tvNotFound.setText("等待老師廣播后自動發送組長請求。");

        leaderModels = new ArrayList<>();

        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_GROUP);
        repeatHelper = new RepeatHelper(this::syncData);
        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stopScanning();
        repeatHelper.stop();
    }
}