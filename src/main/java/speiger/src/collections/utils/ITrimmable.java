package speiger.src.collections.utils;

/**
 * Interface that allows to test for if a collection is trimmable.
 * This also allows that synchronization-wrappers are trimmable without extracting the original collection.
 */
public interface ITrimmable
{
	/**
	 * Trims the original collection down to the size of the current elements
	 */
	public default void trim() {
		trim(0);
	}
	
	/**
	 * Trims the original collection down to the size of the current elements or the requested size depending which is bigger
	 * @param size the requested trim size.
	 */
	public void trim(int size);
}