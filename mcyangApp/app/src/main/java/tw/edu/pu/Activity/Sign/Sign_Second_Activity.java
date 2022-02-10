package tw.edu.pu.Activity.Sign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;
import java.util.Map;

import tw.edu.pu.Activity.MainActivity;
import tw.edu.pu.ActivityModel.SignModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.BeaconModel.BeaconController;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.RecyclerAdapter.SignAdapter;
import tw.edu.pu.StoredData.ShareData;
import tw.edu.pu.ViewModel.SignViewModel;

public class Sign_Second_Activity extends AppCompatActivity {

    public static final String TAG = "SignSecondActivity: ";

    boolean checkedBeacon = false;
    String signID;

    ArrayList<SignModel> signList;
    ArrayList<String> studentID;

    ShapeableImageView btnBack;
    MaterialTextView btnOpen, btnClose, tvNotFound;
    MaterialButton btnEnter;
    RecyclerView recyclerView;

    BeaconController beaconController;
    SignViewModel signViewModel;
    SignAdapter signAdapter;
    VolleyApi volleyApi;
    ShareData shareData;

    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_second);

        initView();
        signCreated();
        initButton();
        initRecyclerView();
    }

    private void initView() {
        shareData = new ShareData(this);
        Log.e(TAG, "CourseID: " + shareData.getCourseID());

        volleyApi = new VolleyApi(this);

        beaconController = new BeaconController(this);

        studentID = new ArrayList<>();
        signList = new ArrayList<>();

        tvNotFound = findViewById(R.id.signSecond_textView_NotFound);
        btnBack = findViewById(R.id.signSecond_btn_Back);
        btnOpen = findViewById(R.id.signSecond_btn_BeaconOn);
        btnClose = findViewById(R.id.signSecond_btn_BeaconOff);
        btnEnter = findViewById(R.id.signSecond_btn_Enter);
        recyclerView = findViewById(R.id.signSecond_recycleView);
    }

    private void initButton() {
        btnOpen.setOnClickListener(v -> {
            if (checkedBeacon)
                Toast.makeText(getApplicationContext(), "已開始廣播！", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(), "開始廣播", Toast.LENGTH_SHORT).show();
                beaconController.init_Sign_BroadcastBeacon();
                beaconController.start_BroadcastBeacon();
                checkedBeacon = true;

                handler.postDelayed(runnable = () -> {
                    Log.e(TAG, "run: ");
                    initData();
                    handler.postDelayed(runnable, 2000);
                }, 2000);
            }
        });

        btnClose.setOnClickListener(v -> {
            if (checkedBeacon) {
                Toast.makeText(getApplicationContext(), "關閉廣播", Toast.LENGTH_SHORT).show();
                beaconController.stop_BroadcastBeacon();
                checkedBeacon = false;

                handler.removeCallbacks(runnable);

            } else
                Toast.makeText(getApplicationContext(), "已關閉廣播！", Toast.LENGTH_SHORT).show();
        });

        btnEnter.setOnClickListener(v -> {
            Intent ii = new Intent(this, MainActivity.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean isExisted_initData(String sid) {
        boolean isExist = true;
        if (studentID.size() > 0)
            for (String s : studentID)
                if (sid.equals(s)) {
                    isExist = false;
                    break;
                }

        return isExist;
    }

    private boolean isExisted_StudentData(String name) {
        boolean isExist = true;
        if (signList.size() > 0)
            for (SignModel signModel : signList)
                if (signModel.getName().equals(name)) {
                    isExist = false;
                    break;
                }

        return isExist;
    }

    private void signCreated() {
        volleyApi.postApi(DefaultSetting.URL_SIGN, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "Success! " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    signID = jsonObject.getString("Sign_id");
                    shareData.saveMinor(signID);
                    Log.e(TAG, "signCreated: " + signID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(getApplicationContext(), "無法連接伺服器，請稍後重試。", Toast.LENGTH_SHORT).show();
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            Log.e(TAG, "signCreated: " + shareData.getMajor());
            params.put("C_id", shareData.getMajor());
            return params;
        });
    }

    private void initData() {
        volleyApi.getApi(DefaultSetting.URL_SIGN_RECORD + signID, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String str = jsonObject.getString("S_id");

                        Log.e(TAG, "str = " + str + " ; isExisted = " + isExisted_initData(str));

                        if (isExisted_initData(str))
                            studentID.add(str);

                        Log.e(TAG, "studentID: " + studentID.size() + " | " + studentID);
                    }

                    getStudentData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(getApplicationContext(), "無法連接伺服器，請稍後重試。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getStudentData() {
        for (String s : studentID) {
            volleyApi.getApi(DefaultSetting.URL_STUDENT + s, new VolleyApi.VolleyGet() {
                @Override
                public void onSuccess(String result) {
                    try {
                        byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                        result = new String(text);
                        JSONObject jsonObject = new JSONObject(result);
                        String name = jsonObject.getString("S_Name");

                        if (isExisted_StudentData(name))
                            signList.add(new SignModel(name, "已簽到"));

                        Log.e(TAG, "signList: " + signList.size() + " signListData: " + signList);
                        initViewModel();
                        notFound();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "無法連接伺服器，請稍後重試。", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //signAdapter.notifyDataSetChanged();
    }

    private void initViewModel() {
        signViewModel = new ViewModelProvider(this).get(SignViewModel.class);
        signViewModel.getSignListObserver().observe(this, signModels -> {
            if (signModels != null) {
                signAdapter.updateSignAdapter(signModels);
            }
        });
        signViewModel.setSignList(signList);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        signAdapter = new SignAdapter(signList);
        recyclerView.setAdapter(signAdapter);
    }

    private void notFound() {
        if (signList.size() > 0)
            tvNotFound.setVisibility(View.GONE);
        else
            tvNotFound.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkedBeacon)
            beaconController.stop_BroadcastBeacon();

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (checkedBeacon)
            beaconController.stop_BroadcastBeacon();

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkedBeacon)
            beaconController.stop_BroadcastBeacon();

        handler.removeCallbacks(runnable);
    }
}