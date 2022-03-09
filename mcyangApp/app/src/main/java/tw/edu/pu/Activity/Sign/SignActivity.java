package tw.edu.pu.Activity.Sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import tw.edu.pu.Helper.CustomViewHelper;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class SignActivity extends AppCompatActivity {

    private static final String TAG = "SignActivity: ";
    boolean isClicked = false;
    String id, name;

    ArrayList<String> coursesID;
    ArrayList<String> coursesName;

    AutoCompleteTextView autoCompleteTextView;
    ShapeableImageView btn_Back;
    MaterialButton btn_Enter;

    ArrayAdapter<String> arrayAdapter;

    ShareData shareData;
    VolleyApi volleyApi;
    CustomViewHelper viewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        initButton();
        checkSignID();
    }

    private void checkSignID() {
        if (shareData.getCourseID() != null) {
            viewHelper.showAlertBuilder("繼續點名", "請問要開放補點給同學？", new CustomViewHelper.AlertListener() {
                @Override
                public void onPositive(DialogInterface dialogInterface, int i) {
                    shareData.saveMajor(shareData.getCourseID());
                    Intent ii = new Intent(getApplicationContext(), Sign_Second_Activity.class);
                    ii.putExtra("isResume", true);
                    startActivity(ii);
                    dialogInterface.dismiss();
                }

                @Override
                public void onNegative(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    }

    private void initButton() {
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), " " + item, Toast.LENGTH_SHORT).show();

            id = coursesID.get(i);
            name = coursesName.get(i);
            isClicked = true;
            Log.e(TAG, "selected: " + id + ", " + name);
        });

        btn_Enter.setOnClickListener(view -> {
            if (isClicked) {
                isClicked = false;
                shareData.saveMajor(id);
                shareData.saveCourseID(id);
                Intent ii = new Intent(getApplicationContext(), Sign_Second_Activity.class);
                startActivity(ii);
            }
        });

        btn_Back.setOnClickListener(view -> finish());
    }

    private void initData() {
        coursesName = new ArrayList<>();
        coursesID = new ArrayList<>();

        volleyApi.getApi(DefaultSetting.URL_COURSE + shareData.getID(), new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        coursesID.add(jsonObject.getString("C_id"));
                        coursesName.add(jsonObject.getString("C_Name"));
                    }

                    Log.e(TAG, coursesID.toString() + " " + coursesName.toString());

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



    private void initView() {
        shareData = new ShareData(this);
        volleyApi = new VolleyApi(this);
        viewHelper = new CustomViewHelper(this);

        initData();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, coursesName);

        autoCompleteTextView = findViewById(R.id.sign_AutoCompleteText);
        btn_Back = findViewById(R.id.race_btn_Back);
        btn_Enter = findViewById(R.id.sign_btn_Enter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}