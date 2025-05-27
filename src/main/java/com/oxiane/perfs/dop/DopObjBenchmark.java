package com.oxiane.perfs.dop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.oxiane.perfs.dop.dop.PaymentDopProcessor;
import com.oxiane.perfs.dop.loader.EventFileReader;
import com.oxiane.perfs.dop.obj.PaymentObjProcessor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class DopObjBenchmark {
  private InternState state;

  static class InternState {
    List<com.oxiane.perfs.dop.dop.PaymentEvent> dopEvents;
    List<com.oxiane.perfs.dop.obj.PaymentEvent> objEvents;

    public void loadData(List<String> strEvents) {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
      objectMapper.configure(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES, false);
      dopEvents = strEvents.stream()
                           .map(s -> {
                             try {
                               return objectMapper.readValue(s, com.oxiane.perfs.dop.dop.PaymentEvent.class);
                             } catch (JsonProcessingException e) {
                               throw new RuntimeException(e);
                             }
                           })
                           .toList();
      objEvents = strEvents.stream()
                           .map(s -> {
                             try {
                               return objectMapper.readValue(s, com.oxiane.perfs.dop.obj.PaymentEvent.class);
                             } catch (JsonProcessingException e) {
                               throw new RuntimeException(e);
                             }
                           })
                           .toList();
    }
  }

  @Setup(Level.Trial)
  public void setup() {
    EventFileReader fileReader = new EventFileReader();
    state = new InternState();
    state.loadData(fileReader.getEVTS());
  }

  @Benchmark
  public void process_dop(Blackhole blackhole) {
    PaymentDopProcessor paymentDopProcessor = new PaymentDopProcessor();
    state.dopEvents
        .stream()
        .map(paymentDopProcessor::process)
        .forEach(blackhole::consume);
  }

  @Benchmark
  public void process_obj(Blackhole blackhole) {
    PaymentObjProcessor paymentObjProcessor = new PaymentObjProcessor();
    state.objEvents
        .stream()
        .map(paymentObjProcessor::process)
        .forEach(blackhole::consume);
  }
}
