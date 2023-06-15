package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface Double2ObjectConcurrentMap<V> extends ConcurrentMap<Double, V>, Double2ObjectMap<V>
{
	@Override
	@Deprecated
	public default V compute(Double key, BiFunction<? super Double, ? super V, ? extends V> mappingFunction) {
		return Double2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfAbsent(Double key, Function<? super Double, ? extends V> mappingFunction) {
		return Double2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfPresent(Double key, BiFunction<? super Double, ? super V, ? extends V> mappingFunction) {
		return Double2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super V> action) {
		Double2ObjectMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default V merge(Double key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Double2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default V getOrDefault(Object key, V defaultValue) {
		return Double2ObjectMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Double key, V value) {
		return Double2ObjectMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2ObjectMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, V oldValue, V newValue) {
		return Double2ObjectMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Double key, V value) {
		return Double2ObjectMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super V, ? extends V> mappingFunction) {
		Double2ObjectMap.super.replaceAll(mappingFunction);
	}
}