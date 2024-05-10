package p2.sorts;

import cse332.exceptions.NotYetImplementedException;

import java.util.Arrays;
import java.util.Comparator;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }
    public final static int CUTOFF = 10;

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        System.out.println(Arrays.toString(array));
        sort(array, 0, array.length - 1, comparator);
        // throw new NotYetImplementedException();
    }

    public static <E> void sort(E[] array, int lo, int hi, Comparator<E> comparator) {
        if (hi - lo <= 10) {
            insertionSort(array, lo, hi, comparator);
            // throw new NotYetImplementedException(); // do insertion sort
        } else {
            int pivot = (lo + hi) / 2;
            E pivotElement = array[pivot];
            swap(array, pivot, lo);

            int smaller = lo + 1;
            int bigger = hi;

            while (smaller < bigger) {
                while (smaller < bigger && comparator.compare(array[smaller], pivotElement) <= 0) {
                    smaller++;
                }
                while (bigger > smaller && comparator.compare(array[bigger], pivotElement) > 0) {
                    bigger--;
                }

                swap(array, smaller, bigger);
                smaller++;
                bigger--;
            }
            //System.out.println("right before" + Arrays.toString(array));
            swap(array, lo, bigger);

            //System.out.println("right after: " + Arrays.toString(array));

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

//    public static <E> int partition(E[] array, int lo, int hi, Comparator<E> comparator) {
//        E firstElement = array[lo];
//        E middleElement = array[(lo + hi) / 2];
//        E lastElement = array[hi];
//
//    }
//
//    public static <E> E getMax(E e1, E e2, Comparator<E> comparator) {
//        return comparator.compare(e1, e2) > 0 ? e1 : e2;
//    }
}
