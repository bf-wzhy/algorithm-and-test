package org.bf.alg.sort;

import java.util.Comparator;

public class ShellSort<T> implements Sort<T> {

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        int gap = array.length;
        do {
            gap /= 2;
            for (int i = 0; i < gap; i++) {
                for (int j = i + gap; j < array.length; j += gap) {
                    int k = j - gap;
                    while (k >= 0 && comparator.compare(array[k], array[k + gap]) > 0) {
                        T temp = array[k];
                        array[k] = array[k + gap];
                        array[k + gap] = temp;
                        k -= gap;
                    }
                }
            }
        } while (gap != 1);
    }
}
