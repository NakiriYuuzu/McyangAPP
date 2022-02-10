package tw.edu.pu.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import tw.edu.pu.ActivityModel.RaceModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class RaceAdapter extends RecyclerView.Adapter<RaceAdapter.RaceViewHolder> {

    ArrayList<RaceModel> raceModels;
    ArrayList<String> sid;
    ArrayList<String> raceList_ID;

    Activity activity;

    ShareData shareData;
    VolleyApi volleyApi;

    public RaceAdapter(Activity activity, ArrayList<RaceModel> raceModels, ArrayList<String> studentID, ArrayList<String> raceListID) {
        this.raceModels = raceModels;
        this.activity = activity;

        sid = studentID;
        raceList_ID = raceListID;

        shareData = new ShareData(activity);
        volleyApi = new VolleyApi(activity);
    }

    @NonNull
    @Override
    public RaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_3_recyclerview, parent, false);
        return new RaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RaceViewHolder holder, int position) {
        holder.tvLeft.setText(raceModels.get(position).getRaceID());
        holder.tvMid.setText(raceModels.get(position).getStudentNames());
        holder.tvRight.setText(raceModels.get(position).getRaceCorrect());

        holder.btnEnter.setOnClickListener(view -> bottomSheet(position));
    }

    private void bottomSheet(int position) {
        final BottomSheetDialog bsd = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_sheet_race, null);

        MaterialTextView tvNames = view.findViewById(R.id.bottom_sheet_Race_textView_Names);
        MaterialTextView tvDetail = view.findViewById(R.id.bottom_sheet_Race_textView_Detail);

        tvNames.setText(raceModels.get(position).getStudentNames());
        tvDetail.setText(raceModels.get(position).getRaceCorrect());

        String url = "/" + raceList_ID.get(position) + "/";

        view.findViewById(R.id.bottom_sheet_Race_btn_Send).setOnClickListener(v ->
                volleyApi.putApi(DefaultSetting.URL_RACE_LIST + url, new VolleyApi.VolleyGet() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(activity, "批改完成！", Toast.LENGTH_SHORT).show();
                        raceModels.get(position).setRaceCorrect("正確");
                        bsd.dismiss();
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        Toast.makeText(activity, "無法連接伺服器，請稍後再嘗試。", Toast.LENGTH_SHORT).show();
                        bsd.dismiss();
                    }
                }, () -> {
                    Map<String, String> params = new HashMap<>();
                    params.put("Answer", "true");
                    params.put("R_id", shareData.getRaceID());
                    params.put("S_id", sid.get(position));
                    return params;
                })
        );

        bsd.setContentView(view);
        bsd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bsd.show();
    }

    @Override
    public int getItemCount() {
        if (this.raceModels != null)
            return this.raceModels.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCreateAdapter(ArrayList<RaceModel> raceModels, ArrayList<String> studentID, ArrayList<String> raceListID) {
        this.sid = studentID;
        this.raceModels = raceModels;
        this.raceList_ID = raceListID;
        notifyDataSetChanged();
    }

    public static class RaceViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvLeft, tvMid, tvRight;
        LinearLayout btnEnter;

        public RaceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.three_recycleView_tv_Left);
            tvMid = itemView.findViewById(R.id.three_recycleView_tv_Mid);
            tvRight = itemView.findViewById(R.id.three_recycleView_tv_Right);
            btnEnter = itemView.findViewById(R.id.three_recyclerView_Button);
        }
    }
}
