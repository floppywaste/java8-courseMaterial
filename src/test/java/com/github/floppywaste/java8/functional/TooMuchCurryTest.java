package com.github.floppywaste.java8.functional;

import static org.fest.assertions.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Another example for the use of {@link FunctionalInterface}.
 */
public class TooMuchCurryTest {

	@FunctionalInterface
	interface TriFunction<S, T, U, R> {

		R apply(S s, T t, U u);

		default BiFunction<T, U, R> curry(final S curried) {
			return (t, u) -> this.apply(curried, t, u);
		}
	}

	@Test
	public void functionWith3Args_java8KnowsOnly2_thenCurryIt() throws Exception {
		final TriFunction<Integer, Integer, Integer, Integer> weightedSum = (w, a, b) -> w * (a + b);
		final BiFunction<Integer, Integer, Integer> weightedWith10 = weightedSum.curry(10);

		Integer result = Stream.of(1, 2, 3, 4, 5, 6, 7).reduce(0, weightedWith10, dummyCombiner());

		assertThat(result / 10).isEqualTo(1234567);
	}

	/*
	 * Combiner function has no use when reduce is called on a non-parallel stream.
	 */
	private BinaryOperator<Integer> dummyCombiner() {
		return (i, j) -> i;
	}

}
