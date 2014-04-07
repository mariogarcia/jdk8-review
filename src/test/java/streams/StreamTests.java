package streams;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItems;
import static lambda.Sort.sort;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.groupingBy;


import java.math.BigInteger;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.IntSummaryStatistics;
import java.util.function.Supplier;
import java.util.function.BinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import lambda.Car;
import lambda.Author;

/**
 * Exercises for using the Stream API of JDK8
 */
@RunWith(JUnit4.class)
public  class StreamTests {

    private static List<String> words = Arrays.asList(
        "consequences", "happiness", "knowledge"
    );

    private static Stream<Car> cars = Stream.of(
        new Car("citroen","ds3",5000.50),
        new Car("citroen","ds4",4000.50),
        new Car("citroen","ds5",3000.50)
    );

    @Test
    public void iterationVersusStreams() {
     /* Classical bad way of incrementing a counter */
        int letterCount = 0;
        for(String word: words) {
            letterCount += word.length();
        }
        assertThat(letterCount, is(30));
        assertThat(
            words.stream().mapToInt(x -> x.length()).sum(),
            is(letterCount)
        );
    }

    @Test
    public void streamGeneration() {
        // Creating a stream from a list of strings
        Stream<String> song = Stream.of("some", "list", "of", "song");
        assertThat(song.count(), is(4L));

        // Creating an EMPTY stream
        Stream<String> emptyStream = Stream.empty();
        assertThat(emptyStream.count(), is(0L));

        // Generating the content of a given stream
        Stream<String> echos = Stream.generate(()-> "Echo");
        Optional<String> echo = echos.findFirst();
        assertThat(echo.get(), is("Echo"));

        // Generating a given number of random entries using reference methods
        Stream<Double> randoms = Stream.generate(Math::random).limit(1);
        Optional<Double> randomNumber = randoms.findFirst();
        assertThat(randomNumber.isPresent(), is(true));

        // Generating a stream from a regular expression
        Stream<String> tokens = Stream.of("something really cool".split("\\s")).skip(1);
        assertThat(tokens.count(), is(2L));

        // Generating a given stream as a result of mixing two previous streams
        Stream<String> concatentation = Stream.concat(Stream.of("Hello"), Stream.of("World"));
        assertThat(concatentation.count(), is(2L));

        // Generating a stream using iterate(...)
        List<Double> powers = Stream.iterate(1.0, p -> p + 2).
                peek(e -> System.out.println("Fetching " + e)).
                limit(20).
                collect(Collectors.toList());
        assertThat(powers.size(), is(20));
    }

    @Test
    public void filterMapAndFlatMap() {
        // Filtering a list of cars by their price
        Stream<Car> lessEqualFourThousand = cars.filter( car -> car.price >= 4000);
        assertThat(lessEqualFourThousand.count(), is(2L));

        // Transforming the values of a given stream by using map
        Stream<String> transformedStrings = words.stream().map(String::toUpperCase);
        List<String> toUpperCaseValues = transformedStrings.collect(Collectors.toList());
        assertThat(toUpperCaseValues, hasItems("CONSEQUENCES", "HAPPINESS", "KNOWLEDGE"));
    }

    @Test
    public void substreamsAndCombiningStreams() {
        // Limiting the number of items in an infinite stream
        Stream<Double> randomNumbers = Stream.generate(Math::random).limit(100);
        assertThat(randomNumbers.count(), is(100L));

        // Using ranges for mapping an arithmetic progression
        // Ranges upper bound is not inclusive
        IntUnaryOperator arithmetic = n -> 1 + (n - 1) * 5;
        OptionalInt an =
            IntStream.
                range(1, 6).
                map(arithmetic).
                skip(4).
            findFirst();

        assertThat(an.getAsInt(), is(21));

        //Concatenating two streams
        List<String> combined =
            Stream.concat(
                Stream.of("Hello"),
                Stream.of("JDK8")
            ).collect(Collectors.toList());

        assertThat(combined, hasItems("Hello", "JDK8"));
    }

    @Test
    public void statefulTransformations() {
        //Distinct words
        Stream<String> advices = Stream.of("practice", "practice", "practice").distinct();
        assertThat(advices.count(), is(1L));

        //Sorting words by length
        Stream<String> longestsFirst =
            Stream.of("This", "is", "getting", "better").
                sorted(Comparator.comparing(String::length).reversed());

        Optional<String> optional = longestsFirst.findFirst();
        assertThat(optional.get(), is("getting"));
    }

