public class StatisticsHelper {
	// TODO(Mohsen) Write function desc
	//
	// u: is uniform random variable from 0 to 1
	//
	public static double exponentialRandomDeviate(int lamda, int u) {
		return (-1 / lamda * Math.log(u));
	}
}
