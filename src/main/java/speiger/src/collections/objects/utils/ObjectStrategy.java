package speiger.src.collections.objects.utils;

/**
 * A Type Specific Strategy class that allows to give control hashcode generation and equals comparason for maps
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectStrategy<T>
{
	/**
	 * Identity Strategy
	 */
	public static final ObjectStrategy<?> IDENTITY = new IdentityStrategy<>();
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return a IdentityStrategy that allows to emulate a IdentityHashMap
	 */
	public static <T> ObjectStrategy<T> identityStrategy() { return (ObjectStrategy<T>)IDENTITY; }
	
	/**
	 * Type Specific HashCode function
	 * @param o the element that the hashcode is requested for (if object may be null)
	 * @return hashcode for the given entry
	 */
	public int hashCode(T o);
	
	/**
	 * Type Specific Equals function
	 * @param key the first element that should be compared with
	 * @param value the second element that should be compared with (if object may be null)
	 * @return if the elements match
	 */
	public boolean equals(T key, T value);
	
	/**
	 * A Strategy that uses Identity HashCode instead of the normal hashing function.
	 * This simulates a IdentityHashMap without having the need to actually implement it.
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class IdentityStrategy<T> implements ObjectStrategy<T>
	{
		@Override
		public int hashCode(T o) { return System.identityHashCode(o); }
		@Override
		public boolean equals(T key, T value) { return key == value; }
	}
}