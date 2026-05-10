package speiger.src.collections.bytes.collections;

public interface ByteOrderedCollection extends ByteCollection {
	
	ByteOrderedCollection reversed();
	
	/**
	 * A method to add an element to the start of a collection
	 * @param e that should be added at the start.
	 */
	public void addFirst(byte e);
	
	/**
	 * A method to add an element to the end of a collection
	 * @param e that should be added at the end.
	 */
	public void addLast(byte e);
	
	/**
	 * A method to get the first element in the collection
	 * @return first element in the collection
	 */
	public byte getFirstByte();
	/**
	 * A method to get and remove the first element in the collection
	 * @return first element in the collection
	 */
	public byte removeFirstByte();
	/**
	 * A method to get the last element in the collection
	 * @return last element in the collection
	 */
	public byte getLastByte();
	/**
	 * A method to get and remove the last element in the collection
	 * @return last element in the collection
	 */
	public byte removeLastByte();
	
}