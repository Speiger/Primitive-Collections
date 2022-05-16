package speiger.src.collections.objects.functions.function;

import java.util.function.BiFunction;

/**
 * A Type Specific Unary Operator to support Compute/Merge functions with type specific methods to reduce boxing/unboxing
 * @param <T> the type of elements maintained by this Collection
 * @param <V> the type of elements maintained by this Collection
 */
public interface ObjectObjectUnaryOperator<T, V> extends BiFunction<T, V, V>
{

}