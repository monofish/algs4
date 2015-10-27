import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {

    public static void main(String[] args) {

        RandomizedQueue<String> queue = new RandomizedQueue<String>();  
        int k = Integer.parseInt(args[0]);
        for (double count = 1; !StdIn.isEmpty(); count++) {
            String item = StdIn.readString();
            if (k < 0)
                throw new IllegalArgumentException("Positive integer parameters required!");
            else if (StdRandom.uniform() > 1 - k / count) { // If the item is chosen to add in to the queue;
                if (count > k) queue.dequeue(); // If the queue already has k items in it (full), then dequeue one of them;
                queue.enqueue(item);
            } // Otherwise the item is NOT chosen, then do nothing
        }
        for (String str : queue) StdOut.println(str);
   }
}