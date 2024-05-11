package p2.sorts;

import cse332.exceptions.NotYetImplementedException;

import java.util.Arrays;
import java.util.Comparator;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }
    public final static int CUTOFF = 10;

    // Main sort function
    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, 0, array.length - 1, comparator);
    }

    // Helper method to sort area (is recursive and has additional parameters to specify range)
    public static <E> void sort(E[] array, int lo, int hi, Comparator<E> comparator) {
        // If range is below a threshold, use insertion sort
        if (hi - lo <= CUTOFF) {
            insertionSort(array, lo, hi, comparator);
        } else { // Else use quick sort
            // Pick a pivot (just gets median), stores value, and moves it to first position
            int pivot = (lo + hi) / 2;
            E pivotElement = array[pivot];
            swap(array, pivot, lo);

            // Gets 'pointers' for quick sort, represent indices
            int smaller = lo + 1;
            int bigger = hi;

            // While our pointers haven't crossed
            while (smaller <= bigger) {
                // While haven't crossed, we are moving smallest pointer forward till
                // we find a value that is greater than the pivot
                while (smaller <= bigger && comparator.compare(array[smaller], pivotElement) <= 0) {
                    smaller++;
                }
                // While haven't crossed, we are moving biggest pointer backward till
                // we find a value that is less than the pivot
                while (bigger >= smaller && comparator.compare(array[bigger], pivotElement) > 0) {
                    bigger--;
                }
                // At this point both pointers are pointing to an element that is in the wrong
                // spot and that needs to be swapped

                // Additional error checking, then swap elements, and modify pointer indexes
                if (bigger > smaller) {
                    swap(array, smaller, bigger);
                    smaller++;
                    bigger--;
                }
            }
            // Move pivot back to where it belongs
            swap(array, lo, bigger);

            // Perform recursive calls on two smaller subarrays
            sort(array, lo, bigger - 1, comparator);
            sort(array, bigger + 1, hi, comparator);
        }
    }

    // Method to swap two elements in an array
    public static <E> void swap(E[] array, int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Helper function for insertion sort (just has additional parameters to specify range)
    public static <E> void insertionSort(E[] array, int lo, int hi, Comparator<E> comparator) {
        for (int i = lo + 1; i <= hi; i++) {
            E x = array[i];
            int j;
            for (j=i-1; j>=0; j--) {
                if (comparator.compare(x, array[j]) >= 0) { break; }
                array[j + 1] = array[j];
            }
            array[j + 1] = x;
        }
    }
}
