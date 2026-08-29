package algorithms;

import java.util.List;
import java.util.Random;

public class Shuffle {

    public static <T> void shuffle(List<T> list) {
        Random random = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }
}
