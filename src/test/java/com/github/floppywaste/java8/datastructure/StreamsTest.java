package com.github.floppywaste.java8.datastructure;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Splitter;

public class StreamsTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	Stream<String> names;
	Stream<Integer> numbers;

	@Before
	public void setUp() {
		names = Stream.of("Alwina", "Denis", "Kai", "Olga", "Tommaso");
		numbers = Stream.of(1, 2, 3, 4, 5);
	}

	@Test
	public void filter() throws Exception {
		final Stream<String> shortNames = names.filter(s -> s.length() <= 4);

		assertThat(shortNames.collect(Collectors.toList())).containsExactly("Kai", "Olga");
	}

	@Test
	public void streamsCannotBeConsumedTwice() throws Exception {
		names.forEach(System.out::println);

		exception.expect(IllegalStateException.class);
		exception.expectMessage("stream has already been operated upon or closed");

		names.forEach(System.out::println);
	}

	@Test
	public void reduce() throws Exception {
		final Optional<Integer> result = numbers.reduce((a, b) -> a + b);

		assertThat(result.get()).isEqualTo(15);
	}

	@Test
	public void max() throws Exception {
		final Optional<String> result = names.max((a, b) -> a.length() - b.length());

		assertThat(result.get()).isEqualTo("Tommaso");
	}

	@Test
	public void flatMap() throws Exception {
		final Stream<String> characters = names.flatMap(s -> s.chars().mapToObj(i -> String.valueOf((char) i)));

		assertThat(characters.collect(Collectors.toList())).containsExactly("A", "l", "w", "i", "n", "a", "D", "e",
				"n", "i", "s", "K", "a", "i", "O", "l", "g", "a", "T", "o", "m", "m", "a", "s", "o");
	}

	@Test
	public void streamsAndIO_niceForParsingData() throws Exception {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
				"/readings.csv")))) {
			Optional<LocalDateTime> maxDate = reader.lines().skip(1).map(Splitter.on(",").trimResults()::splitToList)
					.map(column -> column.get(1)).map(LocalDateTime::parse).max(Comparator.naturalOrder());

			assertThat(maxDate.get()).isEqualTo(LocalDateTime.parse("2014-03-31T01:00"));
		}
	}

	@Test
	public void otherWaysToBuildStreams() throws Exception {
		Stream<Integer> fromABuilder = Stream.<Integer> builder().add(1).add(2).add(3).build();

		Stream<Integer> infinite = Stream.iterate(4, i -> i + 1);

		Stream<Integer> fromTwoStreams = Stream.concat(fromABuilder, infinite);

		assertThat(fromTwoStreams.limit(7).collect(toList())).containsExactly(1, 2, 3, 4, 5, 6, 7);

		Stream<ZonedDateTime> fromASupplier = Stream.generate(ZonedDateTime::now);
		ZonedDateTime now = ZonedDateTime.now();

		boolean actualGenerationIsDeferred = fromASupplier.limit(10).allMatch(time -> time.isAfter(now));
		assertThat(actualGenerationIsDeferred).as("it is the lazy nature of streams.");
	}
}
