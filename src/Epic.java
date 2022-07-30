import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subTasksIds;


    public Epic(String title, String description, int id, String status, ArrayList<Integer> subTasksIds) {
        super(title, description, id, status);
        this.subTasksIds = subTasksIds;
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    @Override
    public String toString() {
        if (subTasksIds != null) {
            return "Epic{" +
                    "subTasksIds=" + subTasksIds +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", status='" + status + '\'' +
                    '}';
        } else {
            return "Epic{" +
                    "subTasksIds=null" +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
