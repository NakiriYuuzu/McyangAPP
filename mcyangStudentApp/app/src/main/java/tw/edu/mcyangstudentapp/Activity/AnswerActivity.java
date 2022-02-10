package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import tw.edu.mcyangstudentapp.R;

public class AnswerActivity extends AppCompatActivity {

    ShapeableImageView btn_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_acitivity);

        initView();
        initButton();
    }

    private void initButton() {
        btn_Back.setOnClickListener(v -> finish());
    }

    private void initView() {
        btn_Back = findViewById(R.id.answer_btn_Back);
    }
}