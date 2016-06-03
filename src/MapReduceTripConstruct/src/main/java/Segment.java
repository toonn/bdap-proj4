import java.io.*;
import java.util.*;

import org.apache.hadoop.io.*;

public class Segment implements Writable {
  private Date startDate;
  private Date endDate;
  private double startLat;
  private double startLon;
  private double endLat;
  private double endLon;

  public Segment(double startLat, double startLon, double endLat, double endLon) {
    set(startLat, startLon, endLat, endLon);
  }

  public void set(double startLat, double startLon, double endLat, double endLon) {
    this.startLat = startLat;
    this.startLon = startLon;
    this.endLat = endLat;
    this.endLon = endLon;
  }

  public void write(DataOutput out) throws IOException {
    out.writeDouble(startLat);
    out.writeChar(' ');
    out.writeDouble(startLon);
    out.writeChar(' ');
    out.writeDouble(endLat);
    out.writeChar(' ');
    out.writeDouble(endLon);
  }

  public void readFields(DataInput in) throws IOException {
    startLat = in.readDouble();
    startLon = in.readDouble();
    endLat = in.readDouble();
    endLon = in.readDouble();
  }
}
