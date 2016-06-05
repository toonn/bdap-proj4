import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class TripRevenue {
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "MapReduceTripRevenue--toonn");
    job.setJarByClass(TripConstruct.class);
    job.setMapOutputKeyClass(LongWritable.class);
    job.setMapOutputValueClass(Segment.class);
    job.setOutputKeyClass(NullWritable.class);
    job.setOutputValueClass(Text.class);
    job.setMapperClass(TRMap.class);
    job.setReducerClass(TRReduce.class);
    //job.setNumReduceTasks(1);
    FileInputFormat.setMaxInputSplitSize(job, 1000000);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
