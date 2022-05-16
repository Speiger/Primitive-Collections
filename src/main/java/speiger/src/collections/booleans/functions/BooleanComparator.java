package speiger.src.collections.booleans.functions;

import java.util.Comparator;
import java.util.Objects;

/**
 * Type-Specific Class for Comparator to reduce (un)boxing
 */
public interface BooleanComparator extends Comparator<Boolean> 
{
	/**
	 * Type-Specific compare function to reduce (un)boxing
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
	 * @see Comparator#compare(Object, Object)
	 */
	int compare(boolean o1, boolean o2);
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	default int compare(Boolean o1, Boolean o2) {
		return compare(o1.booleanValue(), o2.booleanValue());
	}
	
	/**
	 * A Wrapper function to convert a Non-Type-Specific Comparator to a Type-Specific-Comparator
	 * @param c comparator to convert
	 * @return the wrapper of the comparator
	 * @throws NullPointerException if the comparator is null
	 */
	public static BooleanComparator of(Comparator<Boolean> c) {
		Objects.requireNonNull(c);
		return (K, V) -> c.compare(Boolean.valueOf(K), Boolean.valueOf(V));
	}
	
	@Override
	public default BooleanComparator reversed() {
		return new Reversed(this);
	}
	
	/**
	 * A Type Specific Reversed Comparator to reduce boxing/unboxing
	 */
	static class Reversed implements BooleanComparator
	{
		BooleanComparator original;
		
		/**
		 * default constructor
		 * @param original that is going to be reversed
		 */
		public Reversed(BooleanComparator original) {
			this.original = original;
		}
		
		public int compare(boolean o1, boolean o2) {
			return original.compare(o2, o1);
		}
		
		@Override
		public BooleanComparator reversed() {
			return original;
		}
	}
}