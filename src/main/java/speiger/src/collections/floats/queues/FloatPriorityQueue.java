package speiger.src.collections.floats.queues;

import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.utils.FloatPriorityQueues;


/**
 * A Simple PriorityQueue (or Queue) interface that provides with the nessesary functions to interact with it, without cluttering with the Collection interface.
 */
public interface FloatPriorityQueue extends FloatIterable
{
	/**
	 * @return true if the PriorityQueue is empty
	 */
	public default boolean isEmpty() { return size() <= 0; }
	/**
	 * @return the amount of elements that are stored in the PriorityQueue
	 */
	public int size();
	/**
	 * clears all elements within the PriorityQueue,
	 * this does not resize the backing arrays
	 */
	public void clear();
	
	/**
	 * Method to insert a element into the PriorityQueue
	 * @param e the element that should be inserted
	 */
	public void enqueue(float e);
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param e the elements that should be inserted
	 */
	public default void enqueueAll(float... e) {
		enqueueAll(e, 0, e.length);
	}
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param e the elements that should be inserted
	 * @param length the amount of elements that should be inserted
	 */
	public default void enqueueAll(float[] e, int length) {
		enqueueAll(e, 0, length);
	}
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param e the elements that should be inserted
	 * @param offset the offset where in the array should be started
	 * @param length the amount of elements that should be inserted
	 */
	public default void enqueueAll(float[] e, int offset, int length) {
		for(int i = 0;i<length;i++) 
			enqueue(e[i+offset]);
	}
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param c the elements that should be inserted from the Collection
	 */
	public default void enqueueAll(FloatCollection c) {
		for(FloatIterator iter = c.iterator();iter.hasNext();)
			enqueue(iter.nextFloat());
	}
	
	
	/**
	 * Method to extract a element from the PriorityQueue
	 * @return a element from the Queue
	 * @throws java.util.NoSuchElementException if no element is present
	 */
	public float dequeue();
	
	/**
	 * Peeking function to see whats inside the queue.
	 * @param index of the element that is requested to be viewed.
	 * @return the element that is requested
	 */
	public float peek(int index);
	/**
	 * Shows the element that is to be returned next
	 * @return the first element in the Queue
	 */
	public default float first() { return peek(0); }
	
	/**
	 * Removes the first found element in the queue
	 * @param e the element that should be removed
	 * @return if a searched element was removed
	 */
	public boolean removeFirst(float e);
	/**
	 * Removes the last found element in the queue
	 * @param e the element that should be removed
	 * @return if a searched element was removed
	 */
	public boolean removeLast(float e);
	
	/**
	 * Allows to notify the Queue to be revalidate its data
	 */
	public void onChanged();
	
	/**
	 * A Function that does a shallow clone of the PriorityQueue itself.
	 * This function is more optimized then a copy constructor since the PriorityQueue does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the PriorityQueue
	 * @note Wrappers and view PriorityQueues will not support this feature
	 */
	public FloatPriorityQueue copy();
	
	/**
	 * @return the sorter of the Queue, can be null
	 */
	public FloatComparator comparator();
	
	
	/**
	 * Creates a Wrapped PriorityQueue that is Synchronized
	 * @return a new PriorityQueue that is synchronized
	 * @see FloatPriorityQueues#synchronize
	 */
	public default FloatPriorityQueue synchronizeQueue() { return FloatPriorityQueues.synchronize(this); }
	
	/**
	 * Creates a Wrapped PriorityQueue that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new PriorityQueue Wrapper that is synchronized
	 * @see FloatPriorityQueues#synchronize
	 */
	public default FloatPriorityQueue synchronizeQueue(Object mutex) { return FloatPriorityQueues.synchronize(this, mutex); }
	
	/**
	 * A method to drop the contents of the Queue without clearing the queue
	 * @return the contents of the queue into a seperate array.
	 */
	public default float[] toFloatArray() { return toFloatArray(new float[size()]); }
	/**
	 * A method to drop the contents of the Queue without clearing the queue
	 * @param input where the elements should be inserted to. If it does not fit then it creates a new appropiatly created array
	 * @return the contents of the queue into a seperate array.
	 * @note if the Type is generic then a Object Array is created instead of a Type Array
	 */
	public float[] toFloatArray(float[] input);
}