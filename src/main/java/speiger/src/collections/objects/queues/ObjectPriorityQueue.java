package speiger.src.collections.objects.queues;

import java.util.Comparator;
import java.util.Collection;
import java.util.Iterator;

import speiger.src.collections.ints.functions.function.Int2ObjectFunction;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectPriorityQueues;


/**
 * A Simple PriorityQueue (or Queue) interface that provides with the nessesary functions to interact with it, without cluttering with the Collection interface.
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectPriorityQueue<T> extends ObjectIterable<T>
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
	public void enqueue(T e);
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param e the elements that should be inserted
	 */
	public default void enqueueAll(T... e) {
		enqueueAll(e, 0, e.length);
	}
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param e the elements that should be inserted
	 * @param length the amount of elements that should be inserted
	 */
	public default void enqueueAll(T[] e, int length) {
		enqueueAll(e, 0, length);
	}
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param e the elements that should be inserted
	 * @param offset the offset where in the array should be started
	 * @param length the amount of elements that should be inserted
	 */
	public default void enqueueAll(T[] e, int offset, int length) {
		for(int i = 0;i<length;i++) 
			enqueue(e[i+offset]);
	}
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * @param c the elements that should be inserted from the Collection
	 */
	public default void enqueueAll(ObjectCollection<T> c) {
		for(ObjectIterator<T> iter = c.iterator();iter.hasNext();)
			enqueue(iter.next());
	}
	
	/**
	 * Method to mass insert elements into the PriorityQueue
	 * This method exists to add support for Java Collections to make it more useable
	 * @param c the elements that should be inserted from the Collection
	 */
	public default void enqueueAll(Collection<? extends T> c) {
		for(Iterator<? extends T> iter = c.iterator();iter.hasNext();)
			enqueue(iter.next());
	}
	
	
	/**
	 * Method to extract a element from the PriorityQueue
	 * @return a element from the Queue
	 * @throws java.util.NoSuchElementException if no element is present
	 */
	public T dequeue();
	
	/**
	 * Peeking function to see whats inside the queue.
	 * @param index of the element that is requested to be viewed.
	 * @return the element that is requested
	 */
	public T peek(int index);
	/**
	 * Shows the element that is to be returned next
	 * @return the first element in the Queue
	 */
	public default T first() { return peek(0); }
	
	/**
	 * Removes the first found element in the queue
	 * @param e the element that should be removed
	 * @return if a searched element was removed
	 */
	public boolean removeFirst(T e);
	/**
	 * Removes the last found element in the queue
	 * @param e the element that should be removed
	 * @return if a searched element was removed
	 */
	public boolean removeLast(T e);
	
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
	public ObjectPriorityQueue<T> copy();
	
	/**
	 * @return the sorter of the Queue, can be null
	 */
	public Comparator<? super T> comparator();
	
	/**
	 * @return draining iterator of the PriorityQueue
	 */
	public ObjectIterator<T> iterator();
	
	/**
	 * Creates a Wrapped PriorityQueue that is Synchronized
	 * @return a new PriorityQueue that is synchronized
	 * @see ObjectPriorityQueues#synchronize
	 */
	public default ObjectPriorityQueue<T> synchronizeQueue() { return ObjectPriorityQueues.synchronize(this); }
	
	/**
	 * Creates a Wrapped PriorityQueue that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new PriorityQueue Wrapper that is synchronized
	 * @see ObjectPriorityQueues#synchronize
	 */
	public default ObjectPriorityQueue<T> synchronizeQueue(Object mutex) { return ObjectPriorityQueues.synchronize(this, mutex); }
	
	/**
	 * A method to drop the contents of the Queue without clearing the queue
	 * @param <E> the type of elements maintained by this Collection
	 * @return the contents of the queue into a seperate array.
	 */
	public default <E> E[] toArray() { return toArray((E[])new Object[size()]); }
	/**
	 * A method to drop the contents of the Queue without clearing the queue
	 * @param input where the elements should be inserted to. If it does not fit then it creates a new appropiatly created array
	 * @param <E> the type of elements maintained by this Collection
	 * @return the contents of the queue into a seperate array.
	 * @note if the Type is generic then a Object Array is created instead of a Type Array
	 */
	public <E> E[] toArray(E[] input);
	/**
	 * A Helper function that simplifies the process of creating a new Array.
	 * @param action the array creation function
	 * @return an array containing all of the elements in this collection
	 * @see Collection#toArray(Object[])
	 */
	default T[] toArray(Int2ObjectFunction<T[]> action) {
		return toArray(action.get(size()));
	}
}