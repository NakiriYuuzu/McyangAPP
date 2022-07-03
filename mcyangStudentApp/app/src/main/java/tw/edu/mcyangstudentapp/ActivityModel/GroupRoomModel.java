package tw.edu.mcyangstudentapp.ActivityModel;

public class GroupRoomModel {
    private String groupNumbers, groupNames;

    public GroupRoomModel(String groupNumbers, String groupNames) {
        this.groupNumbers = groupNumbers;
        this.groupNames = groupNames;
    }

    public String getGroupNumbers() {
        return groupNumbers;
    }

    public void setGroupNumbers(String groupNumbers) {
        this.groupNumbers = groupNumbers;
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
                "groupNumbers='" + groupNumbers + '\'' +
                ", groupNames='" + groupNames + '\'' +
                '}';
    }
}
