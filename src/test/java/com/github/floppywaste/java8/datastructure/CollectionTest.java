package com.github.floppywaste.java8.datastructure;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CollectionTest {

	List<Integer> numbers;

	@Before
	public void setUp() {
		numbers = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
	}

	@Test
	public void iterable_forEach() throws Exception {
		numbers.forEach(i -> System.out.println(i));
	}

	@Test
	public void collection_removeIf() throws Exception {
		numbers.removeIf(i -> i >= 3);

		assertThat(numbers).containsExactly(1, 2);
	}

	@Test
	public void list_sort() throws Exception {
		numbers.sort((i, j) -> j - i);

		assertThat(numbers).containsExactly(7, 6, 5, 4, 3, 2, 1);
	}

	@Test
	public void list_replaceAll() throws Exception {
		numbers.replaceAll(i -> i += 2);

		assertThat(numbers).containsExactly(3, 4, 5, 6, 7, 8, 9);
	}
	
}
