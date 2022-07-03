package tw.edu.pu.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import tw.edu.pu.Activity.MainActivity;
import tw.edu.pu.ActivityModel.GroupMemberModel;
import tw.edu.pu.ActivityModel.GroupSecondModel;
import tw.edu.pu.ActivityModel.GroupThirdModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.Firebase.FirebaseVariables;
import tw.edu.pu.Helper.CustomViewHelper;
import tw.edu.pu.Helper.RepeatHelper;
import tw.edu.pu.R;
import tw.edu.pu.RecyclerAdapter.GroupThirdAdapter;
import tw.edu.pu.StoredData.ShareData;
import tw.edu.pu.ViewModel.GroupThirdViewModel;

public class GroupThirdActivity extends AppCompatActivity {
    private static final String TAG = "GroupThirdActivity";
    boolean isBroadcast = false;

    ArrayList<GroupThirdModel> groupModels;
    ArrayList<GroupMemberModel> groupMemberModels;


    ShapeableImageView btnBack;
    MaterialButton btn_Finish;
    MaterialTextView tvBeacon, tv_NotFound;
    MaterialCardView btnBeacon;
    RecyclerView recyclerView;

    BeaconController beaconController;
    GroupThirdViewModel groupThirdViewModel;
    GroupThirdAdapter groupAdapter;
    CustomViewHelper viewHelper;
    RepeatHelper repeatHelper;
    VolleyApi volleyApi;
    ShareData shareData;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_third);

        initView();
        initButton();
        sendData();
        updateData();
        initRecyclerView();
    }

    private void sendData() {
        if (shareData.getRemoveTeam_ID().size() > 0) {
            for (String s : shareData.getRemoveTeam_ID()) {
                volleyApi.api(Request.Method.DELETE, DefaultSetting.URL_GROUP_TEAM_ID + s + "/", new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("sendData: ", "Success!");
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Log.e("sendData: ", "Error!");
                    }
                });
            }
        }

        syncData();
    }

    private synchronized void syncData() {
        if (shareData.getGroupSecond() != null) {
            for (GroupSecondModel groupSecondModel : shareData.getGroupSecond())
                groupModels.add(new GroupThirdModel(groupSecondModel.getTeamID(), groupSecondModel.getLeaderName(), 1));
        }

        notFound();
        syncViewModel();
    }

    private synchronized void updateData() {
        for (GroupThirdModel groupModel : groupModels)
            volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_MEMBER + groupModel.getTeamID() + "/", new VolleyApi.VolleyGet() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String memberID = jsonObject.getString("TeamMember_id");
                            String memberName = jsonObject.getString("S_id");
                            getStudentName(groupModel.getTeamID(), memberID, memberName);
                        }

                        groupModel.setTotal(jsonArray.length() + 1);

                        Log.e("syncData: ", groupModels.toString());
                        Log.e("syncData: ", groupMemberModels.toString());

                        syncViewModel();

                    } catch (JSONException e) {
                        Log.e(TAG, "UpdateData: JSONError!");
                    }
                }

                @Override
                public void onFailed(VolleyError error) {
                    Log.e(TAG, "UpdateData: VolleyError!");
                }
            });
    }

    private void updateDataToFirebase() {
        for (GroupThirdModel groupModel : groupModels) {
            myRef = database.getReference(shareData.getDesc_ID()).child(groupModel.getTeamID());

            for (GroupMemberModel memberModel : groupMemberModels)
                if (groupModel.getTeamID().equals(memberModel.getTeamID()))
                    myRef.child(memberModel.getMemberName()).child("Chat").setValue(new Date().getTime());

            myRef.child(groupModel.getLeaderName()).child("Chat").setValue(new Date().getTime());
        }
    }


    private void editDataToFirebase() {
        // TODO: Add edit firebase data to here
    }

    private void getStudentName(String teamID, String memberID, String memberName) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_STUDENT + memberName, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String names = jsonObject.getString("S_Name");

                    if (isExisted(names))
                        groupMemberModels.add(new GroupMemberModel(teamID, memberID, names));

                    shareData.saveGroupMember(groupMemberModels);

                } catch (JSONException e) {
                    Log.e(TAG, "getStudentName: JSONError!");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "getStudentName: VolleyError!");
            }
        });
    }

    private boolean isExisted(String memberName) {
        boolean isExist = true;
        if (groupMemberModels.size() > 0)
            for (GroupMemberModel groupMemberModel : groupMemberModels) {
                if (groupMemberModel.getMemberName().equals(memberName)) {
                    isExist = false;
                    break;
                }
            }
        return isExist;
    }

    private void notFound() {
        if (groupModels.size() > 0)
            tv_NotFound.setVisibility(View.GONE);
        else
            tv_NotFound.setVisibility(View.VISIBLE);
    }

    private synchronized void syncViewModel() {
        groupThirdViewModel = new ViewModelProvider(this).get(GroupThirdViewModel.class);
        groupThirdViewModel.getGroupObserver().observe(this, groupThirdModels -> {
            if (groupModels != null)
                groupAdapter.updateGroupAdapter(groupModels);
        });

        groupThirdViewModel.setGroupList(groupModels);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupAdapter = new GroupThirdAdapter(this, groupModels);
        recyclerView.setAdapter(groupAdapter);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());

        btnBeacon.setOnClickListener(v -> {
            if (isBroadcast) {
                isBroadcast = false;
                beaconController.stop_BroadcastBeacon();
                repeatHelper.stop();
                tvBeacon.setText(R.string.btn_StartBroadcast);
                btnBeacon.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            } else {
                isBroadcast = true;
                beaconController.init_GroupThird_BroadcastBeacon();
                beaconController.start_BroadcastBeacon();
                repeatHelper.start(2000);
                tvBeacon.setText(R.string.btn_StopBroadcast);
                btnBeacon.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
            }
        });

        btn_Finish.setOnClickListener(v -> viewHelper.showAlertBuilder("設定聊天室", "請問要建立新的聊天室嗎?", "建立", "取消", new CustomViewHelper.AlertListener() {
            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                updateDataToFirebase();
                Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ii);
            }

            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {
                editDataToFirebase();
                Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ii);
            }
        }));
    }

    private void initView() {
        btnBack = findViewById(R.id.groupThird_btn_Back);
        btn_Finish = findViewById(R.id.groupThird_btn_NextPage);
        btnBeacon = findViewById(R.id.groupThird_btn_beaconBtn);
        tvBeacon = findViewById(R.id.groupThird_tv_beacon);
        tv_NotFound = findViewById(R.id.groupThird_textView_NotFound);
        recyclerView = findViewById(R.id.groupThird_recyclerview);

        groupModels = new ArrayList<>();
        groupMemberModels = new ArrayList<>();

        viewHelper = new CustomViewHelper(this);
        beaconController = new BeaconController(this);
        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
        repeatHelper = new RepeatHelper(this::updateData);

        database = FirebaseDatabase.getInstance(FirebaseVariables.FIREBASE_URL);
        myRef = database.getReference(shareData.getDesc_ID());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBroadcast)
            beaconController.stop_BroadcastBeacon();
        repeatHelper.stop();
    }
}