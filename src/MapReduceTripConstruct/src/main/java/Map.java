import java.io.*;
import java.util.*;
import java.text.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class Map extends Mapper<LongWritable, Text, Long, Segment> {
  private static SimpleDateFormat sdf = new SimpleDateFormat("'yyyy-MM-dd HH:mm:ss'", Locale.US);

  public void map(LongWritable key, Text value, Context context) throws IOException {
    Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("America/San_Francisco"));
    Calendar endDate = Calendar.getInstance(TimeZone.getTimeZone("America/San_Francisco"));
    String line = value.toString();
    String[] trip = line.split(",");
    if (trip.length == 8) {
      // Only valid trips count
      try {
        long taxi = Long.parseLong(trip[0]); // <taxi-id>
        startDate.setTime(sdf.parse(trip[1])); // <start date>
        double lat1 = Double.parseDouble(trip[2]); // <start pos (lat)>
        double lon1 = Double.parseDouble(trip[3]); // <end pos (lat)>
        char startStatus = trip[4].charAt(1); // <start status>
        endDate.setTime(sdf.parse(trip[5])); // <end date>
        double lat2 = Double.parseDouble(trip[6]); // <start pos (long)>
        double lon2 = Double.parseDouble(trip[7]); // <end pos (long)>

        Segment s = new Segment(startDate, endDate, startStatus, lat1, lon1, lat2, lon2);
        // Drop segments with an avg speed above 200 km/h
        if (s.getAvgSpeed() <= 200) {
          context.write(taxi, s);
        }
      } catch (Exception e) {
        // Ignore exception on the assumption that the data in the file was
        // invalid on this line (bad practice)
      }
    }
  }
}
