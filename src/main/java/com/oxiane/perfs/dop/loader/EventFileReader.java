package com.oxiane.perfs.dop.loader;

import com.oxiane.perfs.dop.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class EventFileReader {

  private final List<String> EVTS;

  public EventFileReader() {
    super();
    try (
      InputStream stream = getClass().getResourceAsStream(Constants.EVENTS_RES_NAME);
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
      EVTS = reader.lines().toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<String> getEVTS() {
    return EVTS;
  }
}
