package speiger.src.collections.objects.collections;

public interface ObjectOrderedCollection<T> extends ObjectCollection<T> {
	
	ObjectOrderedCollection<T> reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(T e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(T e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public T getFirst();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public T removeFirst();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public T getLast();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public T removeLast();
	
}