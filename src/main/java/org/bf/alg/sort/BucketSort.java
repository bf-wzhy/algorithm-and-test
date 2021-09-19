package org.bf.alg.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

public class BucketSort<T> implements Sort<T> {

    static class LinkedListNode<T> {
        T val;
        LinkedListNode<T> next;

        LinkedListNode(T val) {
            this.val = val;
            this.next = null;
        }
    }

    static class Bucket<T> {
        LinkedListNode<T> head;
    }

    private void insert(@NotNull Bucket<T> bucket, T val, Comparator<T> comparator) {
        LinkedListNode<T> p = bucket.head;
        LinkedListNode<T> s = null;
        while (p != null) {
            if (comparator.compare(p.val, val) > 0) break;
            s = p;
            p = p.next;
        }
        LinkedListNode<T> target = new LinkedListNode<>(val);
        if (s != null) {
            target.next = s.next;
            s.next = target;
        } else {
            bucket.head = target;
        }
    }

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        int M = 2;
        int len = end - start + 1;
        while (M < len / 4) M <<= 1;
        Bucket<T>[] bucket = new Bucket[M];
        for (int i = start; i <= end; i++) {
            int bucketId = array[i].hashCode() & (M - 1);
            if (bucket[bucketId] == null) {
                bucket[bucketId] = new Bucket<T>();
                bucket[bucketId].head = new LinkedListNode<>(array[i]);
                continue;
            }
            insert(bucket[bucketId], array[i], comparator);
        }
        Sort<Bucket<T>> sort = new MergeSort<>();
        Comparator<Bucket<T>> c = (b1, b2) -> {
            if (b1 != null && b1.head != null) {
                if (b2 != null && b2.head != null) return comparator.compare(b1.head.val, b2.head.val);
                return -1;
            }
            if (b2 != null && b2.head != null) return 1;
            return 0;
        };
        sort.doSort(bucket, 0, M - 1, c);
        int cnt = start;
        while (bucket[0].head != null) {
            array[cnt++] = bucket[0].head.val;
            bucket[0].head = bucket[0].head.next;
            sort.doSort(bucket, 0, M - 1, c);
        }
    }
}
