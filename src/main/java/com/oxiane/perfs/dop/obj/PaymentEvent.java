package com.oxiane.perfs.dop.obj;

import org.openjdk.jmh.infra.Blackhole;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentEvent.PaymentSuccess.class),
    @JsonSubTypes.Type(value = PaymentEvent.PaymentFailure.class),
    @JsonSubTypes.Type(value = PaymentEvent.PaymentRefund.class)
})

public abstract class PaymentEvent {
  private long timestamp;

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public abstract String process();
  public static class PaymentSuccess extends PaymentEvent {

    private String transactionId;
    private double amount;

    public String getTransactionId() {
      return transactionId;
    }

    public void setTransactionId(String transactionId) {
      this.transactionId = transactionId;
    }

    public double getAmount() {
      return amount;
    }

    public void setAmount(double amount) {
      this.amount = amount;
    }

    @Override
    public String process() {
      return "Success for "+getTransactionId();
    }
  }

  public static class PaymentFailure extends PaymentEvent {
    private String transactionId;
    private String reason;

    public String getTransactionId() {
      return transactionId;
    }

    public void setTransactionId(String transactionId) {
      this.transactionId = transactionId;
    }

    public String getReason() {
      return reason;
    }

    public void setReason(String reason) {
      this.reason = reason;
    }

    @Override
    public String process() {
      return "Failure for "+getTransactionId();
    }
  }

  public static class PaymentRefund extends PaymentEvent {
    private String originalTransactionId;
    private double refundAmount;

    public double getRefundAmount() {
      return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
      this.refundAmount = refundAmount;
    }

    public String getOriginalTransactionId() {
      return originalTransactionId;
    }

    public void setOriginalTransactionId(String originalTransactionId) {
      this.originalTransactionId = originalTransactionId;
    }

    @Override
    public String process() {
      return "Refund for "+getOriginalTransactionId();
    }
  }
}
