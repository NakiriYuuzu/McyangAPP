package tw.edu.pu.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.edu.pu.ActivityModel.AnswerModel;
import tw.edu.pu.ApiModel.VolleyApi;
import tw.edu.pu.DefaultSetting;
import tw.edu.pu.R;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    ArrayList<AnswerModel> answerModels;

    Activity activity;
    VolleyApi volleyApi;

    public AnswerAdapter(Activity activity, ArrayList<AnswerModel> answerList) {
        this.answerModels = answerList;
        this.activity = activity;
        volleyApi = new VolleyApi(activity);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAnswerAdapter(ArrayList<AnswerModel> answerList) {
        this.answerModels = answerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_3_recyclerview, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        StringBuilder miniDoc = new StringBuilder();
        if (answerModels.get(position).getDoc().length() > 6) {
            char[] txt = answerModels.get(position).getDoc().toCharArray();
            for (int j = 0; j < 6; j++)
                miniDoc.append(txt[j]);
            miniDoc.append("...");
        } else {
            miniDoc.append(answerModels.get(position).getDoc());
        }

        if (answerModels.get(position).getAnswer().equals("false"))
            holder.tvRight.setText("");
        else
            holder.tvRight.setText("正確");

        holder.tvLeft.setText(answerModels.get(position).getStudentNames());
        holder.tvMid.setText(miniDoc);

        holder.btnEnter.setOnClickListener(v -> bottomSheet(position));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void bottomSheet(int position) {
        final BottomSheetDialog bsd = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_sheet_race, null);

        MaterialTextView tvNames = view.findViewById(R.id.bottom_sheet_Race_textView_Names);
        MaterialTextView tvDetail = view.findViewById(R.id.bottom_sheet_Race_textView_Detail);

        tvNames.setText(answerModels.get(position).getStudentNames());
        tvDetail.setText(answerModels.get(position).getDoc());

        view.findViewById(R.id.bottom_sheet_Race_btn_Send).setOnClickListener(v ->
            new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                    .setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner))
                    .setTitle("確定批改該學生？")
                    .setPositiveButton("確認", (dialogInterface, i) -> {
                        volleyApi.putApi(DefaultSetting.URL_ANSWER_MEMBER + answerModels.get(position).getAnswer_ID() + "/", new VolleyApi.VolleyGet() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(activity, "批改完成!", Toast.LENGTH_SHORT).show();
                                answerModels.get(position).setAnswer("正確");
                                updateAnswerAdapter(answerModels);
                                bsd.dismiss();
                            }

                            @Override
                            public void onFailed(VolleyError error) {
                                Toast.makeText(activity, "無法連接伺服器，請稍後再嘗試。", Toast.LENGTH_SHORT).show();
                                bsd.dismiss();
                            }

                        }, () -> {
                            Map<String, String> params = new HashMap<>();
                            params.put("Answer_doc", answerModels.get(position).getDoc());
                            params.put("Answer", "true");
                            params.put("Q_id", answerModels.get(position).getQuestion_ID());
                            params.put("S_id", answerModels.get(position).getSid());
                            return params;
                        });
                        dialogInterface.dismiss();
                    })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    bsd.dismiss();
                })
                .show()
        );

        bsd.setContentView(view);
        bsd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bsd.show();
    }

    @Override
    public int getItemCount() {
        if (this.answerModels != null)
            return this.answerModels.size();
        else
            return 0;
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btnEnter;
        MaterialTextView tvLeft, tvMid, tvRight;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            btnEnter = itemView.findViewById(R.id.three_recyclerView_Button);
            tvLeft = itemView.findViewById(R.id.three_recycleView_tv_Left);
            tvMid = itemView.findViewById(R.id.three_recycleView_tv_Mid);
            tvRight = itemView.findViewById(R.id.three_recycleView_tv_Right);
        }
    }
}
