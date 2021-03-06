package lambda;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItems;
import static lambda.Sort.sort;

import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GettingStartedTests {

    @Test
    public void sortingListByName() {
        GettingStarted gettingStarted = new GettingStarted();
        List<String> sortedNames = gettingStarted.sortListByName(GettingStarted.NAMES);

        assertThat(
            sortedNames,
            hasItems("Claire", "Francis", "John", "Peter")
        );
    }

    /**
     * When lambda expression use one parameter we can ommit the parenthesis
     */
    @Test
    public void withoutParenthesis() {
        List<Car> cars =
            Arrays.asList(
                new Car("citroen","ds3",5000.50),
                new Car("citroen","ds4",4000.50),
                new Car("citroen","ds5",3000.50)
            );

        List<Car> carsByModelAsc = sort(cars).by(car-> car.model);
        List<Car> carsByPriceAsc = sort(cars).by(car-> car.price);

        assertThat(carsByModelAsc.get(0).model, is("ds3"));
        assertThat(carsByPriceAsc.get(0).model, is("ds5"));
    }

}
