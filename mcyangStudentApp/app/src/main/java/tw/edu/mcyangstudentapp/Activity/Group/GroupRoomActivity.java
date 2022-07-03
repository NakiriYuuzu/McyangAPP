package tw.edu.mcyangstudentapp.Activity.Group;

import  androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
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

import tw.edu.mcyangstudentapp.ActivityModel.GroupRoomModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Firebase.FirebaseVariables;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.GroupRoomAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.GroupRoomViewModel;

public class GroupRoomActivity extends AppCompatActivity {

    ArrayList<GroupRoomModel> groupRoom;
    ArrayList<String> memberNames;

    ShapeableImageView btnBack;
    MaterialButton btnCreate;
    MaterialTextView tvNotFound;
    RecyclerView recyclerView;

    ShareData shareData;
    VolleyApi volleyApi;

    GroupRoomAdapter roomAdapter;
    GroupRoomViewModel groupRoomViewModel;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_room);

        initView();
        checkLeader();
        firebaseData();
        initData();
        initButton();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new GroupRoomAdapter(this, groupRoom);
        recyclerView.setAdapter(roomAdapter);
    }

    private synchronized void initData() {
        volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_MEMBER + shareData.getChat_ID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String sid = jsonObject.getString("S_id");

                        volleyApi.api(Request.Method.GET, DefaultSetting.URL_STUDENT + sid, new VolleyApi.VolleyGet() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                                    result = new String(text);

                                    JSONObject jsonObject = new JSONObject(result);
                                    String name = jsonObject.getString("S_Name");
                                    memberNames.add(name);

                                } catch (JSONException e) {
                                    Toast.makeText(GroupRoomActivity.this, "資料異常", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailed(VolleyError error) {
                                Toast.makeText(GroupRoomActivity.this, "網絡異常", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    Toast.makeText(GroupRoomActivity.this, "資料異常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(GroupRoomActivity.this, "網路異常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private synchronized void firebaseData() {
        ref = database.getReference(shareData.getChat_Room()).child(shareData.getChat_ID()).child(shareData.getStudentNames()).child(FirebaseVariables.CHAT);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<GroupRoomModel> groupRooms = new ArrayList<>();
                for (DataSnapshot data1 : snapshot.getChildren()) {
                    groupRooms.add(new GroupRoomModel(data1.getKey()));
                }

                groupRoom = groupRooms;

                notFound();
                syncViewModel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("onCancelled: ", error.getMessage());
            }
        });
    }

    private synchronized void syncViewModel() {
        groupRoomViewModel = new ViewModelProvider(this).get(GroupRoomViewModel.class);
        groupRoomViewModel.getGroupRoomObserver().observe(this, groupRoomModels -> {
            if (groupRoom != null)
                roomAdapter.updateGroupRoomAdapter(groupRoom);
        });

        groupRoomViewModel.setGroupRoomList(groupRoom);
    }

    private void checkLeader() {
        if (shareData.getStudentNames().equals(shareData.getChat_Name())) {
            btnCreate.setVisibility(View.VISIBLE);
            tvNotFound.setText("請麻煩組長請創建聊天室");
        } else {
            btnCreate.setVisibility(View.GONE);
            tvNotFound.setText("等待組長創建聊天室");
        }
    }

    private void notFound() {
        if (groupRoom.size() > 0)
            tvNotFound.setVisibility(View.GONE);
        else
            tvNotFound.setVisibility(View.VISIBLE);
    }

    private void bottomSheet() {
        final BottomSheetDialog bsd = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_grouproom, null);
        TextInputEditText et_groupName = view.findViewById(R.id.bottom_sheet_input_Names);

        view.findViewById(R.id.bottom_sheet_groupRoom_btn).setOnClickListener(v -> {
            String groupName;
            if (et_groupName.getText() != null) {
                groupName = et_groupName.getText().toString();
                if (groupName.equals("")) {
                    Toast.makeText(this, "請輸入群組名稱！", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "創建成功！", Toast.LENGTH_SHORT).show();
                    ref = database.getReference(shareData.getChat_Room()).child(shareData.getChat_ID()).child(shareData.getStudentNames()).child(FirebaseVariables.CHAT).child(groupName);
                    ref.setValue(groupName);

                    for (String memberName : memberNames) {
                        ref = database.getReference(shareData.getChat_Room()).child(shareData.getChat_ID()).child(memberName).child(FirebaseVariables.CHAT).child(groupName);
                        ref.setValue(groupName);
                    }
                }

                bsd.dismiss();
                firebaseData();
            }
        });

        bsd.setContentView(view);
        bsd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bsd.show();
    }

    private void initButton() {
        btnCreate.setOnClickListener(v -> bottomSheet());

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.groupRoom_btn_Back);
        btnCreate = findViewById(R.id.groupRoom_btn_Create);
        tvNotFound = findViewById(R.id.groupRoom_textView_NoFound);
        recyclerView = findViewById(R.id.groupRoom_recycleView);

        groupRoom = new ArrayList<>();
        memberNames = new ArrayList<>();

        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);

        database = FirebaseDatabase.getInstance(FirebaseVariables.FIREBASE_URL);
    }
}