package tw.edu.mcyangstudentapp.StoredData;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShareData {

    private final Activity activity;

    public ShareData(Activity activity) {
        this.activity = activity;
    }

    // TODO: AutoLogin
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

    // TODO:
}
