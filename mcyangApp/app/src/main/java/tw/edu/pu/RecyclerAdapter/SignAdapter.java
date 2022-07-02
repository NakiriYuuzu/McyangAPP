package tw.edu.pu.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.SignModel;
import tw.edu.pu.R;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignViewHolder> {

    private ArrayList<SignModel> signList;

    public SignAdapter(ArrayList<SignModel> signModels) {
        this.signList = signModels;
    }

    @NonNull
    @Override
    public SignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_recyclerview, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, int position) {
        holder.tvLeft.setText(signList.get(position).getAttendance());
        holder.tvRight.setText(signList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (signList != null)
            return signList.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSignAdapter(ArrayList<SignModel> signModels) {
        this.signList = signModels;
        notifyDataSetChanged();
    }

    public static class SignViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvLeft, tvRight;

        public SignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.recycleView_tv_Right);
        }
    }
}
