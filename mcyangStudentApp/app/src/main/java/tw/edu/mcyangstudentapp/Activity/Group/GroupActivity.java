package tw.edu.mcyangstudentapp.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class GroupActivity extends AppCompatActivity {

    ShapeableImageView btnBack;
    MaterialButton btnLeader, btnMember, btnChat;

    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initView();
        initButton();
        btnPermission();
        shareData.saveTeam_ID_Array(null);
    }

    private void btnPermission() {
        if (shareData.getMajor() != null) {
            btnLeader.setEnabled(true);
            btnLeader.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            btnMember.setEnabled(true);
            btnMember.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        } else {
            btnLeader.setEnabled(false);
            btnLeader.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
            btnMember.setEnabled(false);
            btnMember.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        }
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());
        btnLeader.setOnClickListener(v -> {
            Intent ii = new Intent(this, GroupLeaderActivity.class);
            startActivity(ii);
        });

        btnMember.setOnClickListener(v -> {
            Intent ii = new Intent(this, GroupMemberActivity.class);
            startActivity(ii);
        });

        btnChat.setOnClickListener(v -> {
            Intent ii = new Intent(this, GroupViewActivity.class);
            startActivity(ii);
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.group_btn_Back);
        btnLeader = findViewById(R.id.group_btn_Leader);
        btnMember = findViewById(R.id.group_btn_member);
        btnChat = findViewById(R.id.group_btn_chat);

        shareData = new ShareData(this);
    }
}