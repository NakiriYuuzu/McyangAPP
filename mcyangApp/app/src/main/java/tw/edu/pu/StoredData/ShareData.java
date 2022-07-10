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
import tw.edu.pu.ActivityModel.GroupMemberModel;
import tw.edu.pu.ActivityModel.GroupSecondModel;

public class ShareData {
    private static final String TAG = "ShareData: ";

    private final Activity activity;

    public ShareData(Activity activity) {
        this.activity = activity;
    }

    ///////////////////////////////////////////////////////////////////////////
    // fixme: BeaconController
    ///////////////////////////////////////////////////////////////////////////

    public void cleanData() {
        saveMajor(null);
        saveMinor(null);
        saveQuestion_ID(null);
        saveRaceID(null);
    }

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

    ///////////////////////////////////////////////////////////////////////////
    // TODO: LoginActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveSplashScreen(String isClicked) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.isChecked, isClicked);
        editor.apply();
    }

    public String getSplashScreen() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.isChecked, null);
    }

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

    public void saveUserNames(String userName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.USERNAME, userName);
        editor.apply();
    }

    public String getUserNames() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.USERNAME, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TODO: CreateActivity
    ///////////////////////////////////////////////////////////////////////////

    private ArrayList<CreateModel> createModels;

    public void create_saveData(ArrayList<CreateModel> createModels) {
        this.createModels = createModels;

        if (createModels.size() == 0)
            Log.e(TAG, "No Data Can Save!");

        SharedPreferences sharedPreferences = activity.getSharedPreferences(ShareVariables.CREATE_SAVE_LEFT_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(createModels);

        editor.putString(ShareVariables.CREATE_GET_RIGHT_DATA, json);
        editor.apply();
    }

    public ArrayList<CreateModel> create_getData() {
        if (activity != null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(ShareVariables.CREATE_SAVE_LEFT_DATA, Context.MODE_PRIVATE);

            Gson gson = new Gson();
            String json = sharedPreferences.getString(ShareVariables.CREATE_GET_RIGHT_DATA, null);

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
    // Todo: SignActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveCourseID(String id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.COURSE_ID, id);
        editor.apply();
    }

    public String getCourseID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.COURSE_ID, null);
    }

    public void saveSignID(String id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.SIGN_ID, id);
        editor.apply();
    }

    public String getSignID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.SIGN_ID, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Todo: RaceActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveDoc(String doc) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.RACE_DOC, doc);
        editor.apply();
    }

    public String getDoc() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.RACE_DOC, null);
    }

    public void saveRaceID(String raceID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.RACE_ID, raceID);
        editor.apply();
    }

    public String getRaceID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.RACE_ID, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Todo: AnswerActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveQuestion_ID(String questionID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.QUESTION_ID, questionID);
        editor.apply();
    }

    public String getQuestion_ID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.QUESTION_ID, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Todo: GroupActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveDesc_ID(String desc_ID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.DESC_ID, desc_ID);
        editor.apply();
    }

    public String getDesc_ID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.DESC_ID, null);
    }

    public void saveNumberOfLeader(int numberOfLeader) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ShareVariables.NUMBER_OF_LEADER, numberOfLeader);
        editor.apply();
    }

    public int getNumberOfLeader() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getInt(ShareVariables.NUMBER_OF_LEADER, 0);
    }

    public void saveNumberOfMember(int numberOfMember) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ShareVariables.NUMBER_OF_MEMBER, numberOfMember);
        editor.apply();
    }

    public int getNumberOfMember() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getInt(ShareVariables.NUMBER_OF_MEMBER, 0);
    }

    public void saveTeam_ID(ArrayList<String> teamID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(teamID);

        editor.putString(ShareVariables.TEAM_ID, json);
        editor.apply();
    }

    public ArrayList<String> getTeam_ID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        ArrayList<String> teamID;
        String json = preferences.getString(ShareVariables.TEAM_ID, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        teamID = gson.fromJson(json, type);

        return teamID;
    }

    public void saveRemoveTeam_ID(ArrayList<String> teamID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(teamID);

        editor.putString(ShareVariables.REMOVE_TEAM_ID, json);
        editor.apply();
    }

    public ArrayList<String> getRemoveTeam_ID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        ArrayList<String> teamID;
        String json = preferences.getString(ShareVariables.REMOVE_TEAM_ID, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        teamID = gson.fromJson(json, type);

        return teamID;
    }

    public void saveGroupSecond(ArrayList<GroupSecondModel> groupSeconds) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(groupSeconds);

        editor.putString(ShareVariables.GROUP_SECOND, json);
        editor.apply();
    }

    public ArrayList<GroupSecondModel> getGroupSecond() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        ArrayList<GroupSecondModel> groupSeconds;
        String json = preferences.getString(ShareVariables.GROUP_SECOND, null);
        Type type = new TypeToken<ArrayList<GroupSecondModel>>() {}.getType();

        groupSeconds = gson.fromJson(json, type);

        return groupSeconds;
    }

    public void saveGroupMember(ArrayList<GroupMemberModel> groupMemberList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(groupMemberList);

        editor.putString(ShareVariables.GROUP_MEMBER, json);
        editor.apply();
    }

    public ArrayList<GroupMemberModel> getGroupMember() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        ArrayList<GroupMemberModel> groupMemberList;
        String json = preferences.getString(ShareVariables.GROUP_MEMBER, null);
        Type type = new TypeToken<ArrayList<GroupMemberModel>>() {}.getType();

        groupMemberList = gson.fromJson(json, type);

        return groupMemberList;
    }

    // TODO: Firebase
    public void saveFirebaseToken(String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.FIREBASE_TOKEN, token);
        editor.apply();
    }

    public String getFirebaseToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.FIREBASE_TOKEN, null);
    }
}
