package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.GroupRoomModel;

public class GroupRoomViewModel extends ViewModel {
    MutableLiveData<ArrayList<GroupRoomModel>> groupRoomList;

    public GroupRoomViewModel() {
        groupRoomList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<GroupRoomModel>> getGroupRoomObserver() {
        return groupRoomList;
    }

    public void setGroupRoomList(ArrayList<GroupRoomModel> groupRoomModels) {
        if (groupRoomModels != null)
            groupRoomList.setValue(groupRoomModels);
    }
}
