package com.github.floppywaste.java8.language;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class GenericsTest {

	private <T> List<T> doSomething(final List<T> list) {
		return list;
	}

	@Test
	@SuppressWarnings("unused")
	public void contextualInference_nowWorksOnGenericArguments() throws Exception {
		List<String> java7TypeInference = Lists.newArrayList("A", "B");
		List<String> wouldNotHaveCompiledInJava7 = doSomething(Lists.newArrayList());
	}

	@Test
	public void contextualInference_stillDoesntWorkOnChainedCalls() throws Exception {
		// String thisWillNotCompile = Lists.newArrayList().set(0,
		// "Don't you see I'm a string, stupid?");
	}
}
