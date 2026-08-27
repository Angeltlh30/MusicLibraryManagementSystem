package structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularDoublyLinkedList<T> implements Iterable<T> {

    private class Node {
        T value;
        Node prev;
        Node next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node head;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addLast(T value) {
        Node newNode = new Node(value);
        if (head == null) {
            newNode.next = newNode;
            newNode.prev = newNode;
            head = newNode;
        } else {
            Node tail = head.prev;
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
        }
        size++;
    }

    public void addFirst(T value) {
        addLast(value);
        head = head.prev;
    }

    public T get(int index) {
        return node(index).value;
    }

    public T removeAt(int index) {
        Node target = node(index);
        return unlink(target);
    }

    public boolean remove(T value) {
        Node current = head;
        for (int i = 0; i < size; i++) {
            if (equalsValue(current.value, value)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    public int indexOf(T value) {
        Node current = head;
        for (int i = 0; i < size; i++) {
            if (equalsValue(current.value, value)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    public void clear() {
        head = null;
        size = 0;
    }

    private Node node(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private T unlink(Node target) {
        T value = target.value;
        if (size == 1) {
            head = null;
        } else {
            target.prev.next = target.next;
            target.next.prev = target.prev;
            if (target == head) {
                head = target.next;
            }
        }
        size--;
        return value;
    }

    private boolean equalsValue(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node current = head;
            private int visited = 0;

            @Override
            public boolean hasNext() {
                return visited < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = current.value;
                current = current.next;
                visited++;
                return value;
            }
        };
    }
}
