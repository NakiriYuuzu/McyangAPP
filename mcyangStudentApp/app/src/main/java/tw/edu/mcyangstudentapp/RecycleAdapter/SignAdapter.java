package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.edu.mcyangstudentapp.ActivityModel.SignModel;
import tw.edu.mcyangstudentapp.ApiModel.VolleyApi;
import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.Helper.CustomViewHelper;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ClassID_Status;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignViewHolder> {

    private static final String TAG = "SignAdapter: ";

    private String btmSheetTitle = "";
    private int selectedPosition = -1;

    Activity activity;
    ShareData shareData;
    ClassID_Status status;
    CustomViewHelper viewHelper;
    VolleyApi volleyApi;

    private ArrayList<SignModel> signData;
    private ArrayList<String> sign_ID;

    public SignAdapter(Activity activity, ArrayList<SignModel> signModels, ArrayList<String> signID) {
        this.activity = activity;
        this.signData = signModels;
        this.sign_ID = signID;

        viewHelper = new CustomViewHelper(activity);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_radio_recyclerview, parent, false);
        return new SignViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.e(TAG, " adapter size: " + signData.size());

        btmSheetTitle = status.getClassNames(signData.get(position).getMajor());

        holder.tvMid.setText(signData.get(position).getMajor());
        holder.tvRight.setText(btmSheetTitle);

        holder.btnLeft.setChecked(position == selectedPosition);
        holder.btnLeft.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedPosition = holder.getAdapterPosition();
                holder.btnLeft.setOnClickListener(v ->
                        volleyApi.api(Request.Method.GET, DefaultSetting.URL_COURSE_RECORD + signData.get(position).getMajor(), new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(result.getBytes(StandardCharsets.ISO_8859_1)));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String sid = jsonObject.getString("S_id");
                                if (sid.equals(shareData.getStudentID())) {
                                    bottomSheet(position);
                                    return;
                                }
                            }

                            viewHelper.showAlertBuilder("簽到提示", "您還沒有選修此課程", "確認", "取消", new CustomViewHelper.AlertListener() {
                                @Override
                                public void onPositive(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }

                                @Override
                                public void onNegative(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                        } catch (JSONException e) {
                            Toast.makeText(activity, "資料編譯失敗，請通知工作人員。", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Toast.makeText(activity, "連線異常！", Toast.LENGTH_SHORT).show();
                    }
                }));
                notifyDataSetChanged();
            }
        });
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
        MaterialRadioButton btnLeft;
        MaterialTextView tvMid, tvRight;
        LinearLayout btnEnter;

        public SignViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLeft = itemView.findViewById(R.id.layout2_radio_recyclerview_left);
            tvMid = itemView.findViewById(R.id.layout2_radio_recyclerview_mid);
            tvRight = itemView.findViewById(R.id.layout2_radio_recyclerview_right);
            btnEnter = itemView.findViewById(R.id.layout2_radio_recyclerview_btn);
        }
    }
}
