package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.SecondMemberModel;

public class SecondMemberViewModel extends ViewModel {
    MutableLiveData<ArrayList<SecondMemberModel>> memberList;

    public SecondMemberViewModel() {
        memberList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<SecondMemberModel>> getMemberObserver() {
        return memberList;
    }

    public void setMemberList(ArrayList<SecondMemberModel> memberModels) {
        if (memberModels != null)
            memberList.setValue(memberModels);
    }
}
