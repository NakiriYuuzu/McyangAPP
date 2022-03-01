package tw.edu.mcyangstudentapp.Activity;

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

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.CustomViewHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.Helper.RequestHelper;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity: ";

    private boolean loginBtnChecked = true;

    TextInputEditText editText_Acc, editText_Pass;
    MaterialButton btn_SignIn;
    CheckBox btn_rememberMe;

    CustomViewHelper customViewHelper;
    RequestHelper requestHelper;
    VolleyApi volleyApi;
    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        requestHelper.flushBluetooth();

        if (requestHelper.checkInternet_Enabled()) {
            initButton();
            autoLogin();
            customViewHelper.setupUI(findViewById(R.id.activity_main_view));

        } else {
            Toast.makeText(getApplicationContext(), R.string.tag_NoInternet, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(this::finish, 3000);
        }
    }

    private void autoLogin() {
        try {
            if (shareData.getLoginAccount() != null && shareData.getLoginPassword() != null) {
                volleyApi.getApi(DefaultSetting.URL_LOGIN + shareData.getLoginAccount(), new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            byte[] text = result.getBytes(StandardCharsets.ISO_8859_1);
                            result = new String(text);
                            Log.e(TAG, result);
                            JSONObject jsonObject = new JSONObject(result);
                            String student_ID = jsonObject.getString("S_id");
                            String api_Password = jsonObject.getString("S_Password");
                            shareData.saveStudentName(jsonObject.getString("S_Name") + "同學");

                            if (shareData.getLoginPassword().equals(api_Password)) {
                                Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                                ii.putExtra("check", true);
                                ii.putExtra("studentID", student_ID);
                                startActivity(ii);

                            } else
                                Log.e(TAG, "AutoLogin: failed.");

                        } catch (JSONException e) {
                            Log.e("autoLogin", "onSuccess");
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
            }

        } catch (Exception e) {
            Log.e("autoLogin", "Problem");
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
                            String student_ID = jsonObject.getString("S_id");
                            String api_Password = jsonObject.getString("S_Password");
                            shareData.saveStudentName(jsonObject.getString("S_Name") + "同學");

                            if (pass.equals(api_Password)) {
                                if (btn_rememberMe.isChecked()) {
                                    shareData.saveLoginAccount(id);
                                    shareData.saveLoginPassword(pass);
                                }

                                Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                                ii.putExtra("check", true);
                                ii.putExtra("studentID", student_ID);
                                startActivity(ii);

                            } else {
                                Toast.makeText(getApplicationContext(), "密碼錯誤，請重新輸入密碼！", Toast.LENGTH_SHORT).show();
                                editText_Pass.setText("");
                            }

                        } catch (JSONException e) {
                            Log.e("loginFunction", "onSuccess");
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Log.e("loginFunction", "onFailed");
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
            }
        });
    }

    private void initView() {
        editText_Acc = findViewById(R.id.login_input_acc);
        editText_Pass = findViewById(R.id.login_input_pass);
        btn_SignIn = findViewById(R.id.login_btn_signIn);
        btn_rememberMe = findViewById(R.id.login_checkBox_rememberMe);

        customViewHelper = new CustomViewHelper(this);
        requestHelper = new RequestHelper(this);
        volleyApi = new VolleyApi(this);
        shareData = new ShareData(this);
    }

}