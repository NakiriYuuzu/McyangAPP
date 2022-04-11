package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.Activity.Group.GroupChatActivity;
import tw.edu.mcyangstudentapp.ActivityModel.GroupViewModel;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.GroupViewHolder> {

    ArrayList<GroupViewModel> groupView;
    ShareData shareData;
    Activity activity;

    public GroupViewAdapter(Activity activity, ArrayList<GroupViewModel> groupView) {
        this.activity = activity;
        this.groupView = groupView;
        shareData = new ShareData(activity);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_recyclerview, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.tvLeft.setText(groupView.get(position).getTeamID());
        holder.tvRight.setText(groupView.get(position).getLeaderName());

        holder.btnEnter.setOnClickListener(v -> {
            shareData.saveChat_ID(groupView.get(position).getTeamID());
            shareData.saveChat_Name(groupView.get(position).getLeaderName());
            shareData.saveChat_Room(groupView.get(position).getTeamDesc());
            Intent ii = new Intent(activity, GroupChatActivity.class);
            activity.startActivity(ii);
        });
    }

    @Override
    public int getItemCount() {
        if (groupView != null)
            return groupView.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateGroupViewAdapter(ArrayList<GroupViewModel> groupView) {
        this.groupView = groupView;
        notifyDataSetChanged();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialTextView tvLeft, tvRight;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            btnEnter = itemView.findViewById(R.id.sign_recycleView_btn);
            tvLeft = itemView.findViewById(R.id.sign_recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.sign_recycleView_tv_Right);
        }
    }
}
