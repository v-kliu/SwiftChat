package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;

    private int size;
    private Comparator<E> comparator;

    // Constructor for our heap, set default size to 10
    public MinFourHeap(Comparator<E> comparator) { // TODO: CHECKK, also generics???
        this.data = (E[]) new Object[10];
        this.size = 0;
        this.comparator = comparator;
    }

    // Returns if stuff exists in our heap
    @Override
    public boolean hasWork() {
        return this.size != 0;
    }

    // Adds to our heap, maintaining structural properties and then percolates up
    @Override
    public void add(E work) {
        // Checks if we need to resize before adding to our heap which is implemented with an array
        if (this.size == this.data.length - 1) {
            // Resizes array by doubling size
            E[] newArray = (E[]) new Object [this.data.length * 2];
            for (int i = 0; i < this.data.length; i++) {
                newArray[i] = this.data[i];
            }
            this.data = newArray;
        }

        // Gets the index to insert new data, then inserts it, increments size
        int insertHole = percolateUp(this.size, work);
        this.data[insertHole] = work;
        size++;
    }

    // Function to percolateUp, returns the index of the hole to
    // insert new value
    public int percolateUp(int hole, E val) {

        // While we aren't at the root and the parent is a lesser value
        while (hole > 0 && comparator.compare(val, (this.data[(hole - 1) / 4])) < 0) {
            // Swap parent's data (don't care about child because we insert at end
            // it's just a 'hole')
            this.data[hole] = this.data[(hole - 1) / 4];
            // Gets to parent's index
            hole = (hole - 1) / 4;
        }
        // Returns the index for new data to be inserted
        return hole;
    }

    // Returns the minimum value (first value) in heap
    @Override
    public E peek() {
        // Exception handling
        if (!hasWork()) {
            throw new NoSuchElementException("Heap is empty in peek!");
        }
        // Returns first and min data in array/heap
        return this.data[0];
    }

    // Next function returns old value and then traverses to next data
    @Override
    public E next() {
        // Exception handling
        if (!hasWork()) {
            throw new NoSuchElementException("Heap is empty in next!");
        }
        // Stores the minimum value, brings last value to root, and decrements size
        E minElement = this.data[0];
        this.data[0] = this.data[this.size - 1];
        size--;
        // Begin to percolate down the root until heap is correct format
        percolateDown();

        // Return the data we deleted
        return minElement;
    }

    // Function to percolateDown, note how it is void, all restructuring occurs within this function
    public void percolateDown() {
        // Sets hole to root at first, gets root data
        int hole = 0;
        E base = this.data[0];

        // While a child node exists
        while ((4 * hole) + 1 <= this.size - 1) {
            // Originally set the min child and index to left most child
            int minIndex = (4 * hole) + 1;
            E minChild = this.data[minIndex];

            // For all other existing child nodes, find the min index and min child
            for (int i = 2; i <= 4 && (4 * hole) + i <= size - 1; i++) {
                if (comparator.compare(this.data[(4 * hole) + i], minChild) < 0) {
                    minIndex = (4 * hole) + i;
                    minChild = this.data[minIndex];
                }
            }

            // If our parent is larger than a child, we must swap
            if (comparator.compare(minChild, base) < 0) {
                // Swap and update hold index
                this.data[hole] = minChild;
                hole = minIndex;
            } else { // Else we break and since heap structure is good and hole is right index
                break;
            }
        }
        // Relate the original root data to hole
        this.data[hole] = base;
    }

    // Returns the size of heap
    @Override
    public int size() {
        return this.size;
    }

    // 'Clears' heap by setting size to 0
    @Override
    public void clear() {
        this.size = 0;
    }
}

