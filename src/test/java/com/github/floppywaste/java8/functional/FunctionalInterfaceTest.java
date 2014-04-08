package com.github.floppywaste.java8.functional;

import org.junit.Test;

public class FunctionalInterfaceTest {

	@FunctionalInterface
	interface TheListener {
		void fired(TheEvent event);

		/**
		 * Like with any plain old interfaces, default methods are allowed here.
		 * 
		 * @return
		 */
		default TheListener withImportance() {
			return e -> this.fired(new TheEvent(e.happening + " React now!!!"));
		}
	}

	static class TheEvent {
		final String happening;

		public TheEvent(final String happening) {
			this.happening = happening;
		}
	}

	static class EventSource {
		final TheListener listener;

		public EventSource(final TheListener listener) {
			this.listener = listener;
		}

		void raiseEvent() {
			listener.fired(new TheEvent("Weired stuff happened!"));
		}

		void raiseImportantEvent() {
			listener.withImportance().fired(new TheEvent("Weired stuff happened!"));
		}

	}

	@Test
	public void definingMyOwnFunctionalInterface() throws Exception {
		EventSource eventSource = new EventSource(e -> System.out.println(e.happening));

		eventSource.raiseEvent();
		eventSource.raiseImportantEvent();
	}

}
