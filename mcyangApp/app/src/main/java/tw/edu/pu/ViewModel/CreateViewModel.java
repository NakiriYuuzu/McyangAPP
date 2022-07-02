package tw.edu.pu.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.CreateModel;

public class CreateViewModel extends ViewModel {
    MutableLiveData<ArrayList<CreateModel>> createList;

    public CreateViewModel() {createList = new MutableLiveData<>();}

    public MutableLiveData<ArrayList<CreateModel>> getCreateListObserver() {return createList;}

    public void setCreateList(ArrayList<CreateModel> createModels) {
        createList.setValue(createModels);
        if (createModels != null)
            for (CreateModel c : createModels)
                Log.e("CreateViewModel", c.getClassName());
    }
}
