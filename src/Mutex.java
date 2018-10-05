import java.util.concurrent.Semaphore;

public class Mutex {
	static Semaphore semaphore = new Semaphore(1);

	Mutex(){
	
	}
	
	public void acquire(){
		try {
			semaphore.acquire();
		} catch (InterruptedException ignore) { }
	}
	public void release() {
		semaphore.release();
	}
	
}