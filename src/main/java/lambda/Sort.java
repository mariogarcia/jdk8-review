package lambda;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simply way of sorting a list
 *
 */
public class Sort<T> {

    private final List<T> items;

    private Sort(final List<T> items) {
        this.items = items;
    }

    public static <T> Sort<T> sort(final List<T> items) {
        return new Sort(items);
    }

    /**
     * Use Streams instead. This is not thread-safe
     */
    public List<T> by(final SortStrategy<T,Comparable> strategy) {
        Collections.sort(
            items,
            (a, b)-> strategy.get(a).compareTo(strategy.get(b))
        );

        return new ArrayList(items);
    }

}


