package tw.edu.mcyangstudentapp.Activity.LearningRecord;

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

import tw.edu.mcyangstudentapp.Activity.MainActivity;
import tw.edu.mcyangstudentapp.ActivityModel.LearningModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RecycleAdapter.LearningAdapter;
import tw.edu.mcyangstudentapp.StoredData.ShareData;
import tw.edu.mcyangstudentapp.ViewModel.LearningViewModel;

public class LearningRecord2Activity extends AppCompatActivity {

    private static final String TAG = "LearningRecord2: ";

    ArrayList<LearningModel> learningModels;

    ShapeableImageView btnBack;
    MaterialButton btnBackToMenu;
    MaterialTextView tv_notFound;
    RecyclerView recyclerView;

    ShareData shareData;
    VolleyApi volleyApi;
    LearningAdapter learningAdapter;
    LearningViewModel learningViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_record2);

        initView();
        initButton();
        initDomJudgeData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        learningAdapter = new LearningAdapter(this, learningModels);
        recyclerView.setAdapter(learningAdapter);
    }

    private void initDomJudgeData() {
        volleyApi.getDomJudgeAPI(DefaultSetting.DOM_JUDGE_SCOREBOARD + shareData.getDomJudgeCourseID() + "/scoreboard", new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject getRow = jsonArray.getJSONObject(i);
                        String teamID = getRow.getString("team_id");

                        if (teamID.equals(shareData.getDomJudgeStudentID())) {
                            JSONArray problems = getRow.getJSONArray("problems");
                            Log.e(TAG, "problems_Length: " + problems.length() + " | problems: " + problems);

                            for (int j = 0; j < problems.length(); j++) {
                                JSONObject getProblems = problems.getJSONObject(j);
                                String label = getProblems.getString("label");
                                String problem_id = getProblems.getString("problem_id");
                                String num_judged = getProblems.getString("num_judged");
                                String solved = getProblems.getString("solved");

                                if (num_judged.equals("0"))
                                    solved = "未答題";
                                else
                                    if (solved.equals("true"))
                                        solved = "正確";
                                    else
                                        solved = "不正確";

                                Log.e(TAG, "label: " + label + " | problem_id: " + problem_id + " | num_judged: " + num_judged + " | solved: " + solved);
                                learningModels.add(new LearningModel(label, problem_id, num_judged, solved));
                            }

                            notFound();
                            syncViewModel();
                            return;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(LearningRecord2Activity.this, "Failed to connect DomJudge.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notFound() {
        if (learningModels.size() > 0)
            tv_notFound.setVisibility(View.GONE);
        else
            tv_notFound.setVisibility(View.VISIBLE);
    }

    private void syncViewModel() {
        learningViewModel = new ViewModelProvider(this).get(LearningViewModel.class);
        learningViewModel.getLearningListObserver().observe(this, learningList -> {
            if (learningList != null)
                learningAdapter.updateLearningAdapter(learningModels);
        });

        learningViewModel.setLearningList(learningModels);
    }

    private void initButton() {
        btnBackToMenu.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.learningRecord2_btn_Back);
        btnBackToMenu = findViewById(R.id.learningRecord2_btn_Enter);
        tv_notFound = findViewById(R.id.learningRecord2_textView_NotFound);
        recyclerView = findViewById(R.id.learningRecord2_recyclerview);

        learningModels = new ArrayList<>();

        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
        Log.e(TAG, "DomJudgeStudent: " + shareData.getDomJudgeStudentID() + " | DomJudgeCourse:" + shareData.getDomJudgeCourseID());
    }
}