import java.util.List;

public class Epic extends Task {

    protected List<Integer> subTasksIds;


    public Epic(String title, String description, int id, int status, List<Integer> subTasksIds) {
        super(title, description, id, status);
        this.subTasksIds = subTasksIds;
    }

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(List<Integer> subTasksIds) {
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
                    ", status='" + super.getStatus() + '\'' +
                    '}';
        } else {
            return "Epic{" +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", status='" + super.getStatus() + '\'' +
                    '}';
        }
    }
}
