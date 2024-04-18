package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    // Fields of ArrayStack class
    private E[] array;
    private int size;

    // Constructor class of ArrayStack
    public ArrayStack() {
        this.array = (E[]) new Object[10];
        this.size = 0;
    }

    // Adds/pushes to the top of the stack
    @Override
    public void add(E work) {
        if (this.size == array.length) {
            E[] newArray = (E[]) new Object[this.array.length * 2];
            for (int i = 0; i < array.length; i++) {
                newArray[i] = this.array[i];
            }
            this.array = newArray;
        }
        this.array[this.size] = work;
        size++;
    }

    // Returns data from top of stack
    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return this.array[this.size - 1];
    }

    // Returns current top of stack, then pops top
    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException("Stack is empty");
        }
        E top = this.array[this.size - 1];
        this.size--;
        return top;
    }

    // Returns the current size of the stack
    @Override
    public int size() {
        return this.size;
    }

    // Clears the entirety of the stack
    @Override
    public void clear() {
        this.size = 0;
    }
}
