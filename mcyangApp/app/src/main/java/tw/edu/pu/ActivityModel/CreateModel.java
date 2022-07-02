package tw.edu.pu.ActivityModel;

public class CreateModel {
    private String classID, className;

    public CreateModel(String major, String className) {
        this.classID = major;
        this.className = className;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
