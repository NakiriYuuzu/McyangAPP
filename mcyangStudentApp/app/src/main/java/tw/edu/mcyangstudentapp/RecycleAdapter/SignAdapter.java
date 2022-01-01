package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.R;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignViewHolder> {

    ArrayList<String> leftData;
    ArrayList<String> rightData;
    Activity activity;

    public SignAdapter(Activity activity, ArrayList<String> left, ArrayList<String> right) {
        this.activity = activity;
        this.leftData = left;
        this.rightData = right;
    }

    @NonNull
    @Override
    public SignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sign_recycle, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, int position) {
        String left = leftData.get(position);
        String right = rightData.get(position);

        holder.tvLeft.setText(left);
        holder.tvRight.setText(right);

        holder.tvLeft.setOnClickListener(v -> {
            final BottomSheetDialog bsd = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);

            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_sheet, null);

            view.findViewById(R.id.bottom_sheet_btn_Send).setOnClickListener(view1 -> {
                Toast.makeText(activity, "Send~", Toast.LENGTH_SHORT).show();
                bsd.dismiss();
            });

            bsd.setContentView(view);
            bsd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bsd.show();
        });
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (leftData.size() != 0 && rightData.size() != 0)
            itemCount = leftData.size();

        return itemCount;
    }

    public static class SignViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvLeft, tvRight;

        public SignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.sign_recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.sign_recycleView_tv_Right);
        }
    }
}
