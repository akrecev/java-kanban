package com.yandex.practicum.node;

import com.yandex.practicum.tasks.Task;

public class Node {
    private Task task;
    private Node next;
    private Node prev;

    public Node(Task task) {
        this.task = task;
        this.next = null;
        this.prev = null;
    }

    public Task getTask() {
        return task;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
