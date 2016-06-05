package distance;

public class Distance {
  /**
   * Calculates the distance in kilometers, input in degrees
   */
  public static double sphereDistance(double lat1, double lon1, double lat2, double lon2) {
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
}
