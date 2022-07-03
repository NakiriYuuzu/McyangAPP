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
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private boolean isSearch = false;
    private boolean isClicked = false;

    ArrayList<GroupChatModel> chatMessage;

    ShapeableImageView btnBack, btnSearch;
    FrameLayout btnSend;
    MaterialTextView tvTitle;
    TextInputEditText inputMsg;
    RecyclerView recyclerView;

    MaterialDatePicker.Builder dateBuilder;
    MaterialDatePicker datePicker;

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

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Log.e(TAG, "datePicker: " + datePicker.getHeaderText());
            isClicked = false;
            String dateTime = datePicker.getHeaderText();
            dateSearch(dateTime);
        });

        datePicker.addOnNegativeButtonClickListener(selection -> {
            isClicked = false;
            initData();
        });

        datePicker.addOnCancelListener(dialogInterface -> {
            isClicked = false;
            initData();
        });

        datePicker.addOnDismissListener(dialog -> {
            isClicked = false;
            initData();
        });
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new GroupChatAdapter(this, chatMessage);
        recyclerView.setAdapter(chatAdapter);
    }

    private synchronized void dateSearch(String dateTime) {
        isSearch = true;
        ArrayList<GroupChatModel> groupChatList = new ArrayList<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 6));
        int day = Integer.parseInt(dateTime.substring(7, 8));

        for (int i = 0; i < chatMessage.size(); i++) {
            String chatTime = chatMessage.get(i).getTime();
            String chatOldMonth = chatTime.substring(4, 7);
            int chatYear = Integer.parseInt(chatTime.substring(20, 24));
            int chatMonth = 0;
            int chatDay = Integer.parseInt(chatTime.substring(8, 10));

            for (int j = 0; j < months.length; j++) {
                if (chatOldMonth.equals(months[j])) {
                    chatMonth = j + 1;
                    break;
                }
            }

            Log.e(TAG, "dateSearch: " + year + " " + month + " " + day);
            Log.e(TAG, "CHAT: " + chatYear + " " + chatMonth + " " + chatDay);

            if (chatYear == year && chatMonth == month && chatDay == day)
                groupChatList.add(chatMessage.get(i));
        }

        if (groupChatList.size() == 0) {
            Toast.makeText(this, "查無資料", Toast.LENGTH_SHORT).show();

        } else {
            chatMessage = groupChatList;
            syncViewModel();
        }
    }

    private synchronized void initData() {
        ref = database.getReference(shareData.getChat_Room()).child(shareData.getChat_ID());
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<GroupChatModel> chatTempData = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    String names = child.getKey();
                    for (DataSnapshot childChild : child.getChildren()) {
                        for (DataSnapshot childChildChild : childChild.getChildren()) {
                            for (DataSnapshot data : childChildChild.getChildren()) {
                                int i = 0;
                                String[] chatData = new String[2];
                                Log.e(TAG, "onDataChange: " + data);

                                for (DataSnapshot dataChild : data.getChildren())
                                    chatData[i++] = Objects.requireNonNull(dataChild.getValue()).toString();

                                String msg = chatData[1];
                                String date = chatData[0].substring(0, 19) + " " + chatData[0].substring(30, 34);

                                chatTempData.add(new GroupChatModel(names, msg, date));
                            }
                        }
                    }
                }

                Collections.sort(chatTempData, Comparator.comparing(GroupChatModel::getTime));

                if (isSearch) {
                    chatMessage = chatTempData;
                    isSearch = false;

                } else {
                    if (chatMessage.size() > 0)
                        chatMessage.add(chatTempData.get(chatTempData.size() - 1));
                    else
                        chatMessage.addAll(chatTempData);
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

    private synchronized void syncViewModel() {
        chatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);
        chatViewModel.getChatObserver().observe(this, groupChatModels -> {
            if (chatMessage != null)
                chatAdapter.updateGroupChatAdapter(chatMessage);
        });

        chatViewModel.setChatMessage(chatMessage);
    }

    private void sendMessage(String msg) {
        ref = database.getReference(shareData.getChat_Room()).child(shareData.getChat_ID()).child(shareData.getStudentNames()).child(FirebaseVariables.CHAT).child(shareData.getChatRoom_Name()).child(String.valueOf(new Date().getTime()));
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

        btnSearch.setOnClickListener(v -> {
            if (!isClicked) {
                isClicked = true;
                datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        btnSearch = findViewById(R.id.groupChat_btn_Search);
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

        dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText("搜尋：選擇日期");
        datePicker = dateBuilder.build();

        tvTitle.setText(shareData.getChatRoom_Name());
    }
}