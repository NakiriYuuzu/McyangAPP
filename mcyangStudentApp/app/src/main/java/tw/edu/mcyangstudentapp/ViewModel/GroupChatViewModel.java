package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.GroupChatModel;

public class GroupChatViewModel extends ViewModel {
    MutableLiveData<ArrayList<GroupChatModel>> chatMessage;

    public GroupChatViewModel() {
        chatMessage = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<GroupChatModel>>getChatObserver() {
        return chatMessage;
    }

    public void setChatMessage(ArrayList<GroupChatModel> chatMessages) {
        if (chatMessages != null)
            chatMessage.setValue(chatMessages);
    }
}
