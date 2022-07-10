package tw.edu.pu.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.GroupSecondModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class GroupSecondAdapter extends RecyclerView.Adapter<GroupSecondAdapter.GroupSecondViewHolder> {

    ArrayList<GroupSecondModel> groupModels;
    ArrayList<String> teamID;

    Activity activity;
    VolleyApi volleyApi;
    ShareData shareData;

    public GroupSecondAdapter(Activity activity, ArrayList<GroupSecondModel> groupModels) {
        this.activity = activity;
        this.groupModels = groupModels;

        teamID = new ArrayList<>();
        volleyApi = new VolleyApi(activity);
        shareData = new ShareData(activity);
    }

    @NonNull
    @Override
    public GroupSecondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_checkbox_recyclerview, parent, false);
        return new GroupSecondViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupSecondViewHolder holder, int position) {
        holder.tvLeft.setText(groupModels.get(position).getTeamID());
        holder.tvRight.setText(groupModels.get(position).getLeaderName());
        holder.checkBox.setOnClickListener(v -> {
            String tid = groupModels.get(position).getTeamID();

            if (isExisted(tid)) {
                teamID.add(tid);
                groupModels.get(position).setIsSelected("已選擇");

            } else {
                for (int i = 0; i < teamID.size(); i++)
                    if (teamID.get(i).equals(tid)) {
                        teamID.remove(i);
                        break;
                    }

                groupModels.get(position).setIsSelected("選擇");
            }

            if (teamID.size() > 0)
                shareData.saveTeam_ID(teamID);
            else
                shareData.saveTeam_ID(null);

            Log.e("teamID", teamID.toString() + " | " + shareData.getTeam_ID());
            updateGroupAdapter(groupModels);
        });

//        holder.btnEnter.setOnClickListener(v -> {
//            String tid = groupModels.get(position).getTeamID();
//
//            if (isExisted(tid)) {
//                teamID.add(tid);
//                groupModels.get(position).setIsSelected("已選擇");
//
//            } else {
//                for (int i = 0; i < teamID.size(); i++)
//                    if (teamID.get(i).equals(tid)) {
//                        teamID.remove(i);
//                        break;
//                    }
//
//                groupModels.get(position).setIsSelected("選擇");
//            }
//
//            if (teamID.size() > 0)
//                shareData.saveTeam_ID(teamID);
//            else
//                shareData.saveTeam_ID(null);
//
//            Log.e("teamID", teamID.toString() + " | " + shareData.getTeam_ID());
//            updateGroupAdapter(groupModels);
//        });
    }

    private boolean isExisted(String tid) {
        boolean isExist = true;
        if (teamID.size() > 0)
            for (String id : teamID)
                if (id.equals(tid)) {
                    isExist = false;
                    break;
                }
        return isExist;
    }

    @Override
    public int getItemCount() {
        if (groupModels != null) {
            return groupModels.size();
        } else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateGroupAdapter(ArrayList<GroupSecondModel> groupModels) {
        this.groupModels = groupModels;
        notifyDataSetChanged();
    }

    public static class GroupSecondViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialCheckBox checkBox;
        MaterialTextView tvLeft, tvRight;

        public GroupSecondViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.layout2_checkbox_recyclerview_left);
            btnEnter = itemView.findViewById(R.id.layout2_checkbox_recyclerview_btn);
            tvLeft = itemView.findViewById(R.id.layout2_checkbox_recyclerview_mid);
            tvRight = itemView.findViewById(R.id.layout2_checkbox_recyclerview_right);
        }
    }
}
