import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int generateId = 1;

    // таблицы для хранения данных
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();


    /* ------ Методы для задач типа Task ------ */

    // получение списка всех задач
    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    // удаление всех задач
    @Override
    public void delAllTasks() {
        tasks.clear();
    }

    // получение задачи по id
    @Override
    public Task getTaskById(int id) {
        return tasks.getOrDefault(id, null);

    }

    // создание задачи
    @Override
    public int addTask(Task task) {
        task.setId(generateId++);
        return updateTask(task);
    }

    // обновление задачи
    @Override
    public int updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // удаление задачи по идентификатору
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    /* ------ Методы для задач типа Epic ------ */

    // обновление статуса эпика
    @Override
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
    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    // удаление всех эпиков
    @Override
    public void delAllEpics() {
        epics.clear();
    }

    // получение эпика по id
    @Override
    public Epic getEpicById(int id) {
        return epics.getOrDefault(id, null);
    }

    // создание эпика
    @Override
    public int addEpic(Epic epic) {
        epic.setId(generateId++);
        return updateEpic(epic);
    }

    // обновление эпика
    @Override
    public int updateEpic(Epic epic) {
        epic.setStatus(epicStatus(epic));
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    // удаление эпика по идентификатору
    @Override
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
    @Override
    public List<Subtask> getSubtaskListByEpic(int id) {
        if (epics.containsKey(id)) {
            List<Subtask> subtaskList = new ArrayList<>();
            for (int i : epics.get(id).subTasksIds) {
                subtaskList.add(subtasks.get(i));
            }
            return subtaskList;
        }
        return null;
    }

    // получение списка всех подзадач
    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    // удаление всех подзадач
    @Override
    public void delAllSubtask() {
        for (Subtask subtask : subtasks.values()) {
            epics.get(subtask.getEpicId()).subTasksIds.clear();
        }
        subtasks.clear();

    }

    // получение подзадачи по id
    @Override
    public Subtask getSubtaskById(int id) {
        return subtasks.getOrDefault(id, null);
    }

    // создание подзадачи
    @Override
    public int addSubtask(Subtask subtask) {
        subtask.setId(generateId++); // установка id подзадаче
        subtasks.put(subtask.getId(), subtask); // добавление подзадачи в таблицу позадач
        epics.get(subtask.getEpicId()).subTasksIds.add(subtask.getId()); // добавление id подзадачи в список внутри эпика
        checkEpicStatus(subtask); // проверка статуса эпика
        return subtask.getId();
    }

    // обновление подзадачи
    @Override
    public int updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask); // замена подзадачи в таблице позадач
        checkEpicStatus(subtask); // проверка статуса эпика
        return subtask.getId();
    }

    // удаление подзадачи по идентификатору
    @Override
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