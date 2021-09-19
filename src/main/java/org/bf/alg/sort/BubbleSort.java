package org.bf.alg.sort;

import java.util.Comparator;

/**
 * Time Cost: O(n<sup>2</sup>)<br>
 * Do not use it !!!
 */
public class BubbleSort<T> implements Sort<T> {

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        for (int i = start; i < end - start; ++i) {
            for (int j = start + 1; j < end - start + 1 - i; ++j) {
                if (comparator.compare(array[j], array[j - 1]) < 0) {
                    T tmp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = tmp;
                }
            }
        }
    }
}
