package tw.edu.mcyangstudentapp.ActivityModel;

public class LeaderModel {
    private String teamID, leaderName;

    public LeaderModel(String teamID, String leaderName) {
        this.teamID = teamID;
        this.leaderName = leaderName;
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
}
