package tw.edu.mcyangstudentapp.ActivityModel;

public class GroupRoomModel {
    private String groupNames;

    public GroupRoomModel(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    @Override
    public String toString() {
        return "GroupRoomModel{" +
                "groupNames='" + groupNames + '\'' +
                '}';
    }
}
