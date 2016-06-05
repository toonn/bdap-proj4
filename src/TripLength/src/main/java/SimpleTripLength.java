import java.io.*;
import java.util.Scanner;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;


public class SimpleTripLength {
  private static final String tripFile = "2010_03.trips";
  private static final String distanceFile = "tripDistances.data";
  private static List<Double> distances = new ArrayList<Double>();

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

  /**
   * Read the trip distances from the input file
   */
  private static void readDistances(String tripFile) throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(tripFile));
      String line = null;
      while ((line = br.readLine()) != null) {
        String[] trip = line.split("\\s+");
        if (trip.length == 7) {
          // Only need the positions but only valid trips count
          try {
            double lat1 = Double.parseDouble(trip[2]); // <start pos (lat)>
            double lon1 = Double.parseDouble(trip[3]); // <end pos (lat)>
            double lat2 = Double.parseDouble(trip[5]); // <start pos (long)>
            double lon2 = Double.parseDouble(trip[6]); // <end pos (long)>

            distances.add(sphereDistance(lat1, lon1, lat2, lon2));
          } catch (Exception e) {
            // Ignore exception on the assumption that the data in the file was
            // invalid on this line (bad practice)
          }
        }
      }
    } finally {
      if (br != null) {
        br.close();
      }
    }
  }

  /**
   * Write list of distances to file
   */
  private static void writeDistances(String distanceFile) throws IOException {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new BufferedWriter(new FileWriter(distanceFile)));
      for (double dist : distances) {
        pw.println(dist);
      }
    } finally {
      if (pw != null) {
        pw.close();
      }
    }
  }

  /**
   * First argument is the file with the trip data, "/tmp/2010_03.trips" by
   * default. Second argument is the file to write the data to,
   * "tripDistances.data" by default.
   */
  public static void main(String[] args) throws IOException {
    readDistances((args.length > 0) ? args[0] : tripFile);
    writeDistances((args.length > 1) ? args[1] : distanceFile);
  }
}
