package com.oxiane.perfs.dop.dop;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentEvent.PaymentSuccess.class),
    @JsonSubTypes.Type(value = PaymentEvent.PaymentFailure.class),
    @JsonSubTypes.Type(value = PaymentEvent.PaymentRefund.class)
})
public sealed interface PaymentEvent {

  record PaymentSuccess(String transactionId, double amount, long timestamp) implements PaymentEvent {
  }

  record PaymentFailure(String transactionId, String reason, long timestamp) implements PaymentEvent {
  }

  record PaymentRefund(String originalTransactionId, double refundAmount, long timestamp) implements PaymentEvent {
  }
}