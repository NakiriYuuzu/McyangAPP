package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.MemberModel;
import tw.edu.mcyangstudentapp.ActivityModel.SubMemberModel;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private int selectedPosition = -1;

    Activity activity;
    ShareData shareData;
    ArrayList<SubMemberModel> selected;
    ArrayList<MemberModel> memberModels;

    public MemberAdapter(Activity activity, ArrayList<MemberModel> memberModels) {
        this.activity = activity;
        this.memberModels = memberModels;
        shareData = new ShareData(activity);

        selected = new ArrayList<>();
        Log.e("selected", selected.size() + "");
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_radio_recyclerview, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
//        holder.tvLeft.setText(memberModels.get(position).getIsSelected());
        holder.tvLeft.setText(memberModels.get(position).getTeamID());
        holder.tvRight.setText(memberModels.get(position).getTeamLeaderName());

        holder.btnRadio.setChecked(position == selectedPosition);
        holder.btnRadio.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedPosition = holder.getAdapterPosition();
                holder.btnRadio.setOnClickListener(v -> {
                    String tName = memberModels.get(position).getTeamLeaderName();
                    String teamDesc = memberModels.get(position).getTeamDescID();
                    String leaderID = memberModels.get(position).getTeamLeaderID();
                    if (shareData.getStudentNames().equals(tName)) {
                        Toast.makeText(activity, "您已經是組長！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (selected.size() > 0) {
                            selected = new ArrayList<>();
                            if (shareData.getTeam_ID_Array() != null)
                                shareData.saveTeam_ID_Array(null);

                            selected.add(new SubMemberModel(memberModels.get(position).getTeamID(), tName, teamDesc, leaderID));
                            shareData.saveTeam_ID_Array(selected);

                        } else {
                            selected.add(new SubMemberModel(memberModels.get(position).getTeamID(), tName,teamDesc, leaderID));
                            shareData.saveTeam_ID_Array(selected);
                        }
                    }
//
//                    if (selected.size() > 0) {
//                        if (selected.get(0).getLeaderName().equals(tName)) {
//                            memberModels.get(position).setIsSelected("未選擇");
//                            selected.remove(0);
//                            shareData.saveTeam_ID_Array(null);
//                        }
//                        else
//                            Toast.makeText(activity, "只能選擇一個組別！", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        selected.add(new SubMemberModel(memberModels.get(position).getTeamID(), tName));
//                        memberModels.get(position).setIsSelected("已選擇");
//                        shareData.saveTeam_ID_Array(selected);
//                    }

                    Log.e("ShareData_TEAMID", "onBindViewHolder: " + shareData.getTeam_ID_Array().toString());
                    updateLeaderAdapter(memberModels);
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (memberModels != null)
            return memberModels.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateLeaderAdapter(ArrayList<MemberModel> memberModels) {
        this.memberModels = memberModels;
        notifyDataSetChanged();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialRadioButton btnRadio;
        MaterialTextView tvLeft, tvRight;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);

            btnRadio = itemView.findViewById(R.id.layout2_radio_recyclerview_left);
            btnEnter = itemView.findViewById(R.id.layout2_radio_recyclerview_btn);
            tvLeft = itemView.findViewById(R.id.layout2_radio_recyclerview_mid);
            tvRight = itemView.findViewById(R.id.layout2_radio_recyclerview_right);
        }
    }
}
