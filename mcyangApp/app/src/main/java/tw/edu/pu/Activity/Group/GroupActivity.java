package tw.edu.pu.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import tw.edu.pu.R;

public class GroupActivity extends AppCompatActivity {

    AutoCompleteTextView tv_Choose;
    ShapeableImageView btn_Back;
    TextInputEditText et_Info, et_groupCount, et_peopleCount;
    MaterialButton btn_Enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        initView();
        initButton();
    }

    private void initButton() {
        btn_Enter.setOnClickListener(v -> {
            Intent ii = new Intent(this, GroupSecondActivity.class);
            startActivity(ii);
        });

        btn_Back.setOnClickListener(v -> finish());
    }

    private void initView() {
        tv_Choose = findViewById(R.id.group_AutoCompleteText);
        et_Info = findViewById(R.id.group_input_info);
        et_groupCount = findViewById(R.id.group_input_groupNum);
        et_peopleCount = findViewById(R.id.group_input_people);
        btn_Enter = findViewById(R.id.group_btn_Enter);
        btn_Back = findViewById(R.id.group_btn_Back);
    }
}