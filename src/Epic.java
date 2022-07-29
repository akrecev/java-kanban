import java.util.ArrayList;
import java.util.HashMap;

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




}
