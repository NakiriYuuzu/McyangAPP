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
import tw.edu.mcyangstudentapp.ActivityModel.GroupRoomModel;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class GroupRoomAdapter extends RecyclerView.Adapter<GroupRoomAdapter.GroupRoomViewHolder> {

    ArrayList<GroupRoomModel> groupRooms;
    Activity activity;
    ShareData shareData;

    public GroupRoomAdapter(Activity activity, ArrayList<GroupRoomModel> groupRooms) {
        this.activity = activity;
        this.groupRooms = groupRooms;
        shareData = new ShareData(activity);
    }

    @NonNull
    @Override
    public GroupRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_recyclerview, parent, false);
        return new GroupRoomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroupRoomViewHolder holder, int position) {
        holder.tvLeft.setText(position + 1 + "");
        holder.tvRight.setText(groupRooms.get(position).getGroupNames());

        holder.btnEnter.setOnClickListener(v -> {
            shareData.saveChat_Count(0);
            shareData.saveChatRoom_Name(groupRooms.get(position).getGroupNames());
            Intent ii = new Intent(activity, GroupChatActivity.class);
            activity.startActivity(ii);
        });
    }

    @Override
    public int getItemCount() {
        if (groupRooms != null)
            return groupRooms.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateGroupRoomAdapter(ArrayList<GroupRoomModel> groupRooms) {
        this.groupRooms = groupRooms;
        notifyDataSetChanged();
    }

    static class GroupRoomViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialTextView tvLeft, tvRight;

        public GroupRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            btnEnter = itemView.findViewById(R.id.sign_recycleView_btn);
            tvLeft = itemView.findViewById(R.id.sign_recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.sign_recycleView_tv_Right);
        }
    }
}
