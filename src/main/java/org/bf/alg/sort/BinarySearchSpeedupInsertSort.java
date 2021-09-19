package org.bf.alg.sort;

import java.util.Comparator;

/**
 * Compare Time Cost: O(n * log n)<br>
 * Copy Time Cost: O(n<sup>2</sup>)
 */
public class BinarySearchSpeedupInsertSort<T> implements Sort<T> {

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        for (int i = start; i <= end; ++i) {
            int insertPosition = -1;
            int low = 0, high = i - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                if (comparator.compare(array[mid], array[i]) > 0) {
                    high = mid - 1;
                } else if (comparator.compare(array[mid], array[i]) < 0) {
                    low = mid + 1;
                } else {
                    insertPosition = mid;
                    break;
                }
            }
            if (insertPosition == -1) insertPosition = low;
            T tmp = array[i];
            System.arraycopy(array, insertPosition, array, insertPosition + 1, i - insertPosition);
            array[insertPosition] = tmp;
        }
    }
}
