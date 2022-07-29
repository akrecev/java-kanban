public class Status {

    private String status = "NEW";

    public String getStatus() {
        return status;
    }

    public void setStatusNew() {
        status = "NEW";
    }

    public void setStatusInProgress() {
        status = "IN_PROGRESS";
    }

    public void setStatusDone() {
        status = "DONE";
    }
}
