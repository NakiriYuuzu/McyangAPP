package tw.edu.mcyangstudentapp.ActivityModel;

public class LearningModel {
    private String label, problem_id, num_judged, solved;

    public LearningModel(String label, String problem_id, String num_judged, String solved) {
        this.label = label;
        this.problem_id = problem_id;
        this.num_judged = num_judged;
        this.solved = solved;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    public String getNum_judged() {
        return num_judged;
    }

    public void setNum_judged(String num_judged) {
        this.num_judged = num_judged;
    }

    public String getSolved() {
        return solved;
    }

    public void setSolved(String solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return "LearningModel{" +
                "label='" + label + '\'' +
                ", problem_id='" + problem_id + '\'' +
                ", num_judged='" + num_judged + '\'' +
                ", solved='" + solved + '\'' +
                '}';
    }
}
