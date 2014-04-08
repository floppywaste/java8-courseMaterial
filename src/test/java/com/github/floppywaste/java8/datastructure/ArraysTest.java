package com.github.floppywaste.java8.datastructure;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ArraysTest {

	public int[] array;

	@Before
	public void setUp() {
		array = new int[] { 1, 2, 3, 4, 5 };
	}

	@Test
	public void parallelPrefix_cumulatesInPlace() throws Exception {
		Arrays.parallelPrefix(array, (a, b) -> a + b);

		assertThat(array).containsOnly(1, 3, 6, 10, 15);
	}

	@Test
	public void parallelSetAll_generatesValuesForIndex() throws Exception {
		Arrays.parallelSetAll(array, (i) -> i);

		assertThat(array).containsOnly(0, 1, 2, 3, 4);
	}

	@Test
	public void parallelSort_onlyMakesSenseForReallyLargeArrays() throws Exception {
		array = new int[] { 3, 2, 5, 1, 4 };
		Arrays.parallelSort(array);

		assertThat(array).containsOnly(1, 2, 3, 4, 5);
	}
}
