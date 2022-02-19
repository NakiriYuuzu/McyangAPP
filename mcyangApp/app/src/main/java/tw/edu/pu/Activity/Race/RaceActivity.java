package tw.edu.pu.Activity.Race;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class RaceActivity extends AppCompatActivity {

    private static final String TAG = "RaceActivity: ";

    ShapeableImageView btnBack;
    TextInputEditText tvInput;
    MaterialButton btnEnter;

    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        initView();
        initButton();
    }

    private void initButton() {
        btnEnter.setOnClickListener(v -> {
            String inputText;

            if (tvInput.getText() != null) {
                inputText = tvInput.getText().toString();

                if (inputText.equals(""))
                    Toast.makeText(this, "請輸入題目！", Toast.LENGTH_SHORT).show();
                else {
                    volleyApi.postApi(DefaultSetting.URL_RACE_ANSWER, new VolleyApi.VolleyGet() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                shareData.saveRaceID(jsonObject.getString("R_id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            shareData.saveDoc(inputText);
                            tvInput.setText("");
                            Intent ii = new Intent(getApplicationContext(), RaceSecondActivity.class);
                            startActivity(ii);
                        }

                        @Override
                        public void onFailed(VolleyError error) {
                            try {
                                if (error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 500)
                                    Toast.makeText(RaceActivity.this, "無法連接伺服器，請重試。", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(RaceActivity.this, "題目不能重複！", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(RaceActivity.this, "請簽到在操作!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, () -> {
                        Map<String, String> params = new HashMap<>();
                        params.put("Race_doc", inputText);
                        params.put("Status", "true");
                        params.put("C_id",  shareData.getCourseID());
                        return params;
                    });
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.race_btn_Back);
        tvInput = findViewById(R.id.race_et_Input);
        btnEnter = findViewById(R.id.race_btn_Enter);

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);

        if (shareData.getCourseID() != null)
            Log.e(TAG, shareData.getCourseID());
    }
}