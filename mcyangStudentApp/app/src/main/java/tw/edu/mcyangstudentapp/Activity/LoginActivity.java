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

import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.RequestModel.RequestHelper;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity: ";

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
        if (requestHelper.checkInternet_Enabled()) {
            initButton();
            autoLogin();
        } else {
            Toast.makeText(getApplicationContext(), "偵測無網絡，請打開網絡在重試。", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(this::finish, 3000);
        }
    }

    private void autoLogin() {
        try {
            if (shareData.getAccount() != null && shareData.getPassword() != null) {
                volleyApi.postApi(DefaultSetting.URL_LOGIN, shareData.getAccount(), shareData.getPassword(), new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(ii);
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        try {
                            shareData.saveAccount("");
                            shareData.savePassword("");
                            Log.e(TAG, "autoLogin Failed.");

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
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
                Toast.makeText(this, "請輸入賬號和密碼！", Toast.LENGTH_SHORT).show();
            else {
                //For API
                volleyApi.postApi(DefaultSetting.URL_LOGIN, id, pass, new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            if (btn_rememberMe.isChecked()) {
                                shareData.saveAccount(id);
                                shareData.savePassword(pass);
                            }

                            Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(ii);

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        try {
                            if (error.networkResponse.statusCode == 401)
                                Toast.makeText(getApplicationContext(), "登入失敗，賬號密碼錯誤！", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                            Toast.makeText(getApplicationContext(), "連接伺服器失敗，請稍後嘗試。", Toast.LENGTH_SHORT).show();
                        }

                        clearFunction();
                    }
                });
            }
        }

        loginBtnChecked = true;
        btn_SignIn.setEnabled(true);
    }

    private void clearFunction() {
        editText_Acc.setText("");
        editText_Pass.setText("");
    }

    private void initButton() {
        btn_SignIn.setOnClickListener(v -> {
            if (loginBtnChecked) {
                loginBtnChecked = false;
                btn_SignIn.setEnabled(false);
                loginFunction();
            } else
                Toast.makeText(this, "登入中，請稍後...", Toast.LENGTH_SHORT).show();
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