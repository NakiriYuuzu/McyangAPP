package tw.edu.pu.ActivityModel;

public class AnswerModel {
    private String answer_ID, doc, answer, sid, studentNames, question_ID;

    public AnswerModel(String answer_ID, String doc, String answer, String sid, String studentNames, String question_ID) {
        this.answer_ID = answer_ID;
        this.doc = doc;
        this.answer = answer;
        this.sid = sid;
        this.studentNames = studentNames;
        this.question_ID = question_ID;
    }

    public String getAnswer_ID() {
        return answer_ID;
    }

    public void setAnswer_ID(String answer_ID) {
        this.answer_ID = answer_ID;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(String studentNames) {
        this.studentNames = studentNames;
    }

    public String getQuestion_ID() {
        return question_ID;
    }

    public void setQuestion_ID(String question_ID) {
        this.question_ID = question_ID;
    }

    @Override
    public String toString() {
        return "AnswerModel{" +
                "answer_ID='" + answer_ID + '\'' +
                ", doc='" + doc + '\'' +
                ", answer='" + answer + '\'' +
                ", sid='" + sid + '\'' +
                ", studentNames='" + studentNames + '\'' +
                ", question_ID='" + question_ID + '\'' +
                '}';
    }
}
