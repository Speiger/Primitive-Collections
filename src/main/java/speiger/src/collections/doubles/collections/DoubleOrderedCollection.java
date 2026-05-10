package speiger.src.collections.doubles.collections;

public interface DoubleOrderedCollection extends DoubleCollection {
	
	DoubleOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(double e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(double e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public double getFirstDouble();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public double removeFirstDouble();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public double getLastDouble();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public double removeLastDouble();
	
}