import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Machine {
	public enum State {
		BUSY, IDL
	}

	private Queue<Item> waitingQueue;

	private Item currentProcessingItem;
	private double startTimeOfProcessing = 0;
	private double expectedDepartureTime = Integer.MAX_VALUE;

	private String id;
	private State currentState;

	private LCGRandomNumberGenerator randomeGenerator;

	private MachineListner machineListner;

	private double currentTime = 0;
	private double timeInterval = 0;

	private double minProcessingTime;
	private double maxProcessingTime;

	private double serviceTime = 0;

	private long totalQueueLenghts = 0;
	private ArrayList<Double> MeanQueueLength = new ArrayList<Double>();
	private ArrayList<Double> hourlyThroput = new ArrayList<Double>();

	private long numberOfProcessedItems = 0;

	private PrintWriter pw;

	public Machine(String id, double timeInterval, double minProcessingTime,
			double maxProcessingTime) throws FileNotFoundException {
		this.id = id;
		this.waitingQueue = new LinkedList<Item>();
		this.currentState = State.IDL;
		this.timeInterval = timeInterval;
		this.minProcessingTime = minProcessingTime;
		this.maxProcessingTime = maxProcessingTime;

		this.pw = new PrintWriter(this.id + ".mkm");

		int a = (int) Math.pow(7, 5);
		int m = (int) (Math.pow(2, 32) - 1);
		this.randomeGenerator = new LCGRandomNumberGenerator(1, a, m);
	}

	public void setMachineListner(MachineListner machineListner) {
		this.machineListner = machineListner;
	}

	public State getCurrentState() {
		return currentState;
	}

	public int getQueueLength() {
		return waitingQueue.size();
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void addToQueue(Item item) {
		log("Item " + item.getId() + " added to Queue at min "
				+ item.getArrivalTime());
		waitingQueue.add(item);

		if (currentState == State.IDL) { // No one was waiting, so process the
											// new arrived item
			startTimeOfProcessing = item.getArrivalTime();

			currentState = State.BUSY;
			double processingTime = randomeGenerator.getNext(minProcessingTime,
					maxProcessingTime);
			expectedDepartureTime = startTimeOfProcessing + processingTime;

			currentProcessingItem = waitingQueue.remove();

			log(this.id + " state changed to " + currentState);
			log("Item " + currentProcessingItem.getId()
					+ " will be processed at min "
					+ currentProcessingItem.getArrivalTime()
					+ " and it's expected to departute at min "
					+ expectedDepartureTime);
		}
	}

	public void breakIteam() {
		double breakingTime = randomeGenerator.getNext(8, 12);
		expectedDepartureTime += breakingTime;

		log(this.id + " is breaked at min " + currentTime
				+ ", with breaking time " + breakingTime);
	}

	public void update() {
		currentTime += timeInterval;
		totalQueueLenghts += waitingQueue.size();

		MeanQueueLength.add(totalQueueLenghts * 1.0 / currentTime * 1.0);

		if ((Math.floor(currentTime*100)/100) % 60.00== 0.0) {
			hourlyThroput.add(numberOfProcessedItems * 1.0 / 60);
			numberOfProcessedItems = 0;
		}

		if (currentProcessingItem == null)
			return;

		if (currentState == State.BUSY)
			serviceTime += timeInterval;

		if (currentTime >= expectedDepartureTime) {
			processItem(currentProcessingItem);
			if (machineListner != null) {
				currentProcessingItem.setDepartureTime(expectedDepartureTime);
				machineListner.onItemProcessed(currentProcessingItem);
				numberOfProcessedItems++;
			}

			if (waitingQueue.isEmpty()) { // No one is waiting
				currentState = State.IDL;
				currentProcessingItem = null;

				log(this.id + " state changed to " + State.IDL);
			} else { // Process next Item
				startTimeOfProcessing = currentProcessingItem.getArrivalTime();

				currentState = State.BUSY;
				double processingTime = randomeGenerator.getNext(
						minProcessingTime, maxProcessingTime);
				expectedDepartureTime = startTimeOfProcessing + processingTime;

				currentProcessingItem = waitingQueue.remove();

				log(this.id + " state changed to " + State.BUSY);
				log("Item " + currentProcessingItem.getId()
						+ " added to Queue at min "
						+ currentProcessingItem.getArrivalTime()
						+ " and it's expected to departute at min "
						+ expectedDepartureTime);
			}
		} /*
		 * else if (!waitingQueue.isEmpty() && currentTime <
		 * expectedDepartureTime){ System.err
		 * .println("Erorr: Current time is greater than expected departure time!"
		 * ); }
		 */
	}

	private void processItem(Item item) {
		log("Item " + item.getId() + " is processed at " + currentTime);
	}

	public void log(String msg) {
		if (pw != null) {
			pw.write(msg + "\n");
			pw.flush();
		}
	}

	public ArrayList<Double> getMeanQueueLength() {
		return MeanQueueLength;
	}

	public ArrayList<Double> getHourlyThrouput() {
		return hourlyThroput;
	}

	public void close() {
		pw.close();
	}

	public interface MachineListner {
		public abstract void onItemProcessed(Item item);
	}
}
