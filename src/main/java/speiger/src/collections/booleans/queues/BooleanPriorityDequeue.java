package speiger.src.collections.booleans.queues;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.utils.BooleanPriorityQueues;

/**
 * A Type Speciifc PriorityDeque or Dequeue interface to allow implementations like FIFO queues.
 */
public interface BooleanPriorityDequeue extends BooleanPriorityQueue
{
	/**
	 * Method to insert a element into the first Index instead of the last.
	 * @param e the element that should be inserted into the first place
	 */
	public void enqueueFirst(boolean e);
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 */
	public default void enqueueAllFirst(boolean... e) {
		enqueueAllFirst(e, 0, e.length);
	}
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 * @param length the amount of elements that should be inserted
	 */
	public default void enqueueAllFirst(boolean[] e, int length) {
		enqueueAllFirst(e, 0, length);
	}
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 * @param offset the offset where in the array should be started
	 * @param length the amount of elements that should be inserted
	 */	
	public default void enqueueAllFirst(boolean[] e, int offset, int length) {
		for(int i = 0;i<length;i++)
			enqueueFirst(e[i+offset]);
	}
	
	/**
	 * Method to mass insert elements into first Index of the PriorityDequeue.
	 * @param c the elements that should be inserted from the Collection
	 */
	public default void enqueueAllFirst(BooleanCollection c) {
		for(BooleanIterator iter = c.iterator();iter.hasNext();)
			enqueueFirst(iter.nextBoolean());
	}
	
	/**
	 * A Method to remove a element from the last place instead of the first
	 * @return the last element inserted
	 * @throws java.util.NoSuchElementException if no element is in the deque
	 */
	public boolean dequeueLast();
	/**
	 * Peeking function for the last element
	 * @return the Last Element within the dequeue without deleting it
	 */
	public default boolean last() { return peek(size()-1); }
	
	/**
	 * Creates a Wrapped PriorityDequeue that is Synchronized
	 * @return a new PriorityDequeue that is synchronized
	 * @see BooleanPriorityQueues#synchronize
	 */
	public default BooleanPriorityDequeue synchronizeQueue() { return BooleanPriorityQueues.synchronize(this); }
	
	/**
	 * Creates a Wrapped PriorityDequeue that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new PriorityDequeue Wrapper that is synchronized
	 * @see BooleanPriorityQueues#synchronize
	 */
	public default BooleanPriorityDequeue synchronizeQueue(Object mutex) { return BooleanPriorityQueues.synchronize(this, mutex); }
	
	@Override
	public BooleanPriorityDequeue copy();
}