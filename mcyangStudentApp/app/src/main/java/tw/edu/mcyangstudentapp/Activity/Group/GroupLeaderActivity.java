package tw.edu.mcyangstudentapp.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import tw.edu.mcyangstudentapp.Activity.MainActivity;
import tw.edu.mcyangstudentapp.R;

public class GroupLeaderActivity extends AppCompatActivity {

    MaterialButton btnBack, btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_leader);

//        initView();
//        initButton();
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());

        btnEnter.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.groupLeader_btn_Back);
        btnEnter = findViewById(R.id.groupLeader_btn_Enter);
    }
}