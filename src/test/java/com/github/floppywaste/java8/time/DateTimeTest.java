package com.github.floppywaste.java8.time;

import static org.fest.assertions.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class DateTimeTest {

	@Test
	public void clocks_canBeUsedForBetterTestability() throws Exception {
		Clock clock = Clock.fixed(Instant.parse("2014-04-04T04:04:04.00Z"), ZoneId.of("UTC"));

		assertThat(ZonedDateTime.now(clock)).isEqualTo(ZonedDateTime.parse("2014-04-04T04:04:04.00Z[UTC]"));
		assertThat(LocalDateTime.now(clock)).isEqualTo(LocalDateTime.parse("2014-04-04T04:04:04.00"));
		assertThat(LocalDate.now(clock)).isEqualTo(LocalDate.parse("2014-04-04"));
		assertThat(LocalTime.now(clock)).isEqualTo(LocalTime.parse("04:04:04.00"));
	}

}
