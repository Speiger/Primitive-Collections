package speiger.src.collections.objects.functions.consumer;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * A Type Specific BiConsumer class to reduce boxing/unboxing and that fills the gaps that java has.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectBooleanConsumer<T> extends BiConsumer<T, Boolean>
{
	/**
	 * A Type Specific operation method to reduce boxing/unboxing
	 * Performs this operation on the given arguments.
	 *
	 * @param k the first input argument
	 * @param v the second input argument
	 */
	void accept(T k, boolean v);
	
	/**
	 * Type Specific sequencing method to reduce boxing/unboxing.
	 * @param after a operation that should be performed afterwards
	 * @return a sequenced biconsumer that does 2 operations
	 * @throws NullPointerException if after is null
	 */
	public default ObjectBooleanConsumer<T> andThen(ObjectBooleanConsumer<T> after) {
		Objects.requireNonNull(after);
		return (K, V) -> {accept(K, V); after.accept(K, V);};
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	default void accept(T k, Boolean v) { accept(k, v.booleanValue()); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
 	 * @deprecated Please use the corresponding type-specific function instead. 
 	 */
	@Override
	@Deprecated
	default ObjectBooleanConsumer<T> andThen(BiConsumer<? super T, ? super Boolean> after) {
		Objects.requireNonNull(after);
		return (K, V) -> {accept(K, V); after.accept(K, Boolean.valueOf(V));};
	}
}