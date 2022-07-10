package tw.edu.pu.Activity.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.Helper.CustomViewHelper;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class GroupActivity extends AppCompatActivity {

    private static final String TAG = "GroupActivity: ";
    private int selected = 0;
    boolean isSelected_CurrentGroup = false;

    ArrayAdapter<String> groupAdapter;
    ArrayList<String> groupList, groupID;

    AutoCompleteTextView autoCompleteTextView_Choose;
    ShapeableImageView btn_Back;
    TextInputEditText et_Info, et_groupCount, et_peopleCount;
    MaterialButton btn_Enter;

    CustomViewHelper viewHelper;
    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initView();
        cleanData();
        syncData();
        initButton();
        viewHelper.setupUI(findViewById(R.id.group_activity));
    }

    private void cleanData() {
        shareData.saveGroupSecond(null);
        shareData.saveTeam_ID(null);
    }

    private void syncData() {
        volleyApi.getApi(DefaultSetting.URL_GROUP_TEAM_DESCRIPTION + shareData.getMajor(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        groupList.add(jsonObject.getString("Team_doc"));
                        groupID.add(jsonObject.getString("TeamDesc_id"));
                    }
                    Log.e(TAG, groupList.toString());

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
        groupAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, groupList);
        autoCompleteTextView_Choose.setAdapter(groupAdapter);
        autoCompleteTextView_Choose.setOnItemClickListener((adapterView, view, i, l) -> {
            isSelected_CurrentGroup = true;
            selected = i;
        });

        btn_Enter.setOnClickListener(v -> {
            if (isSelected_CurrentGroup) {
                shareData.saveDesc_ID(groupID.get(selected));
                Log.e(TAG, groupID.get(selected) + " | " + shareData.getDesc_ID());

                volleyApi.api(Request.Method.GET, DefaultSetting.URL_GROUP_TEAM_DESCRIPTION, new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.getString("TeamDesc_id").equals(groupID.get(selected))) {
                                    shareData.saveNumberOfLeader(jsonObject.getInt("Group_Total"));
                                    shareData.saveNumberOfMember(jsonObject.getInt("Group_limit"));

                                    Intent ii = new Intent(getApplicationContext(), GroupSecondActivity.class);
                                    startActivity(ii);
                                    break;
                                }
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

            } else {
                if (et_Info.getText() != null && et_groupCount.getText() != null && et_peopleCount.getText() != null) {
                    if (et_Info.getText().toString().equals("") || et_groupCount.getText().toString().equals("") || et_peopleCount.getText().toString().equals(""))
                        Toast.makeText(this, "請填寫所有欄位！", Toast.LENGTH_SHORT).show();
                    else
                        uploadData();
                }
            }
        });

        btn_Back.setOnClickListener(v -> finish());
    }

    private void uploadData() {
        volleyApi.postApi(DefaultSetting.URL_GROUP_TEAM_DESCRIPTION, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                    result = new String(text);
                    JSONObject jsonObject = new JSONObject(result);
                    shareData.saveDesc_ID(jsonObject.getString("TeamDesc_id"));
                    shareData.saveNumberOfLeader(jsonObject.getInt("Group_Total"));
                    shareData.saveNumberOfMember(jsonObject.getInt("Group_limit"));

                    Intent ii = new Intent(getApplicationContext(), GroupSecondActivity.class);
                    startActivity(ii);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(getApplicationContext(), "連接不上伺服器，無法更新資料。", Toast.LENGTH_SHORT).show();
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            if (et_Info.getText() != null && et_groupCount.getText() != null && et_peopleCount.getText() != null) {
                params.put("Team_doc", et_Info.getText().toString());
                params.put("Group_Total", et_groupCount.getText().toString());
                params.put("Group_limit", et_peopleCount.getText().toString());
                params.put("C_id", shareData.getMajor());
            }
            return params;
        });
    }

    private void initView() {
        autoCompleteTextView_Choose = findViewById(R.id.group_AutoCompleteText);
        et_Info = findViewById(R.id.group_input_Description);
        et_groupCount = findViewById(R.id.group_input_groupNum);
        et_peopleCount = findViewById(R.id.group_input_people);
        btn_Enter = findViewById(R.id.group_btn_Enter);
        btn_Back = findViewById(R.id.group_btn_Back);

        groupList = new ArrayList<>();
        groupID = new ArrayList<>();

        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
        shareData.saveDesc_ID(null);
        shareData.saveNumberOfLeader(0);
        shareData.saveNumberOfMember(0);
        viewHelper = new CustomViewHelper(this);
    }
}