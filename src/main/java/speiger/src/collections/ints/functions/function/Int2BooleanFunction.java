package speiger.src.collections.ints.functions.function;

import java.util.Objects;

/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 */
@FunctionalInterface
public interface Int2BooleanFunction extends java.util.function.IntPredicate
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public boolean get(int k);
	
	@Override
	public default boolean test(int k) { return get(k); }
	
	/**
	 * A Type specific and-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a and comparason
	 */
	public default Int2BooleanFunction andType(Int2BooleanFunction other) {
		Objects.requireNonNull(other);
		return T -> get(T) && other.get(T);
	}
	
	@Override
	@Deprecated
	public default Int2BooleanFunction and(java.util.function.IntPredicate other) {
		Objects.requireNonNull(other);
		return T -> get(T) && other.test(T);
	}
	
	@Override
	public default Int2BooleanFunction negate() {
		return T -> !get(T);
	}
	
	/**
	 * A Type specific or-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a or comparason
	 */
	public default Int2BooleanFunction orType(Int2BooleanFunction other) {
		Objects.requireNonNull(other);
		return T -> get(T) || other.get(T);
	}
	
	@Override
	@Deprecated
	public default Int2BooleanFunction or(java.util.function.IntPredicate other) {
		Objects.requireNonNull(other);
		return T -> get(T) || other.test(T);
	}
}