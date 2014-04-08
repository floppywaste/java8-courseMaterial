package com.github.floppywaste.java8.datastructure;

import static com.google.common.collect.Maps.newHashMap;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.floppywaste.java8.functional.FunctionalOverdoTest;

public class MapTest {

	Map<String, String> byInitials;

	@Before
	public void setUp() {
		byInitials = newHashMap();
		byInitials.put("J", "Jens");
		byInitials.put("D", "Denis");
		byInitials.put("K", "Kai");
	}

	@Test
	public void compute_replaceValuesThroughFunctions() throws Exception {
		String computed = byInitials.compute("J", (k, v) -> v.toUpperCase());

		assertThat(computed).isEqualTo("JENS");
		assertThat(byInitials.get("J")).isEqualTo("JENS");
	}

	/**
	 * @see FunctionalOverdoTest
	 */
	@Test
	public void computeIfAbsent_niceForCaching() throws Exception {
		String value = byInitials.computeIfAbsent("T", s -> "Tommaso");
		assertThat(value).isEqualTo("Tommaso");
	}

	@Test
	public void getOrDefault_goodAgainstNullPointers() throws Exception {
		assertThat(byInitials.getOrDefault("A", "Alwina")).isEqualTo("Alwina");
		assertThat(byInitials.getOrDefault("D", "David")).isEqualTo("Denis");
	}

	@Test
	public void forEach_consumeAllKeyValuePairs() throws Exception {
		byInitials.forEach((k, v) -> System.out.printf("%s->%s\n", k, v));
	}

}
