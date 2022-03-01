package tw.edu.mcyangstudentapp.Activity.Answer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.altbeacon.beacon.Beacon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.CustomViewHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class AnswerActivity extends AppCompatActivity {

    private static final String TAG = "AnswerActivity: ";

    MaterialCardView card_Block;
    MaterialTextView tv_QuestionInfo;
    MaterialButton btn_Send;
    TextInputEditText et_QuestionInput;
    ShapeableImageView btn_Back;

    BeaconController beaconController;
    CustomViewHelper viewHelper;
    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_acitivity);

        initView();
        initButton();
        viewHelper.setupUI(findViewById(R.id.activity_answer));
    }

    private void beaconScanning() {
        beaconController.startScanning((beacons, region) -> {
            if (beacons.size() > 0) {
                for (Beacon beacon : beacons) {
                    String id = beacon.getId2().toString();

                    if (id.equals(shareData.getMajor())) {
                        beaconController.stopScanning();
                        shareData.saveQuestionID(beacon.getId3().toString());
                        Log.e(TAG, "Major: " + shareData.getMajor() + "Minor: " + shareData.getQuestionID());
                        getQuestion();
                    } else {
                        Toast.makeText(this, "課程不匹配。。。", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getQuestion() {
        if (shareData.getQuestionID() != null)
            volleyApi.getApi(DefaultSetting.URL_ANSWER_QUESTION + shareData.getQuestionID(), new VolleyApi.VolleyGet() {
                @Override
                public void onSuccess(String result) {
                    Log.e(TAG, result);
                    try {
                        JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String questionDoc = jsonObject.getString("Q_doc");
                            tv_QuestionInfo.setText(questionDoc);
                        }
                        card_Block.setVisibility(View.INVISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(VolleyError error) {
                    Toast.makeText(AnswerActivity.this, "伺服器連接失敗，請稍後嘗試！", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void initButton() {
        btn_Send.setOnClickListener(v -> {
            if (et_QuestionInput.getText() != null)
                if (!et_QuestionInput.getText().toString().equals(""))
                    sendAnswer();
                else
                    Toast.makeText(this, "請輸入答案！", Toast.LENGTH_SHORT).show();
        });

        btn_Back.setOnClickListener(v -> finish());
    }

    private void sendAnswer() {
        volleyApi.postApi(DefaultSetting.URL_ANSWER_MEMBER, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);
                    JSONObject jsonObject = new JSONObject(result);
                    shareData.saveAnswerID(jsonObject.getString("Answer_id"));

                    Intent ii = new Intent(getApplicationContext(), AnswerSecondActivity.class);
                    startActivity(ii);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(AnswerActivity.this, "Unable connect to server!", Toast.LENGTH_SHORT).show();
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            params.put("Answer_doc", et_QuestionInput.getText() + "");
            params.put("Answer", "false");
            params.put("Q_id", shareData.getQuestionID());
            params.put("S_id", shareData.getStudentID());
            return params;
        });
    }

    private void initView() {
        btn_Back = findViewById(R.id.answer_btn_Back);
        btn_Send = findViewById(R.id.answer_btn_Send);
        card_Block = findViewById(R.id.answer_Card_Block);
        tv_QuestionInfo = findViewById(R.id.answer_tv_Question);
        et_QuestionInput = findViewById(R.id.answer_et_Input);

        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
        viewHelper = new CustomViewHelper(this);

        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_ANSWER);
        beaconScanning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stopScanning();

    }
}