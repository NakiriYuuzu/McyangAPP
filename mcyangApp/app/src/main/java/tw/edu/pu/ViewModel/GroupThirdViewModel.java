package tw.edu.pu.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.GroupThirdModel;

public class GroupThirdViewModel extends ViewModel {
    MutableLiveData<ArrayList<GroupThirdModel>> groupList;

    public GroupThirdViewModel() {
        groupList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<GroupThirdModel>> getGroupObserver() {
        return groupList;
    }

    public void setGroupList(ArrayList<GroupThirdModel> groupModels) {
        if (groupModels != null)
            groupList.setValue(groupModels);
    }
}
