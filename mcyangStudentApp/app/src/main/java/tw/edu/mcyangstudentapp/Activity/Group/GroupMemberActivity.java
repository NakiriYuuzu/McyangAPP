package tw.edu.mcyangstudentapp.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

import tw.edu.mcyangstudentapp.ActivityModel.MemberModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.RepeatHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.MemberAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.MemberViewModel;

public class GroupMemberActivity extends AppCompatActivity {

    private static final String TAG = "GroupMemberActivity: ";

    ArrayList<MemberModel> memberModels;

    ShapeableImageView btnBack;
    MaterialTextView tvNotFound;
    MaterialButton btnEnter;
    RecyclerView recyclerView;

    BeaconController beaconController;
    MemberViewModel memberViewModel;
    MemberAdapter memberAdapter;
    RepeatHelper repeatHelper;
    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        initView();
        initButton();
        initRecyclerView();
        startScanning();
    }

    private void startScanning() {
        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                for (Beacon beacon : beacons) {
                    String id = beacon.getId2().toString();

                    if (id.equals(shareData.getMajor())) {
                        beaconController.stopScanning();
                        shareData.saveGroup_ID(beacon.getId3().toString());
                        repeatHelper.start(1000);
                    }
                }
            }
        });
    }

    private boolean isExisted(String leaderName) {
        boolean isExist = true;
        if (memberModels.size() > 0)
            for (MemberModel memberModel : memberModels)
                if (memberModel.getTeamLeaderName().equals(leaderName)) {
                    isExist = false;
                    break;
                }
        return isExist;
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
                        String teamDesc = jsonObject.getString("Team_desc");
                        getStudentData(team_id, leader_id, teamDesc);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "syncData: JSONError!");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "SyncData: volleyError!");
            }
        });
    }

    private synchronized void getStudentData(String team_id, String leader_id, String teamDesc) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_STUDENT + leader_id, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String names = jsonObject.getString("S_Name");

                    if (isExisted(names))
                        memberModels.add(new MemberModel(team_id, names, "未選擇", teamDesc, leader_id));

                    notFound();
                    syncViewModel();

                } catch (JSONException e) {
                    Log.e("GroupLeaderActivity: ", "getStudentData: JSONError!");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "getStudentData: volleyError!");
            }
        });
    }

    private synchronized void syncViewModel() {
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        memberViewModel.getMemberObserver().observe(this, memberModel -> {
            if (memberModels != null)
                memberAdapter.updateLeaderAdapter(memberModels);
        });

        memberViewModel.setMemberList(memberModels);
    }

    private void notFound() {
        if (memberModels.size() > 0)
            tvNotFound.setVisibility(View.GONE);
        else
            tvNotFound.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new MemberAdapter(this, memberModels);
        recyclerView.setAdapter(memberAdapter);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());

        btnEnter.setOnClickListener(v -> {
            //Fixme:
            getTeamSize();
            if (shareData.getTeam_ID_Array() != null) {
                volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_DESCRIPTION + shareData.getGroup_ID(), new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (jsonObject.getInt("TeamDesc_id") == Integer.parseInt(shareData.getTeam_ID_Array().get(0).getTeamDesc())) {
                                    int memberSize = jsonObject.getInt("Group_limit");
                                    Log.e(TAG, memberSize + " | " + shareData.getNumberOfMember());

                                    if (shareData.getNumberOfMember() >= memberSize) {
                                        Toast.makeText(GroupMemberActivity.this, "此組已滿", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else {
                                        Toast.makeText(GroupMemberActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                        repeatHelper.stop();
                                        Intent ii = new Intent(getApplicationContext(), GroupMemberSecondActivity.class);
                                        startActivity(ii);
                                    }
                                    // return;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Toast.makeText(GroupMemberActivity.this, "連線異常...", Toast.LENGTH_SHORT).show();
                    }
                });

            } else
                Toast.makeText(this, "請選擇組別！", Toast.LENGTH_SHORT).show();
        });
    }

    private void getTeamSize() {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_MEMBER + shareData.getTeam_ID_Array().get(0).getTeamID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                int count = 1;
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                    count += jsonArray.length();
                    shareData.saveNumberOfMember(count);

                } catch (JSONException e) {
                    Toast.makeText(GroupMemberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(GroupMemberActivity.this, "連線異常...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.groupMember_btn_Back);
        btnEnter = findViewById(R.id.groupMember_btn_Enter);
        tvNotFound = findViewById(R.id.groupMember_textView_NoFound);
        recyclerView = findViewById(R.id.groupMember_recycleView);

        memberModels = new ArrayList<>();

        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_TEAM);
        repeatHelper = new RepeatHelper(this::syncData);
        shareData = new ShareData(this);
        shareData.saveNumberOfMember(0);
        volleyApi = new VolleyApi(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stopScanning();
        repeatHelper.stop();
    }
}