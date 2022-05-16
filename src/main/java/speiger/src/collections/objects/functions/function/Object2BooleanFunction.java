package speiger.src.collections.objects.functions.function;

import java.util.Objects;

/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 * @param <T> the type of elements maintained by this Collection
 */
@FunctionalInterface
public interface Object2BooleanFunction<T> extends java.util.function.Predicate<T>
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public boolean getBoolean(T k);
	
	@Override
	public default boolean test(T k) { return getBoolean(k); }
	
	/**
	 * A Type specific and-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a and comparason
	 */
	public default Object2BooleanFunction<T> andType(Object2BooleanFunction<T> other) {
		Objects.requireNonNull(other);
		return T -> getBoolean(T) && other.getBoolean(T);
	}
	
	@Override
	@Deprecated
	public default Object2BooleanFunction<T> and(java.util.function.Predicate<? super T> other) {
		Objects.requireNonNull(other);
		return T -> getBoolean(T) && other.test(T);
	}
	
	@Override
	public default Object2BooleanFunction<T> negate() {
		return T -> !getBoolean(T);
	}
	
	/**
	 * A Type specific or-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a or comparason
	 */
	public default Object2BooleanFunction<T> orType(Object2BooleanFunction<T> other) {
		Objects.requireNonNull(other);
		return T -> getBoolean(T) || other.getBoolean(T);
	}
	
	@Override
	@Deprecated
	public default Object2BooleanFunction<T> or(java.util.function.Predicate<? super T> other) {
		Objects.requireNonNull(other);
		return T -> getBoolean(T) || other.test(T);
	}
}