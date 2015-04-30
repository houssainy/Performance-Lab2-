public class StatisticsHelper {

	// u: is uniform random variable from 0 to 1
	//
	public static double exponentialRandomDeviate(double lamda, double u) {
		return(-1 / lamda * Math.log(u));
	}
}
