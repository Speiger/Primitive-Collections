package speiger.src.collections.chars.collections;

public interface CharOrderedCollection extends CharCollection {
	
	CharOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(char e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(char e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public char getFirstChar();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public char removeFirstChar();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public char getLastChar();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public char removeLastChar();
	
}