import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class MapReduceTripLength {

  /**
   * Calculates the distance in kilometers, input in radians
   */
  private static double sphereDistance(double lat1, double lon1, double lat2, double lon2) {
    double R = 6371009; // kilometers
    double dPhi = lat1 - lat2;
    double dLambda = lon1 - lon2;
    double phi_m = (lat1 + lat2) / 2;
    double D = R * Math.sqrt(Math.pow(dPhi, 2) + Math.pow(Math.cos(phi_m) * dLambda, 2));
    return D;
  }

  public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, String, Double> {
    public void map(LongWritable key, Text value, OutputCollector<String, Double> output, Reporter reporter) throws IOException {
      String line = value.toString();
      String[] trip = line.split("\\s+");
      if (trip.length == 7) {
        // Only need the positions but only valid trips count
        try {
          double lat1 = Double.parseDouble(trip[1]);
          double lon1 = Double.parseDouble(trip[1]);
          double lat2 = Double.parseDouble(trip[1]);
          double lon2 = Double.parseDouble(trip[1]);

          output.collect("TheKey",sphereDistance(lat1, lon1, lat2, lon2));
        } catch (Exception e) {
          // Ignore exception on the assumption that the data in the file was
          // invalid on this line (bad practice)
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    JobConf conf = new JobConf(MapReduceTripLength.class);
    conf.setJobName("MapReduceTripLength--toonn");
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(IntWritable.class);
    conf.setMapperClass(Map.class);
    conf.setNumReduceTasks(0);
    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);
    FileInputFormat.setInputPaths(conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(conf, new Path(args[1]));
    JobClient.runJob(conf);
  }
}
