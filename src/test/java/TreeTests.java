import org.bf.alg.tree.BinarySearchTree;
import org.bf.alg.tree.RedBlackTree;
import org.bf.alg.tree.Tree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TreeTests {
    Set<Integer> data;

    @BeforeEach
    public void prepareData() {
        int size = 1<<18;
        data = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < size; i++)
            data.add(random.nextInt());
    }

    @RepeatedTest(80)
    public void testInsertion() {
        Tree<Integer> tree = new RedBlackTree<>(Comparator.comparingInt(o -> o));
        for (Integer i : data)
            tree.insert(i);
    }

    @RepeatedTest(80)
    public void testDeletion() {
        Tree<Integer> tree = new BinarySearchTree<>(Comparator.comparingInt(o -> o));
        for (Integer i : data)
            tree.insert(i);
        for (Integer i : data)
            tree.delete(i);
    }

}
