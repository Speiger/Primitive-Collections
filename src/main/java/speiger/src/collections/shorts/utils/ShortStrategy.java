package speiger.src.collections.shorts.utils;

/**
 * A Type Specific Strategy class that allows to give control hashcode generation and equals comparason for maps
 */
public interface ShortStrategy
{
	/**
	 * Type Specific HashCode function
	 * @param o the element that the hashcode is requested for (if object may be null)
	 * @return hashcode for the given entry
	 */
	public int hashCode(short o);
	
	/**
	 * Type Specific Equals function
	 * @param key the first element that should be compared with
	 * @param value the second element that should be compared with (if object may be null)
	 * @return if the elements match
	 */
	public boolean equals(short key, short value);
	
}