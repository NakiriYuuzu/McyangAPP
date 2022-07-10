package tw.edu.pu.Activity.Answer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class AnswerActivity extends AppCompatActivity {

    private static final String TAG = "AnswerActivity: ";
    boolean isSelected_Topic = false;
    boolean isSelected_Question = false;
    private String showQuestion;

    ArrayAdapter<String> topicArrayAdapter, questionArrayAdapter;
    ArrayList<String> topic, question, topicID, questionID, questionFullText;

    MaterialButton btn_Send;
    ShapeableImageView btn_back;
    AutoCompleteTextView autoCompleteTextView_Topic, autoCompleteTextView_Question;

    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initView();
        syncData();
        initButton();
    }

    private void syncData() {
        volleyApi.getApi(DefaultSetting.URL_ANSWER_TOPIC + shareData.getCourseID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String doc = jsonObject.getString("QA_doc");
                        if (doc.length() > 16) {
                            char[] chr = doc.toCharArray();
                            StringBuilder miniDoc = new StringBuilder();

                            for (int j = 0; j < 16; j++)
                                miniDoc.append(chr[j]);

                            topic.add(miniDoc.toString());

                        } else {
                            topic.add(doc);
                        }

                        topicID.add(jsonObject.getString("QA_id"));
                    }

                } catch (JSONException e) {
                    Toast.makeText(AnswerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(getApplicationContext(), "連接不上伺服器，無法更新資料。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initButton() {
        //init ArrayAdapter
        topicArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, topic);
        autoCompleteTextView_Topic.setAdapter(topicArrayAdapter);
        autoCompleteTextView_Topic.setOnItemClickListener((adapterView, view, i, l) -> {
            isSelected_Topic = true;
            String topic_ID = topicID.get(i);
            getQuestionData(topic_ID);
        });

        autoCompleteTextView_Question.setOnClickListener(v -> {
            if (isSelected_Topic) {
                autoCompleteTextView_Question.setOnItemClickListener((adapterView, view, i, l) -> {
                    isSelected_Question = true;
                    showQuestion = questionFullText.get(i);
                    String question_ID = questionID.get(i);
                    shareData.saveQuestion_ID(question_ID);
                });
            } else
                Toast.makeText(this, "請選擇題庫之後在選擇題目！", Toast.LENGTH_SHORT).show();
        });


        btn_Send.setOnClickListener(v -> {
            if (isSelected_Question) {
                new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setBackground(getResources().getDrawable(R.drawable.rounded_corner))
                        .setTitle("已選擇以下題目")
                        .setMessage(showQuestion)
                        .setPositiveButton("確認", (dialogInterface, i) -> {
                            Intent ii = new Intent(getApplicationContext(), AnswerSecondActivity.class);
                            startActivity(ii);
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();

            } else
                Toast.makeText(this, "請選擇題目！", Toast.LENGTH_SHORT).show();
        });

        btn_back.setOnClickListener(v -> finish());
    }

    private void getQuestionData(String topicID) {
        question = new ArrayList<>();
        questionID = new ArrayList<>();
        questionFullText = new ArrayList<>();

        volleyApi.getApi(DefaultSetting.URL_ANSWER_QUESTION + topicID, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                    Log.e(TAG, "getQuestionData: " + jsonArray);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String doc = jsonObject.getString("Q_doc");
                        StringBuilder miniDoc = new StringBuilder();
                        if (doc.length() > 16) {
                            char[] txt = doc.toCharArray();

                            for (int j = 0; j < 16; j++)
                                miniDoc.append(txt[j]);

                            question.add(miniDoc.toString());
                        } else
                            question.add(doc);

                        questionFullText.add(doc);
                        questionID.add(jsonObject.getString("Q_id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(AnswerActivity.this, "Unable connect to server...", Toast.LENGTH_SHORT).show();
            }
        });

        questionArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, question);
        autoCompleteTextView_Question.setAdapter(questionArrayAdapter);
    }

    private void initView() {
        autoCompleteTextView_Topic = findViewById(R.id.answer_AutoCompleteText_Bank);
        autoCompleteTextView_Question = findViewById(R.id.answer_AutoCompleteText_Question);
        btn_back = findViewById(R.id.answer_btn_Back);
        btn_Send = findViewById(R.id.answer_btn_Enter);

        topic = new ArrayList<>();
        topicID = new ArrayList<>();

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
    }
}