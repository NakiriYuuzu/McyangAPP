package tw.edu.pu.Activity.Answer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import tw.edu.pu.Activity.MainActivity;
import tw.edu.pu.ActivityModel.AnswerModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.Helper.RepeatHelper;
import tw.edu.pu.R;
import tw.edu.pu.RecyclerAdapter.AnswerAdapter;
import tw.edu.pu.StoredData.ShareData;
import tw.edu.pu.ViewModel.AnswerViewModel;

public class AnswerSecondActivity extends AppCompatActivity {

    private static final String TAG = "AnswerSecond: ";
    boolean isActive = false;

    ArrayList<AnswerModel> answerModels;

    ShapeableImageView btnBack;
    MaterialTextView btnStart, btnEnd, tv_NotFound;
    MaterialButton btnFinish, btnNext;
    RecyclerView recyclerView;

    ShareData shareData;
    VolleyApi volleyApi;
    RepeatHelper repeatHelper;
    AnswerAdapter answerAdapter;
    AnswerViewModel answerViewModel;
    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_answer);

        initView();
        initButton();
        initRecyclerView();
    }

    private boolean isExisted_answerData(String id) {
        boolean isExist = true;
        if (answerModels.size() > 0)
            for (AnswerModel answerModel : answerModels)
                if (answerModel.getAnswer_ID().equals(id)) {
                    isExist = false;
                    break;
                }
        return isExist;
    }

    private void notFound() {
        if (answerModels.size() > 0)
            tv_NotFound.setVisibility(View.GONE);
        else
            tv_NotFound.setVisibility(View.VISIBLE);
    }

    private void initButton() {
        btnStart.setOnClickListener(v -> {
            if (!isActive) {
                Toast.makeText(this, "開始廣播", Toast.LENGTH_SHORT).show();
                beaconController.start_BroadcastBeacon();
                repeatHelper.start(2000);
                isActive = true;

            } else {
                Toast.makeText(this, "已開始廣播！", Toast.LENGTH_SHORT).show();
            }
        });

        btnEnd.setOnClickListener(v -> {
            if (isActive) {
                Toast.makeText(this, "關閉廣播", Toast.LENGTH_SHORT).show();
                beaconController.stop_BroadcastBeacon();
                repeatHelper.stop();
                isActive = false;
            } else {
                Toast.makeText(this, "廣播已關閉！", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnFinish.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnNext.setOnClickListener(v -> {
            Intent ii = new Intent(this, AnswerSecondActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.answerSecond_btn_Back);
        btnStart = findViewById(R.id.answerSecond_btn_BeaconOn);
        btnEnd = findViewById(R.id.answerSecond_btn_BeaconOff);
        btnFinish = findViewById(R.id.answerSecond_btn_Finish);
        btnNext = findViewById(R.id.answerSecond_btn_NextPage);
        tv_NotFound = findViewById(R.id.answerSecond_textView_NotFound);
        recyclerView = findViewById(R.id.answerSecond_recyclerview);

        answerModels = new ArrayList<>();

        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
        beaconController = new BeaconController(this);
        repeatHelper = new RepeatHelper(this::syncData);
        beaconController.init_Answer_BroadcastBeacon();
    }

    private void syncData() {
        Log.e(TAG, "Syncing...");
        volleyApi.getApi(DefaultSetting.URL_ANSWER_MEMBER, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("Answer_id");
                        String doc = jsonObject.getString("Answer_doc");
                        String answer = jsonObject.getString("Answer");
                        String sid = jsonObject.getString("S_id");
                        String qid = jsonObject.getString("Q_id");

                        if (qid.equals(shareData.getQuestion_ID())) {
                            if (isExisted_answerData(id))
                                getStudentID(id, doc, answer, sid, qid);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(AnswerSecondActivity.this, "Unable connect to server...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStudentID(String id, String doc, String answer, String sid, String qid) {
        volleyApi.getApi(DefaultSetting.URL_STUDENT + sid, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);

                    JSONObject jsonObject = new JSONObject(result);
                    String names = jsonObject.getString("S_Name");
                    answerModels.add(new AnswerModel(id, doc, answer, sid, names, qid));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                notFound();
                syncViewModel();
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(AnswerSecondActivity.this, "(student data)Unable connect to server...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void syncViewModel() {
        answerViewModel = new ViewModelProvider(this).get(AnswerViewModel.class);
        answerViewModel.getAnswerListObserver().observe(this, answerList -> {
            if (answerList != null)
                answerAdapter.updateAnswerAdapter(answerList);
        });

        answerViewModel.setAnswerList(answerModels);
    }

    private void initRecyclerView() {
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         answerAdapter = new AnswerAdapter(this, answerModels);
         recyclerView.setAdapter(answerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isActive) {
            beaconController.stop_BroadcastBeacon();
            repeatHelper.stop();
        }
    }
}