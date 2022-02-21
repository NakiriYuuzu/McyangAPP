package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.LearningModel;

public class LearningViewModel extends ViewModel {
    MutableLiveData<ArrayList<LearningModel>> learningList;

    public LearningViewModel() {
        learningList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<LearningModel>> getLearningListObserver() {
        return learningList;
    }

    public void setLearningList(ArrayList<LearningModel> signModels) {
        if (signModels != null) {
            learningList.setValue(signModels);
        }
    }
}
