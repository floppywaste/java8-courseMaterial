package com.github.floppywaste.java8.functional;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LambdasAndMethodReferenceTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	Stream<Integer> numbers;

	@Before
	public void setUp() {
		numbers = Stream.of(1, 2, 3);
	}

	@Test
	public void reusingLegacyGuavaFunctions() throws Exception {
		final Stream<Integer> inced = numbers.map(inc()::apply);

		assertThat(inced.collect(toList())).containsExactly(2, 3, 4);
	}

	private com.google.common.base.Function<Integer, Integer> inc() {
		return new com.google.common.base.Function<Integer, Integer>() {

			@Override
			public Integer apply(final Integer arg) {
				return arg + 1;
			}
		};
	}

	@Test
	public void this_isNotLikeAnAnonymousClass() throws Exception {
		Stream<String> thisIsFromALambda = numbers.map(n -> this.getClass() + ": " + n);

		assertThat(thisIsFromALambda.collect(toList())).containsExactly(
				"class com.github.floppywaste.java8.functional.LambdasAndMethodReferenceTest: 1",
				"class com.github.floppywaste.java8.functional.LambdasAndMethodReferenceTest: 2",
				"class com.github.floppywaste.java8.functional.LambdasAndMethodReferenceTest: 3");

		numbers = Stream.of(1, 2, 3);
		Stream<String> thisIsFromAnonClass = numbers.map(new Function<Integer, String>() {

			@Override
			public String apply(final Integer input) {
				return this.getClass() + ": " + input;
			}
		});

		assertThat(thisIsFromAnonClass.collect(toList())).containsExactly(
				"class com.github.floppywaste.java8.functional.LambdasAndMethodReferenceTest$2: 1",
				"class com.github.floppywaste.java8.functional.LambdasAndMethodReferenceTest$2: 2",
				"class com.github.floppywaste.java8.functional.LambdasAndMethodReferenceTest$2: 3");
	}

	@Test
	public void methodReferences_mustNotThrowCheckedExceptions() throws Exception {
		// won't compile. Eclipse offers try-catch, but that won't help either.
		// numbers.forEach(LambdasAndMethodReferenceTest::mayFail);

		// this works though
		numbers.forEach(v -> {
			try {
				mayFail(v);
			} catch (Exception e) {
			}
		});
	}

	static void mayFail(final Integer val) throws Exception {
		System.out.println(val);
	}

	@Test
	public void defaultMethods_cannotBeCalledOnLambdasDirectly() throws Exception {
		Function<Integer, Integer> first = i -> i + 2;
		Function<Integer, Integer> second = i -> i * 10;

		// (i -> i + 2).andThen(i -> i * 10) won't compile
		Stream<Integer> mapped = numbers.map(first.andThen(second));

		assertThat(mapped.collect(toList())).containsExactly(30, 40, 50);
	}

}
