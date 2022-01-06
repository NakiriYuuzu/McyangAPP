package tw.edu.pu.Activity.Race;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import tw.edu.pu.R;

public class RaceActivity extends AppCompatActivity {

    ShapeableImageView btnBack;
    TextInputEditText tvInput;
    MaterialButton btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        initView();
        initButton();
    }

    private void initButton() {
        btnEnter.setOnClickListener(v -> {
            Intent ii = new Intent(this, RaceSecondActivity.class);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.race_btn_Back);
        tvInput = findViewById(R.id.race_et_Input);
        btnEnter = findViewById(R.id.race_btn_Enter);
    }
}