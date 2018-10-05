class Plant2 implements Runnable{
	private static final int NUM_THREADS = 5;
	public static final long PROCESSING_TIME = 5 * 1024;
	public static final int ORANGES_PER_BOTTLE = 4;
	public static int counter = 0;
	public int orangeCounter;
	public static boolean badOrange = false;
	public boolean badOrange2 = false;
	public static int numBadOranges = 0;
	public int numOrangesWasted;
	public static long timerStart = System.currentTimeMillis();
	private final String name;
	private final Thread t;
	public static boolean inUse = false;
	private final Mutex m;
	private final Mutex mBad;
    public Plant2(String name, Mutex m, Mutex mBad) {
		this.name = name;
		this.m = m;
		this.mBad = mBad;
		orangeCounter =0;
	
			numOrangesWasted = 0;
		t = new Thread(this);
		t.start();
	}
	public Thread getThread(){
		return t;
	}
	public void processThisChild(Orange o) {
		while (o.getState() != Orange.State.Processed && (System.currentTimeMillis() - timerStart) < PROCESSING_TIME) {
			o.runProcess();
		}
		if (o.getState() != Orange.State.Processed){
			badOrange=true;
			badOrange2 = true;
			
			try {
				mBad.acquire();
				numBadOranges++;
			}finally {
				mBad.release();
			}
			
		
			this.numOrangesWasted++;
		}
	}
	public static void main(String[] args) { 
        Mutex m = new Mutex();
        Mutex mBad = new Mutex();
		Plant2[] plant = new Plant2[NUM_THREADS];
		String thisString = "Thread";
		for (int i = 0; i < NUM_THREADS; i++){
			plant[i]= new Plant2(thisString + i, m, mBad); 
		}
		for (int j = 0; j < NUM_THREADS; j++) {
			try {
				System.out.println(j + " is going to kill itself");
				//System.out.println(plant[j].getThread());
				plant[j].getThread().join();
				System.out.println(j + " killed itself");
			} catch (InterruptedException ignored) {}
		}
		if (!badOrange) {
			System.out.printf("\n%d Oranges were processed\n%d Bottles were made\n%d Oranges were wasted\n", counter,
					counter / ORANGES_PER_BOTTLE, counter % ORANGES_PER_BOTTLE);
		} else {
			System.out.printf("\n%d Oranges were processed\n%d Bottles were made\n%d Oranges were wasted\n", counter,
					counter / ORANGES_PER_BOTTLE, counter % ORANGES_PER_BOTTLE +numBadOranges);
		}
		for (int i =0; i < NUM_THREADS; i++) {
			System.out.print(plant[i].toString());
		}
	}
	@Override
	public void run() {
		System.out.println("Starting thread " + name);
		while ((System.currentTimeMillis() - timerStart) < PROCESSING_TIME) {
			Orange o = new Orange();
			processThisChild(o);
			if (!badOrange2) {
				m.acquire();
				try{
					counter++;
				} finally {
					m.release();
				}
				

				this.orangeCounter++;

			}
			badOrange2 = false;
		}	
	}
	public String toString() {
		String str = String.format(name + "\n  Oranges Made: %d\n  Oranges Not Finished: %d\n", orangeCounter, numOrangesWasted);
		return str;
	}
}
