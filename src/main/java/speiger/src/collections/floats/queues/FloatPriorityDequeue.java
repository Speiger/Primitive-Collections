package speiger.src.collections.floats.queues;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.utils.FloatPriorityQueues;

/**
 * A Type Speciifc PriorityDeque or Dequeue interface to allow implementations like FIFO queues.
 */
public interface FloatPriorityDequeue extends FloatPriorityQueue
{
	/**
	 * Method to insert a element into the first Index instead of the last.
	 * @param e the element that should be inserted into the first place
	 */
	public void enqueueFirst(float e);
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 */
	public default void enqueueAllFirst(float... e) {
		enqueueAllFirst(e, 0, e.length);
	}
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 * @param length the amount of elements that should be inserted
	 */
	public default void enqueueAllFirst(float[] e, int length) {
		enqueueAllFirst(e, 0, length);
	}
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 * @param offset the offset where in the array should be started
	 * @param length the amount of elements that should be inserted
	 */	
	public default void enqueueAllFirst(float[] e, int offset, int length) {
		for(int i = 0;i<length;i++)
			enqueueFirst(e[i+offset]);
	}
	
	/**
	 * Method to mass insert elements into first Index of the PriorityDequeue.
	 * @param c the elements that should be inserted from the Collection
	 */
	public default void enqueueAllFirst(FloatCollection c) {
		for(FloatIterator iter = c.iterator();iter.hasNext();)
			enqueueFirst(iter.nextFloat());
	}
	
	/**
	 * A Method to remove a element from the last place instead of the first
	 * @return the last element inserted
	 * @throws java.util.NoSuchElementException if no element is in the deque
	 */
	public float dequeueLast();
	/**
	 * Peeking function for the last element
	 * @return the Last Element within the dequeue without deleting it
	 */
	public default float last() { return peek(size()-1); }
	
	/**
	 * Creates a Wrapped PriorityDequeue that is Synchronized
	 * @return a new PriorityDequeue that is synchronized
	 * @see FloatPriorityQueues#synchronize
	 */
	public default FloatPriorityDequeue synchronizeQueue() { return FloatPriorityQueues.synchronize(this); }
	
	/**
	 * Creates a Wrapped PriorityDequeue that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new PriorityDequeue Wrapper that is synchronized
	 * @see FloatPriorityQueues#synchronize
	 */
	public default FloatPriorityDequeue synchronizeQueue(Object mutex) { return FloatPriorityQueues.synchronize(this, mutex); }
	
	@Override
	public FloatPriorityDequeue copy();
}