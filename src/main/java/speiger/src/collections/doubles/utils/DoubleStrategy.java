package speiger.src.collections.doubles.utils;

import java.util.Objects;

/**
 * A Type Specific Strategy class that allows to give control hashcode generation and equals comparason for maps
 */
public interface DoubleStrategy
{
	/**
	 * Normal Strategy
	 */
	public static final DoubleStrategy NORMAL = new NormalStrategy();
	
	/**
	 * @return a Normal Strategy that is behaving exactly like the normal Hash Strategy in the Hash Collections
	 */
	public static DoubleStrategy normalStrategy() { return (DoubleStrategy)NORMAL; }
	
	/**
	 * Type Specific HashCode function
	 * @param o the element that the hashcode is requested for (if object may be null)
	 * @return hashcode for the given entry
	 */
	public int hashCode(double o);
	
	/**
	 * Type Specific Equals function
	 * @param key the first element that should be compared with
	 * @param value the second element that should be compared with (if object may be null)
	 * @return if the elements match
	 */
	public boolean equals(double key, double value);
	
	/**
	 * A Strategy that simulates the normal Hash Collection Behavior if you want to use Hash Control Collections to replace normal ones.
	 * Only real reason to do that is you have to use this and want to get rid of implementations.
	 */
	public static class NormalStrategy implements DoubleStrategy
	{
		@Override
		public int hashCode(double o) { return Double.hashCode(o); }
		@Override
		public boolean equals(double key, double value) { return Objects.equals(value, Double.valueOf(key)); }
	}
}