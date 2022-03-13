package tw.edu.mcyangstudentapp.Activity.LearningRecord;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.imageview.ShapeableImageView;

import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.R;

public class LearningRecord3Activity extends AppCompatActivity {

    private static final String TAG = "LearningRecord3: ";

    ShapeableImageView btnBack;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_record3);

        initView();
        initButton();
        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        String problems_id = getIntent().getStringExtra("problemID");
        String url = DefaultSetting.URL_DOM_JUDGE_PROBLEMS + problems_id + "/text";
        Log.e(TAG, "initWebView: " + url);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    private void initButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.learningRecord3_btn_Back);
        webView = findViewById(R.id.learningRecord3_webView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}