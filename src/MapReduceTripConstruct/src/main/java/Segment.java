import java.io.*;
import java.util.*;
import java.text.*;

import org.apache.hadoop.io.*;

import static distance.Distance.*;

public class Segment implements Writable {
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  private Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/San_Francisco"));
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
    this.date = startDate;
    this.hours = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (60 * 60 * 1000.0);
    this.full = ('E' != startStatus);
    this.startLat = startLat;
    this.startLon = startLon;
    this.endLat = endLat;
    this.endLon = endLon;
  }

  public void write(DataOutput out) throws IOException {
    out.writeUTF(sdf.format(date.getTime()));
    out.writeDouble(hours);
    out.writeBoolean(full);
    out.writeDouble(startLat);
    out.writeDouble(startLon);
    out.writeDouble(endLat);
    out.writeDouble(endLon);
  }

  public void readFields(DataInput in) throws IOException {
    try {
      date.setTime(sdf.parse(in.readUTF()));
    } catch (ParseException e) {
      date.setTimeInMillis(0);
    }
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

  public Calendar getDate() {
    return date;
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
    hours += s.getHours();
    if (endLat == s.getStartLat() && endLon == s.getStartLon()) {
      endLat = s.getEndLat();
      endLon = s.getEndLon();
    } else {
      startLat = s.getStartLat();
      startLon = s.getStartLon();
      date = s.getDate();
    }
  }

  public String toString() {
    return sdf.format(date.getTime()) + " " + hours + " " + full + " " + startLat + " " + startLon + " " + endLat + " " + endLon;
  }
}
