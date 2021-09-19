package org.bf.alg.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Time Cost: O(2n * log n)<br>
 *
 * @param <T>
 */
public class BinaryHeapSpeedupSelectionSort<T> implements Sort<T> {

    static class BinaryHeap<T> {
        int cnt;
        int[] heap;
        T[] array;
        Comparator<T> comparator;

        BinaryHeap(T[] array, int start, int end, Comparator<T> comparator) {
            this.array = Arrays.copyOfRange(array, start, end + 1);
            this.comparator = comparator;
            this.cnt = 0;
            this.heap = new int[array.length + 1];
            for (int i = 0; i < this.array.length; i++) insert(i);
        }

        void shiftUp(int i) {
            while (i > 1) {
                int parent = i / 2;
                if (comparator.compare(array[heap[parent]], array[heap[i]]) > 0) {
                    int tmp = heap[parent];
                    heap[parent] = heap[i];
                    heap[i] = tmp;
                    i = parent;
                } else break;
            }
        }

        void insert(int index) {
            heap[++cnt] = index;
            shiftUp(cnt);
        }

        void shiftDown() {
            int i = 1;
            int next = 2 * i;
            while (next <= cnt) {
                if (next < cnt && comparator.compare(array[heap[next + 1]], array[heap[next]]) < 0) next++;
                if (comparator.compare(array[heap[i]], array[heap[next]]) > 0) {
                    int tmp = heap[i];
                    heap[i] = heap[next];
                    heap[next] = tmp;
                    i = next;
                    next = 2 * i;
                } else break;
            }
        }

        T delete() {
            if (cnt <= 0) return null;
            int tmp = heap[1];
            heap[1] = heap[cnt];
            heap[cnt] = tmp;
            cnt--;
            shiftDown();
            return array[tmp];
        }
    }

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        BinaryHeap<T> binaryHeap = new BinaryHeap<>(array, start, end, comparator);
        for (int i = start; i <= end; i++) array[i] = binaryHeap.delete();
    }
}
