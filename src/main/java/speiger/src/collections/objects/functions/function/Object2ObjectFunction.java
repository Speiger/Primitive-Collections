package speiger.src.collections.objects.functions.function;


/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 * @param <T> the type of elements maintained by this Collection
 * @param <V> the type of elements maintained by this Collection
 */
@FunctionalInterface
public interface Object2ObjectFunction<T, V> extends java.util.function.Function<T, V>
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public V getObject(T k);
	

	
	@Override
	public default V apply(T k) { return getObject(k); }
}