package tw.edu.pu.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.GroupThirdModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.Helper.CustomViewHelper;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class GroupThirdAdapter extends RecyclerView.Adapter<GroupThirdAdapter.GroupThirdViewHolder> {

    ArrayList<GroupThirdModel> groupModels;
    ShareData shareData;
    Activity activity;

    public GroupThirdAdapter(Activity activity, ArrayList<GroupThirdModel> groupModels) {
        this.groupModels = groupModels;
        this.activity = activity;
        shareData = new ShareData(activity);
    }

    @NonNull
    @Override
    public GroupThirdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_3_recyclerview, parent, false);
        return new GroupThirdViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroupThirdViewHolder holder, int position) {
        holder.tvLeft.setText(groupModels.get(position).getTeamID() + "組");
        holder.tvMid.setText(groupModels.get(position).getLeaderName());
        holder.tvRight.setText(String.valueOf(groupModels.get(position).getTotal()));

        holder.btnEnter.setOnClickListener(v -> {
            CustomViewHelper viewHelper = new CustomViewHelper(activity);
            VolleyApi volleyApi = new VolleyApi(activity);

            viewHelper.showAlertBuilder(groupModels.get(position).getLeaderName() + "的組員信息", "", new CustomViewHelper.AlertListener() {
                @Override
                public void onPositive(DialogInterface dialogInterface, int i) {

                }

                @Override
                public void onNegative(DialogInterface dialogInterface, int i) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        if (groupModels != null)
            return groupModels.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateGroupAdapter(ArrayList<GroupThirdModel> groupModels) {
        this.groupModels = groupModels;
        notifyDataSetChanged();
    }

    public static class GroupThirdViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvLeft, tvMid, tvRight;
        LinearLayout btnEnter;

        public GroupThirdViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLeft = itemView.findViewById(R.id.three_recycleView_tv_Left);
            tvMid = itemView.findViewById(R.id.three_recycleView_tv_Mid);
            tvRight = itemView.findViewById(R.id.three_recycleView_tv_Right);
            btnEnter = itemView.findViewById(R.id.three_recyclerView_Button);
        }
    }
}
