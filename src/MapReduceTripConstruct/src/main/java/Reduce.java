import java.io.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import static distance.Distance.*;

public class Reduce extends Reducer<LongWritable, Segment, NullWritable, Text> {
  public void Reduce(LongWritable key, Iterable<Segment> values, Context context) throws IOException {
  }
}

