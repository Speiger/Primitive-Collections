package speiger.src.collections.ints.collections;

public interface IntOrderedCollection extends IntCollection {
	
	IntOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(int e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(int e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public int getFirstInt();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public int removeFirstInt();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public int getLastInt();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public int removeLastInt();
	
}