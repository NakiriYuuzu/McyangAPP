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

    public void save_TeamID(String team_ID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.TEAM_ID, team_ID);
        editor.apply();
    }

    public String get_TeamID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.TEAM_ID, null);
    }

    public void saveTeam_ID_Array(ArrayList<SubMemberModel> teamID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(teamID);

        editor.putString(ShareVariables.TEAM_ID_2, json);
        editor.apply();
    }

    public ArrayList<SubMemberModel> getTeam_ID_Array() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        ArrayList<SubMemberModel> teamID;
        String json = preferences.getString(ShareVariables.TEAM_ID_2, null);
        Type type = new TypeToken<ArrayList<SubMemberModel>>() {}.getType();

        teamID = gson.fromJson(json, type);

        return teamID;
    }

    ///////////////////////////////////////////////////////////////////////////
    //  TODO: GroupView/Room and Chat Activity
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

    public void saveChatRoom_Name(String roomNames) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ShareVariables.CHATROOM_NAME, roomNames);
        editor.apply();
    }

    public String getChatRoom_Name() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString(ShareVariables.CHATROOM_NAME, null);
    }

    public void saveChat_Count(int count) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ShareVariables.CHAT_COUNT, count);
        editor.apply();
    }

    public int getChat_Count() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getInt(ShareVariables.CHAT_COUNT, 0);
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
        saveChat_Count(0);
    }
}
