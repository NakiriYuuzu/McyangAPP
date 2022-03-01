package tw.edu.pu.ActivityModel;

public class SignModel {
    private String name, attendance, sid;

    public SignModel(String name, String attendance, String sid) {
        this.name = name;
        this.attendance = attendance;
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "SignModel{" +
                "name='" + name + '\'' +
                ", attendance='" + attendance + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}
