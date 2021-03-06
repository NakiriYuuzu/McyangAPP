package tw.edu.mcyangstudentapp;

public class DefaultSetting {
    // FIXME: Beacon (BeaconController Remember to change Region UUID!)
    public final static String BEACON_UUID_SIGN = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6";
    public final static String BEACON_UUID_RACE = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa5";
    public static final String BEACON_UUID_MAIN = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa4";
    public static final String BEACON_UUID_ANSWER = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa3";
    public static final String BEACON_UUID_GROUP = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa2";
    public static final String BEACON_UUID_TEAM = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa1";

    //FIXME: Url for WebView
    public static final String URL_WEB = "http://120.110.114.104/domjudge/login";

    // FIXME: Url for api
    public final static String URL_LOGIN = "http://120.110.115.128:8080/api/StudentLogin/";
    public final static String URL_STUDENT = "http://120.110.115.128:8080/api/Student/";
    public static final String URL_COURSE = "http://120.110.115.128:8080/api/CourseCreate/";
    public static final String URL_COURSE_RECORD = "http://120.110.115.128:8080/api/CourseRecord/";
    public static final String URL_SIGN_RECORD = "http://120.110.115.128:8080/api/SignRecord/";
    public static final String URL_RACE_LIST = "http://120.110.115.128:8080/api/Race_List/";
    public static final String URL_RACE_ANSWER = "http://120.110.115.128:8080/api/Race_Answer/";
    public static final String URL_ANSWER_TOPIC = "http://120.110.115.128:8080/api/QA_Topic/";
    public static final String URL_ANSWER_QUESTION = "http://120.110.115.128:8080/api/Question/";
    public static final String URL_ANSWER_MEMBER = "http://120.110.115.128:8080/api/Answer_Member/";
    public static final String URL_GROUP_TEAM = "http://120.110.115.128:8080/api/Team/";
    public static final String URL_GROUP_TEAM_ID = "http://120.110.115.128:8080/api/Team_ID/";
    public static final String URL_GROUP_TEAM_MEMBER = "http://120.110.115.128:8080/api/Team_Member/";
    public static final String URL_GROUP_TEAM_DESCRIPTION = "http://120.110.115.128:8080/api/Team_Desc/";

    //FIXME:Url for DomJudge Api
    public static final String URL_DOM_JUDGE = "http://120.110.114.104/domjudge/api/v4/";
    public static final String DOM_JUDGE_CONTESTS = "contests";
    public static final String URL_DOM_JUDGE_USERS = "http://120.110.114.104/domjudge/api/v4/users/";
    public static final String DOM_JUDGE_SCOREBOARD = "http://120.110.114.104/domjudge/api/v4/contests/";
    public static final String URL_DOM_JUDGE_PROBLEMS = "http://120.110.114.104/domjudge/team/problems/";
}
