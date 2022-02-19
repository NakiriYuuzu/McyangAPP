package tw.edu.mcyangstudentapp.Activity.Answer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import tw.edu.mcyangstudentapp.R;

public class AnswerSecondActivity extends AppCompatActivity {

    ShapeableImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_second);

        initView();
        initButton();
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.answerSecond_btn_Back);
    }
}