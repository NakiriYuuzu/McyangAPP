package tw.edu.mcyangstudentapp.Activity.LearningRecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.CustomViewHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class LearningRecordActivity extends AppCompatActivity {

    private static final String TAG = "LearningRecord: ";
    private boolean isExistStudentID = false, isSelected = false, isVerification = false;
    private ArrayList<String> topic;
    private ArrayList<String> topicID;

    ArrayAdapter<String> arrayAdapter;

    ShapeableImageView btnBack;
    TextInputEditText et_StudentID;
    AutoCompleteTextView autoText_Course;
    MaterialButton btn_Enter;

    ShareData shareData;
    VolleyApi volleyApi;
    CustomViewHelper viewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_record);

        initView();
        initButton();
        viewHelper.setupUI(findViewById(R.id.activity_learning));
    }

    private void initAutoTextData() {
        volleyApi.getDomJudgeAPI(DefaultSetting.URL_DOM_JUDGE + DefaultSetting.DOM_JUDGE_CONTESTS, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("id");
                        topic.add(name);
                        topicID.add(id);
                    }

                    Log.e(TAG, "topic" + topic.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(LearningRecordActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, topic);
    }

    private void recoverData() {
        if (shareData.getDomJudgeStudentID() != null)
            et_StudentID.setText(shareData.getDomJudgeStudentID());
    }

    private void initButton() {
        autoText_Course.setAdapter(arrayAdapter);
        autoText_Course.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            shareData.saveDomJudgeCourseID(topicID.get(i));
            isSelected = true;
            Toast.makeText(this, "選擇：" + item, Toast.LENGTH_SHORT).show();
        });

        btn_Enter.setOnClickListener(v -> {
            if (!isVerification) {
                if (et_StudentID.getText() != null)
                    if (!et_StudentID.getText().toString().equals("")) {
                        String id = et_StudentID.getText().toString();
                        if (id.length() < 11)
                            checkStudentID(id);
                        else
                            Toast.makeText(this, "學號過長！", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(this, "請輸入學號！", Toast.LENGTH_SHORT).show();
            } else {
                if (isExistStudentID && isSelected) {
                    Intent ii = new Intent(this, LearningRecord2Activity.class);
                    startActivity(ii);
                } else {
                    if (!isExistStudentID)
                        Toast.makeText(this, "學號還未驗證, 請在發送多一次！", Toast.LENGTH_SHORT).show();
                    if (!isSelected)
                        Toast.makeText(this, "請選擇題庫！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void checkStudentID(String studentID) {
        volleyApi.getDomJudgeAPI(DefaultSetting.URL_DOM_JUDGE_USERS, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userNames = jsonObject.getString("username");

                        if (studentID.equals(userNames)) {
                            isVerification = true;
                            isExistStudentID = true;
                            btn_Enter.setText(R.string.learningRecord_btn_Send);
                            shareData.saveDomJudgeStudentID(userNames);
                            Log.e(TAG, "Input: " + studentID + " | API: " + userNames);
                            Toast.makeText(LearningRecordActivity.this, "學號驗證成功！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    Toast.makeText(LearningRecordActivity.this, "查無此學號！", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                try {
                    if (error.networkResponse.statusCode == 401)
                        Toast.makeText(LearningRecordActivity.this, "授權失敗。。。", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.learningRecord_btn_Back);
        et_StudentID = findViewById(R.id.learningRecord_studentID);
        autoText_Course = findViewById(R.id.learningRecord_Course);
        btn_Enter = findViewById(R.id.learningRecord_btn_Enter);

        topic = new ArrayList<>();
        topicID = new ArrayList<>();

        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
        viewHelper = new CustomViewHelper(this);

        initAutoTextData();
        recoverData();
    }
}