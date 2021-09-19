package org.bf.alg.sort;

import java.util.Comparator;

/**
 * Time Cost: O(n<sup>2</sup>)<br>
 * Do not use it !!!
 */
public class SelectionSort<T> implements Sort<T> {

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        T[] t = array.clone();
        int[] visited = new int[array.length];
        for (int i = start; i <= end; i++) {
            int next = -1;
            for (int j = start; j <= end; j++) {
                if (visited[j] == 0)
                    next = next == -1 || comparator.compare(t[j], t[next]) < 0 ? j : next;
            }
            visited[next] = 1;
            array[i] = t[next];
        }
    }
}
