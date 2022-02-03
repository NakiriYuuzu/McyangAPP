package tw.edu.mcyangstudentapp.ActivityModel;

public class SignModel {
    private String major, minor;

    public SignModel(String major, String minor) {
        this.major = major;
        this.minor = minor;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    @Override
    public String toString() {
        return "SignModel{" +
                "major='" + major + '\'' +
                ", minor='" + minor + '\'' +
                '}';
    }
}
