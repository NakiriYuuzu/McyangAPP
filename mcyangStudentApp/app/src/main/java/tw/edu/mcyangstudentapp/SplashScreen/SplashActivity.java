package tw.edu.mcyangstudentapp.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.Activity.LoginActivity;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private final int scene = 3;
    private int currentPage = 0;
    ArrayList<Integer> sceneList;

    ShareData shareData;

    MaterialButton btnNext;
    MaterialButton btnPrev;
    MaterialCheckBox checkBox;
    ShapeableImageView imgSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        buttonInit();
    }

    private void buttonInit() {
        btnNext.setOnClickListener(v -> {
            if (currentPage == scene) {
                if (checkBox.isChecked())
                    shareData.saveSplashScreen("true");
                else
                    shareData.saveSplashScreen("once");

                Intent ii = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(ii);
                finish();

            } else if (currentPage == scene - 1) {
                currentPage++;
                btnNext.setText(R.string.splash_Start);
                imgSplash.setImageResource(sceneList.get(currentPage));

            } else {
                currentPage++;
                btnPrev.setVisibility(View.VISIBLE);
                imgSplash.setImageResource(sceneList.get(currentPage));
            }

            Log.e("buttonInit: ", "currentPage: " + currentPage);
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage != 0) {
                btnNext.setText(R.string.splash_Next);
                currentPage--;
                if (currentPage == 0) {
                    btnPrev.setVisibility(View.INVISIBLE);
                }
                imgSplash.setImageResource(sceneList.get(currentPage));
            }

            Log.e("buttonInit: ", "currentPage: " + currentPage);
        });
    }

    private void initView() {
        initSceneList();

        imgSplash = findViewById(R.id.splash_img_backgroundImg);
        imgSplash.setImageResource(sceneList.get(currentPage));
        checkBox = findViewById(R.id.splash_checkbox);
        btnNext = findViewById(R.id.splash_btn_Next);
        btnPrev = findViewById(R.id.splash_btn_Prev);
        btnPrev.setVisibility(View.INVISIBLE);

        shareData = new ShareData(this);
    }

    private void initSceneList() {
        sceneList = new ArrayList<>();
        sceneList.add(R.drawable.scene1);
        sceneList.add(R.drawable.scene2);
        sceneList.add(R.drawable.scene3);
        sceneList.add(R.drawable.scene4);
    }
}