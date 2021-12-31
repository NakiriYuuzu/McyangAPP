package tw.edu.pu.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import tw.edu.pu.R;
import tw.edu.pu.RequestModel.RequestHelper;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editText_Acc, editText_Pass;
    MaterialButton btn_SignIn;

    RequestHelper requestHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        requestHelper.checkInternet_Enabled();
    }

    private void initView() {
        requestHelper = new RequestHelper(this);
    }
}