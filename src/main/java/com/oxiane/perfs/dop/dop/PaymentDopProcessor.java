package com.oxiane.perfs.dop.dop;

public class PaymentDopProcessor {

  public String process(PaymentEvent event) {
    return switch(event) {
      case PaymentEvent.PaymentSuccess(String tId, _, _) -> "Success for "+tId;
      case PaymentEvent.PaymentFailure(String tId, _, _) -> "Failure for "+tId;
      case PaymentEvent.PaymentRefund(String tId, _, _) -> "Refund for "+tId;
    };
  }
}
