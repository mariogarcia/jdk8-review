package streams;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItems;
import static lambda.Sort.sort;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public  class StreamTests {

    private static List<String> words = Arrays.asList("consequences", "happiness", "knowledge");

    @Test
    public void iterationVersusStreams() {
     /* Classical bad way of incrementing a counter */
        int outerCount = 0;
        for(String word: words) {
            outerCount += 1;
        }

        assertThat(outerCount, is(3));

        long count = words.stream().count();
        assertThat(count, is(3L));
    }

    public void streamGeneration() {
        Stream<String> song = Stream.of("some", "list", "of", "song");
        assertThat(song.count(), is(4));

        Stream<String> emptyStream = Stream.empty();
        assertThat(song.count(), is(0));

        Stream<String> echos = Stream.generate(()-> "Echo");
        Optional<String> echo = echos.findFirst();
        assertThat(echo.get(), is("Echo"));
    }

}
