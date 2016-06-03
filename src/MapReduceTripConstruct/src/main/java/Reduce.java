import java.io.*;
import java.util.*;
import java.text.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import static distance.Distance.*;

public class Reduce extends Reducer<LongWritable, Segment, NullWritable, Text> {
  public void Reduce(LongWritable key, Iterable<Segment> values, Context context) throws IOException {
    List<Segment> partialTrips = new ArrayList<Segment>();
    boolean matched;
    for (Segment segment : values) {
      matched = false;
      for (Segment partialTrip : partialTrips) {
        if (partialTrip.match(segment)) {
          partialTrip.merge(segment);
          matched = true;
        }
      }
      if (!matched) {
        partialTrips.add(segment);
      }
    }
    // This doesn't solve the problem start middle and end are now potentially
    // longer... :-(
    List<Segment> trips = new ArrayList<Segment>();
    for (Segment partialTrip : partialTrips) {
      matched = false;
      for (Segment trip : trips) {
        if (trip.match(partialTrip)) {
          trip.merge(partialTrip);
          matched = true;
        }
      }
      if (!matched) {
        trips.add(partialTrip);
      }
    }
  }
}

