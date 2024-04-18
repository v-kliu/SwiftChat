package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {
    // Implement inner class for nodes, everything is private to not restrict
    // what is exposed to the client
    private class ListNode<E> {
        // Fields for the node class, data and next pointer
        private E data;
        private ListNode<E> next;

        // Constructor for ListNode class for implementing linked lists
        // which is used to implement this ListFIFOQueue
        private ListNode(E data) {
            this.data = data;
            this.next = null;
        }
    }

    // Private fields of the ListFIFOQueue
    private ListNode<E> head;
    private ListNode<E> tail;
    private int size;

    // Public constructor - accessible to the client, sets everything to null at first
    public ListFIFOQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Add function to add nodes to the list
    @Override
    public void add(E work) {
        // Creates a new ListNode for the data provided
        ListNode newNode = new ListNode(work);

        // Adds newNode to the list accordingly if first node or not
        if (!hasWork()) {
            this.head = newNode;
        } else {
            this.tail.next = newNode;
        }
        // Updates tail and size
        this.tail = newNode;
        size++;
    }

    // Returns the first item in the list
    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException("Queue is empty");
        }

        return this.head.data;
    }

    // Returns the first item in the list and iterates to next item
    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException("Queue is empty");
        }

        // Stores the original head data, iterates, then checks if list is empty
        E headData = this.head.data;
        this.head = this.head.next;
        size--;
        if (!hasWork()) {
            this.tail = null;
        }
        return headData;
    }

    // Returns the size of the list
    @Override
    public int size() {
        return size;
    }

    // Clears the entire list and resets fields
    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        size = 0;
    }
}
