import java.io.*;
import java.util.*;
import java.text.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import static distance.Distance.*;

public class TCReduce extends Reducer<LongWritable, Segment, NullWritable, Segment> {
  private ArrayDeque<Segment> full = new ArrayDeque<Segment>();
  private Queue<Segment> empty = new ArrayDeque<Segment>();

  @Override
  public void reduce(LongWritable key, Iterable<Segment> values, Context context) throws IOException, InterruptedException {
    split(values);
    List<Segment> fullTrips = merge(full);
    List<Segment> emptyTrips = merge(empty);
    for (Segment trip : fullTrips) {
      context.write(NullWritable.get(), trip);
    }
    for (Segment trip : emptyTrips) {
      context.write(NullWritable.get(), trip);
    }
  }

  private void split(Iterable<Segment> values) {
    // Split full and empty segments because they can never be part of the same
    // trip. This leads to slightly better performance because merging segments
    // is O(n^2): each segment has to be compared with every other segment.
    for (Segment value : values) {
      if (value.isFull()) {
        full.add(new Segment(value));
      } else {
        empty.add(new Segment(value));
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

