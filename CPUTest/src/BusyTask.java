
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BusyTask {
	
	public static void main(String[] args) throws InterruptedException{
		while(true){
			ExecutorService exec = Executors.newFixedThreadPool(100);
			for(int i=0;i<1000;i++)
				exec.execute(new TaskRunnable());
		}
	}
	
}

class TaskRunnable implements Runnable{

	@Override
	public void run() {	
		while(true){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				
			}
			for(int i=0;i<1000000;i++)
				System.out.println(Thread.currentThread().getName()+ ": busy task running...");
		}
	}
	
}
