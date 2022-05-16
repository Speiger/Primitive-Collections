package speiger.src.collections.ints.functions.function;


/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 * @param <V> the type of elements maintained by this Collection
 */
@FunctionalInterface
public interface Int2ObjectFunction<V> extends java.util.function.IntFunction<V>
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public V get(int k);
	

	
	@Override
	public default V apply(int k) { return get(k); }
}