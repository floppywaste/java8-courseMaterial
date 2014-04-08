package com.github.floppywaste.java8.datastructure;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class StringTest {

	List<String> names;

	@Before
	public void setUp() {
		names = Lists.newArrayList("Alwina", "Denis", "Kai", "Olga", "David", "Jens");
	}

	@Test
	public void join() throws Exception {
		assertThat(String.join(", ", names)).isEqualTo("Alwina, Denis, Kai, Olga, David, Jens");
	}

}
