package speiger.src.collections.longs.functions.function;


/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 */
@FunctionalInterface
public interface Long2LongFunction extends java.util.function.LongUnaryOperator
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public long get(long k);
	

	
	@Override
	public default long applyAsLong(long k) { return get(k); }
}