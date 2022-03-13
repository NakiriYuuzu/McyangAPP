package tw.edu.mcyangstudentapp.ActivityModel;

public class MemberModel {
    private String teamID, teamLeaderName, isSelected;

    public MemberModel(String teamID, String teamLeaderName, String isSelected) {
        this.teamID = teamID;
        this.teamLeaderName = teamLeaderName;
        this.isSelected = isSelected;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getTeamLeaderName() {
        return teamLeaderName;
    }

    public void setTeamLeaderName(String teamLeaderName) {
        this.teamLeaderName = teamLeaderName;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
