package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.LeaderModel;
import tw.edu.mcyangstudentapp.R;

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.LeaderViewHolder> {

    ArrayList<LeaderModel> leaderModels;
    Activity activity;

    public LeaderAdapter(Activity activity, ArrayList<LeaderModel> leaderModels) {
        this.activity = activity;
        this.leaderModels = leaderModels;
    }

    @NonNull
    @Override
    public LeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_recyclerview, parent, false);
        return new LeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderViewHolder holder, int position) {
        holder.tvLeft.setText(leaderModels.get(position).getTeamID());
        holder.tvRight.setText(leaderModels.get(position).getLeaderName());
    }

    @Override
    public int getItemCount() {
        if (leaderModels != null) {
            return leaderModels.size();
        } else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateLeaderAdapter(ArrayList<LeaderModel> leaderModels) {
        this.leaderModels = leaderModels;
        notifyDataSetChanged();
    }

    public static class LeaderViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialTextView tvLeft, tvRight;

        public LeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            btnEnter = itemView.findViewById(R.id.sign_recycleView_btn);
            tvLeft = itemView.findViewById(R.id.sign_recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.sign_recycleView_tv_Right);
        }
    }
}
