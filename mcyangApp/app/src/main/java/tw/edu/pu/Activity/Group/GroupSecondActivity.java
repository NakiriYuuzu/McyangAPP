package tw.edu.pu.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import tw.edu.pu.ActivityModel.GroupSecondModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.Helper.CustomViewHelper;
import tw.edu.pu.Helper.RepeatHelper;
import tw.edu.pu.R;
import tw.edu.pu.RecyclerAdapter.GroupSecondAdapter;
import tw.edu.pu.StoredData.ShareData;
import tw.edu.pu.ViewModel.GroupSecondViewModel;

public class GroupSecondActivity extends AppCompatActivity {

    private static final String TAG = "GroupSecondActivity: ";
    boolean isBroadcast = false;

    ArrayList<GroupSecondModel> groupModels;
    ArrayList<String> removeTeamID;
    ArrayList<String> sid;

    MaterialButton btnEnter;
    MaterialCardView btnBeacon;
    MaterialTextView tvBeacon, tv_NotFound;
    ShapeableImageView btnBack;
    RecyclerView recyclerView;

    GroupSecondViewModel groupSecondViewModel;
    GroupSecondAdapter groupAdapter;
    CustomViewHelper viewHelper;
    BeaconController beaconController;
    RepeatHelper repeatHelper;
    ShareData shareData;
    VolleyApi volleyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_second);

        initView();
        initButton();
        initRecyclerView();
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());

        btnEnter.setOnClickListener(v -> {
            if (shareData.getTeam_ID() != null) {
                if (shareData.getTeam_ID().size() > 0) {
                    viewHelper.showAlertBuilder("選擇隊長", "目前已選擇" + shareData.getTeam_ID().size() + "位隊長", new CustomViewHelper.AlertListener() {
                        @Override
                        public void onPositive(DialogInterface dialogInterface, int i) {
                            for (GroupSecondModel groupModel : groupModels)
                                if (!isRemoved(groupModel.getTeamID(), shareData.getTeam_ID()))
                                    removeTeamID.add(groupModel.getTeamID());

                            shareData.saveRemoveTeam_ID(removeTeamID);
                            shareData.saveGroupSecond(groupModels);

                            if (isBroadcast)
                                beaconController.stop_BroadcastBeacon();
                            repeatHelper.stop();

                            Intent ii = new Intent(getApplicationContext(), GroupThirdActivity.class);
                            startActivity(ii);
                            dialogInterface.dismiss();
                        }

                        @Override
                        public void onNegative(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(this, "請選擇隊長！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "請選擇隊長！", Toast.LENGTH_SHORT).show();
            }
        });

        btnBeacon.setOnClickListener(v -> {
            if (isBroadcast) {
                isBroadcast = false;
                repeatHelper.stop();
                beaconController.stop_BroadcastBeacon();
                tvBeacon.setText(R.string.btn_StartBroadcast);
                btnBeacon.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            } else {
                isBroadcast = true;
                repeatHelper.start(2000);
                beaconController.init_GroupSecond_BroadcastBeacon();
                beaconController.start_BroadcastBeacon();
                tvBeacon.setText(R.string.btn_StopBroadcast);
                btnBeacon.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            }
        });
    }

    private boolean isRemoved(String teamID, ArrayList<String> list) {
        return list.contains(teamID);
    }

    private synchronized void syncData() {
        volleyApi.getApi(DefaultSetting.URL_GROUP_TEAM + shareData.getDesc_ID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String teamID = jsonObject.getString("Team_id");
                        String leaderID = jsonObject.getString("Leader_id");

                        if (isExisted(leaderID)) {
                            sid.add(leaderID);
                            getStudentData(teamID, leaderID);
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "syncData_JsonError");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "syncData_VolleyError");
            }
        });
    }

    private void notFound() {
        if (groupModels.size() > 0)
            tv_NotFound.setVisibility(View.GONE);
        else
            tv_NotFound.setVisibility(View.VISIBLE);
    }

    private boolean isExisted(String studentID) {
        boolean isExist = true;
        if (sid.size() > 0)
            for (String s : sid)
                if (s.equals(studentID)) {
                    isExist = false;
                    break;
                }
        return isExist;
    }

    private synchronized void getStudentData(String teamID, String leaderID) {
        volleyApi.getApi(DefaultSetting.URL_STUDENT + leaderID, new VolleyApi.VolleyGet() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String names = jsonObject.getString("S_Name");
                    groupModels.add(new GroupSecondModel(teamID, names, "選擇"));

                    notFound();
                    syncViewModel();

                } catch (JSONException e) {
                    Log.e(TAG, "getStudentData_JsonERROR");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "getStudentData_VolleyERROR");
            }
        });
    }

    private synchronized void syncViewModel() {
        groupSecondViewModel = new ViewModelProvider(this).get(GroupSecondViewModel.class);
        groupSecondViewModel.getGroupObserver().observe(this, groupSecondViewModels -> {
            if (groupModels != null)
                groupAdapter.updateGroupAdapter(groupModels);
        });

        groupSecondViewModel.setGroupList(groupModels);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupAdapter = new GroupSecondAdapter(this, groupModels);
        recyclerView.setAdapter(groupAdapter);
    }

    private void initView() {
        btnEnter = findViewById(R.id.groupSecond_btn_NextPage);
        btnBeacon = findViewById(R.id.groupSecond_btn_beaconBtn);
        btnBack = findViewById(R.id.groupSecond_btn_Back);
        tvBeacon = findViewById(R.id.groupSecond_tv_beacon);
        tv_NotFound = findViewById(R.id.groupSecond_textView_NotFound);
        recyclerView = findViewById(R.id.groupSecond_recyclerview);

        groupModels = new ArrayList<>();
        removeTeamID = new ArrayList<>();
        sid = new ArrayList<>();

        beaconController = new BeaconController(this);
        repeatHelper = new RepeatHelper(this::syncData);
        viewHelper = new CustomViewHelper(this);
        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBroadcast)
            beaconController.stop_BroadcastBeacon();
        repeatHelper.stop();
    }
}