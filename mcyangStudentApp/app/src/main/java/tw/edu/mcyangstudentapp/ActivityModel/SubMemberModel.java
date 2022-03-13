package tw.edu.mcyangstudentapp.ActivityModel;

public class SubMemberModel {
    private String teamID, leaderName;

    public SubMemberModel(String teamID, String leaderName) {
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

    @Override
    public String toString() {
        return "SubMemberModel{" +
                "teamID='" + teamID + '\'' +
                ", leaderName='" + leaderName + '\'' +
                '}';
    }
}
