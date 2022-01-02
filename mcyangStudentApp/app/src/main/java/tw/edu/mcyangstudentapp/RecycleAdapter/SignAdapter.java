package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SignModel;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ClassID_Status;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignViewHolder> {

    private static final String TAG = "SignAdapter: ";

    Activity activity;
    ShareData shareData;
    ClassID_Status status = new ClassID_Status();

    private ArrayList<SignModel> signData;

    public SignAdapter(Activity activity, ArrayList<SignModel> signModels) {
        this.activity = activity;
        this.signData = signModels;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSignAdapter(ArrayList<SignModel> signModels) {
        this.signData = signModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sign_recycle, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, int position) {
        Log.e(TAG, " adapter size: " + signData.size());
        holder.tvLeft.setText(signData.get(position).getMajor());
        holder.tvRight.setText(status.getClassNames(signData.get(position).getMinor()));

        holder.btnSummit.setOnClickListener(v -> bottomSheet());
    }

    @Override
    public int getItemCount() {
        if (this.signData != null)
            return this.signData.size();
        else
            return 0;
    }

    public void bottomSheet() {
        final BottomSheetDialog bsd = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        shareData = new ShareData(activity);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_sheet, null);

        TextInputEditText studentID = view.findViewById(R.id.bottom_sheet_input_Names);
        TextInputEditText studentNames = view.findViewById(R.id.bottom_sheet_input_ID);

        if (shareData.getStudentID() != null && shareData.getStudentNames() != null) {
            Log.e(TAG, "StudentID: " + shareData.getStudentID() + " StudentName: " + shareData.getStudentNames());
            studentID.setText(shareData.getStudentID());
            studentNames.setText(shareData.getStudentNames());

        } else {
            shareData.saveStudentName(null);
            shareData.saveStudentID(null);
        }

        view.findViewById(R.id.bottom_sheet_btn_Send).setOnClickListener(view1 -> {
            String id, name;
            if (studentID.getText() != null && studentNames.getText() != null) {
                id = studentID.getText().toString();
                name = studentNames.getText().toString();

                if (id.equals("") || name.equals("")) {
                    Toast.makeText(activity, R.string.tag_NoInput, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Input Please~");
                    shareData.saveStudentName(null);
                    shareData.saveStudentID(null);

                } else {
                    shareData.saveStudentID(id);
                    shareData.saveStudentName(name);

                    Toast.makeText(activity, "Send~", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Success~");
                    bsd.dismiss();
                }
            }
        });

        bsd.setContentView(view);
        bsd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bsd.show();
    }

    public static class SignViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvLeft, tvRight;
        LinearLayout btnSummit;

        public SignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.sign_recycleView_tv_Left);
            tvRight = itemView.findViewById(R.id.sign_recycleView_tv_Right);
            btnSummit = itemView.findViewById(R.id.sign_recycleView_btn);
        }
    }
}
