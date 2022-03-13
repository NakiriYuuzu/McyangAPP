package tw.edu.pu.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.RaceModel;

public class RaceViewModel extends ViewModel {
    MutableLiveData<ArrayList<RaceModel>> raceList;

    public RaceViewModel() {
        raceList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<RaceModel>> getRaceObserver() {
        return raceList;
    }

    public void setRaceList(ArrayList<RaceModel> raceModels) {
        if (raceModels != null)
            raceList.setValue(raceModels);
    }
}
