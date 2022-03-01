package tw.edu.pu.Activity.Race;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.edu.pu.Activity.MainActivity;
import tw.edu.pu.ActivityModel.RaceModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.RecyclerAdapter.RaceAdapter;
import tw.edu.pu.Helper.RepeatHelper;
import tw.edu.pu.StoredData.ShareData;
import tw.edu.pu.ViewModel.RaceViewModel;

public class RaceSecondActivity extends AppCompatActivity {

    private static final String TAG = "RaceSecondActivity: ";
    boolean checkedBeacon = false;
    String current = "";
    int count = 1;

    ArrayList<RaceModel> raceModels;
    ArrayList<String> studentID;
    ArrayList<String> raceListID;

    ShapeableImageView btnBack;
    MaterialCardView btnBeacon;
    MaterialTextView tvNotFound, tvBeacon;
    MaterialButton btnNext, btnEnd;
    RecyclerView recyclerView;

    ShareData shareData;
    VolleyApi volleyApi;
    RaceAdapter raceAdapter;
    RaceViewModel raceViewModel;
    RepeatHelper repeatHelper;
    BeaconController beaconController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_second);

        initView();
        initButton();
        initRecycleView();
    }

    private void notFound() {
        if (raceModels.size() > 0)
            tvNotFound.setVisibility(View.GONE);
        else
            tvNotFound.setVisibility(View.VISIBLE);
    }

    private void syncViewModel() {
        raceViewModel = new ViewModelProvider(this).get(RaceViewModel.class);
        raceViewModel.getRaceObserver().observe(this, raceModels -> {
            if (raceModels != null)
                raceAdapter.updateCreateAdapter(raceModels, studentID, raceListID);
        });

        raceViewModel.setRaceList(raceModels);
    }

    private void getStudentData() {
        for (String s : studentID)
            volleyApi.getApi(DefaultSetting.URL_STUDENT + s, new VolleyApi.VolleyGet() {
                @Override
                public void onSuccess(String result) {
                    try {
                        byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                        result = new String(text);

                        JSONObject jsonObject = new JSONObject(result);
                        String name = jsonObject.getString("S_Name");

                        if (isExisted_StudentData(name)) {
                            raceModels.add(new RaceModel(String.valueOf(count), name, ""));
                            count ++;
                        }

                        notFound();
                        syncViewModel();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(VolleyError error) {
                    Toast.makeText(RaceSecondActivity.this, "無法連接伺服器，請稍後嘗試。", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void syncData() {
        volleyApi.getApi(DefaultSetting.URL_RACE_LIST_R + shareData.getRaceID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    //Log.e(TAG, "syncData: " + result);
                    Log.e(TAG, "syncData: studentID: " + studentID.size() + " | " + studentID);
                    if (result.equals("[]") || current.equals(result))
                        return;

                    Log.e(TAG, "onSuccess:" + " hello~");

                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String sid = jsonObject.getString("S_id");

                        if (isExisted_studentID(sid)) {
                            studentID.add(sid);
                            raceListID.add(jsonObject.getString("id"));
                        }
                    }

                    current = result;
                    getStudentData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(RaceSecondActivity.this, "無法連接伺服器，請稍後嘗試。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isExisted_StudentData(String name) {
        boolean isExist = true;
        if (raceModels.size() > 0)
            for (RaceModel raceModel : raceModels)
                if (raceModel.getStudentNames().equals(name)) {
                    isExist = false;
                    break;
                }

        return isExist;
    }

    private boolean isExisted_studentID(String sid) {
        boolean isExist = true;
        if (studentID.size() > 0)
            for (String s : studentID)
                if (sid.equals(s)) {
                    isExist = false;
                    break;
                }

        return isExist;
    }

    private void endApi() {
        volleyApi.putApi(DefaultSetting.URL_RACE_ANSWER + shareData.getRaceID() + "/", new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "Stop");
            }

            @Override
            public void onFailed(VolleyError error) {
                Log.e(TAG, "onFailed: unable to stop!");
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            params.put("Race_doc", shareData.getDoc());
            params.put("Status", "false");
            params.put("C_id", shareData.getCourseID());
            return params;
        });
    }

    private void initRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        raceAdapter = new RaceAdapter(this, raceModels, studentID, raceListID);
        recyclerView.setAdapter(raceAdapter);
    }

    private void initButton() {
        btnBeacon.setOnClickListener(v -> {
            if (checkedBeacon) {
                checkedBeacon = false;
                beaconController.stop_BroadcastBeacon();
                repeatHelper.stop();
                tvBeacon.setText("開放搶答");
                btnBeacon.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                Toast.makeText(getApplicationContext(), "關閉廣播", Toast.LENGTH_SHORT).show();

            } else {
                checkedBeacon = true;
                beaconController.init_Race_BroadcastBeacon();
                beaconController.start_BroadcastBeacon();

                if (shareData.getRaceID() != null)
                    repeatHelper.start(2000);

                tvBeacon.setText("停止搶答");
                btnBeacon.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
                Toast.makeText(getApplicationContext(), "開始廣播", Toast.LENGTH_SHORT).show();
            }
        });

        btnNext.setOnClickListener(v -> {
            endApi();
            finish();
        });

        btnEnd.setOnClickListener(v -> {
            endApi();
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> {
            endApi();
            finish();
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.raceSecond_btn_Back);
        btnNext = findViewById(R.id.raceSecond_btn_Next);
        btnEnd = findViewById(R.id.raceSecond_btn_Enter);
        tvNotFound = findViewById(R.id.raceSecond_textView_NotFound);
        recyclerView = findViewById(R.id.raceSecond_recycleView);
        btnBeacon = findViewById(R.id.raceSecond_btn_Beacon);
        tvBeacon = findViewById(R.id.raceSecond_tv_Beacon);

        beaconController = new BeaconController(this);

        studentID = new ArrayList<>();
        raceModels = new ArrayList<>();
        raceListID = new ArrayList<>();

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
        repeatHelper = new RepeatHelper(this::syncData);

        Log.e(TAG, "getRaceID(): " + shareData.getRaceID());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkedBeacon)
            beaconController.stop_BroadcastBeacon();

        repeatHelper.stop();
        endApi();
    }
}