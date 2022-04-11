package tw.edu.mcyangstudentapp.ActivityModel;

public class GroupViewModel {
    private String teamID, leaderName, teamDesc;

    public GroupViewModel(String teamID, String leaderName, String teamDesc) {
        this.teamID = teamID;
        this.leaderName = leaderName;
        this.teamDesc = teamDesc;
    }

    public String getTeamDesc() {
        return teamDesc;
    }

    public void setTeamDesc(String teamDesc) {
        this.teamDesc = teamDesc;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    @Override
    public String toString() {
        return "GroupViewModel{" +
                "teamID='" + teamID + '\'' +
                ", leaderName='" + leaderName + '\'' +
                '}';
    }
}
