public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task task = new Task("Задача", "Тестовая задача", 0, "NEW");
        taskManager.addTask(task);

    }
}
