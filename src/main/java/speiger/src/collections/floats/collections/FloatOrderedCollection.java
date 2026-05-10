package speiger.src.collections.floats.collections;

public interface FloatOrderedCollection extends FloatCollection {
	
	FloatOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(float e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(float e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public float getFirstFloat();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public float removeFirstFloat();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public float getLastFloat();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public float removeLastFloat();
	
}