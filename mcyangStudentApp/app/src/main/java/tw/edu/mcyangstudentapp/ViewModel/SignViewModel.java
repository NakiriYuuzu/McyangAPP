package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SignModel;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class SignViewModel extends ViewModel {
    private final MutableLiveData<String> classNO = new MutableLiveData<>();
    private final MutableLiveData<String> classID = new MutableLiveData<>();

    public void setClassNO(String s) {
        classNO.setValue(s);
    }

    public LiveData<String> getClassNO() {
        return classNO;
    }

    public void setClassID(String s) {
        classID.setValue(s);
    }

    public LiveData<String> getClassID() {
        return classID;
    }
}


//public class SignViewModel extends ViewModel {
//
//    MutableLiveData<ArrayList<SignModel>> signLiveData;
//    ArrayList<SignModel> signArrayList;
//    ShareData shareData;
//
//    public SignViewModel() {
//        signLiveData = new MutableLiveData<>();
//        shareData = new ShareData();
//        init();
//    }
//
//    public MutableLiveData<ArrayList<SignModel>> getUserMutableLiveData() {
//        return signLiveData;
//    }
//
//    public void init() {
//        signDataList();
//        signLiveData.setValue(signArrayList);
//    }
//
//    private void signDataList() {
//        signArrayList = shareData.sign_getData();
//    }
//}
