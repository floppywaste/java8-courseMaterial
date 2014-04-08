package com.github.floppywaste.java8.functional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class ReduceTheFunTest {

	Stream<String> names;
	Stream<Integer> numbers;

	@Before
	public void setUp() {
		names = Stream.of("Alwina", "Denis", "Kai", "Olga", "David", "Jens");
		numbers = Stream.of(1, 2, 3, 4, 5);
	}

	static class Averager implements IntConsumer {
		private static int averagerId = 0;

		private final int id;

		private int total = 0;
		private int count = 0;

		public Averager() {
			id = averagerId++;
			log("Averager [%s] created.", id);
		}

		public double average() {
			return count > 0 ? ((double) total) / count : 0;
		}

		@Override
		public void accept(final int i) {
			total += i;
			count++;
			log("Accepted int [%s], current state = [%s].", i, this);
		}

		public void combine(final Averager other) {
			total += other.total;
			count += other.count;
			log("Accepted Averager [%s] (%s), current state = [%s].", other.id, other, this);
		}

		private void log(final String msg, final Object... args) {
			System.out.println(id + ": " + String.format(msg, args));
		}

		@Override
		public String toString() {
			return "Averager [total=" + total + ", count=" + count + ", average()=" + average() + "]";
		}

	}

	@Test
	public void calculateAverage_withACustomAverager() throws Exception {
		final Averager averageCollect = numbers.collect(Averager::new, Averager::accept, Averager::combine);

		assertThat(averageCollect.average()).isEqualTo(3);
	}

	@Test
	public void calculateAverage_onAParallelStream_withACustomAverager() throws Exception {
		final Averager averageCollect = numbers.parallel().collect(Averager::new, Averager::accept, Averager::combine);

		assertThat(averageCollect.average()).isEqualTo(3);
	}

	@Test
	public void calculateAverage_withABuiltInCollector() throws Exception {
		final Double collect = numbers.collect(Collectors.averagingInt(i -> i));

		assertThat(collect).isEqualTo(3);
	}

	@Test
	public void accumulators_onParallelStreams_shouldBeAssociative() throws Exception {
		// following is associative
		// (a op b) op c == a op (b op c)

		BinaryOperator<Integer> associativeFn = (a, b) -> a + b;
		assertThat(Stream.of(1, 2, 3).reduce(0, associativeFn)).isEqualTo(6);
		assertThat(Stream.of(1, 2, 3).parallel().reduce(0, associativeFn)).isEqualTo(6);

		BinaryOperator<Integer> nonAssociativeFn = (a, b) -> a - b;
		assertThat(Stream.of(1, 2, 3).reduce(0, nonAssociativeFn)).isEqualTo(-6);
		assertThat(Stream.of(1, 2, 3).parallel().reduce(0, nonAssociativeFn)).isNotEqualTo(-6);
	}

	@Test
	public void groupBy() throws Exception {
		final Map<Integer, List<String>> byLength = names
				.collect(Collectors.groupingBy((final String n) -> n.length()));

		assertThat(byLength).hasSize(4).includes(entry(3, newArrayList("Kai")), entry(4, newArrayList("Olga", "Jens")),
				entry(5, newArrayList("Denis", "David")), entry(6, newArrayList("Alwina")));
	}

	@Test
	public void downstream_cascadeYourCollectors() throws Exception {
		Function<String, Character> firstChar = n -> n.charAt(0);
		Function<String, Character> lastChar = n -> n.charAt(n.length() - 1);

		final Map<Character, Set<Character>> firstLettersByLastLetters = names.collect(Collectors.groupingBy(lastChar,
				Collectors.mapping(firstChar, Collectors.toSet())));

		assertThat(firstLettersByLastLetters).hasSize(4).includes(entry('a', newHashSet('A', 'O')),
				entry('s', newHashSet('D', 'J')), entry('d', newHashSet('D')), entry('i', newHashSet('K')));
	}

}
