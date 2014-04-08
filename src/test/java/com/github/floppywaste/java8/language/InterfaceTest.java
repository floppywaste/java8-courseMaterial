package com.github.floppywaste.java8.language;

import static com.github.floppywaste.java8.language.InterfaceTest.TheNewInterface.sinceJava8;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class InterfaceTest {

	static interface TheNewInterface {

		static TheNewInterface sinceJava8() {
			return new TheNewInterface() {

				@Override
				public String whatYouGet() {
					return implementIfYouWant();
				}
			};
		}

		String whatYouGet();

		default String implementIfYouWant() {
			return "what you see";
		}

	}

	static class WithImplementationsNotOverridingDefaults implements TheNewInterface {

		@Override
		public String whatYouGet() {
			return "what you want";
		}

		static WithImplementationsNotOverridingDefaults sometimes() {
			return new WithImplementationsNotOverridingDefaults();
		}

	}

	@Test
	public void interfaces_canHaveStaticMethods() throws Exception {
		assertThat(sinceJava8().whatYouGet()).isEqualTo("what you see");
	}

	@Test
	public void interfaces_canHaveDefaultMethods() throws Exception {
		assertThat(WithImplementationsNotOverridingDefaults.sometimes().whatYouGet()).isEqualTo("what you want");
	}

}
