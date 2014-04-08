package com.github.floppywaste.java8.application;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.junit.Test;

public class MeasurementsTest {

	static final ZonedDateTime START = ZonedDateTime.parse("2014-03-29T00:00:00+01:00");

	static class Measurement {

		final ZonedDateTime dateTime;
		final BigDecimal value;
		final String unit;

		public Measurement(final ZonedDateTime dateTime, final BigDecimal value, final String unit) {
			this.dateTime = dateTime;
			this.value = value;
			this.unit = unit;
		}

		@Override
		public String toString() {
			return "Measurement [" + dateTime + ": " + value + " " + unit + "]";
		}

	}

	@Test
	public void generateSomeRandomMeasurements() throws Exception {
		final Stream<Measurement> measurements = measurementsStream(START, date -> Math.random() * date.getMinute()
				+ 10);

		measurements.limit(96).forEach(System.out::println);
	}

	private static Stream<Measurement> measurementsStream(final ZonedDateTime start,
			final Function<ZonedDateTime, Double> valueFunction) {
		return Stream.iterate(start, date -> date.plusMinutes(15)).map(timeToMeasurement(valueFunction));
	}

	private static Function<ZonedDateTime, Measurement> timeToMeasurement(
			final Function<ZonedDateTime, Double> valueFunction) {
		return time -> new Measurement(time, BigDecimal.valueOf(valueFunction.apply(time)), "kWh");
	}

	public static void main(final String[] args) {
		final Stream<Measurement> measurements = generateMeasurements();
		TimeSeriesCollection dataSet = dataSet(measurements);
		render(dataSet);
	}

	private static TimeSeriesCollection dataSet(final Stream<Measurement> measurements) {
		final TimeSeries series = new TimeSeries("Measurements");
		measurements.forEach(m -> series.add(dataItem(m)));
		return new TimeSeriesCollection(series);
	}

	private static TimeSeriesDataItem dataItem(final Measurement m) {
		return new TimeSeriesDataItem(new Minute(Date.from(m.dateTime.toInstant())), m.value);
	}

	private static Stream<Measurement> generateMeasurements() {
		final Stream<Measurement> measurements = measurementsStream(START, MeasurementsTest::value);
		return measurements.limit(96);
	}

	private static double value(final ZonedDateTime date) {
		double hourOfDay = date.getHour();
		return (hourOfDay * hourOfDay - 24 * hourOfDay - 432) / -576;
	}

	private static void render(final TimeSeriesCollection series) {
		final JFreeChart chart = createChart(series);
		final ChartPanel chartPanel = new ChartPanel(chart);
		final ApplicationFrame frame = new ApplicationFrame("Measurements");
		frame.setContentPane(chartPanel);
		frame.setVisible(true);
		frame.setBounds(0, 0, 1024, 768);
	}

	private static JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart chart = ChartFactory.createTimeSeriesChart("Measurements Demo", "Date", "kWh", dataset, true,
				true, false);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		plot.setRenderer(new XYAreaRenderer());

		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yy HH:mm"));

		return chart;
	}

}
