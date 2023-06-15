package speiger.src.collections.longs.functions.function;

import java.util.Objects;

/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 */
@FunctionalInterface
public interface Long2BooleanFunction extends java.util.function.LongPredicate
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public boolean test(long k);
	
	
	/**
	 * A Type specific and-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a and comparason
	 */
	public default Long2BooleanFunction andType(Long2BooleanFunction other) {
		Objects.requireNonNull(other);
		return T -> test(T) && other.test(T);
	}
	
	@Override
	@Deprecated
	public default Long2BooleanFunction and(java.util.function.LongPredicate other) {
		Objects.requireNonNull(other);
		return T -> test(T) && other.test(T);
	}
	
	@Override
	public default Long2BooleanFunction negate() {
		return T -> !test(T);
	}
	
	/**
	 * A Type specific or-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a or comparason
	 */
	public default Long2BooleanFunction orType(Long2BooleanFunction other) {
		Objects.requireNonNull(other);
		return T -> test(T) || other.test(T);
	}
	
	@Override
	@Deprecated
	public default Long2BooleanFunction or(java.util.function.LongPredicate other) {
		Objects.requireNonNull(other);
		return T -> test(T) || other.test(T);
	}
}