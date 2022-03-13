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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.Activity.MainActivity;
import tw.edu.mcyangstudentapp.ActivityModel.SecondMemberModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.RepeatHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.MemberSecondAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.SecondMemberViewModel;

public class GroupMemberSecondActivity extends AppCompatActivity {

    private static final String TAG = "MemberSecondActivity";

    ArrayList<SecondMemberModel> memberModels;

    ShapeableImageView btnBack;
    MaterialTextView tvNotFound;
    MaterialButton btnEnter;
    RecyclerView recyclerView;

    SecondMemberViewModel memberViewModel;
    MemberSecondAdapter memberAdapter;
    RepeatHelper repeatHelper;
    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_second);

        initView();
        initButton();
        initRecyclerView();
        sendData();
        repeatHelper.start(2000);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new MemberSecondAdapter(memberModels);
        recyclerView.setAdapter(memberAdapter);
    }

    private synchronized void syncViewModel() {
        memberViewModel = new ViewModelProvider(this).get(SecondMemberViewModel.class);
        memberViewModel.getMemberObserver().observe(this, secondMemberViewModels -> {
            if (memberModels != null)
                memberAdapter.updateMemberAdapter(memberModels);
        });

        memberViewModel.setMemberList(memberModels);
    }

    private void notFound() {
        if (memberModels.size() > 0)
            tvNotFound.setVisibility(View.GONE);
        else
            tvNotFound.setVisibility(View.VISIBLE);
    }

    private boolean isExisted(String leaderName) {
        boolean isExist = true;
        if (memberModels.size() > 0)
            for (SecondMemberModel memberModel : memberModels)
                if (memberModel.getGroupMember().equals(leaderName)) {
                    isExist = false;
                    break;
                }
        return isExist;
    }

    private synchronized void syncData() {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_MEMBER + shareData.getTeam_ID().get(0).getTeamID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String sid = jsonObject.getString("S_id");
                        getStudentData(sid);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "SyncData: JSONError!");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "SyncData: VolleyError!");
            }
        });
    }

    private synchronized void getStudentData(String sid) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_STUDENT + sid, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String names = jsonObject.getString("S_Name");

                    if (isExisted(names))
                        memberModels.add(new SecondMemberModel("組員", names));

                    notFound();
                    syncViewModel();

                } catch (JSONException e) {
                    Log.e(TAG, "getStudentData: JSONException!");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "getStudentData: VolleyError!");
            }
        });
    }

    private synchronized void sendData() {
        volleyApi.api(Request.Method.POST, DefaultSetting.URL_GROUP_TEAM_MEMBER, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "SendData: onSuccess");
                memberModels.add(new SecondMemberModel("隊長", shareData.getTeam_ID().get(0).getLeaderName()));
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "SendData: VolleyError");
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            params.put("S_id", shareData.getStudentID());
            params.put("Team_id", shareData.getTeam_ID().get(0).getTeamID());
            return params;
        });
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> {
            repeatHelper.stop();
            finish();
        });

        btnEnter.setOnClickListener(v -> {
            repeatHelper.stop();
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.groupMemberSecond_btn_Back);
        btnEnter = findViewById(R.id.groupMemberSecond_btn_Enter);
        tvNotFound = findViewById(R.id.groupMemberSecond_textView_NoFound);
        recyclerView = findViewById(R.id.groupMemberSecond_recycleView);

        memberModels = new ArrayList<>();

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
        repeatHelper = new RepeatHelper(this::syncData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repeatHelper.stop();
    }
}