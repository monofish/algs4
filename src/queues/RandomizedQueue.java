import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.*;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private int head, tail, length;
	private Item[] randque;

	// construct an empty randomized queue
	public RandomizedQueue(int capacity) {
		head = 0;
		tail = 0;
		length = capacity;
		randque = (Item[]) new Object[capacity];
	}

	// is the queue empty?
	public boolean isEmpty() { return head == tail; }

	// return the number of items on the queue
	public int size() { return (tail - head) % length; }

	// add the item
	public void enqueue(Item item) {
		if (item == null) throw new java.lang.NullPointerException();
		randque[tail] = item;
		tail = (tail + 1) % length;
		if (head == tail) resize(2 * length);
	}
	
	// remove and return a random item
	public Item dequeue() {
		int randIdx = randomIndex();
		Item item = randque[randIdx];
		randque[randIdx] = randque[head];
		randque[head] = null;
		head = (head + 1) % length;
		int size = size();
		if (size > 0 && size <= length / 4) resize(length / 2);
		return item;
	}
	
	// return (but do not remove) a random item
	public Item sample() { return randque[randomIndex()]; }
	
	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {return new RandomizedArrayIterator();}         

	private class RandomizedArrayIterator implements Iterator<Item> {
		private int i = 0;

		public RandomizedArrayIterator() {
			if (head <= tail) StdRandom.shuffle(randque, head, tail - 1);
			else {
				StdRandom.shuffle(randque, 0, tail - 1);
				StdRandom.shuffle(randque, head, length - 1);
			}

		}
		
		@Override
		public boolean hasNext() { return i < size(); }

		@Override
		public Item next() {
			if (i >= size()) throw new java.util.NoSuchElementException();
			else {
				return randque[(head + (i++)) % length];
			}
		}

		@Override
		public void remove() { throw new java.lang.UnsupportedOperationException("Method remove() unsupported"); }
	}
	
	private int randomIndex() { return (StdRandom.uniform(this.size()) + head) % length; }
	
	private void resize(int capacity) {
		length = capacity;
		Item[] copy = (Item[]) new Object[capacity];
		int count = 0;
		for(Item item : randque)
			copy[count++] = item;
		randque = copy;
		head = 0;
		tail = count;
	}
	
	public static void main(String[] args) {

		//Testing Client
		RandomizedQueue<String> queue = new RandomizedQueue<String>(10);
		queue.enqueue("1");
		queue.enqueue("2");
		queue.enqueue("3");
		queue.enqueue("4");
		queue.enqueue("5");
		queue.enqueue("7");
		queue.enqueue("6");
		queue.enqueue("8");
		queue.enqueue("9");
		queue.enqueue("a");
		queue.enqueue("e");
		queue.enqueue("c");
		queue.enqueue("f");
		queue.enqueue("g");
		queue.enqueue("b");
		queue.enqueue("h");
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();

		
		StdOut.println(queue.size());

		for(String str : queue)
			StdOut.print(str);

	}

}
