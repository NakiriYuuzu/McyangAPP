package tw.edu.mcyangstudentapp.Activity.Answer;

import androidx.appcompat.app.AppCompatActivity;

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

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.DefaultSetting;
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
    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_acitivity);

        initView();
        initButton();
    }

    private void beaconScanning() {
        beaconController.startScanning((beacons, region) -> {
            Log.e(TAG, "Scanning!" + beacons.size());
            if (beacons.size() > 0) {
                for (Beacon beacon : beacons) {
                    String id = beacon.getId2().toString();

                    if (id.equals(shareData.getMajor())) {
                        beaconController.stopScanning();
                        shareData.saveAnswerID(beacon.getId3().toString());
                        getQuestion();
                    }
                }
            }
        });
    }

    private void getQuestion() {
        if (shareData.getAnswerID() != null)
            volleyApi.getApi(DefaultSetting.URL_ANSWER_QUESTION + shareData.getAnswerID(), new VolleyApi.VolleyGet() {
                @Override
                public void onSuccess(String result) {
                    Log.e(TAG, result);
                    try {
                        JSONArray jsonArray = new JSONArray(result.getBytes(StandardCharsets.ISO_8859_1));
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            String questionDoc = jsonObject.getString("Q_doc");
                            tv_QuestionInfo.setText(questionDoc);
                            card_Block.setVisibility(View.GONE);
                        }


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
        if (et_QuestionInput.getText() != null)
            if (!et_QuestionInput.getText().toString().equals(""))
                btn_Send.setOnClickListener(v -> {

                });
            else
                Toast.makeText(this, "請輸入答案！", Toast.LENGTH_SHORT).show();

        btn_Back.setOnClickListener(v -> finish());
    }

    private void initView() {
        btn_Back = findViewById(R.id.answer_btn_Back);
        btn_Send = findViewById(R.id.answer_btn_Send);
        card_Block = findViewById(R.id.answer_Card_Block);
        tv_QuestionInfo = findViewById(R.id.answer_tv_Question);
        et_QuestionInput = findViewById(R.id.answer_et_Input);

        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);

        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_ANSWER);
        beaconScanning();
    }
}