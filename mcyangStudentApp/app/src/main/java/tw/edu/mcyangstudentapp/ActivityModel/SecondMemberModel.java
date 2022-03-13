package tw.edu.mcyangstudentapp.ActivityModel;

public class SecondMemberModel {
    private String groupStatus, groupMember;

    public SecondMemberModel(String groupStatus, String groupMember) {
        this.groupStatus = groupStatus;
        this.groupMember = groupMember;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(String groupMember) {
        this.groupMember = groupMember;
    }

    @Override
    public String toString() {
        return "SecondMemberModel{" +
                "groupStatus='" + groupStatus + '\'' +
                ", groupMember='" + groupMember + '\'' +
                '}';
    }
}
