class Plant {
	public static final long PROCESSING_TIME = 5 * 1077;
	public final int ORANGES_PER_BOTTLE = 4;
	public static int counter = 0;
	public static boolean badOrange = false;
	public static long timerStart = System.currentTimeMillis();
	public Plant() {
		while ((System.currentTimeMillis() - timerStart) < PROCESSING_TIME) {
			Orange o = new Orange();
			processThisChild(o);
			System.out.print(".");
			if (!badOrange) {
				counter++;
			} 
		}
	}
	public void processThisChild(Orange o) {
		while (o.getState() != Orange.State.Processed && (System.currentTimeMillis() - timerStart) < PROCESSING_TIME) {
			
			o.runProcess();

		}
		if (o.getState() != Orange.State.Processed){
			badOrange=true;
		}
	}
	public static void main(String[] args) {  
		Plant plant = new Plant();
		if (!badOrange) {
			System.out.printf("\n%d Oranges were processed\n%d Bottles were made\n%d Oranges were wasted", counter,
					counter / plant.ORANGES_PER_BOTTLE, counter % plant.ORANGES_PER_BOTTLE);
		} else {
			System.out.printf("\n%d Oranges were processed\n%d Bottles were made\n%d Oranges were wasted", counter,
					counter / plant.ORANGES_PER_BOTTLE, counter % plant.ORANGES_PER_BOTTLE +1);
		}
	}
}
