package com.yandex.practicum;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private final Map<Integer, Node> historyMap = new HashMap<>();

    // добавление просмотра задачи в список
    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
        historyMap.put(task.getId(), tail);
    }

    // удаление просмотра задачи из списка
    @Override
    public void remove(int id) {
        removeNode(historyMap.remove(id));
    }

    // получение истории просмотров задач
    @Override
    public List<Task> getHistory() {
        Node currentNode = head;
        List<Task> list = new ArrayList<>();
        while (currentNode != null) {
            list.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return list;
    }

    // добавление задачи в конец списка просмотров
    void linkLast(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;

    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) { // удаляемый узел не первый
            node.prev.next = node.next;
            if (node.next == null) { // удаляемый узел последний
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else { // удаляемый узел первый
            head = node.next;
            if (head == null) { // удаляемый узел один в списке
                tail = null;
            } else head.prev = null;
        }
    }
}
