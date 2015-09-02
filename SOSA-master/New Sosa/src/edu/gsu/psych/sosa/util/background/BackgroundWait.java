package edu.gsu.psych.sosa.util.background;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BackgroundWait {
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
//Creates a ExecutorSrvice thread
	private class Waiter implements Runnable{
		private long waitForMilli = 0L;
		
		public Waiter(long wait) {
			waitForMilli = wait;
			//Assigns a value to wait
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(waitForMilli);
			} catch (Exception e) {
				e.printStackTrace();
			}//Assigns a thread sleep time of waitForMilli
		}
	}
	
	public static Future<?> Wait(long milli) {
		BackgroundWait x = new BackgroundWait();
		return executor.submit(x.new Waiter(milli));
		//Creates a static Future<?> that returns a an Executor object with a Waiter of milli
	}

}
