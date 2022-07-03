package tw.edu.mcyangstudentapp.ActivityModel;

public class GroupChatModel {
    private String user, message, time;
    private int current;

    public GroupChatModel(String user, String message, String time, int current) {
        this.user = user;
        this.message = message;
        this.time = time;
        this.current = current;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "GroupChatModel{" +
                "user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", current='" + current + '\'' +
                '}';
    }
}
