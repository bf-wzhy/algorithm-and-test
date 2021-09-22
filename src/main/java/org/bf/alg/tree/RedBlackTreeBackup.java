package org.bf.alg.tree;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * Properties:<br>
 * （1）每个节点或者是黑色，或者是红色。<br>
 * （2）根节点是黑色。<br>
 * （3）每个叶子节点（NIL）是黑色。<br>
 * （4）如果一个节点是红色的，则它的子节点必须是黑色的。<br>
 * （5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。<br>
 */
public class RedBlackTreeBackup<E> {

    public static class Node<E> {

        E key;
        boolean isRed;
        Node<E> left;
        Node<E> right;

        Node(@NotNull E key, boolean isRed, Node<E> left, Node<E> right) {
            this.key = key;
            this.isRed = isRed;
            this.left = left;
            this.right = right;
        }

        Node(E key, boolean isRed) {
            this(key, isRed, null, null);
        }

        Node(E key) {
            this(key, true);
        }

        public final E getKey() {
            return this.key;
        }

        public final boolean isRed() {
            return this.isRed;
        }

        public final Node<E> getLeft() {
            return this.left;
        }

        public final Node<E> getRight() {
            return this.right;
        }
    }

    static class NodeDescription<E> {

        Node<E> root;
        Node<E> parent;
        boolean isLeft;
        Node<E> ref;

        /**
         * 定位键在树中的节点位置，将描述信息封装起来，方便旋转、变色、插入、删除操作
         */
        NodeDescription(Node<E> root, E key, Comparator<E> comparator) {
            this.root = root;
            this.ref = null;
            if (root != null && key != null) {
                Node<E> c = root;
                Node<E> p = null;
                while (c != null) {
                    if (comparator.compare(key, c.key) == 0) break;
                    p = c;
                    if (comparator.compare(key, c.key) < 0) {
                        c = c.left;
                        this.isLeft = true;
                    } else {
                        c = c.right;
                        this.isLeft = false;
                    }
                }
                this.parent = p;
                this.ref = c;
            }
        }

        NodeDescription(Node<E> root, Node<E> parent, boolean isLeft, Node<E> ref) {
            this.root = root;
            this.parent = parent;
            this.isLeft = isLeft;
            this.ref = ref;
        }
    }

    Node<E> root;
    Comparator<E> comparator;

