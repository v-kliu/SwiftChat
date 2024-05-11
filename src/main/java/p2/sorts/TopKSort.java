package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }


    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        // KEY IDEA: to maintain runtime, never have more than K elements in heap

        // Defensive programming, make sure k is within bounds
        if (k > array.length) {
            k = array.length;
        }

        // Create a heap with k elements
        MinFourHeap<E> newHeap = new MinFourHeap<>(comparator);
        for (int i = 0; i < k; i++) {
            newHeap.add(array[i]);
        }

        // For the reamining elements, if they are larger than the smallest of
        // our current K elements, remove min and add new element, peek is O(1)
        for (int i = k; i < array.length; i++) {
            if (comparator.compare(array[i], newHeap.peek()) > 0) {
                newHeap.next();
                newHeap.add(array[i]);
            }
        }

        // Copy largest events (k elements) into original array
        for (int i = 0; i < k; i++) {
            array[i] = newHeap.next();
        }

        // Set the rest of the array to null
        for (int i = k; i < array.length; i++) {
            array[i] = null;
        }
    }
}
