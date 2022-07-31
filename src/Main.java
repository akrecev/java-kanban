import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();
        ArrayList<Integer> subTasksIds1 = new ArrayList<>();
        ArrayList<Integer> subTasksIds2 = new ArrayList<>();

        Task task1 = new Task("Задача-1", "Тестовая задача №1", 0, 1);
        taskManager.addTask(task1);

        Task task2 = new Task("Задача-2", "Тестовая задача №2", 0, 1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик-1", "Тестовый эпик №1", 0, 1, subTasksIds1);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("подзадача-1", "тестовая подзадача №1 к эпику-1", 0, 1, 3);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("подзадача-2", "тестовая подзадача №2 к эпику-1", 0, 1, 3);
        taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Эпик-2", "Тестовый эпик №2", 0, 1, subTasksIds2);
        taskManager.addEpic(epic2);

        Subtask subtask3 = new Subtask("подзадача-3", "тестовая подзадача №3 к эпику-2", 0, 1, 6);
        taskManager.addSubtask(subtask3);

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());
        System.out.println();

        Task task1update = new Task("Задача-1", "Обновленная тестовая задача №1", 1, 2);
        taskManager.updateTask(task1update);
        Task task2update = new Task("Задача-2", "Обновленная тестовая задача №2", 2, 3);
        taskManager.updateTask(task2update);
        Epic epic1update = new Epic("Эпик-1", "Обновленный тестовый эпик №1", 3, 1, subTasksIds1);
        taskManager.updateEpic(epic1update);
        Subtask subtask1Update = new Subtask("подзадача-1", "обновленная тестовая подзадача №1 к эпику-1", 4, 2, 3);
        taskManager.updateSubtask(subtask1Update);
        Subtask subtask2Update = new Subtask("подзадача-2", "обновленная тестовая подзадача №2 к эпику-1", 5, 3, 3);
        taskManager.updateSubtask(subtask2Update);
        Subtask subtask3Update = new Subtask("подзадача-3", "обновленная тестовая подзадача №3 к эпику-2", 7, 3, 6);
        taskManager.updateSubtask(subtask3Update);

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());
        System.out.println("Список подзадач эпика-1: " + taskManager.getSubtaskListByEpic(3));
        System.out.println("Список подзадач эпика-2: " + taskManager.getSubtaskListByEpic(6));
        System.out.println("Задача-1 : " + taskManager.getTaskById(1));
        System.out.println("Эпик-1 : " + taskManager.getEpicById(3));
        System.out.println("Эпик-2 : " + taskManager.getEpicById(6));
        System.out.println("Подзадача-1 : " + taskManager.getSubtaskById(4));

        System.out.println();

        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(6);
        taskManager.deleteSubtaskById(4);

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());
        System.out.println();

        taskManager.delAllTasks();
        taskManager.delAllSubtask();

        System.out.println("Список задач: " + taskManager.getTaskList().toString());
        System.out.println("Список подзадач: " + taskManager.getSubtaskList().toString());
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());
        taskManager.delAllEpics();
        System.out.println("Список эпиков: " + taskManager.getEpicList().toString());


    }
}
