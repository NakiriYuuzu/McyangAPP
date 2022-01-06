package tw.edu.pu.RecyclerAdapter;

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

import tw.edu.pu.ActivityModel.CreateModel;
import tw.edu.pu.R;
import tw.edu.pu.StoredData.ShareData;

public class CreateAdapter extends RecyclerView.Adapter<CreateAdapter.CreateViewHolder> {

    private ArrayList<CreateModel> createList;

    Activity activity;
    ShareData shareData;

    public CreateAdapter(Activity activity, ArrayList<CreateModel> createList) {
        this.createList = createList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_2_recyclerview, parent, false);
        return new CreateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateViewHolder holder, int position) {
        shareData = new ShareData(activity);
        createList = shareData.create_getData();
        holder.tvLeft.setText(createList.get(position).getClassID());
        holder.tvRight.setText(createList.get(position).getClassName());
    }

    @Override
    public int getItemCount() {
        if (createList != null)
            return createList.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCreateAdapter(ArrayList<CreateModel> createModels) {
        this.createList = createModels;
        notifyDataSetChanged();
    }

    public static class CreateViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvLeft, tvRight;
        LinearLayout btnEnter;

        public CreateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.recycleView_tv_Right);
            btnEnter = itemView.findViewById(R.id.create_recycleView_btn);
        }
    }
}
