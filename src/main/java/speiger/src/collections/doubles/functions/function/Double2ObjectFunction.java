package speiger.src.collections.doubles.functions.function;


/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 * @param <V> the type of elements maintained by this Collection
 */
@FunctionalInterface
public interface Double2ObjectFunction<V> extends java.util.function.DoubleFunction<V>
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public V get(double k);
	

	
	@Override
	public default V apply(double k) { return get(k); }
}