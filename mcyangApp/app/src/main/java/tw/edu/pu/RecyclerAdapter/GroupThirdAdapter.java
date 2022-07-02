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

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.GroupMemberModel;
import tw.edu.pu.ActivityModel.GroupThirdModel;
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

            int i = 1;
            StringBuilder text = new StringBuilder();

            String teamID = groupModels.get(position).getTeamID();
            for (GroupMemberModel groupMemberModel : shareData.getGroupMember()) {
                if (groupMemberModel.getTeamID().equals(teamID)) {
                    text.append(i).append(": ").append(groupMemberModel.getMemberName()).append("\n");
                    i++;
                }
            }

            viewHelper.showAlertBuilder(groupModels.get(position).getLeaderName() + "的組員信息", text.toString(), new CustomViewHelper.AlertListener() {
                @Override
                public void onPositive(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }

                @Override
                public void onNegative(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
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
