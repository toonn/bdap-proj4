import java.io.*;
import java.util.*;
import java.text.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import static distance.Distance.*;

public class Reduce extends Reducer<LongWritable, Segment, NullWritable, Text> {
  private Queue<Segment> full = new ArrayDeque<Segment>();
  private Queue<Segment> empty = new ArrayDeque<Segment>();

  public void Reduce(LongWritable key, Iterable<Segment> values, Context context) throws IOException, InterruptedException {
    split(values);
    List<Segment> fullTrips = merge(full);
    List<Segment> emptyTrips = merge(empty);
    for (Segment trip : fullTrips) {
      context.write(NullWritable.get(), new Text(trip.toString()));
    }
    for (Segment trip : emptyTrips) {
      context.write(NullWritable.get(), new Text(trip.toString()));
    }
  }

  private void split(Iterable<Segment> values) {
    // Split full and empty segments because they can never be part of the same
    // trip. This leads to slightly better performance because merging segments
    // is O(n^2): each segment has to be compared with every other segment.
    for (Segment value : values) {
      if (value.isFull()) {
        full.add(value);
      } else {
        empty.add(value);
      }
    }
  }

  private List<Segment> merge(Queue<Segment> segments) {
    List<Segment> trips = new ArrayList<Segment>();
    Segment current = segments.poll();
    boolean matched = false;
    while (current != null) {
      for (Segment segment : segments) {
        if (current.match(segment)) {
          segment.merge(current);
          matched = true;
          break;
        }
      }
      if (!matched) {
        trips.add(current);
      }
      matched = false;
      current = segments.poll();
    }
    return trips;
  }
}

