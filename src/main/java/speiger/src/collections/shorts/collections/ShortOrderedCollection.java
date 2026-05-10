package speiger.src.collections.shorts.collections;

public interface ShortOrderedCollection extends ShortCollection {
	
	ShortOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(short e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(short e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public short getFirstShort();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public short removeFirstShort();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public short getLastShort();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public short removeLastShort();
	
}