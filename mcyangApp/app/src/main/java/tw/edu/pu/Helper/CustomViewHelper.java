package tw.edu.pu.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import tw.edu.pu.R;

public class CustomViewHelper {

    Activity activity;

    public CustomViewHelper(Activity activity) {
        this.activity = activity;
    }

    public void showSnackBar(ConstraintLayout constraintLayout, String text) {
        Snackbar snackbar = Snackbar.make(constraintLayout, "", Snackbar.LENGTH_INDEFINITE);

        @SuppressLint("InflateParams")
        View view = activity.getLayoutInflater().inflate(R.layout.custom_snackbar, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        MaterialTextView textView = view.findViewById(R.id.custom_snack_bar_text);
        textView.setText(text);
        (view.findViewById(R.id.custom_snack_bar_button)).setOnClickListener(v -> snackbar.dismiss());

        snackbarLayout.addView(view, 0);
        snackbar.show();
    }
}
