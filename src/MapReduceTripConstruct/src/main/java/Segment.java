import java.io.*;
import java.util.*;

import org.apache.hadoop.io.*;

import static distance.Distance.*;

public class Segment implements Writable {
  private double hours;
  private boolean full;
  private double startLat;
  private double startLon;
  private double endLat;
  private double endLon;

  public Segment() {
  }

  public Segment(Segment s) {
    this.hours = s.getHours();
    this.full = s.isFull();
    this.startLat = s.getStartLat();
    this.startLon = s.getStartLon();
    this.endLat = s.getEndLat();
    this.endLon = s.getEndLon();
  }

  public Segment(Calendar startDate, Calendar endDate, char startStatus, double startLat, double startLon, double endLat, double endLon) {
    set(startDate, endDate, startStatus, startLat, startLon, endLat, endLon);
  }

  public void set(Calendar startDate, Calendar endDate, char startStatus, double startLat, double startLon, double endLat, double endLon) {
    this.hours = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (60 * 60 * 1000.0);
    this.full = ('F' == startStatus);
    this.startLat = startLat;
    this.startLon = startLon;
    this.endLat = endLat;
    this.endLon = endLon;
  }

  public void write(DataOutput out) throws IOException {
    out.writeDouble(hours);
    out.writeBoolean(full);
    out.writeDouble(startLat);
    out.writeDouble(startLon);
    out.writeDouble(endLat);
    out.writeDouble(endLon);
  }

  public void readFields(DataInput in) throws IOException {
    hours = in.readDouble();
    full = in.readBoolean();
    startLat = in.readDouble();
    startLon = in.readDouble();
    endLat = in.readDouble();
    endLon = in.readDouble();
  }

  public double getAvgSpeed() {
    double v = sphereDistance(startLat, startLon, endLat, endLon) / hours;
    return v;
  }

  public double getRevenue() {
    if (full) {
      return 3.5 + 1.71 * sphereDistance(startLat, startLon, endLat, endLon);
    } else {
      return 0.0;
    }
  }

  public double getHours() {
    return hours;
  }

  public boolean isFull() {
    return full;
  }

  public double getStartLat() {
    return startLat;
  }

  public double getStartLon() {
    return startLon;
  }

  public double getEndLat() {
    return endLat;
  }

  public double getEndLon() {
    return endLon;
  }

  public boolean match(Segment s) {
    return (full == s.isFull())
            && ((endLat == s.getStartLat() && endLon == s.getStartLon())
                || (startLat == s.getEndLat() && startLon == s.getEndLon()));
  }

  /**
   * If segments don't match this segment will change start coordinates
   * to those of s, keeping it's filled status;
   */
  public void merge(Segment s) {
    if (endLat == s.getStartLat() && endLon == s.getStartLon()) {
      endLat = s.getEndLat();
      endLon = s.getEndLon();
    } else {
      startLat = s.getStartLat();
      startLon = s.getStartLon();
    }
  }

  public String toString() {
    return hours + " " + full + " " + startLat + " " + startLon + " " + endLat + " " + endLon;
  }
}
