package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeapComparable<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;

    public MinFourHeapComparable() {
        this.data = (E[]) new Comparable[10];
        this.size = 0;
        // throw new NotYetImplementedException();
    }

    @Override
    public boolean hasWork() {
        return this.size != 0;
        // throw new NotYetImplementedException();
    }

    @Override
    public void add(E work) {
        if (this.size == this.data.length - 1) {
            E[] newArray = (E[]) new Comparable[this.data.length * 2];
            for (int i = 0; i < this.data.length; i++) {
                newArray[i] = this.data[i];
            }
            this.data = newArray;
        }

        //System.out.println("this.size and work are :" + this.size + " and " + work);
        int insertHole = percolateUp(this.size, work);
        //System.out.println("insert hole is " + insertHole);
        this.data[insertHole] = work;
        size++;
        // throw new NotYetImplementedException();
    }

    // Function to percolateUp, returns the index of the hole to
    // insert new value
    public int percolateUp(int hole, E val) {
        while (hole > 0 && val.compareTo(this.data[(hole - 1) / 4]) < 0) {
            this.data[hole] = this.data[(hole - 1) / 4];
            hole = (hole - 1) / 4;
        }
        return hole;
    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException("Heap is empty in peek!");
        }
        return this.data[0];
        // throw new NotYetImplementedException();
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException("Heap is empty in next!");
        }
        E minElement = this.data[0];
        this.data[0] = this.data[this.size - 1];
        size--;

        percolateDown();


        return minElement;
        // throw new NotYetImplementedException();
    }

    // Function to percolateDown
    public void percolateDown() {
        int hole = 0;
        E base = this.data[0];

        while ((4 * hole) + 1 <= this.size - 1) {
            int minIndex = (4 * hole) + 1;
            E minChild = this.data[minIndex];

            for (int i = 2; i <= 4 && (4 * hole) + i <= size - 1; i++) {
                if (this.data[(4 * hole) + i].compareTo(minChild) < 0) {
                    minIndex = (4 * hole) + i;
                    minChild = this.data[minIndex];
                }
            }

            if (minChild.compareTo(base) < 0) {
                this.data[hole] = minChild;
                hole = minIndex;
            } else {
                break;
            }
        }
        this.data[hole] = base;
    }

    @Override
    public int size() {
        return this.size;
        // throw new NotYetImplementedException();
    }

    @Override
    public void clear() {
        this.size = 0;
        // throw new NotYetImplementedException();
    }
}
