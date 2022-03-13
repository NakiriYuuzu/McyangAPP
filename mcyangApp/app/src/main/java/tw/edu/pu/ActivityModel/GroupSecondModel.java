package tw.edu.pu.ActivityModel;

public class GroupSecondModel {
    private String teamID, leaderName, isSelected;

    public GroupSecondModel(String teamID, String leaderName, String isSelected) {
        this.teamID = teamID;
        this.leaderName = leaderName;
        this.isSelected = isSelected;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    @Override
    public String toString() {
        return "GroupSecondModel{" +
                "teamID='" + teamID + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", isSelected='" + isSelected + '\'' +
                '}';
    }
}
