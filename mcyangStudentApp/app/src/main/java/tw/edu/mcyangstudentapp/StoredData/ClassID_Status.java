package tw.edu.mcyangstudentapp.StoredData;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;

public class ClassID_Status {
    public static final String TAG = "STATUS: ";

    Activity activity;
    VolleyApi volleyApi;

    Map<String, String> selections = new HashMap<>();

    public ClassID_Status(Activity activity) {
        this.activity = activity;
        volleyApi = new VolleyApi(activity);
        initClassNames();
    }

    private void initClassNames() {
        volleyApi.getApi(DefaultSetting.URL_COURSE, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                    Log.e(TAG, jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e(TAG, jsonObject.toString());
                        selections.put(jsonObject.getString("C_id"), jsonObject.getString("C_Name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(activity, "無法連結伺服器。。。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getClassNames(String major) {
        Log.e("Status: ", selections.size() + " ");
        String classNames;
        if (selections == null || selections.size() == 0)
            return "查無此課程名稱";

        classNames = selections.get(major);
        if (classNames == null || classNames.equals(""))
            return "查無此課程名稱";

        return classNames;
    }
}
