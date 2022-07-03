package tw.edu.mcyangstudentapp.Activity.Group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tw.edu.mcyangstudentapp.ActivityModel.GroupViewModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Firebase.FirebaseVariables;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.GroupViewAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.GroupViewViewModel;

public class GroupViewActivity extends AppCompatActivity {

    private static final String TAG = "GroupViewActivity";
    ArrayList<GroupViewModel> groupView;
    boolean checkMember = false;
    boolean checkLeader = false;

    ShapeableImageView btnBack;
    RecyclerView recyclerView;
    MaterialTextView tvNotFound;

    ShareData shareData;
    VolleyApi volleyApi;

    GroupViewAdapter viewAdapter;
    GroupViewViewModel groupViewModel;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        initView();
        initButton();
        firebaseData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAdapter = new GroupViewAdapter(this, groupView);
        recyclerView.setAdapter(viewAdapter);
    }

    private synchronized void syncViewModel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(groupView, Comparator.comparing(GroupViewModel::getTeamID));
        }
        groupViewModel = new ViewModelProvider(this).get(GroupViewViewModel.class);
        groupViewModel.getGroupObserver().observe(this, groupViewList -> {
            if (groupView != null)
                viewAdapter.updateGroupViewAdapter(groupView);
        });

        groupViewModel.setGroupViewList(groupView);
    }

    private synchronized void firebaseData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren())
                    for (DataSnapshot childChild : child.getChildren())
                        findGroup(childChild.getKey());
                        //findLeader(childChild.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    private void notFound() {
        if (groupView.size() > 0)
            tvNotFound.setVisibility(View.GONE);
        else
            tvNotFound.setVisibility(View.VISIBLE);
    }

    private synchronized void findGroup(String key) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_MEMBER + key + "/", new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("S_id");
                        getStudentName(name, key);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: ");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "onFailed: ");
            }
        });
    }

    private void getStudentName(String name, String key) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_STUDENT + name + "/", new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String names = jsonObject.getString("S_Name");

                    if (names.equals(shareData.getStudentNames())) {
                        checkMember = true;
                    }

                    findLeader(key, names);

                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: ");
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "onFailed: ");
            }
        });
    }

    private synchronized void findLeader(String key, String memberName) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_ID + key + "/", new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String teamDesc = jsonObject.getString("Team_desc");
                        String leaderID = jsonObject.getString("Leader_id");

                        if (!isExisted_LeaderData(key))
                            getStudentName(key, leaderID, teamDesc, memberName);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "JSON Array Error" + e.getMessage());
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "findLeaderError!");
            }
        });
    }

    private boolean isExisted_LeaderData(String key) {
        boolean isExisted = false;
        for (int i = 0; i < groupView.size(); i++) {
            if (groupView.get(i).getTeamID().equals(key)) {
                isExisted = true;
                break;
            }
        }
        return isExisted;
    }

    private synchronized void getStudentName(String teamID, String leaderID, String teamDesc, String memberName) {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_STUDENT + leaderID + "/", new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String leaderName = jsonObject.getString("S_Name");

                    if (leaderName.equals(shareData.getStudentNames()))
                        checkLeader = true;

                    if (checkMember || checkLeader) {
                        if (!isExisted_SaveData(teamID)) {
                            if (memberName.equals(shareData.getStudentNames()) || leaderName.equals(shareData.getStudentNames())) {
                                groupView.add(new GroupViewModel(teamID, leaderName, teamDesc));
                                syncViewModel();
                                notFound();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "getStudentName Error! ");
            }
        });
    }

    private boolean isExisted_SaveData(String teamID) {
        boolean isExisted = false;
        for (int i = 0; i < groupView.size(); i++) {
            if (groupView.get(i).getTeamID().equals(teamID)) {
                isExisted = true;
                break;
            }
        }
        return isExisted;
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.groupView_btn_Back);
        recyclerView = findViewById(R.id.groupView_recycleView);
        tvNotFound = findViewById(R.id.groupView_textView_NoFound);

        groupView = new ArrayList<>();

        shareData = new ShareData(this);

        volleyApi = new VolleyApi(this);

        database = FirebaseDatabase.getInstance(FirebaseVariables.FIREBASE_URL);
        ref = database.getReference();
    }
}