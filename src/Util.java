
public class Util {
	// Convert minutes to milliseconds with scale 0.05 minutes to 1 milisceonds
	public static long scaleMinToMiliSec(double minute){
		return (long) (minute*1.0 / 0.05);
	}
}
