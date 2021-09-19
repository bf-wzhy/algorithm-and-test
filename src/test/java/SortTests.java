import org.bf.alg.sort.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.*;

public class SortTests {
    static Integer[] array;
    @BeforeEach
    public void init() {
        int size = 1<<14;
        array = new Integer[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100);
        }
    }

    @RepeatedTest(8)
    public void sortTest() {
        Sort<Integer> sort = new BucketSort<>();
        sort.doSort(array, 0, array.length - 1, Comparator.comparingInt(o -> o));
        boolean testResult = true;
        for (int i = 0; i < array.length - 1; ++i) {
            if (array[i] > array[i + 1]) {
                testResult = false;
                break;
            }
        }
        assert testResult;
    }

}
