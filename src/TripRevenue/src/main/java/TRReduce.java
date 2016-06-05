import java.io.*;
import java.util.*;
import java.text.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class TRReduce extends Reducer<LongWritable, Segment, NullWritable, Text> {
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

  @Override
  public void reduce(LongWritable key, Iterable<Segment> values, Context context) throws IOException, InterruptedException {
    for (Segment trip : values) {
      double revenue = trip.getRevenue();
      String date = sdf.format(trip.getDate().getTime());
      context.write(NullWritable.get(), new Text(date + " " + revenue));
    }
  }
}

