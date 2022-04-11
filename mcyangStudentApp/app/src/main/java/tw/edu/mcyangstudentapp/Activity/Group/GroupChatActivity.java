package tw.edu.mcyangstudentapp.Activity.Group;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import tw.edu.mcyangstudentapp.ActivityModel.GroupChatModel;
import tw.edu.mcyangstudentapp.Firebase.FirebaseVariables;
import tw.edu.mcyangstudentapp.Helper.CustomViewHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.GroupChatAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.GroupChatViewModel;

public class GroupChatActivity extends AppCompatActivity {

    private static final String TAG = "GroupChatActivity";

    ArrayList<GroupChatModel> chatMessage;

    ShapeableImageView btnBack;
    FrameLayout btnSend;
    MaterialTextView tvTitle;
    TextInputEditText inputMsg;
    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference ref;

    GroupChatAdapter chatAdapter;
    GroupChatViewModel chatViewModel;

    CustomViewHelper viewHelper;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initView();
        initButton();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new GroupChatAdapter(this, chatMessage);
        recyclerView.setAdapter(chatAdapter);
    }

    private synchronized void initData() {
        ref = database.getReference(shareData.getChat_Room()).child(shareData.getChat_ID());
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.getKey();
                    Log.e(TAG, "onDataChange: " + name);

                    for (DataSnapshot child2 : child.getChildren()) {
                        for (DataSnapshot child3 : child2.getChildren()) {
                            int i = 0;
                            String[] data = new String[2];

                            Log.e(TAG, "onDataChange: " + child3);

                            for (DataSnapshot child4 : child3.getChildren())
                                data[i++] = Objects.requireNonNull(child4.getValue()).toString();

                            String msg = data[1];
                            String date = data[0].substring(0, 19);

                            Log.e(TAG, "onDataChang1e: " + msg + " | " + date);
                            if (!isExisted_Data(date)) {
                                chatMessage.add(new GroupChatModel(name, msg, date));
                            }
                        }
                    }
                }

                syncViewModel();
                recyclerView.scrollToPosition(chatMessage.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    private boolean isExisted_Data(String date) {
        boolean isExisted = false;
        for (GroupChatModel groupChatModel : chatMessage)
            if (groupChatModel.getTime().equals(date)) {
                isExisted = true;
                break;
            }

        return isExisted;
    }

    private synchronized void syncViewModel() {
        chatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);
        chatViewModel.getChatObserver().observe(this, groupChatModels -> {
            if (chatMessage != null)
                chatAdapter.updateGroupChatAdapter(chatMessage);
        });

        chatViewModel.setChatMessage(chatMessage);
    }

    private void sendMessage(String msg) {
        ref = database.getReference(shareData.getChat_Room()).child(shareData.getChat_ID()).child(shareData.getStudentNames()).child(FirebaseVariables.CHAT).child(String.valueOf(new Date().getTime()));
        HashMap<String, String> message = new HashMap<>();
        message.put(FirebaseVariables.MESSAGE, msg);
        message.put(FirebaseVariables.DATETIME, String.valueOf(new Date()));
        ref.setValue(message);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            if (inputMsg.getText() != null) {
                if (!inputMsg.getText().toString().equals("")) {
                    sendMessage(inputMsg.getText().toString());
                    inputMsg.setText("");
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        btnBack = findViewById(R.id.groupChat_btn_Back);
        btnSend = findViewById(R.id.groupChat_btnSend);
        tvTitle = findViewById(R.id.groupChat_title);
        inputMsg = findViewById(R.id.groupChat_textInput);
        recyclerView = findViewById(R.id.groupChat_RecyclerView);

        viewHelper = new CustomViewHelper(this);
        viewHelper.setupUI(findViewById(R.id.groupChat_RecyclerView));

        shareData = new ShareData(this);

        chatMessage = new ArrayList<>();

        database = FirebaseDatabase.getInstance(FirebaseVariables.FIREBASE_URL);

        tvTitle.setText(shareData.getChat_Name() + "的隊伍");
    }
}