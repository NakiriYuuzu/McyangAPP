package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.GroupViewModel;

public class GroupViewViewModel extends ViewModel {
    MutableLiveData<ArrayList<GroupViewModel>> groupViewList;

    public GroupViewViewModel() {
        groupViewList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<GroupViewModel>> getGroupObserver() {
        return groupViewList;
    }

    public void setGroupViewList(ArrayList<GroupViewModel> groupViewModels) {
        if (groupViewModels != null)
            groupViewList.setValue(groupViewModels);
    }
}
