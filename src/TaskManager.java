import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private int generateId = 1;

    // таблицы для хранения данных
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();


    /* ------ Методы для задач типа Task ------ */

    // получение списка всех задач
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (Task task : tasks.values()) {
            taskList.add(task);
        }
        return taskList;
    }

    // удаление всех задач
    public void delAllTasks() {
        tasks.clear();
    }

    // получение задачи по id
    public Task getTaskById(int id) {
        return tasks.containsKey(id) ? tasks.get(id) : null;
    }

    // создание задачи
    public int addTask(Task task) {
        task.setId(generateId++);
        return updateTask(task);
    }

    // обновление задачи
    public int updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // удаление задачи по идентификатору
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    /* ------ Методы для задач типа Epic ------ */

    // обновление статуса эпика
    public int epicStatus(Epic epic) {
        if (epic.subTasksIds == null) {
            return 1;
        }
        if (!epic.subTasksIds.isEmpty()) {
            boolean isStatusNEW = true;
            boolean isStatusDONE = true;
            for (int id : epic.subTasksIds) {
                isStatusNEW &= subtasks.get(id).getStatus().equals("NEW");
                isStatusDONE &= subtasks.get(id).getStatus().equals("DONE");
            }
            if (isStatusNEW) {
                return 1;
            }
            if (isStatusDONE) {
                return 3;
            } else {
                return 2;
            }
        } else {
            return 1;
        }
    }


    // получение списка эпиков
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicList.add(epic);
        }
        return epicList;
    }

    // удаление всех эпиков
    public void delAllEpics() {
        epics.clear();
    }

    // получение эпика по id
    public Epic getEpicById(int id) {
        return epics.containsKey(id) ? epics.get(id) : null;
    }

    // создание эпика
    public int addEpic(Epic epic) {
        epic.setId(generateId++);
        return updateEpic(epic);
    }

    // обновление эпика
    public int updateEpic(Epic epic) {
        epic.setStatus(epicStatus(epic));
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    // удаление эпика по идентификатору
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subTasksId : epics.get(id).subTasksIds) {
                subtasks.remove(subTasksId);
            }
            epics.remove(id);
        }
    }

    /* ------ Методы для подзадач типа Subtask ------ */

    // Получение списка всех подзадач определенного эпика
    public ArrayList<Subtask> getSubtaskListByEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Subtask> subtaskList = new ArrayList<>();
            for (int i : epics.get(id).subTasksIds) {
                subtaskList.add(subtasks.get(i));
            }
            return subtaskList;
        }
        return null;
    }

    // получение списка всех подзадач
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtaskList.add(subtask);
        }
        return subtaskList;
    }

    // удаление всех подзадач
    public void delAllSubtask() {
        for (Subtask subtask : subtasks.values()) {
            epics.get(subtask.getEpicId()).subTasksIds.clear();
        }
        subtasks.clear();

    }

    // получение подзадачи по id
    public Subtask getSubtaskById(int id) {
        return subtasks.containsKey(id) ? subtasks.get(id) : null;
    }

    // создание подзадачи
    public int addSubtask(Subtask subtask) {
        /*Epic epic = epics.get(subtask.getEpicId());*/
        subtask.setId(generateId++); // установка id подзадаче
        subtasks.put(subtask.getId(), subtask); // добавление подзадачи в таблицу позадач
        epics.get(subtask.getEpicId()).subTasksIds.add(subtask.getId()); // добавление id подзадачи в список внутри эпика
        checkEpicStatus(subtask); // проверка статуса эпика
        return subtask.getId();
    }

    // обновление подзадачи
    public int updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask); // замена подзадачи в таблице позадач
        checkEpicStatus(subtask); // проверка статуса эпика
        return subtask.getId();
    }

    // удаление подзадачи по идентификатору
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Epic currentEpic = epics.get(subtasks.get(id).getEpicId());
            subtasks.remove(id);
            for (int i = 0; i < currentEpic.subTasksIds.size(); i++) {
                if (currentEpic.subTasksIds.get(i) == id) {
                    currentEpic.subTasksIds.remove(i);
                }
            }
            currentEpic.setStatus(epicStatus(currentEpic));
        }
    }

    // проверка статуса эпика при изменении подзадачи
    public void checkEpicStatus(Subtask subtask) {
        Epic currentEpic = epics.get(subtask.getEpicId());
        currentEpic.setStatus(epicStatus(currentEpic));
    }

}
