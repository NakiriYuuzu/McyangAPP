package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SecondMemberModel;
import tw.edu.mcyangstudentapp.R;

public class MemberSecondAdapter extends RecyclerView.Adapter<MemberSecondAdapter.MemberSecondViewHolder> {

    ArrayList<SecondMemberModel> memberModels;

    public MemberSecondAdapter(ArrayList<SecondMemberModel> memberModels) {
        this.memberModels = memberModels;
    }

    @NonNull
    @Override
    public MemberSecondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_recyclerview, parent, false);
        return new MemberSecondViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberSecondViewHolder holder, int position) {
        holder.tvLeft.setText(memberModels.get(position).getGroupStatus());
        holder.tvRight.setText(memberModels.get(position).getGroupMember());
    }

    @Override
    public int getItemCount() {
        if (memberModels != null)
            return memberModels.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateMemberAdapter(ArrayList<SecondMemberModel> memberModels) {
        this.memberModels = memberModels;
        notifyDataSetChanged();
    }

    public static class MemberSecondViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialTextView tvLeft, tvRight;

        public MemberSecondViewHolder(@NonNull View itemView) {
            super(itemView);

            btnEnter = itemView.findViewById(R.id.sign_recycleView_btn);
            tvLeft = itemView.findViewById(R.id.sign_recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.sign_recycleView_tv_Right);
        }
    }
}
