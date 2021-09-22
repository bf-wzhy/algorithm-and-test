import org.bf.alg.sort.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.*;
import java.util.stream.Collectors;

public class SortTests {

    Integer[] data;

    @BeforeEach
    public void prepareData() {
        int size = 1<<20;
        data = new Integer[size];
        Random random = new Random();
        for (int i = 0; i < size; i++)
            data[i] = random.nextInt();
    }

    @RepeatedTest(8)
    public void sortTest() {
        Integer[] testArray = Arrays.copyOf(data, data.length);
        Integer[] standerArray = Arrays.copyOf(data, data.length);
        Sort<Integer> sort = new ArraysBuiltinSort<>();
        sort.doSort(testArray, 0, testArray.length - 1, Comparator.comparingInt(o -> o));
        Arrays.sort(standerArray);
        List<Integer> testList = Arrays.stream(testArray).sorted().collect(Collectors.toList());
        List<Integer> standerList = Arrays.stream(standerArray).sorted().collect(Collectors.toList());
        assert testList.equals(standerList);
    }

}
