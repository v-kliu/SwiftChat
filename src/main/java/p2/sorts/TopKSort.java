package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }


    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        if (k > array.length) {
            k = array.length;
        }

        MinFourHeap<E> newHeap = new MinFourHeap<>(comparator);
        for (int i = 0; i < k; i++) {
            newHeap.add(array[i]);
        }

        for (int i = k; i < array.length; i++) {
            if (comparator.compare(array[i], newHeap.peek()) > 0) {
                newHeap.next();
                newHeap.add(array[i]);
            }
        }

        for (int i = 0; i < k; i++) {
            array[i] = newHeap.next();
        }

        for (int i = k; i < array.length; i++) {
            array[i] = null;
        }
    }
}
