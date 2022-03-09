package tw.edu.pu.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.GroupSecondModel;

public class GroupSecondViewModel extends ViewModel {
    MutableLiveData<ArrayList<GroupSecondModel>> groupList;

    public GroupSecondViewModel() {
        groupList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<GroupSecondModel>> getGroupObserver() {
        return groupList;
    }

    public void setGroupList(ArrayList<GroupSecondModel> groupModels) {
        if (groupModels != null)
            groupList.setValue(groupModels);
    }
}
