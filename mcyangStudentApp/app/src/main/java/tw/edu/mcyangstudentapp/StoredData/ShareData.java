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

    public void saveLoginAccount(String account) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.LOGIN_ACCOUNT, account);
        editor.apply();
    }

    public String getLoginAccount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.LOGIN_ACCOUNT, null);
    }

    public void saveLoginPassword(String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.LOGIN_PASSWORD, password);
        editor.apply();
    }

    public String getLoginPassword() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.LOGIN_PASSWORD, null);
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

    ///////////////////////////////////////////////////////////////////////////
    // TODO: AnswerActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveAnswerID(String answerID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.ANSWER_ID, answerID);
        editor.apply();
    }

    public String getAnswerID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.ANSWER_ID, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TODO: Other
    ///////////////////////////////////////////////////////////////////////////

    public void cleanData() {
        saveMajor(null);
        saveMinor(null);
        saveStudentName(null);
        saveStudentID(null);
    }
}
