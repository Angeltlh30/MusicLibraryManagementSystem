package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {

    public static <T> List<T> sort(List<T> items, Comparator<T> comparator) {
        List<T> copy = new ArrayList<>(items);
        if (copy.size() <= 1) {
            return copy;
        }
        int mid = copy.size() / 2;
        List<T> left = sort(copy.subList(0, mid), comparator);
        List<T> right = sort(copy.subList(mid, copy.size()), comparator);
        return merge(left, right, comparator);
    }

    private static <T> List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        List<T> result = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }
        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }
        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }
        return result;
    }
}
