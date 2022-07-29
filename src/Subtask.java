public class Subtask extends Task {

    protected int epicId;

    public Subtask(String title, String description, int id, String status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
