package org.bf.alg.tree;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class BinarySearchTree<K> {

    static final class Node<K> {

        K key;
        Node<K> parent;
        Node<K> left;
        Node<K> right;

        Node(@NotNull K key, Node<K> parent, Node<K> left, Node<K> right) {
            this.key = key;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        Node(K key, Node<K> parent) {
            this(key, parent, null, null);
        }

        Node(K key) {
            this(key, null);
        }
    }

    private transient Node<K> root;

    private final Comparator<? super K> comparator;

    public BinarySearchTree(@NotNull Comparator<? super K> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object o1, Object o2) {
        if (o1 == null) throw new NullPointerException();
        return comparator.compare((K) o1, (K) o2);
    }

    private Node<K> parentOf(Node<K> node) {
        return node == null ? null : node.parent;
    }

    private Node<K> leftOf(Node<K> node) {
        return node == null ? null : node.left;
    }

    private Node<K> rightOf(Node<K> node) {
        return node == null ? null : node.right;
    }

    private Node<K> searchAndInsertImpl(@NotNull K key, boolean needInsert) {
        Node<K> x = root;
        Node<K> p = null;
        int cmp;
        while (x != null) {
            p = x;
            cmp = compare(key, x.key);
            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else
                return x;
        }
        if (!needInsert) return null;
        if (p == null) {
            root = new Node<>(key);
            return root;
        }
        cmp = compare(key, p.key);
        if (cmp < 0)
            p.left = new Node<>(key, p);
        else
            p.right = new Node<>(key, p);
        return p;
    }

    private @NotNull Node<K> getSuccessor(@NotNull Node<K> node) {
        Node<K> x = node.right;
        Node<K> p = node;
        while (x != null) {
            p = x;
            x = x.left;
        }
        if (p != node) return p;
        x = node.left;
        while (x != null) {
            p = x;
            x = x.right;
        }
        return p;
    }

    /**
     * 插入一个键。若键已存在，不做任何处理。
     */
    public void insert(@NotNull K key) {
        searchAndInsertImpl(key, true);
    }

    /**
     * 删除指定的键。<br>
     */
    public void delete(@NotNull K key) {
        if (root == null) return;
        Node<K> x = searchAndInsertImpl(key, false);
        while (x != null) {
            if (x.left == null && x.right == null) {
                if (x.parent == null)
                    root = null;
                else if (x == leftOf(parentOf(x)))
                    x.parent.left = null;
                else
                    x.parent.right = null;
                x = null;
            } else if (x.left == null) {
                if (x.parent == null)
                    root = x.right;
                else if (x == leftOf(parentOf(x)))
                    x.parent.left = x.right;
                else
                    x.parent.right = x.right;
                x.right.parent = x.parent;
                x = null;
            } else if (x.right == null) {
                if (x.parent == null)
                    root = x.left;
                else if (x == leftOf(parentOf(x)))
                    x.parent.left = x.left;
                else
                    x.parent.right = x.left;
                x.left.parent = x.parent;
                x = null;
            } else {
                Node<K> successor = getSuccessor(x);
                x.key = successor.key;
                x = successor;
            }
        }
    }

}
