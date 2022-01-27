package tw.edu.pu.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import tw.edu.pu.Activity.MainActivity;
import tw.edu.pu.R;

public class GroupThirdActivity extends AppCompatActivity {

    MaterialButton btn_Finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_third);

        initView();
        initButton();
    }

    private void initButton() {
        btn_Finish.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });
    }

    private void initView() {
        btn_Finish = findViewById(R.id.groupThird_btn_NextPage);
    }
}