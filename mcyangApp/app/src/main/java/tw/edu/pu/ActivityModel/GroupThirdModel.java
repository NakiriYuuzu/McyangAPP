package tw.edu.pu.ActivityModel;

public class GroupThirdModel {
    private String teamID, leaderName;
    private int total;

    public GroupThirdModel(String teamID, String leaderName, int total) {
        this.teamID = teamID;
        this.leaderName = leaderName;
        this.total = total;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    @Override
    public String toString() {
        return "GroupThirdModel{" +
                "teamID='" + teamID + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", total=" + total +
                '}';
    }
}
