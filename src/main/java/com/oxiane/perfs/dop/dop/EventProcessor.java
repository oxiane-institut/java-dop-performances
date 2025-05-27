package com.oxiane.perfs.dop.dop;

import java.util.function.Consumer;
import java.util.logging.Logger;


public class EventProcessor {
  private static final Logger LOGGER = Logger.getLogger(EventProcessor.class.getCanonicalName());
  private static final double HIGHEST_PRESSURE = 3.5;
  private static final double HIGHEST_TEMP = 75.5;

  public void process(Event event) {
    var logger = getLogger(event);
    logger.accept(toString(event));
  }

  private static Consumer<String> getLogger(Event event) {
    return switch(event) {
      case Event.TemperatureEvent(double t, _) when t > HIGHEST_TEMP -> LOGGER::warning;
      case Event.PressureEvent(double p, _) when p > HIGHEST_PRESSURE -> LOGGER::warning;
      default -> LOGGER::info;
    };
  }

  private static String toString(Event event) {
    return switch(event) {
      case Event.TemperatureEvent(double temp, _) -> "Temperature: " + temp;
      case Event.PressureEvent(double pressure, _) -> "Pressure: " + pressure;
      case Event.EntryEvent(_) -> "Someone enters";
      case Event.ExitEvent(_) -> "Someone gets out";
    };
  }

  public sealed interface Event {
    record TemperatureEvent (double temperature,long timestamp) implements Event {}
    record PressureEvent (double pressure,long timestamp) implements Event {}
    record EntryEvent (long timestamp) implements Event {}
    record ExitEvent (long timestamp) implements Event {}
  }
}