/**
 * Return random number using Multiplicative generator, with avoiding overflows
 * using the following formula: random = ax mod m = g(x) + m * h(x) Where: g(x)
 * = a * (x mod q) - r * (x div q) h(x) = (x div q) - ((a * x) div m) And: q = m
 * div a r = m mod a
 * 
 *
 */
public class LCGRandomNumberGenerator {

	private int currentX = 1;
	private int a = (int) Math.pow(7, 5);
	private int m = (int) (Math.pow(2, 32) - 1);

	public LCGRandomNumberGenerator(int initialX, int a, int m) {
		this.currentX = 500;
		this.a = a;
		this.m = m;
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * 
	 * @return Normalized next uniform random number between min and max
	 */
	public double getNext(double min, double max) {
		int q = m / a;
		int r = m % a;

		if (r > q) {
			System.out.println("r is larger than q");
			return -1;
		}

		int xDivq = currentX / q;
		int xModq = (int) (currentX % q);

		int gx = (int) ((a * xModq) - (r * xDivq));

		// If gx is negative then hx equal to 1 otherwise 0
		if (gx < 0)
			currentX = gx + m;
		else
			currentX = gx;
		
		return normlize(currentX, min, max);
	}

	private double normlize(int currentX, double min, double max) {
		return (currentX - 0) * (max - min) / (m - 0) + min;		
	}
}
