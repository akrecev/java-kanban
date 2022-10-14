package com.yandex.practicum.managers;

import com.yandex.practicum.node.Node;
import com.yandex.practicum.tasks.Task;

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
        if (task != null) {
            remove(task.getId());
            linkLast(task);
            historyMap.put(task.getId(), tail);
        }
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
            list.add(currentNode.getTask());
            currentNode = currentNode.getNext();
        }
        return list;
    }

    // добавление задачи в конец списка просмотров
    void linkLast(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
        }
        tail = node;
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.getPrev() != null) { // удаляемый узел не первый
            node.getPrev().setNext(node.getNext());
            if (node.getNext() == null) { // удаляемый узел последний
                tail = node.getPrev();
            } else {
                node.getNext().setPrev(node.getPrev());
            }
        } else { // удаляемый узел первый
            head = node.getNext();
            if (head == null) { // удаляемый узел один в списке
                tail = null;
            } else {
                head.setPrev(null);
            }
        }
    }
}
