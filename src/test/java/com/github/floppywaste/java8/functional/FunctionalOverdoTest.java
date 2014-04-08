package com.github.floppywaste.java8.functional;

import static com.google.common.collect.Maps.newHashMap;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

public class FunctionalOverdoTest {

	@Test
	public void memoizing_fibonacci() throws Exception {
		Function<Integer, Integer> fib = memoize((f, i) -> i < 2 ? 1 : f.apply(i - 1) + f.apply(i - 2));

		assertThat(fib.apply(0)).isEqualTo(1);
		assertThat(fib.apply(1)).isEqualTo(1);
		assertThat(fib.apply(2)).isEqualTo(2);
		assertThat(fib.apply(3)).isEqualTo(3);
		assertThat(fib.apply(4)).isEqualTo(5);
		assertThat(fib.apply(5)).isEqualTo(8);
		assertThat(fib.apply(20)).isEqualTo(10946);
		assertThat(fib.apply(20)).isEqualTo(10946);
		assertThat(fib.apply(20)).isEqualTo(10946);
		assertThat(fib.apply(20)).isEqualTo(10946);
		assertThat(fib.apply(20)).isEqualTo(10946);
	}

	<T, U, R> Function<U, R> memoize(final BiFunction<Function<U, R>, U, R> function) {
		return new Function<U, R>() {

			final Map<U, R> cache = newHashMap();

			@Override
			public R apply(final U key) {
				return cache.computeIfAbsent(key, this::compute);
			}

			private R compute(final U key) {
				R result = function.apply(this, key);
				System.out.printf("Calculating value for param [%s]: [%s]\n", key, result);
				return result;
			}

		};
	}

}
