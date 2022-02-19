package tw.edu.pu.Activity.Answer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;

public class AnswerActivity extends AppCompatActivity {

    private static final String TAG = "AnswerActivity: ";
    boolean isChecked = false;

    ArrayAdapter<String> topicArrayAdapter, questionArrayAdapter;
    ArrayList<String> topic, question;

    MaterialButton btn_Send;
    ShapeableImageView btn_back;
    AutoCompleteTextView autoCompleteTextView_Topic, autoCompleteTextView_Question;

    VolleyApi volleyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initView();
        initButton();
        syncData();
    }

    private void syncData() {
        volleyApi.getApi(DefaultSetting.URL_ANSWER_TOPIC, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result.getBytes(StandardCharsets.ISO_8859_1));
                    JSONObject jsonObject;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        topic.add(jsonObject.getString("QA_doc"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(getApplicationContext(), "連接不上伺服器，無法更新資料。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButton() {
        btn_Send.setOnClickListener(v -> {
            if (isChecked)
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "請選擇", Toast.LENGTH_SHORT).show();
        });

        btn_back.setOnClickListener(v -> finish());
    }

    private void initView() {
        autoCompleteTextView_Topic = findViewById(R.id.answer_AutoCompleteText_Bank);
        autoCompleteTextView_Question = findViewById(R.id.answer_AutoCompleteText_Question);
        btn_back = findViewById(R.id.answer_btn_Back);
        btn_Send = findViewById(R.id.answer_btn_Enter);

        topic = new ArrayList<>();

        volleyApi = new VolleyApi(this);
    }
}