    @Test
    public void simpleReductions() {
        //Getting largest word
        Optional<String> largest =
            Stream.of("a", "b", "y", "z").
                max(String::compareToIgnoreCase);
        assertThat(largest.get(), is("z"));

        //Getting a particular element
        Optional<String> startingWithL =
            Stream.of("Something", "Love", "Liberation").
            filter(w -> w.startsWith("L")).
        findFirst();
        assertThat(startingWithL.get(), is("Love"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void optionalType() {
        //Creating and transforming an optional instance
        Optional<String> optional = Optional.of("Something");
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), is("Something"));
        assertThat(optional.map(v -> v.toUpperCase()).get(), is("SOMETHING"));

        //Nullable optional
        Optional<String> nullable = Optional.ofNullable(null);
        Supplier<String> supplier = () -> "Some value";
        assertThat(nullable.isPresent(), is(false));
        assertThat(nullable.orElse("option"), is("option"));
        assertThat(nullable.orElseGet(supplier), is("Some value"));

        nullable.orElseThrow(IllegalArgumentException::new);
    }

    @Test
    public void composingOptional() {
        // Transforming an optional into another optional
        Stream<Car> fleet = Stream.of(new Car("citroen","ds3",5000.50));
        Optional<String> firstModel = fleet.findFirst().flatMap(Car::getModel);
        assertThat(firstModel.get(), is("ds3"));

        //What happens if the transformation turns to end in a nullable
        Stream<Car> poorFleet = Stream.of(new Car("citroen","ds3",5000.50));
        Optional<String> model =
                poorFleet.
                    filter(car -> car.brand == "ford").
                    findFirst().
                    flatMap(Car::getModel);
        assertThat(model.isPresent(), is(false));
    }

    @Test
    public void reductionOperations() {
        // Computing total length of all names within the following stream
        Stream<String> names = Stream.of("John", "Peter", "Roger");
        Optional<Integer> namesLength = names.map(String::length).reduce((x, y) -> x + y);

        assertThat(namesLength.isPresent(), is(true));
        assertThat(namesLength.get(), is(14));

        IntUnaryOperator arithmetic = n -> 1 + (n - 1) * 5;
        // Ranges upper bound is not inclusive
        OptionalInt an1 =
            IntStream.
                range(1, 7).
                map(arithmetic).
                peek(x -> System.out.println(x)). // Sneaking into the values
                reduce((x, y) -> x + y);
        assertThat(an1.isPresent(), is(true));
        assertThat(an1.getAsInt(), is(81));

    }

    @Test
    public void collectingResults() {
        // Creating a String array from a stream
        Stream<String> names1 = Stream.of("John", "Peter", "Roger");
        String[] nameArray = names1.toArray(String[]::new);

        Stream<String> names2 = Stream.of("John", "Peter", "Roger");
        Set<String> nameSet = names2.collect(Collectors.toSet());
        assertThat(nameArray.length, is(3));
        assertThat(nameSet.size(), is(3));

        //Joining streams
        Stream<String> names3 = Stream.of("John", "Peter", "Roger");
        String joined = names3.collect(Collectors.joining("|"));
        assertThat(joined, is("John|Peter|Roger"));

        //Joining by mapping
        String modelsSeparatedByPipe =
            Stream.of(
                new Car("citroen","ds3",5000.50),
                new Car("citroen","ds5",5000.50)).
                    map(Car::toString).
                    collect(Collectors.joining("|"));

        assertThat(modelsSeparatedByPipe, is("ds3|ds5"));
    }

    @Test
    public void gettingStatistics() {
        IntSummaryStatistics carStatistics =
            Stream.of(
                new Car("citroen","ds3",5000.50),
                new Car("citroen","ds5",5000.50)).
                    map(car -> car.model).
                    collect(Collectors.summarizingInt(String::length));

        assertThat(carStatistics.getAverage(), is(3.0));
        assertThat(carStatistics.getMax(), is(3));
    }

    @Test
    public void collectingIntoMaps() {
        Map<String,Integer> authors =
            Stream.of(
                new Author("John", 1923),
                new Author("Peter", 1832)).
                    collect(
                        Collectors.toMap(
                            Author::getName,
                            Author::getYear
                        )
                    );

        Optional<Integer> sum = authors.
                values().
                stream().reduce((a, b) -> a + b);
        assertThat(authors.keySet(), hasItems("John", "Peter"));
        assertThat(sum.isPresent(), is(true));
        assertThat(sum.get(), is(3755));
    }

    @Test
    public void collectingIntoMapsWithDuplicatedKeys() {
        // This is the merge function in order to decide when two values have the same
        // key
        BinaryOperator<Integer> takeGreaterYear =
            (Integer existing, Integer current) ->  current >= existing ? current : existing;

        Map<String,Integer> authors =
            Stream.of(
                new Author("John", 1000),
                new Author("John", 2000),
                new Author("Peter", 1000)).
                    collect(
                        Collectors.toMap(Author::getName, Author::getYear, takeGreaterYear)
                    );

            Optional<Integer> sum = authors.
                values().
                stream().reduce((a, b) -> a + b);
        assertThat(authors.keySet(), hasItems("John", "Peter"));
        assertThat(sum.isPresent(), is(true));
        assertThat(sum.get(), is(3000));

    }

    @Test(expected = IllegalStateException.class)
    public void exceptionWhenHavingDuplicateKeys() {
        Map<String,Integer> authors =
            Stream.of(
                new Author("John", 1000),
                new Author("John", 2000),
                new Author("Peter", 1000)).
                    collect(
                        Collectors.toMap(Author::getName, Author::getYear)
                    );
    }

    @Test
    public void groupingAuthors() {
        Stream<Author> authors =
            Stream.of(
                new Author("John", 1929),
                new Author("Jackson", 1929),
                new Author("Peter", 2014));

        Map<Integer, List<Author>> authorByYear =
            authors.collect(
                Collectors.groupingBy(Author::getYear)
            );

        assertThat(authorByYear.get(1929).size(), is(2));
        assertThat(authorByYear.size(), is(2));
        assertThat(authorByYear.get(2014).size(), is(1));
    }

    @Test
    public void partitioningAuthors() {
        Stream<Author> authors =
            Stream.of(
                new Author("John", 1929),
                new Author("Jackson", 1929),
                new Author("Peter", 2014));

        Map<Boolean, List<Author>> authorByName =
            authors.collect(
                Collectors.partitioningBy(author -> author.getName().equals("Peter"))
            );

        assertThat(authorByName.get(Boolean.TRUE).size(), is(1));
        assertThat(authorByName.size(), is(2));
        assertThat(authorByName.get(Boolean.FALSE).size(), is(2));
    }

    @Test
    public void usingGroupingToAggregateData() {
        // List of authors by year
        List<Author> authorList =
            Arrays.asList(new Author("John", 1929),
                new Author("Jackson", 1929),
                new Author("John", 1929),
                new Author("Peter", 2014));

        Map<Integer, Long> authorsByYear =
            authorList.stream().collect(
                Collectors.groupingBy(
                    Author::getYear,
                    Collectors.counting()
                )
            );

        assertThat(authorsByYear.size(), is(2));
        assertThat(authorsByYear.get(1929), is(3L));
        assertThat(authorsByYear.get(2014), is(1L));

        // Adding up years by name (Silly :S I know)
        Map<String, Integer> sumYearsByName =
            authorList.stream().collect(
                Collectors.groupingBy(
                    Author::getName,
                    Collectors.summingInt(Author::getYear)
                )
            );

        assertThat(sumYearsByName.size(), is(3));
        assertThat(sumYearsByName.get("John"), is(3858));
        assertThat(sumYearsByName.get("Jackson"), is(1929));
        assertThat(sumYearsByName.get("Peter"), is(2014));
    }

    @Test
    public void usingGroupingForAggregatingDataContinues() {
        List<Author> authorList =
            Arrays.asList(new Author("John", 1929),
                new Author("Jackson", 1929),
                new Author("John", 1929),
                new Author("Peter", 2014));

        Map<Integer, Optional<String>> maxAuthorByYearAndLongestName =
            authorList.stream().collect(
                groupingBy(Author::getYear,
                    mapping(Author::getName,
                        maxBy(comparing(String::length))
                    )
                )
            );

        // 1929 & 2014
        assertThat(maxAuthorByYearAndLongestName.size(), is(2));
        assertThat(maxAuthorByYearAndLongestName.get(1929).get(), is("Jackson"));
    }
}