    public RedBlackTreeBackup(@NotNull Comparator<E> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    public NodeDescription<E> search(@NotNull E key) {
        return new NodeDescription<>(root, key, comparator);
    }

    private void leftRotation(NodeDescription<E> nodeDescription) {
        if (nodeDescription.parent != null) {
            if (nodeDescription.isLeft) nodeDescription.parent.left = nodeDescription.ref.right;
            else nodeDescription.parent.right = nodeDescription.ref.right;
        } else {
            root = nodeDescription.ref.right;
        }
        Node<E> tmp = nodeDescription.ref.right;
        nodeDescription.ref.right = tmp.left;
        tmp.left = nodeDescription.ref;
    }

    private void rightRotation(NodeDescription<E> nodeDescription) {
        if (nodeDescription.parent != null) {
            if (nodeDescription.isLeft) nodeDescription.parent.left = nodeDescription.ref.left;
            else nodeDescription.parent.right = nodeDescription.ref.left;
        } else {
            root = nodeDescription.ref.left;
        }
        Node<E> tmp = nodeDescription.ref.left;
        nodeDescription.ref.left = tmp.right;
        tmp.right = nodeDescription.ref;
    }

    /**
     * 插入一个键。插入的位置总是在叶子节点处，因为查找总是在叶子节点处失败的。<br>
     * 若键已存在，不做任何处理。因此这个方法可以用来去重。<br>
     * 为满足红黑树对节点颜色的要求，插入之后会触发修正颜色或旋转的操作。
     */
    public void insert(@NotNull E key) {
        if (root == null) {
            root = new Node<>(key, false);
            return;
        }
        NodeDescription<E> nodeDescription = search(key);
        if (nodeDescription.ref != null) return; // key already exists
        Node<E> parent = nodeDescription.parent;
        if (nodeDescription.isLeft) {
            parent.left = new Node<>(key, true);
            nodeDescription.ref = parent.left;
        } else {
            parent.right = new Node<>(key, true);
            nodeDescription.ref = parent.right;
        }
        fixupAfterInsert(nodeDescription);
    }

    private void fixupAfterInsert(@NotNull NodeDescription<E> nodeDescription) {
        if (nodeDescription.parent == null) {
            nodeDescription.ref.isRed = false;
            return;
        }
        NodeDescription<E> parentDescription = search(nodeDescription.parent.key);
        Node<E> parent = parentDescription.ref;
        Node<E> current = nodeDescription.ref;
        Node<E> brother = nodeDescription.isLeft ? parent.right : parent.left;
        if (!parent.isRed && brother != null && brother.isRed) {
            // color flip
            current.isRed = false;
            brother.isRed = false;
            if (parentDescription.parent != null) {
                parent.isRed = true;
                fixupAfterInsert(parentDescription);
            }
            return;
        }
        if (parent.isRed) {
            NodeDescription<E> grandparentDescription = search(parentDescription.parent.key);
            Node<E> grandparent = grandparentDescription.ref;
            if (!parentDescription.isLeft) {
                if (!nodeDescription.isLeft) {
                    // single rotation
                    leftRotation(grandparentDescription);
                    brother = grandparent;
                } else {
                    // double rotation
                    rightRotation(parentDescription);
                    leftRotation(grandparentDescription);
                    brother = grandparent;
                    Node<E> tmp = parent;
                    parent = current;
                    current = tmp;
                }
            } else {
                if (nodeDescription.isLeft) {
                    // single rotation
                    rightRotation(grandparentDescription);
                    brother = grandparent;
                } else {
                    // double rotation
                    leftRotation(parentDescription);
                    rightRotation(grandparentDescription);
                    brother = grandparent;
                    Node<E> tmp = parent;
                    parent = current;
                    current = tmp;
                }
            }
            current.isRed = false;
            brother.isRed = false;
            parent.isRed = true;
            NodeDescription<E> next = search(parent.key);
            fixupAfterInsert(next);
        }
    }

    private boolean isBlack(Node<E> node) {
        return node == null || !node.isRed;
    }

//    private void fixupAfterDelete(@NotNull NodeDescription<E> nodeDescription) {
//        if (nodeDescription.parent == null) return;
//        NodeDescription<E> parentDescription = search(nodeDescription.parent.key);
//        Node<E> parent = nodeDescription.parent;
//        if (nodeDescription.isLeft) {
//            Node<E> brother = parent.right;
//            NodeDescription<E> brotherDescription = search(brother.key);
//            if (parent.isRed) {
//                if (isBlack(brother.left) && isBlack(brother.right)) {
//                    nodeDescription.parent.isRed = false;
//                    brother.isRed = true;
//                } else if (isBlack(brother.right)) {
//                    parent.isRed = false;
//                    rightRotation(brotherDescription);
//                    leftRotation(parentDescription);
//                } else if (isBlack(brother.left)) {
//                    parent.isRed = false;
//                    brother.isRed = true;
//                    brother.right.isRed = false;
//                    leftRotation(parentDescription);
//                }
//            } else {
//                if (isBlack(brother.left) && isBlack(brother.right)) {
//                    brother.isRed = true;
//                    fixupAfterDelete(parentDescription);
//                } else if (isBlack(brother.right)) {
//                    NodeDescription<E> reportNode = new NodeDescription<E>(nodeDescription.root,
//                            parentDescription.parent, parentDescription.isLeft, brother.left);
//                    rightRotation(brotherDescription);
//                    leftRotation(parentDescription);
//                    fixupAfterInsert(reportNode);
//                } else if (isBlack(brother.left)) {
//                    NodeDescription<E> reportNode = new NodeDescription<E>(nodeDescription.root,
//                            parentDescription.parent, parentDescription.isLeft, brother.right);
//                    brother.isRed = true;
//                    brother.right.isRed = false;
//                    leftRotation(parentDescription);
//                    fixupAfterInsert(reportNode);
//                } else {
//                    nodeDescription.ref.key = parent.key;
//                    parent.key = brother.left.key;
//                    brother.left = null;
//                    brother.isRed = false;
//                    brother.right.isRed = true;
//                }
//            }
//        } else {
//            Node<E> brother = parent.left;
//            NodeDescription<E> brotherDescription = search(brother.key);
//            if (parent.isRed) {
//                // assert brother != null && !brother.isRed
//                if (isBlack(brother.left) && isBlack(brother.right)) {
//                    nodeDescription.parent.isRed = false;
//                    brother.isRed = true;
//                } else if (isBlack(brother.left)) {
//                    parent.isRed = false;
//                    leftRotation(brotherDescription);
//                    rightRotation(parentDescription);
//                } else if (isBlack(brother.right)) {
//                    parent.isRed = false;
//                    brother.isRed = true;
//                    brother.left.isRed = false;
//                    rightRotation(parentDescription);
//                }
//            } else {
//                if (isBlack(brother.left) && isBlack(brother.right)) {
//                    brother.isRed = true;
//                    fixupAfterDelete(parentDescription);
//                } else if (isBlack(brother.left)) {
//                    NodeDescription<E> reportNode = new NodeDescription<E>(nodeDescription.root,
//                            parentDescription.parent, parentDescription.isLeft, brother.right);
//                    leftRotation(brotherDescription);
//                    rightRotation(parentDescription);
//                    fixupAfterInsert(reportNode);
//                } else if (isBlack(brother.right)) {
//                    NodeDescription<E> reportNode = new NodeDescription<E>(nodeDescription.root,
//                            parentDescription.parent, parentDescription.isLeft, brother.left);
//                    brother.isRed = true;
//                    brother.left.isRed = false;
//                    rightRotation(parentDescription);
//                    fixupAfterInsert(reportNode);
//                } else {
//                    nodeDescription.ref.key = parent.key;
//                    parent.key = brother.right.key;
//                    brother.right = null;
//                    brother.isRed = false;
//                    brother.left.isRed = true;
//                }
//            }
//        }
//    }

    /**
     * 查找后继者
     */
    private NodeDescription<E> getSuccessor(@NotNull NodeDescription<E> nodeDescription) {
        NodeDescription<E> successorFromRight = null;
        if (nodeDescription.ref.right != null) {
            Node<E> c = nodeDescription.ref.right;
            Node<E> p = nodeDescription.ref;
            while (c.left != null) {
                p = c;
                c = c.left;
            }
            successorFromRight = new NodeDescription<>(nodeDescription.root, p, p != nodeDescription.ref, c);
            if (c.isRed) return successorFromRight;
        }
        NodeDescription<E> successorFromLeft = null;
        if (nodeDescription.ref.left != null) {
            Node<E> c = nodeDescription.ref.left;
            Node<E> p = nodeDescription.ref;
            while (c.right != null) {
                p = c;
                c = c.right;
            }
            successorFromLeft = new NodeDescription<>(nodeDescription.root, p, p == nodeDescription.ref, c);
            if (c.isRed) return successorFromLeft;
        }
        return successorFromRight != null ? successorFromRight : successorFromLeft;
    }

    private void deletePathLast(@NotNull NodeDescription<E> pathLastNode) {
        if (pathLastNode.parent == null) {
            root = null;
            return;
        }
        if (pathLastNode.isLeft) pathLastNode.parent.left = null;
        else pathLastNode.parent.right = null;
        //if (!pathLastNode.ref.isRed) fixupAfterDelete(pathLastNode);
    }

    public void delete(@NotNull E key) {
        if (root == null) return;
        NodeDescription<E> nodeDescription = search(key);
        Node<E> current = nodeDescription.ref;
        if (current == null) return;
        if (current.left == null && current.right == null) {
            deletePathLast(nodeDescription);
            return;
        }
        NodeDescription<E> successor = getSuccessor(nodeDescription);
        nodeDescription.ref.key = successor.ref.key;
        deletePathLast(successor);
    }

//    public void delete(@NotNull E key) {
//        if (root == null) return;
//        NodeDescription<E> nodeDescription = search(key);
//        Node<E> current = nodeDescription.ref;
//        if (current == null) return;
//        if (current.isRed) {
//            if (current.left == null && current.right == null) {
//                if (nodeDescription.parent == null) root = null;
//                else if (nodeDescription.isLeft) nodeDescription.parent.left = null;
//                else nodeDescription.parent.right = null;
//                return;
//            }
//            NodeDescription<E> replaceNode = getMinInRight(nodeDescription);
//            current.key = replaceNode.ref.key;
//            if (replaceNode.isLeft) replaceNode.parent.left = null;
//            else replaceNode.parent.right = null;
//            if (!replaceNode.ref.isRed) fixupAfterDelete(replaceNode);
//            return;
//        }
//        // assert current-node' color is black
//        if (current.left == null && current.right == null) {
//            if (nodeDescription.parent == null) {
//                root = null;
//                return;
//            }
//            if (nodeDescription.isLeft) nodeDescription.parent.left = null;
//            else nodeDescription.parent.right = null;
//            fixupAfterDelete(nodeDescription);
//            return;
//        }
//        if (current.left == null) {
//            current.key = current.right.key;
//            current.right = null;
//        } else if (current.right == null) {
//            current.key = current.left.key;
//            current.left = null;
//        } else {
//            NodeDescription<E> replaceNode = getMinInRight(nodeDescription);
//            current.key = replaceNode.ref.key;
//            if (replaceNode.isLeft) replaceNode.parent.left = null;
//            else replaceNode.parent.right = null;
//            if (!replaceNode.ref.isRed) fixupAfterDelete(replaceNode);
//        }
//    }

    public final void yields(Node<E> node, List<E> addHere) {
        if (node == null) return;
        yields(node.left, addHere);
        addHere.add(node.key);
        yields(node.right, addHere);
    }

    public final Node<E> getRoot() {
        return this.root;
    }
}
