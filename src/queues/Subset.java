import edu.princeton.cs.algs4.*;

public class Subset {

	public static void main(String[] args) {

		RandomizedQueue<String> queue = new RandomizedQueue<String>();  
		int k = Integer.parseInt(args[0]); 
		
		while (!StdIn.isEmpty()) {  
			String item = StdIn.readString();  
			queue.enqueue(item);  
		}  
		while (k > 0){  
			StdOut.println(queue.dequeue());  
			k--;  
		}  
	}

}
