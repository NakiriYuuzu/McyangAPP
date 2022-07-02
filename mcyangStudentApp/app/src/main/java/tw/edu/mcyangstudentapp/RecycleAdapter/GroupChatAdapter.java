package tw.edu.mcyangstudentapp.RecycleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.GroupChatModel;
import tw.edu.mcyangstudentapp.ActivityModel.GroupViewModel;
import tw.edu.mcyangstudentapp.R;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class GroupChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    ArrayList<GroupChatModel> chatMessage;
    ShareData shareData;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public GroupChatAdapter(Activity activity, ArrayList<GroupChatModel> chatMessage) {
        this.activity = activity;
        this.chatMessage = chatMessage;
        shareData = new ShareData(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT)
            return new SendMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_send_message, parent, false));
        else
            return new ReceivedMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SendMessageViewHolder) holder).tv_SendMessage.setText(chatMessage.get(position).getMessage());
            ((SendMessageViewHolder) holder).tv_SendMessageTime.setText(chatMessage.get(position).getTime());
        } else {
            ((ReceivedMessageViewHolder) holder).tv_ReceiveMessageName.setText(chatMessage.get(position).getUser());
            ((ReceivedMessageViewHolder) holder).tv_ReceiveMessage.setText(chatMessage.get(position).getMessage());
            ((ReceivedMessageViewHolder) holder).tv_ReceiveMessageTime.setText(chatMessage.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        if (chatMessage != null)
            return chatMessage.size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessage.get(position).getUser().equals(shareData.getStudentNames()))
            return VIEW_TYPE_SENT;
        else
            return VIEW_TYPE_RECEIVED;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateGroupChatAdapter(ArrayList<GroupChatModel> groupChat) {
        this.chatMessage = groupChat;
        notifyDataSetChanged();
    }

    static class SendMessageViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tv_SendMessage, tv_SendMessageTime;

        public SendMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_SendMessage = itemView.findViewById(R.id.chatSendMessage);
            tv_SendMessageTime = itemView.findViewById(R.id.chatSendMessageTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tv_ReceiveMessage, tv_ReceiveMessageName, tv_ReceiveMessageTime;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ReceiveMessage = itemView.findViewById(R.id.chatReceivedMessage);
            tv_ReceiveMessageName = itemView.findViewById(R.id.chatReceivedMessageUserName);
            tv_ReceiveMessageTime = itemView.findViewById(R.id.chatReceivedMessageTime);
        }
    }
}
