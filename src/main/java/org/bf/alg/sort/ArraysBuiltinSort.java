package org.bf.alg.sort;

import java.util.Arrays;
import java.util.Comparator;

public class ArraysBuiltinSort<T> implements Sort<T> {

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        Arrays.sort(array, start, end + 1, comparator);
    }
}
