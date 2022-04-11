package tw.edu.mcyangstudentapp.ActivityModel;

public class GroupChatModel {
    private String user, message, time;

    public GroupChatModel(String user, String message, String time) {
        this.user = user;
        this.message = message;
        this.time = time;
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

    @Override
    public String toString() {
        return "GroupChatModel{" +
                "user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
