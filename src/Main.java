import java.lang.*;
public class Main {
	
	public static void main(String[] args){
		
		System.out.println("Started");
		
		EventBarrier barrier = new EventBarrier();
		
		Consumer c1 = new Consumer(barrier,0);
		c1.start();
		
		Consumer c2 = new Consumer(barrier,1);
		c2.start();
		System.out.println("l");
		barrier.raise();
		
	}

}
