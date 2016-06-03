import java.io.*;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class Map extends Mapper<LongWritable, Text, Long, Segment> {
  Calendar cal = Calendar.getInstance();
  SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.EN_US);
  cal.setTime(sdf.parse(string);

  public void map(LongWritable key, Text value, Context context) throws IOException {
    String line = value.toString();
    String[] trip = line.split("\\s+");
    if (trip.length == 7) {
      // Only valid trips count
      try {
        long taxi = Long.parseLong(trip[0]); // <taxi-id>
        double lat1 = Double.parseDouble(trip[2]); // <start pos (lat)>
        double lon1 = Double.parseDouble(trip[3]); // <end pos (lat)>
        double lat2 = Double.parseDouble(trip[5]); // <start pos (long)>
        double lon2 = Double.parseDouble(trip[6]); // <end pos (long)>

        context.write(taxi, new Segment(lat1, lon1, lat2, lon2));
      } catch (Exception e) {
        // Ignore exception on the assumption that the data in the file was
        // invalid on this line (bad practice)
      }
    }
  }
}
