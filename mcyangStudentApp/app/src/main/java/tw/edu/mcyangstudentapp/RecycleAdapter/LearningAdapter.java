package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.Activity.LearningRecord.LearningRecord3Activity;
import tw.edu.mcyangstudentapp.ActivityModel.LearningModel;
import tw.edu.mcyangstudentapp.R;

public class LearningAdapter extends RecyclerView.Adapter<LearningAdapter.LearningViewHolder> {

    ArrayList<LearningModel> learningModels;

    Activity activity;

    public LearningAdapter(Activity activity, ArrayList<LearningModel> learningList) {
        this.activity = activity;
        this.learningModels = learningList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateLearningAdapter(ArrayList<LearningModel> learningList) {
        this.learningModels = learningList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LearningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_3_recyclerview, parent, false);
        return new LearningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearningViewHolder holder, int position) {
        holder.tvLeft.setText(learningModels.get(position).getLabel());
        holder.tvMid.setText(learningModels.get(position).getSolved());
        holder.tvRight.setText(learningModels.get(position).getNum_judged());

        holder.btnEnter.setOnClickListener(v -> {
            Intent ii = new Intent(activity, LearningRecord3Activity.class);
            ii.putExtra("problemID", learningModels.get(position).getProblem_id());
            activity.startActivity(ii);
        });
    }

    @Override
    public int getItemCount() {
        if (this.learningModels != null)
            return this.learningModels.size();
        else
            return 0;
    }

    public static class LearningViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialTextView tvLeft, tvMid, tvRight;

        public LearningViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEnter = itemView.findViewById(R.id.three_recyclerView_Button);
            tvLeft = itemView.findViewById(R.id.three_recycleView_tv_Left);
            tvMid = itemView.findViewById(R.id.three_recycleView_tv_Mid);
            tvRight = itemView.findViewById(R.id.three_recycleView_tv_Right);
        }
    }
}
