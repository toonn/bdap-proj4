import java.io.*;
import java.util.Scanner;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;


public class SimpleTripLength {
  private static final String tripFile = "/tmp/2010_03.trips";
  private static List<Double> distances = new ArrayList<Double>();

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

  public static void main(String[] args) throws IOException {
    BufferedReader br = null;
    
    try {
      br = new BufferedReader(new FileReader(tripFile));
      String line = null;
      while ((line = br.readLine()) != null) {
        String[] trip = line.split("\\s+");
        if (trip.length == 7) {
          // Only need the positions but only valid trips count
          try {
            double lat1 = Double.parseDouble(trip[1]);
            double lon1 = Double.parseDouble(trip[1]);
            double lat2 = Double.parseDouble(trip[1]);
            double lon2 = Double.parseDouble(trip[1]);

            distances.add(sphereDistance(lat1, lon1, lat2, lon2));
          } catch (Exception e) {
            // Ignore exception on the assumption that the data in the file was
            // invalid (bad practice)
          }
        }
      }
    } finally {
      if (br != null) {
        br.close();
      }
    }
  }
}
