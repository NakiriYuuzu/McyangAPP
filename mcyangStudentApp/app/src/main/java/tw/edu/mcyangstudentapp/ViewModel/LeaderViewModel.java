package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.LeaderModel;

public class LeaderViewModel extends ViewModel {
    MutableLiveData<ArrayList<LeaderModel>> leaderList;

    public LeaderViewModel() {
        leaderList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<LeaderModel>> getLeaderObserver() {
        return leaderList;
    }

    public void setLeaderList(ArrayList<LeaderModel> groupModels) {
        if (groupModels != null)
            leaderList.setValue(groupModels);
    }
}
