package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
    private E[] array;
    private int size;
    private int frontIndex;
    private int backIndex;


    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        this.array = (E[]) new Object[capacity];
        this.size =  0;
        this.frontIndex = 0;
        this.backIndex = 0;
    }

    @Override
    public void add(E work) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }

        this.array[backIndex] = work;
        size++;
        backIndex = (backIndex + 1) % capacity();
    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException("Queue is empty!");
        }

        return this.array[frontIndex];
    }

    @Override
    public E peek(int i) {
        if (!hasWork()) {
            throw new NoSuchElementException("Queue is empty!");
        } else if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException("Illegal index passed!");
        }
        return this.array[(frontIndex + i) % capacity()];
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException("Queue is empty!");
        }
        E currentHead = this.array[frontIndex];
        frontIndex = (frontIndex + 1) % capacity();
        size--;
        return currentHead;
    }

    @Override
    public void update(int i, E value) {
        if (!hasWork()) {
            throw new NoSuchElementException("Queue is empty!");
        } else if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException("Illegal index passed!");
        }
        this.array[(frontIndex + i) % capacity()] = value;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.frontIndex = 0;
        this.backIndex = 0;
        this.size = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        throw new NotYetImplementedException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // You will finish implementing this method in project 2. Leave this method unchanged for project 1.
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            // Uncomment the line below for p2 when you implement equals
            // FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;

            // Your code goes here

            throw new NotYetImplementedException();
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        throw new NotYetImplementedException();
    }
}
