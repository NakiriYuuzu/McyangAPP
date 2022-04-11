package tw.edu.mcyangstudentapp.StoredData;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SubMemberModel;

public class ShareData {

    private final Activity activity;

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

    ///////////////////////////////////////////////////////////////////////////
    // TODO: AnswerActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveQuestionID(String questionID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.QUESTION_ID, questionID);
        editor.apply();
    }

    public String getQuestionID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.QUESTION_ID, null);
    }

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
    // TODO: LearningRecordActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveDomJudgeStudentID(String studentID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.DOM_JUDGE_STUDENT_ID, studentID);
        editor.apply();
    }

    public String getDomJudgeStudentID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.DOM_JUDGE_STUDENT_ID, null);
    }

    public void saveDomJudgeCourseID(String CourseID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.DOM_JUDGE_COURSE_ID, CourseID);
        editor.apply();
    }

    public String getDomJudgeCourseID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.DOM_JUDGE_COURSE_ID, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TODO: GroupActivity
    ///////////////////////////////////////////////////////////////////////////

    public void saveGroup_ID(String group_ID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.GROUP_ID, group_ID);
        editor.apply();
    }

    public String getGroup_ID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.GROUP_ID, null);
    }

    public void saveTeam_ID(ArrayList<SubMemberModel> teamID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(teamID);

        editor.putString(ShareVariables.TEAM_ID, json);
        editor.apply();
    }

    public ArrayList<SubMemberModel> getTeam_ID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        ArrayList<SubMemberModel> teamID;
        String json = preferences.getString(ShareVariables.TEAM_ID, null);
        Type type = new TypeToken<ArrayList<SubMemberModel>>() {}.getType();

        teamID = gson.fromJson(json, type);

        return teamID;
    }

    ///////////////////////////////////////////////////////////////////////////
    //  TODO: GroupView and Chat Activity
    ///////////////////////////////////////////////////////////////////////////

    public void saveChat_ID(String chat_ID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.CHAT_ID, chat_ID);
        editor.apply();
    }

    public String getChat_ID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.CHAT_ID, null);
    }

    public void saveChat_Name(String chatName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.CHAT_NAME, chatName);
        editor.apply();
    }

    public String getChat_Name() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.CHAT_NAME, null);
    }

    public void saveChat_Room(String chatRoom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.CHATROOM, chatRoom);
        editor.apply();
    }

    public String getChat_Room() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.CHATROOM, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    //  TODO: Firebase
    ///////////////////////////////////////////////////////////////////////////

    public void saveToken(String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.TOKEN, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TODO: Other
    ///////////////////////////////////////////////////////////////////////////

    public void cleanData() {
        saveMajor(null);
        saveMinor(null);
        saveQuestionID(null);
        saveAnswerID(null);
    }
}
