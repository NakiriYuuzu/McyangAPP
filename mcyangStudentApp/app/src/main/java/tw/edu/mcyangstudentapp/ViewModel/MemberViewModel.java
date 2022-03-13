package tw.edu.mcyangstudentapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import tw.edu.mcyangstudentapp.ActivityModel.MemberModel;

public class MemberViewModel extends ViewModel {
    MutableLiveData<ArrayList<MemberModel>> memberList;

    public MemberViewModel() {
        memberList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<MemberModel>> getMemberObserver() {
        return memberList;
    }

    public void setMemberList(ArrayList<MemberModel> memberModels) {
        if (memberModels != null)
            memberList.setValue(memberModels);
    }
}
