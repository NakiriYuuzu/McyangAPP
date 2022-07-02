package tw.edu.pu.ActivityModel;

public class RaceModel {
    private String raceID, studentNames, raceCorrect;

    public RaceModel(String raceID, String studentNames, String raceCorrect) {
        this.raceID = raceID;
        this.studentNames = studentNames;
        this.raceCorrect = raceCorrect;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(String studentNames) {
        this.studentNames = studentNames;
    }

    public String getRaceCorrect() {
        return raceCorrect;
    }

    public void setRaceCorrect(String raceCorrect) {
        this.raceCorrect = raceCorrect;
    }

    @Override
    public String toString() {
        return "RaceModel{" +
                "raceID='" + raceID + '\'' +
                ", studentNames='" + studentNames + '\'' +
                ", raceCorrect='" + raceCorrect + '\'' +
                '}';
    }
}
