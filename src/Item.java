public class Item {

	private String id;
	private double responseTime = 0;
	private double departureTime = 0;
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

	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

	public void setDepartureTime(double departureTime) {
		this.departureTime = departureTime;
	}

	public double getDepartureTime() {
		return departureTime;
	}
}
