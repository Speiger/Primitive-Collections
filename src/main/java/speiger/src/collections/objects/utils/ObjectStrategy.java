package speiger.src.collections.objects.utils;

import java.util.Objects;

/**
 * A Type Specific Strategy class that allows to give control hashcode generation and equals comparason for maps
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectStrategy<T>
{
	/**
	 * Identity Strategy
	 */
	public static final ObjectStrategy<?> IDENTITY = new IdentityStrategy<>();
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a IdentityStrategy that allows to emulate a IdentityHashMap
	 */
	public static <T> ObjectStrategy<T> identityStrategy() { return (ObjectStrategy<T>)IDENTITY; }
	
	/**
	 * Normal Strategy
	 */
	public static final ObjectStrategy<?> NORMAL = new NormalStrategy<>();
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Normal Strategy that is behaving exactly like the normal Hash Strategy in the Hash Collections
	 */
	public static <T> ObjectStrategy<T> normalStrategy() { return (ObjectStrategy<T>)NORMAL; }
	
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
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class IdentityStrategy<T> implements ObjectStrategy<T>
	{
		@Override
		public int hashCode(T o) { return System.identityHashCode(o); }
		@Override
		public boolean equals(T key, T value) { return key == value; }
	}
	
	/**
	 * A Strategy that simulates the normal Hash Collection Behavior if you want to use Hash Control Collections to replace normal ones.
	 * Only real reason to do that is you have to use this and want to get rid of implementations.
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class NormalStrategy<T> implements ObjectStrategy<T>
	{
		@Override
		public int hashCode(T o) { return Objects.hashCode(o); }
		@Override
		public boolean equals(T key, T value) { return Objects.equals(value, key); }
	}
}