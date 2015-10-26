import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.*;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private int head, tail, length;
	private Item[] randque;

	// construct an empty randomized queue
	public RandomizedQueue() {
		int capacity = 5;
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
		if (item == null) throw new java.lang.NullPointerException("Null item unacceptable");
		
		// If tail meets head, resize the queue first, then increment tail.
		if (head == (tail + 1) % length) resize(2 * length);
		randque[tail] = item;
		tail = (tail + 1) % length;
	}
	
	// remove and return a random item
	public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
		int randIdx = randomIndex();
		Item item = randque[randIdx];
		randque[randIdx] = randque[head];
		randque[head] = null; // Avoid loitering
		head = (head + 1) % length;
		int size = size();
		if (size > 0 && size <= length / 4) resize(length / 2);
		return item;
	}
	
	// return (but do not remove) a random item
	public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return randque[randomIndex()];
	}
	
	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {return new RandomizedArrayIterator();}         

	private class RandomizedArrayIterator implements Iterator<Item> {
		private int i = 0;

		public RandomizedArrayIterator() {
			if (head > tail) {
				StdRandom.shuffle(randque, 0, tail - 1);
				StdRandom.shuffle(randque, head, length - 1);
			}
			else if (head < tail - 1) StdRandom.shuffle(randque, head, tail - 1); // There are at least 2 items in the queue.
			else return;
		}
		
		@Override
		public boolean hasNext() { return i < size(); }

		@Override
		public Item next() {
			if (i >= size()) throw new java.util.NoSuchElementException("Next item not exists");
			else {
				return randque[(head + (i++)) % length];
			}
		}

		@Override
		public void remove() { throw new java.lang.UnsupportedOperationException("Method remove() unsupported"); }
	}
	
	private int randomIndex() { return (StdRandom.uniform(this.size()) + head) % length; }
	
	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity];
		int i;
		for(i = 0; i < size(); i ++)
			copy[i] = randque[(head + i) % length];
		randque = copy;
		length = capacity;
		head = 0;
		tail = i;
	}
	
	public static void main(String[] args) {

		//Testing Client
	
	}

}
