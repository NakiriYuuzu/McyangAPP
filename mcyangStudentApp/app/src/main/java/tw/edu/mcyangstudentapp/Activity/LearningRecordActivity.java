package tw.edu.mcyangstudentapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.imageview.ShapeableImageView;

import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.R;

public class LearningRecordActivity extends AppCompatActivity {

    WebView webView;
    ShapeableImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_record);

        initView();
        initButton();
        initWebView();
    }

    private void initWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(DefaultSetting.URL_WEB);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.learningRecord_btn_Back);
        webView = findViewById(R.id.learningRecord_WebView);
    }
}