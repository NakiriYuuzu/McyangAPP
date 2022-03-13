package tw.edu.mcyangstudentapp.Activity.Answer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import tw.edu.mcyangstudentapp.Activity.MainActivity;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.RepeatHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class AnswerSecondActivity extends AppCompatActivity {

    private static final String TAG = "AnswerSecond: ";

    ShapeableImageView btnBack, btnBackground;
    MaterialTextView btnText;
    MaterialButton btnFinish, btnNext;

    RepeatHelper repeatHelper;
    ShareData shareData;
    VolleyApi volleyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_second);

        initView();
        initButton();
        repeatHelper.start(2000);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());

        btnFinish.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnNext.setOnClickListener(v -> {
            Intent ii = new Intent(this, AnswerActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.answerSecond_btn_Back);
        btnBackground = findViewById(R.id.answerSecond_img_Btn);
        btnText = findViewById(R.id.answerSecond_tv_Btn);
        btnFinish = findViewById(R.id.answerSecond_btn_End);
        btnNext = findViewById(R.id.answerSecond_btn_Next);

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
        repeatHelper = new RepeatHelper(this::syncData);
    }

    private void syncData() {
        volleyApi.getApi(DefaultSetting.URL_ANSWER_MEMBER + shareData.getAnswerID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String answer = jsonObject.getString("Answer");
                        if (answer.equals("true")) {
                            repeatHelper.stop();
                            btnText.setText("回答正確！");
                            btnBackground.setImageResource(R.drawable.button_success);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onSuccess: " + result);
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(getApplicationContext(), "伺服器連接失敗，請稍後嘗試！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repeatHelper.stop();
    }
}