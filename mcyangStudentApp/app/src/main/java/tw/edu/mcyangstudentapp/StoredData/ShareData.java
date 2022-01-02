package tw.edu.mcyangstudentapp.StoredData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SignModel;

public class ShareData {
    private static final String TAG = "ShareData: ";

    private final Activity activity;

    private ArrayList<SignModel> signModels;

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

    ///////////////////////////////////////////////////////////////////////////
    // TODO: SignActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveMajor(String major) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.MAJOR, major);
        editor.apply();
    }

    public String getMajor() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.MAJOR, null);
    }

    public void saveMinor(String minor) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.MINOR, minor);
        editor.apply();
    }

    public String getMinor() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.MINOR, null);
    }

    public void saveStudentName(String studentNames) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.STUDENT_NAME, studentNames);
        editor.apply();
    }

    public String getStudentNames() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.STUDENT_NAME, null);
    }

    public void saveStudentID(String studentID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.STUDENT_ID, studentID);
        editor.apply();
    }

    public String getStudentID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.STUDENT_ID, null);
    }

    public void sign_saveData(ArrayList<SignModel> signModels) {
        this.signModels = signModels;

        if (signModels.size() == 0)
            Log.e(TAG, "No Data Can Save!");

        SharedPreferences sharedPreferences = activity.getSharedPreferences(ShareVariables.SIGN_SAVE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(signModels);

        editor.putString(ShareVariables.SIGN_GET_DATA, json);
        editor.apply();
    }

    public ArrayList<SignModel> sign_getData() {
        if (activity != null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(ShareVariables.SIGN_SAVE_DATA, Context.MODE_PRIVATE);

            Gson gson = new Gson();
            String json = sharedPreferences.getString(ShareVariables.SIGN_GET_DATA, null);

            Type type = new TypeToken<ArrayList<SignModel>>() {
            }.getType();

            signModels = gson.fromJson(json, type);


            if (signModels == null)
                signModels = new ArrayList<>();
            else
                Log.e(TAG, signModels.get(0).getMajor());
        }

        return signModels;
    }
}
