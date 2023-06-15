package speiger.src.collections.booleans.utils;

import java.util.Objects;

/**
 * A Type Specific Strategy class that allows to give control hashcode generation and equals comparason for maps
 */
public interface BooleanStrategy
{
	/**
	 * Normal Strategy
	 */
	public static final BooleanStrategy NORMAL = new NormalStrategy();
	
	/**
	 * @return a Normal Strategy that is behaving exactly like the normal Hash Strategy in the Hash Collections
	 */
	public static BooleanStrategy normalStrategy() { return (BooleanStrategy)NORMAL; }
	
	/**
	 * Type Specific HashCode function
	 * @param o the element that the hashcode is requested for (if object may be null)
	 * @return hashcode for the given entry
	 */
	public int hashCode(boolean o);
	
	/**
	 * Type Specific Equals function
	 * @param key the first element that should be compared with
	 * @param value the second element that should be compared with (if object may be null)
	 * @return if the elements match
	 */
	public boolean equals(boolean key, boolean value);
	
	/**
	 * A Strategy that simulates the normal Hash Collection Behavior if you want to use Hash Control Collections to replace normal ones.
	 * Only real reason to do that is you have to use this and want to get rid of implementations.
	 */
	public static class NormalStrategy implements BooleanStrategy
	{
		@Override
		public int hashCode(boolean o) { return Boolean.hashCode(o); }
		@Override
		public boolean equals(boolean key, boolean value) { return Objects.equals(value, Boolean.valueOf(key)); }
	}
}