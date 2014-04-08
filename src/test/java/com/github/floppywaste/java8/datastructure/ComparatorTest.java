package com.github.floppywaste.java8.datastructure;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ComparatorTest {

	List<String> names;

	@Before
	public void setUp() {
		names = Lists.newArrayList("Alwina", "Denis", "Kai", "Olga", "David", "Jens");
	}

	@Test
	public void compare_usingExtractorFunction() throws Exception {
		// note that comparingInt is a static function
		// of the Comparator interface
		names.sort(Comparator.comparingInt(n -> n.length()));

		assertThat(names).containsExactly("Kai", "Olga", "Jens", "Denis", "David", "Alwina");
	}

	@Test
	public void compare_considerNulls() throws Exception {
		names.add(0, null);
		names.add(3, null);
		names.sort(Comparator.nullsLast(Comparator.comparingInt(n -> n.length())));

		assertThat(names).containsExactly("Kai", "Olga", "Jens", "Denis", "David", "Alwina", null, null);
	}

	@Test
	public void compare_inChains() throws Exception {
		// note that Comparator.naturalOrder() must not compare null values!
		names.sort(Comparator.comparingInt((final String n) -> n.length()).thenComparing(Comparator.naturalOrder()));

		assertThat(names).containsExactly("Kai", "Jens", "Olga", "David", "Denis", "Alwina");
	}

}
