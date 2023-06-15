package speiger.src.collections.objects.queues;

import java.util.Collection;
import java.util.Iterator;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectPriorityQueues;

/**
 * A Type Speciifc PriorityDeque or Dequeue interface to allow implementations like FIFO queues.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectPriorityDequeue<T> extends ObjectPriorityQueue<T>
{
	/**
	 * Method to insert a element into the first Index instead of the last.
	 * @param e the element that should be inserted into the first place
	 */
	public void enqueueFirst(T e);
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 */
	public default void enqueueAllFirst(T... e) {
		enqueueAllFirst(e, 0, e.length);
	}
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 * @param length the amount of elements that should be inserted
	 */
	public default void enqueueAllFirst(T[] e, int length) {
		enqueueAllFirst(e, 0, length);
	}
	
	/**
	 * Method to mass insert a elements into the first Index of the PriorityDequeue.
	 * @param e the elements that should be inserted
	 * @param offset the offset where in the array should be started
	 * @param length the amount of elements that should be inserted
	 */	
	public default void enqueueAllFirst(T[] e, int offset, int length) {
		for(int i = 0;i<length;i++)
			enqueueFirst(e[i+offset]);
	}
	
	/**
	 * Method to mass insert elements into first Index of the PriorityDequeue.
	 * @param c the elements that should be inserted from the Collection
	 */
	public default void enqueueAllFirst(ObjectCollection<T> c) {
		for(ObjectIterator<T> iter = c.iterator();iter.hasNext();)
			enqueueFirst(iter.next());
	}
	
	/**
	 * Method to mass insert elements into first Index of the PriorityDequeue.
	 * This method exists to add support for Java Collections to make it more useable
	 * @param c the elements that should be inserted from the Collection
	 */
	public default void enqueueAllFirst(Collection<? extends T> c) {
		for(Iterator<? extends T> iter = c.iterator();iter.hasNext();)
			enqueueFirst(iter.next());
	}
	
	/**
	 * A Method to remove a element from the last place instead of the first
	 * @return the last element inserted
	 * @throws java.util.NoSuchElementException if no element is in the deque
	 */
	public T dequeueLast();
	/**
	 * Peeking function for the last element
	 * @return the Last Element within the dequeue without deleting it
	 */
	public default T last() { return peek(size()-1); }
	
	/**
	 * Creates a Wrapped PriorityDequeue that is Synchronized
	 * @return a new PriorityDequeue that is synchronized
	 * @see ObjectPriorityQueues#synchronize
	 */
	public default ObjectPriorityDequeue<T> synchronizeQueue() { return ObjectPriorityQueues.synchronize(this); }
	
	/**
	 * Creates a Wrapped PriorityDequeue that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new PriorityDequeue Wrapper that is synchronized
	 * @see ObjectPriorityQueues#synchronize
	 */
	public default ObjectPriorityDequeue<T> synchronizeQueue(Object mutex) { return ObjectPriorityQueues.synchronize(this, mutex); }
	
	@Override
	public ObjectPriorityDequeue<T> copy();
}