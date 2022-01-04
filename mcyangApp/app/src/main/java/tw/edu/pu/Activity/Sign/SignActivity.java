package tw.edu.pu.Activity.Sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.CreateModel;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class SignActivity extends AppCompatActivity {

    private static final String TAG = "SignActivity: ";

    ArrayList<String> courses;
    ArrayList<CreateModel> createList;

    AutoCompleteTextView autoCompleteTextView;
    ShapeableImageView btn_Back;
    MaterialButton btn_Enter;

    ArrayAdapter<String> arrayAdapter;

    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        initButton();
    }

    private void initButton() {
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), " " + item, Toast.LENGTH_SHORT).show();
        });

        btn_Enter.setOnClickListener(view -> {
            Intent ii = new Intent(getApplicationContext(), Sign_Second_Activity.class);
            startActivity(ii);
        });

        btn_Back.setOnClickListener(view -> finish());
    }

    private void initData() {
        courses = new ArrayList<>();
        if (shareData.create_getData() != null)
            createList = shareData.create_getData();

        Log.e(TAG, "initData: " + shareData.create_getData().size());

        if (createList.size() != 0)
            for (int i = 0; i < createList.size(); i++)
                courses.add(createList.get(i).getClassName());
    }

    private void initView() {
        shareData = new ShareData(this);
        initData();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, courses);

        autoCompleteTextView = findViewById(R.id.sign_AutoCompleteText);
        btn_Back = findViewById(R.id.race_btn_Back);
        btn_Enter = findViewById(R.id.sign_btn_Enter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, courses);
    }
}