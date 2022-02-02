package tw.edu.pu.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.pu.ActivityModel.SignModel;

public class SignViewModel extends ViewModel {
    MutableLiveData<ArrayList<SignModel>> signList;

    public SignViewModel() {
        signList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<SignModel>> getSignListObserver() {
        return signList;
    }

    public void setSignList(ArrayList<SignModel> signModels) {
        signList.setValue(signModels);
        if (signModels != null)
            for (SignModel c : signModels)
                Log.e("SignViewModel", c.getName() + ", " + c.getAttendance());
    }
}
