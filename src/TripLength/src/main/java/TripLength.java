import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class TripLength {

  /**
   * Calculates the distance in kilometers, input in radians
   */
  private static double sphereDistance(double lat1, double lon1, double lat2, double lon2) {
    double d2r = Math.PI/180; // Conversion factor from degrees to radians
    lat1 *= d2r;
    lon1 *= d2r;
    lat2 *= d2r;
    lon2 *= d2r;
    double R = 6371.009; // kilometers
    double dPhi = lat1 - lat2;
    double dLambda = lon1 - lon2;
    double phi_m = (lat1 + lat2) / 2;
    double D = R * Math.sqrt(Math.pow(dPhi, 2) + Math.pow(Math.cos(phi_m) * dLambda, 2));
    return D;
  }

  public static class Map extends Mapper<LongWritable, Text, NullWritable, Double> {
    public void map(LongWritable key, Text value, Context context) throws IOException {
      String line = value.toString();
      String[] trip = line.split("\\s+");
      if (trip.length == 7) {
        // Only need the positions but only valid trips count
        try {
          double lat1 = Double.parseDouble(trip[2]); // <start pos (lat)>
          double lon1 = Double.parseDouble(trip[3]); // <end pos (lat)>
          double lat2 = Double.parseDouble(trip[5]); // <start pos (long)>
          double lon2 = Double.parseDouble(trip[6]); // <end pos (long)>

          context.write(NullWritable.get(), sphereDistance(lat1, lon1, lat2, lon2));
        } catch (Exception e) {
          // Ignore exception on the assumption that the data in the file was
          // invalid on this line (bad practice)
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "TripLength--toonn");
    job.setJarByClass(TripLength.class);
    job.setOutputKeyClass(NullWritable.class);
    job.setOutputValueClass(DoubleWritable.class);
    job.setMapperClass(Map.class);
    job.setNumReduceTasks(0);
    FileInputFormat.setMaxInputSplitSize(job, 1000000);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
