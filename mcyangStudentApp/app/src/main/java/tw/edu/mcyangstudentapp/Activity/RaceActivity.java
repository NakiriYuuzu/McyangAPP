package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.altbeacon.beacon.Beacon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.RepeatHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class RaceActivity extends AppCompatActivity {

    private static final String TAG = "RaceActivity: ";
    boolean status = false;
    boolean isUnlocked = false;
    boolean isAnswer = false;
    boolean isAnswerStatus = false;
    boolean isRepeat = false;
    String raceID, currentList = "";

    MaterialCardView btnStart;
    MaterialTextView tvHint;
    ShapeableImageView btnBack, imgBtn;
    MaterialButton btnNext, btnEnd;

    VolleyApi volleyApi;
    ShareData shareData;
    RepeatHelper repeatHelper;
    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        initView();
        initButton();
    }

    private void initButton() {
        btnStart.setOnClickListener(v -> {
            if (isUnlocked) {
                beaconController.stopScanning();
                sendAPI();
            } else if (status)
                Toast.makeText(this, "題目已關閉！", Toast.LENGTH_SHORT).show();
            else if (isAnswer)
                Toast.makeText(this, "你已搶答本題目！", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "老師還沒廣播！", Toast.LENGTH_SHORT).show();
        });

        btnNext.setOnClickListener(v -> {
            if (status) {
                imgBtn.setImageResource(R.drawable.button_red);
                isAnswer = false;
                isRepeat = false;
                isAnswerStatus = false;
                status = false;
                currentList = "";
                tvHint.setText("搶答");
                beaconScanning();
                repeatHelper.stop();
            } else
                Toast.makeText(this, "此題目還在執行中請稍後。", Toast.LENGTH_SHORT).show();
        });

        btnEnd.setOnClickListener(v -> finish());

        btnBack.setOnClickListener(v -> finish());
    }

    private void sendAPI() {
        volleyApi.postApi(DefaultSetting.URL_RACE_LIST, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    currentList = jsonObject.getString("id");
                    Log.e(TAG, "sendAPI: " + currentList + " |Result: " + result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                imgBtn.setImageResource(R.drawable.button_red);
                tvHint.setText("等待老師批改");
                isUnlocked = false;
                isAnswer = true;
                Toast.makeText(RaceActivity.this, "搶答成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "onFailed: " + error);
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            params.put("Answer", "false");
            params.put("R_id", raceID);
            params.put("S_id", shareData.getStudentID());
            return params;
        });
    }

    private void checkStatus() {
        volleyApi.getApi(DefaultSetting.URL_RACE_ANSWER + raceID, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "Checking");
                    JSONObject jsonObject = new JSONObject(result);
                    String getStatus = jsonObject.getString("Status");

                    if (getStatus.equals("false")) {
                        repeatHelper.stop();
                        status = true;
                        isUnlocked = false;
                        tvHint.setText("老師已關閉搶答!");
                        Toast.makeText(RaceActivity.this, "老師已關閉搶答!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(RaceActivity.this, "無法取得搶答狀態...", Toast.LENGTH_SHORT).show();
                repeatHelper.stop();
            }
        });
    }

    private void getAnswerStatus() {
        if (isAnswer)
            if (!isAnswerStatus)
                volleyApi.getApi(DefaultSetting.URL_RACE_LIST + currentList, new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            Log.e(TAG, "Answering");
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject jsonObject;
                            String answer = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                answer = jsonObject.getString("Answer");
                            }

                            Log.e(TAG, "getAnswerStatus: " + result);

                            if (answer.equals("true")) {
                                imgBtn.setImageResource(R.drawable.button_success);
                                tvHint.setText("");
                                isAnswerStatus = true;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Log.e(TAG, "onFailed: " + "查詢不到答案資料!");
                    }
                });
    }

    private void initView() {
        imgBtn = findViewById(R.id.race_img_Btn);
        tvHint = findViewById(R.id.race_tv_Btn);
        btnStart = findViewById(R.id.race_btn_Race);
        btnNext = findViewById(R.id.race_btn_Next);
        btnBack = findViewById(R.id.race_btn_Back);
        btnEnd = findViewById(R.id.race_btn_End);

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
        repeatHelper = new RepeatHelper(() -> {
            getAnswerStatus();
            checkStatus();
        });

        beaconController = new BeaconController(this);
        beaconController.beaconInit(DefaultSetting.BEACON_UUID_RACE);
        beaconScanning();
    }

    private void beaconScanning() {
        beaconController.startScanning((beacons, region) -> {
            Log.e(TAG, "Scanning!" + beacons.size());
            if (beacons.size() > 0) {
                for (Beacon beacon : beacons) {
                    String id = beacon.getId2().toString();

                    if (id.equals(shareData.getMajor())) {
                        imgBtn.setImageResource(R.drawable.button_yellow);
                        raceID = beacon.getId3().toString();
                        isUnlocked = true;
                        if (!isRepeat) {
                            repeatHelper.start(2000);
                            isRepeat = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconController.stopScanning();
        repeatHelper.stop();
    }
}