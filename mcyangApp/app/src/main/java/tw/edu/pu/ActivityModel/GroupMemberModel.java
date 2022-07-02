package tw.edu.pu.ActivityModel;

public class GroupMemberModel {
    private String teamID, memberID, memberName;

    public GroupMemberModel(String teamID, String memberID, String memberName) {
        this.teamID = teamID;
        this.memberID = memberID;
        this.memberName = memberName;
    }


    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    @Override
    public String toString() {
        return "GroupMemberModel{" +
                "memberID='" + memberID + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }
}
