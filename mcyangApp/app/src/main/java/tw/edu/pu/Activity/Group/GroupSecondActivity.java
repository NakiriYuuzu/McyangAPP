package tw.edu.pu.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import tw.edu.pu.R;

public class GroupSecondActivity extends AppCompatActivity {

    MaterialButton btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_second);

        initView();
        initButton();
    }

    private void initButton() {
        btnEnter.setOnClickListener(v -> {
            Intent ii = new Intent(this, GroupThirdActivity.class);
            startActivity(ii);
        });
    }

    private void initView() {
        btnEnter = findViewById(R.id.groupSecond_btn_NextPage);
    }
}