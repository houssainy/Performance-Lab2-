public class Item {

	private String id;
	private double arrivalTime = -1;

	public Item(String id, double arrivalTime) {
		this.id = id;
		this.setArrivalTime(arrivalTime);
	}

	public String getId() {
		return id;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
}
