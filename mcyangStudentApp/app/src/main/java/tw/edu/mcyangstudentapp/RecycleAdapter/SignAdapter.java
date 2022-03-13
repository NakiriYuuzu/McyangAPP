package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.Activity.MainActivity;
import tw.edu.mcyangstudentapp.ActivityModel.SignModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ClassID_Status;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignViewHolder> {

    private static final String TAG = "SignAdapter: ";

    private String btmSheetTitle = "";

    Activity activity;
    ShareData shareData;
    ClassID_Status status;
    VolleyApi volleyApi;

    private ArrayList<SignModel> signData;
    private ArrayList<String> sign_ID;

    public SignAdapter(Activity activity, ArrayList<SignModel> signModels, ArrayList<String> signID) {
        this.activity = activity;
        this.signData = signModels;
        this.sign_ID = signID;

        volleyApi = new VolleyApi(activity);
        status = new ClassID_Status(activity);
        shareData = new ShareData(activity);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSignAdapter(ArrayList<SignModel> signModels, ArrayList<String> signID) {
        this.signData = signModels;
        this.sign_ID = signID;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_recyclerview, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, int position) {
        Log.e(TAG, " adapter size: " + signData.size());

        btmSheetTitle = status.getClassNames(signData.get(position).getMajor());

        holder.tvLeft.setText(signData.get(position).getMajor());
        holder.tvRight.setText(btmSheetTitle);

        holder.btnEnter.setOnClickListener(v -> bottomSheet(position));
    }

    @Override
    public int getItemCount() {
        if (this.signData != null)
            return this.signData.size();
        else
            return 0;
    }

    @SuppressLint("SetTextI18n")
    public void bottomSheet(int position) {
        final BottomSheetDialog bsd = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);

        Log.e(TAG, "onClick: " + sign_ID.get(position));

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_sheet, null);
        MaterialTextView classNames = view.findViewById(R.id.bottom_sheet_SelectedCourse);

        if (!btmSheetTitle.equals(""))
            classNames.setText(btmSheetTitle);

        view.findViewById(R.id.bottom_sheet_btn_Send).setOnClickListener(view1 -> volleyApi.postApi(DefaultSetting.URL_SIGN_RECORD, new VolleyApi.VolleyGet() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(activity, "簽到完成！", Toast.LENGTH_SHORT).show();
                shareData.saveMajor(signData.get(position).getMajor());
                shareData.saveMinor(signData.get(position).getMinor());
                activity.finish();
                bsd.dismiss();
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(activity, "簽到失敗！", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailed: " + shareData.getMinor() + shareData.getStudentID());
                bsd.dismiss();
            }
        }, () -> {
            Map<String, String> params = new HashMap<>();
            params.put("Sign_id", sign_ID.get(position));
            params.put("S_id", shareData.getStudentID());
            return params;
        }));

        bsd.setContentView(view);
        bsd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bsd.show();
    }

    public static class SignViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvLeft, tvRight;
        LinearLayout btnEnter;

        public SignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.sign_recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.sign_recycleView_tv_Right);
            btnEnter = itemView.findViewById(R.id.sign_recycleView_btn);
        }
    }
}
