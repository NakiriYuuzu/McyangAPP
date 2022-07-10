package tw.edu.mcyangstudentapp.ActivityModel;

public class MemberModel {
    private String teamID, teamLeaderName, isSelected, teamDescID, teamLeaderID;

    public MemberModel(String teamID, String teamLeaderName, String isSelected, String teamDescID, String teamLeaderID) {
        this.teamID = teamID;
        this.teamLeaderName = teamLeaderName;
        this.isSelected = isSelected;
        this.teamDescID = teamDescID;
        this.teamLeaderID = teamLeaderID;
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

    public String getTeamDescID() {
        return teamDescID;
    }

    public void setTeamDescID(String teamDescID) {
        this.teamDescID = teamDescID;
    }

    public String getTeamLeaderID() {
        return teamLeaderID;
    }

    public void setTeamLeaderID(String teamLeaderID) {
        this.teamLeaderID = teamLeaderID;
    }

    @Override
    public String toString() {
        return "MemberModel{" +
                "teamID='" + teamID + '\'' +
                ", teamLeaderName='" + teamLeaderName + '\'' +
                ", isSelected='" + isSelected + '\'' +
                ", teamDescID='" + teamDescID + '\'' +
                ", teamLeaderID='" + teamLeaderID + '\'' +
                '}';
    }
}
