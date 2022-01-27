package tw.edu.pu.StoredData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tw.edu.pu.ActivityModel.CreateModel;

public class ShareData {
    private static final String TAG = "ShareData: ";

    private final Activity activity;

    public ShareData(Activity activity) {
        this.activity = activity;
    }

    ///////////////////////////////////////////////////////////////////////////
    // TODO: LoginActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveAccount(String account) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.ACCOUNT, account);
        editor.apply();
    }

    public String getAccount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.ACCOUNT, null);
    }

    public void savePassword(String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.PASSWORD, password);
        editor.apply();
    }

    public String getPassword() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.PASSWORD, null);
    }

    public void saveID(String id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.ID, id);
        editor.apply();
    }

    public String getID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.ID, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TODO: CreateActivity
    ///////////////////////////////////////////////////////////////////////////

    private ArrayList<CreateModel> createModels;

    public void create_saveData(ArrayList<CreateModel> createModels) {
        this.createModels = createModels;

        if (createModels.size() == 0)
            Log.e(TAG, "No Data Can Save!");

        SharedPreferences sharedPreferences = activity.getSharedPreferences(ShareVariables.CREATE_SAVE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(createModels);

        editor.putString(ShareVariables.CREATE_GET_DATA, json);
        editor.apply();
    }

    public ArrayList<CreateModel> create_getData() {
        if (activity != null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(ShareVariables.CREATE_SAVE_DATA, Context.MODE_PRIVATE);

            Gson gson = new Gson();
            String json = sharedPreferences.getString(ShareVariables.CREATE_GET_DATA, null);

            Type type = new TypeToken<ArrayList<CreateModel>>() {
            }.getType();

            createModels = gson.fromJson(json, type);

            // FIXME: Fix this shit
            if (createModels == null)
                createModels = new ArrayList<>();

        }

        return createModels;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Todo: LoginActivity
    ///////////////////////////////////////////////////////////////////////////


}
