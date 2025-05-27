package com.oxiane.perfs.dop.obj;

public class PaymentObjProcessor {


  public String process(PaymentEvent event) {
    return event.process();
  }
}
