package org.bf.alg.sort;

import java.util.Comparator;

public class QuickSort<T> implements Sort<T> {

    static class State {
        int start;
        int end;
        State(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        State[] stack = new State[array.length];
        int top = -1;
        stack[++top] = new State(start, end);
        while (top != -1) {
            State s = stack[top--];
            int low = s.start, high = s.end;
            T key = array[low];
            while (high > low) {
                while (high > low && comparator.compare(array[high], key) >= 0) high--;
                if (comparator.compare(array[high], key) <= 0) {
                    T tmp = array[high];
                    array[high] = array[low];
                    array[low] = tmp;
                }
                while (high > low && comparator.compare(array[low], key) <= 0) low++;
                if (comparator.compare(array[low], key) >= 0) {
                    T tmp = array[low];
                    array[low] = array[high];
                    array[high] = tmp;
                }
            }
            if (high < s.end) stack[++top] = new State(high + 1, s.end);
            if (low > s.start) stack[++top] = new State(s.start, low - 1);
        }
    }
}
