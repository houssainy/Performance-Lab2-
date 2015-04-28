public class Simulator {
	
	public static void main(String[] args) {
		long totalTime = 60000; // 1 min
		
		final Machine mc = new Machine("Machine Center");
		final Machine is = new Machine("Inspection Station");

		mc.setMachineListner(new Machine.MachineListner() {
			@Override
			public void onItemProcessed(Item item, double departerTime) {
				is.addToQueue(item);
			}
		});

		is.setMachineListner(new Machine.MachineListner() {
			@Override
			public void onItemProcessed(Item item, double departerTime) {
				// 10% is defeated and 90% are good
				
			}
		});
		
		long intervalCount = 0;
		while(intervalCount < totalTime) {
			// int a = (int) Math.pow(7, 5);
			// int m = (int) (Math.pow(2, 32) - 1);
			// LCGRandomNumberGenerator r = new LCGRandomNumberGenerator(1, a, m);
			//
			// for (int i = 1; i < 10000; i++) {
			// r.getNext();
			// }
			// System.out.println(r.getNext());
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			intervalCount++;
		}
		
		System.out.println("Simulation Done.");
	}
}
