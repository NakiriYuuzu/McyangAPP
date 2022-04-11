package tw.edu.pu.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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
        (view.findViewById(R.id.custom_snack_bar_button)).setOnClickListener(view1 -> snackbar.dismiss());

        snackbarLayout.addView(view, 0);
        snackbar.show();
    }

    public void showSnackBar(ConstraintLayout constraintLayout, String text, SnackBarListener snackBarListener) {
        Snackbar snackbar = Snackbar.make(constraintLayout, "", Snackbar.LENGTH_INDEFINITE);

        @SuppressLint("InflateParams")
        View view = activity.getLayoutInflater().inflate(R.layout.custom_snackbar, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        MaterialTextView textView = view.findViewById(R.id.custom_snack_bar_text);
        textView.setText(text);
        (view.findViewById(R.id.custom_snack_bar_button)).setOnClickListener(view1 -> snackBarListener.onClick(view1, snackbar));

        snackbarLayout.addView(view, 0);
        snackbar.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {
        if (!(view instanceof TextInputEditText)) {
            view.setOnTouchListener((view1, motionEvent) -> {
                hideSoftKeyboard();
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showAlertBuilder(String title, String message, AlertListener alertListener) {
        new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("確認", alertListener::onPositive)
                .setNegativeButton("取消", alertListener::onNegative)
                .show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showAlertBuilder(String title, String message, String positiveButton, String negativeButton, AlertListener alertListener) {
        new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, alertListener::onPositive)
                .setNegativeButton(negativeButton, alertListener::onNegative)
                .show();
    }

    public interface AlertListener {
        void onPositive(DialogInterface dialogInterface, int i);
        void onNegative(DialogInterface dialogInterface, int i);
    }

    public interface SnackBarListener {
        void onClick(View view, Snackbar snackbar);
    }
}
