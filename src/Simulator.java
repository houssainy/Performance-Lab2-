import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Simulator {

	private static ArrayList<Double> interArrivalTimes = new ArrayList<Double>();
	private static ArrayList<Item> processedItems = new ArrayList<Item>();

	private static Machine mc;
	private static Machine is;

	public static void main(String[] args) throws FileNotFoundException {
		int itemId = 0;

		double scaleInterval = 0.05;
		long totalTime = 6000;

		final Random rn = new Random();
		final int badFlag = 1;

		mc = new Machine("Machine Center", scaleInterval, 0.65, 0.70);
		is = new Machine("Inspection Station", scaleInterval, 0.75, 0.80);

		mc.setMachineListner(new Machine.MachineListner() {
			@Override
			public void onItemProcessed(Item item) {
				is.addToQueue(item);
				System.out.println("Item " + item.getId() + " left MC.");

				item.setResponseTime(item.getResponseTime()
						+ (item.getDepartureTime() - item.getArrivalTime()));
			}
		});

		is.setMachineListner(new Machine.MachineListner() {
			@Override
			public void onItemProcessed(Item item) {
				// 10% is defeated and 90% are good
				// random in java generate psudonumber with uniform
				// distribution, so we will generate numbers from 1 to 10 each
				// number have 10% probability to occur.
				int randomNumber = rn.nextInt(10) + 1;
				System.out.println("Item " + item.getId() + " left IS.");
				if (randomNumber == badFlag) {
					mc.addToQueue(item);
				} else {
					// TODO report item is good
				}
				item.setResponseTime(item.getResponseTime()
						+ (item.getDepartureTime() - item.getArrivalTime()));
			}
		});

		int a = (int) Math.pow(7, 5);
		int m = (int) (Math.pow(2, 32) - 1);
		LCGRandomNumberGenerator arrivalRandomeGenerator = new LCGRandomNumberGenerator(
				1, a, m);
		LCGRandomNumberGenerator breakingRandomeGenerator = new LCGRandomNumberGenerator(
				1, a, m);

		double nextArrivaleTimeInMins = StatisticsHelper
				.exponentialRandomDeviate(1,
						arrivalRandomeGenerator.getNext(0, 1));
		interArrivalTimes.add(nextArrivaleTimeInMins);

		// long nextArrivaleTimeInMiliSecs = Math.round(nextArrivaleTimeInMins
		// / scaleInterval);

		double nextBreakingTimeInMins = StatisticsHelper
				.exponentialRandomDeviate(1 / 360,
						breakingRandomeGenerator.getNext(0, 1));
		long nextBreakingTimeInMiliSecs = Math.round(nextBreakingTimeInMins
				/ scaleInterval);

		long currentTime = 0;
		double nextInterval = 0;

		while (currentTime < totalTime) {
			// if (currentTime >= nextArrivaleTimeInMiliSecs) {
			while (currentTime + 1 > Math.round((nextArrivaleTimeInMins + 0.05)
					/ scaleInterval)) {
				mc.addToQueue(new Item("" + (++itemId), nextArrivaleTimeInMins));

				nextInterval = StatisticsHelper.exponentialRandomDeviate(1,
						arrivalRandomeGenerator.getNext(0, 1));
				nextArrivaleTimeInMins += nextInterval;

				interArrivalTimes.add(nextArrivaleTimeInMins);
				// nextArrivaleTimeInMiliSecs =
				// Math.round(nextArrivaleTimeInMins
				// / scaleInterval);
			}
			// }

			if (currentTime == nextBreakingTimeInMiliSecs) {
				mc.breakIteam();

				nextBreakingTimeInMins += StatisticsHelper
						.exponentialRandomDeviate(1 / 360,
								breakingRandomeGenerator.getNext(0, 1));
				nextBreakingTimeInMiliSecs = Math.round(nextBreakingTimeInMins
						/ scaleInterval);
			}

			mc.update();
			is.update();

			currentTime++;
		}

		System.out.println("Simulation Done.");
		report();
	}

	private static void report() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("interArrivalTime.txt");

		for (Double double1 : interArrivalTimes) {
			pw.write(double1 + ",\n");
		}
		pw.close();

		System.out.println("Machine Center Service Time = "
				+ mc.getServiceTime());
		System.out.println("IS Service Time = " + is.getServiceTime());

		pw = new PrintWriter("MachineCenterServiceTime.txt");
		for (Item item : processedItems) {
			pw.write("Item " + item.getId() + " =");
			pw.write(item.getResponseTime() + "");
		}
		pw.close();

		pw = new PrintWriter("itemResponseTime.txt");
		for (Item item : processedItems) {
			pw.write("Item " + item.getId() + " =");
			pw.write(item.getResponseTime() + "");
		}
		pw.close();

		pw = new PrintWriter("MachineCenterMeanQueueLength.txt");
		for (Double double1 : mc.getMeanQueueLength()) {
			pw.write(double1 + ",\n");
		}
		pw.close();

		pw = new PrintWriter("ISMeanQueueLength.txt");
		for (Double double1 : is.getMeanQueueLength()) {
			pw.write(double1 + ",\n");
		}
		pw.close();
		
		pw = new PrintWriter("MachineCenterHourlyThrouput.txt");
		for (Double double1 : mc.getHourlyThrouput()) {
			pw.write(double1 + ",\n");
		}
		pw.close();

		pw = new PrintWriter("ISHourlyThrouput.txt");
		for (Double double1 : is.getHourlyThrouput()) {
			pw.write(double1 + ",\n");
		}
		pw.close();
	}
}
