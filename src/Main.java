import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();
        ArrayList<Integer> subTasksIds1 = new ArrayList<>();
        ArrayList<Integer> subTasksIds2 = new ArrayList<>();

        Task task1 = new Task("Задача-1", "Тестовая задача №1", 0, "NEW");
        taskManager.addTask(task1);

        Task task2 = new Task("Задача-2", "Тестовая задача №2", 0, "NEW");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик-1", "Тестовый эпик №1", 0, "NEW", subTasksIds1);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("подзадача-1", "тестовая подзадача №1 к эпику-1", 0, "NEW", 3);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("подзадача-2", "тестовая подзадача №2 к эпику-1", 0, "NEW", 3);
        taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Эпик-2", "Тестовый эпик №2", 0, "NEW", subTasksIds2);
        taskManager.addEpic(epic2);

        Subtask subtask3 = new Subtask("подзадача-3", "тестовая подзадача №3 к эпику-2", 0, "NEW", 6);
        taskManager.addSubtask(subtask3);

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());
        System.out.println();

        Task task1update = new Task("Задача-1", "Обновленная тестовая задача №1", 1, "IN_PROGRESS");
        taskManager.updateTask(task1update);
        Task task2update = new Task("Задача-2", "Обновленная тестовая задача №2", 2, "DONE");
        taskManager.updateTask(task2update);
        Subtask subtask1Update = new Subtask("подзадача-1", "обновленная тестовая подзадача №1 к эпику-1", 4, "IN_PROGRESS", 3);
        taskManager.updateSubtask(subtask1Update);
        Subtask subtask2Update = new Subtask("подзадача-2", "обновленная тестовая подзадача №2 к эпику-1", 5, "DONE", 3);
        taskManager.updateSubtask(subtask2Update);
        Subtask subtask3Update = new Subtask("подзадача-3", "обновленная тестовая подзадача №3 к эпику-2", 7, "DONE", 6);
        taskManager.updateSubtask(subtask3Update);

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());
        System.out.println();

        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(3);

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());

    }
}
