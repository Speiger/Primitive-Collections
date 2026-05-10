package speiger.src.collections.longs.collections;

public interface LongOrderedCollection extends LongCollection {
	
	LongOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(long e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(long e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public long getFirstLong();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public long removeFirstLong();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public long getLastLong();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public long removeLastLong();
	
}