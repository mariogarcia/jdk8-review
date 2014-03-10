package lambda;

import static java.util.Collections.sort;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

/**
 * First lambda sample
 */
public class GettingStarted {

    static final List<String> NAMES = Arrays.asList("John", "Peter", "Francis", "Claire");
    static interface Action<T> {
        public T execute();
    }

    public List<String> sortListByName(List<String> names) {
        // Dont worry Streams will be much better
        sort(names, (a, b)-> a.compareTo(b));
        return names;
    }



    public Car buildCarWith(Action<Car> builder) {
        return builder.execute();
    }

}
