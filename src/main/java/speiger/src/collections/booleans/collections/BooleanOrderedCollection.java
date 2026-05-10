package speiger.src.collections.booleans.collections;

public interface BooleanOrderedCollection extends BooleanCollection {
	
	BooleanOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(boolean e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(boolean e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public boolean getFirstBoolean();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public boolean removeFirstBoolean();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public boolean getLastBoolean();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public boolean removeLastBoolean();
	
}