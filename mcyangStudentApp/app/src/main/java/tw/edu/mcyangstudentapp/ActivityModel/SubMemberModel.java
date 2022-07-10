package tw.edu.mcyangstudentapp.ActivityModel;

public class SubMemberModel {
    private String teamID, leaderName, teamDesc, leaderID;

    public SubMemberModel(String teamID, String leaderName, String teamDesc) {
        this.teamID = teamID;
        this.leaderName = leaderName;
        this.teamDesc = teamDesc;
    }

    public SubMemberModel(String teamID, String tName, String teamDesc, String leaderID) {
        this.teamID = teamID;
        this.leaderName = tName;
        this.teamDesc = teamDesc;
        this.leaderID = leaderID;
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

    public String getTeamDesc() {
        return teamDesc;
    }

    public void setTeamDesc(String teamDesc) {
        this.teamDesc = teamDesc;
    }

    public String getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(String leaderID) {
        this.leaderID = leaderID;
    }

    @Override
    public String toString() {
        return "SubMemberModel{" +
                "teamID='" + teamID + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", teamDesc='" + teamDesc + '\'' +
                ", leaderID='" + leaderID + '\'' +
                '}';
    }
}
