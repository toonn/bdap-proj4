import java.io.*;
import java.util.*;
import java.text.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import static distance.Distance.*;

public class TRMap extends Mapper<LongWritable, Text, LongWritable, Segment> {
  private double airportLat = 37.62131;
  private double airportLon = -122.37896;

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    Segment trip = new Segment();
    trip.parse(value.toString());
    if (trip.isFull()
        && (sphereDistance(airportLat, airportLon, trip.getStartLat(), trip.getStartLon()) <= 1
          || sphereDistance(airportLat, airportLon, trip.getEndLat(), trip.getEndLon()) <= 1)) {
      context.write(key, trip);
          }
  }
}
