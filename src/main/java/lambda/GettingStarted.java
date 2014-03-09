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

    private static final List<String> NAMES = Arrays.asList("John", "Peter", "Francis", "Claire");

    List<String> sortListByName(List<String> names) {
        // Dont worry Streams will be much better
        sort(names, (a, b)-> a.compareTo(b));
        return names;
    }

}
