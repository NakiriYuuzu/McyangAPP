package tw.edu.mcyangstudentapp.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import tw.edu.mcyangstudentapp.R;

public class GroupActivity extends AppCompatActivity {

    ShapeableImageView btnBack;
    MaterialButton btnLeader, btnMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initView();
        initButton();
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
    }

    private void initView() {
        btnBack = findViewById(R.id.group_btn_Back);
        btnLeader = findViewById(R.id.group_btn_Leader);
        btnMember = findViewById(R.id.group_btn_member);
    }
}