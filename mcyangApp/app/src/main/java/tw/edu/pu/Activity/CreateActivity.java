package tw.edu.pu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.edu.pu.ActivityModel.CreateModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.RecyclerAdapter.CreateAdapter;
import tw.edu.pu.StoredData.ShareData;
import tw.edu.pu.ViewModel.CreateViewModel;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity: ";

    ArrayList<CreateModel> createList;

    ShapeableImageView btnBack;
    MaterialButton btnEnter;
    MaterialTextView tvNotFound;
    RecyclerView recyclerView;

    ShareData shareData;
    VolleyApi volleyApi;
    CreateAdapter createAdapter;
    CreateViewModel createViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initView();
        notFound();
        syncData();
        initButton();
        initViewModel();
        initRecyclerView();
    }

    private void bottomSheet() {
        Log.e(TAG, "CourseData: " + createList.size());
        final BottomSheetDialog bsd = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        shareData = new ShareData(this);

        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet, null);

        TextInputEditText courseNames = v.findViewById(R.id.bottom_sheet_input_Names);

        v.findViewById(R.id.bottom_sheet_btn_Send).setOnClickListener(view -> {
            String names;

            if (courseNames.getText() != null) {
                names = courseNames.getText().toString();

                if (names.equals(""))
                    Toast.makeText(this, "請輸入課程名稱！", Toast.LENGTH_SHORT).show();

                else {
                    int id = 1000;
                    String strID;
                    if (createList.size() != 0) {
                        strID = createList.get(createList.size() - 1).getClassID();
                        id = Integer.parseInt(strID) + 1;

                    } else
                        id += 1;

                    strID = String.valueOf(id);
                    createList.add(new CreateModel(strID, names));
                    shareData.create_saveData(createList);
                    createViewModel.setCreateList(shareData.create_getData());
                    bsd.dismiss();
                }
            }
        });

        bsd.setContentView(v);
        bsd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bsd.show();
    }

    private void syncData() {
        volleyApi = new VolleyApi(this);
        volleyApi.getApi(DefaultSetting.URL_COURSE + shareData.getID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                List<String> course_ID = new ArrayList<>();
                List<String> course_Name = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        course_ID.add(jsonObject.getString("C_id"));
                        course_Name.add(jsonObject.getString("C_Name"));
                    }

                    Log.e(TAG, course_ID.toString());
                    Log.e(TAG, course_Name.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        });
    }

    private void notFound() {
        if (createList.size() == 0)
            tvNotFound.setVisibility(View.VISIBLE);
        else
            tvNotFound.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        createAdapter = new CreateAdapter(this, createList);
        recyclerView.setAdapter(createAdapter);
    }

    private void initViewModel() {
        createViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        createViewModel.getCreateListObserver().observe(this, createModels -> {
            if (createModels != null) {
                createAdapter.updateCreateAdapter(createModels);
                notFound();
            }
        });
    }

    private void initButton() {
        btnEnter.setOnClickListener(v -> bottomSheet());

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.race_btn_Back);
        btnEnter = findViewById(R.id.raceSecond_btn_Enter);
        tvNotFound = findViewById(R.id.raceSecond_textView_NoFound);
        recyclerView = findViewById(R.id.raceSecond_recycleView);

        shareData = new ShareData(this);
        createList = shareData.create_getData();
    }
}