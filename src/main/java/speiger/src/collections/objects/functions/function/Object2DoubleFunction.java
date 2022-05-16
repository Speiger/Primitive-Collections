package speiger.src.collections.objects.functions.function;


/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 * @param <T> the type of elements maintained by this Collection
 */
@FunctionalInterface
public interface Object2DoubleFunction<T> extends java.util.function.ToDoubleFunction<T>
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public double getDouble(T k);
	

	
	@Override
	public default double applyAsDouble(T k) { return getDouble(k); }
}