package org.bf.alg.sort;

import java.util.Comparator;

/**
 * Time Cost: O(n<sup>2</sup>)<br>
 * Do not use it !!!
 */
public class InsertionSort<T> implements Sort<T> {

    static class LinkedListNode<T> {
        T val;
        LinkedListNode<T> next;

        LinkedListNode(T val) {
            this.val = val;
            this.next = null;
        }
    }

    private void insert(LinkedListNode<T> node, T val) {
        LinkedListNode<T> nextNode = new LinkedListNode<>(val);
        nextNode.next = node.next;
        node.next = nextNode;
    }

    @Override
    public void doSort(T[] array, int start, int end, Comparator<T> comparator) {
        if (array == null || comparator == null || start < 0 || start >= end || end >= array.length)
            throw new IllegalArgumentException();
        LinkedListNode<T> nonDataHead = new LinkedListNode<>(null);
        for (int i = start; i <= end; i++) {
            T t = array[i];
            LinkedListNode<T> current = nonDataHead.next;
            LinkedListNode<T> prev = nonDataHead;
            while (current != null) {
                if (comparator.compare(current.val, t) >= 0) break;
                prev = current;
                current = current.next;
            }
            insert(prev, t);
        }
        LinkedListNode<T> p = nonDataHead.next;
        int cnt = 0;
        while (p != null) {
            array[cnt++] = p.val;
            p = p.next;
        }
    }
}
