package org.bf.alg.sort;

import java.util.Arrays;
import java.util.Comparator;

public class MergeSort<T> implements Sort<T> {

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || end >= array.length)
            throw new IllegalArgumentException();
        if (start >= end) return;
        if (start + 1 == end && comparator.compare(array[start], array[end]) > 0) {
            T tmp = array[start];
            array[start] = array[end];
            array[end] = tmp;
            return;
        }
        int mid = (start + end) / 2;
        doSort(array, start, mid, comparator);
        doSort(array, mid + 1, end, comparator);
        merge(array, start, mid, end, comparator);
    }

    private void merge(T[] array, int start, int mid, int end, Comparator<T> comparator) {
        int p = start;
        int q = mid + 1;
        T[] copy = Arrays.copyOfRange(array, start, end + 1);
        int cnt = -1;
        while (p <= mid && q <= end) {
            if (comparator.compare(array[p], array[q]) < 0) copy[++cnt] = array[p++];
            else copy[++cnt] = array[q++];
        }
        while (p <= mid) copy[++cnt] = array[p++];
        while (q <= end) copy[++cnt] = array[q++];
        System.arraycopy(copy, 0, array, start, end - start + 1);
    }
}
