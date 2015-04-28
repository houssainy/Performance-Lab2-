import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class Machine {
	public enum State {
		BUSY, IDL
	}

	private Queue<Item> waitingQueue;

	private Item currentProcessingItem;
	private double startTimeOfProcessing = 0;
	private double expectedDepartureTime = -1;

	private String id;
	private State currentState;

	private LCGRandomNumberGenerator randomeGenerator;

	private MachineListner machineListner;

	private Object synchObject;

	public Machine(String id) {
		this.id = id;
		this.waitingQueue = new LinkedList<Item>();
		this.currentState = State.IDL;

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
		// TODO read (Jain section 25.4)
		return 0;
	}

	public void addToQueue(Item item) {
		synchronized (synchObject) {
			waitingQueue.add(item);
		}

		if (currentState == State.IDL) {
			startTimeOfProcessing = item.getArrivalTime();
			startProcessItems();
		}
	}

	private void startProcessItems() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!isEmptyQueue()) {
					currentState = State.BUSY;
					double processingTime = randomeGenerator
							.getNext(0.65, 0.70);

					expectedDepartureTime = startTimeOfProcessing
							+ processingTime;
					synchronized (synchObject) {
						currentProcessingItem = waitingQueue.remove();
					}
					
					try {
						Thread.sleep(Util.scaleMinToMiliSec(processingTime));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					processItem(currentProcessingItem);

					if (machineListner != null)
						machineListner.onItemProcessed(currentProcessingItem, expectedDepartureTime);

					// For new item, starting time will be the departure time of
					// the last item
					startTimeOfProcessing = expectedDepartureTime;
				}

				currentState = State.IDL;
			}
		}).start();
	}

	private boolean isEmptyQueue() {
		boolean isEmpty;
		synchronized (synchObject) {
			isEmpty = waitingQueue.isEmpty();
		}
		return isEmpty;
	}

	private void processItem(Item remove) {
		// TODO report log
	}

	public void report(PrintWriter pw) {
		if (pw != null) {
			// TODO
		}
	}

	public interface MachineListner {
		public abstract void onItemProcessed(Item item, double departerTime);
	}
}
