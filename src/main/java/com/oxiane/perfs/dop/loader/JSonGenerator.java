package com.oxiane.perfs.dop.loader;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.oxiane.perfs.dop.Constants;
import com.oxiane.perfs.dop.dop.PaymentEvent;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.UUID;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public class JSonGenerator {

  private static final String[] REASONS = {
      "Insufficient funds",
      "bank not reachable",
      "VISA debit limit reached",
      "Debit outside of allowed period"
  };
  private static RandomGenerator random = RandomGenerator.getDefault();
  private static String lastValidUuid;

  public static void main(String[] args) {
    new JSonGenerator().generate(Constants.EVENTS_LOCATION+Constants.EVENTS_RES_NAME);
  }

  private void generate(String location) {
    try(PrintWriter pw = new PrintWriter(new FileWriter(location))) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
      mapper.configure(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES, false);
      IntStream.range(0, 1000)
               .mapToObj(JSonGenerator::generateEvent)
               .map(evt -> {
                try {
                  return mapper.writeValueAsString(evt);
                } catch(Exception ex) {
                  throw new RuntimeException(ex);
                }
               })
               .forEach(pw::println);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static PaymentEvent generateEvent(int i) {
    return switch(i % 6) {
      case 0, 1, 2 -> {
        lastValidUuid = UUID.randomUUID().toString();
        yield new PaymentEvent.PaymentSuccess(
            lastValidUuid,
            random.nextDouble(0, 500),
            Instant.now().getEpochSecond());
      }
      case 3, 4 -> new PaymentEvent.PaymentFailure(
          UUID.randomUUID().toString(),
          REASONS[i % REASONS.length],
          Instant.now().getEpochSecond());
      default -> new PaymentEvent.PaymentRefund(
          lastValidUuid,
          random.nextDouble(0, 100),
          Instant.now().getEpochSecond());
    };
  }
}
