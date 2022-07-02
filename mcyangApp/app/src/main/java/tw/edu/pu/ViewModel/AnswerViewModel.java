package tw.edu.pu.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.AnswerModel;
import tw.edu.pu.ActivityModel.CreateModel;

public class AnswerViewModel extends ViewModel {
    MutableLiveData<ArrayList<AnswerModel>> answerList;

    public AnswerViewModel() {answerList = new MutableLiveData<>();}

    public MutableLiveData<ArrayList<AnswerModel>> getAnswerListObserver() {return answerList;}

    public void setAnswerList(ArrayList<AnswerModel> answerModels) {
        if (answerModels.size() != 0)
            answerList.setValue(answerModels);
    }

}
