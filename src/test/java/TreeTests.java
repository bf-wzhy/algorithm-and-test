import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TreeTests {
    Set<Integer> data;

    @BeforeEach
    public void prepareData() {
        int size = 1<<20;
        data = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < size; i++)
            data.add(random.nextInt());
    }
}
