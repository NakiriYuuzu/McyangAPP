package tw.edu.pu.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import tw.edu.pu.R;

public class SignActivity extends AppCompatActivity {

    private static final String TAG = "SignActivity: ";

    String[] courses;

    AutoCompleteTextView autoCompleteTextView;
    MaterialButton btn_Enter;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        initButton();
    }

    private void initButton() {
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Course: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initView() {
        courses = getResources().getStringArray(R.array.sign_example_course);

        autoCompleteTextView = findViewById(R.id.sign_AutoCompleteText);
        btn_Enter = findViewById(R.id.sign_btn_Enter);

        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, courses);
    }

    @Override
    protected void onResume() {
        super.onResume();
        courses = getResources().getStringArray(R.array.sign_example_course);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, courses);
    }
}