package tw.edu.pu.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.Helper.RequestHelper;
import tw.edu.pu.StoredData.ShareData;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity: ";

    private boolean loginBtnChecked = true;

    TextInputEditText editText_Acc, editText_Pass;
    MaterialButton btn_SignIn;
    CheckBox btn_rememberMe;

    RequestHelper requestHelper;
    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
        if (requestHelper.checkInternet_Enabled()) {
            initButton();
            autoLogin();
        } else {
            Toast.makeText(getApplicationContext(), R.string.tag_NoInternet, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(this::finish, 3000);
        }
    }

    private void initData() {
        shareData.saveCourseID(null);
        shareData.saveMajor(null);
        shareData.saveMinor(null);
        shareData.saveID(null);
    }

    private void autoLogin() {
        try {
            if (shareData.getAccount() != null && shareData.getPassword() != null) {
                volleyApi.getApi(DefaultSetting.URL_LOGIN + shareData.getAccount(), new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                            result = new String(text);
                            Log.e(TAG, result);
                            JSONObject jsonObject = new JSONObject(result);
                            String api_Password = jsonObject.getString("T_Password");

                            if (shareData.getPassword().equals(api_Password)) {
                                Log.e(TAG, jsonObject.getString("T_id"));
                                shareData.saveID(jsonObject.getString("T_id"));
                                Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(ii);

                            } else {
                                Toast.makeText(getApplicationContext(), "Failed login...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void loginFunction() {
        String id, pass;

        if (editText_Acc.getText() != null && editText_Pass.getText() != null) {
            id = editText_Acc.getText().toString();
            pass = editText_Pass.getText().toString();

            if (id.equals("") || pass.equals(""))
                Toast.makeText(this, R.string.tag_NoInput, Toast.LENGTH_SHORT).show();
            else {
                volleyApi.getApi(DefaultSetting.URL_LOGIN + id, new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                            result = new String(text);
                            Log.e(TAG, result);
                            JSONObject jsonObject = new JSONObject(result);
                            String api_Password = jsonObject.getString("T_Password");

                            if (pass.equals(api_Password)) {
                                if (btn_rememberMe.isChecked()) {
                                    shareData.saveAccount(id);
                                    shareData.savePassword(pass);
                                }

                                Log.e(TAG, jsonObject.getString("T_id"));
                                shareData.saveID(jsonObject.getString("T_id"));

                                Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(ii);

                            } else {
                                Toast.makeText(getApplicationContext(), "密碼錯誤，請重新輸入密碼！", Toast.LENGTH_SHORT).show();
                                editText_Pass.setText("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(), "查無此賬號，請重新輸入！", Toast.LENGTH_SHORT).show();
                        editText_Acc.setText("");
                        editText_Pass.setText("");
                    }
                });
            }
        }

        loginBtnChecked = true;
        btn_SignIn.setEnabled(true);
    }

    private void initButton() {
        btn_SignIn.setOnClickListener(v -> {
            if (loginBtnChecked) {
                loginBtnChecked = false;
                btn_SignIn.setEnabled(false);
                loginFunction();

            } else
                Toast.makeText(this, R.string.tag_Login, Toast.LENGTH_SHORT).show();
        });
    }

    private void initView() {
        editText_Acc = findViewById(R.id.login_input_acc);
        editText_Pass = findViewById(R.id.login_input_pass);
        btn_SignIn = findViewById(R.id.login_btn_signIn);
        btn_rememberMe = findViewById(R.id.login_checkBox_rememberMe);

        requestHelper = new RequestHelper(this);
        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
    }
}