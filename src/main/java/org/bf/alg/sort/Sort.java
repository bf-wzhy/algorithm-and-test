package org.bf.alg.sort;

import java.util.Comparator;

public interface Sort<T> {
    /**
     * Sort the array using the specified comparator
     * @param array needs sorting
     * @param start element location
     * @param end element location
     * @param comparator decides sort order
     * @return sorted array
     */
    void doSort(T[] array, int start, int end, Comparator<T> comparator);
}
