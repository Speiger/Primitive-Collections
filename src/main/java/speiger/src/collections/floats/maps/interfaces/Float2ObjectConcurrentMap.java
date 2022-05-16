package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <V> the type of elements maintained by this Collection
 */
public interface Float2ObjectConcurrentMap<V> extends ConcurrentMap<Float, V>, Float2ObjectMap<V>
{
	@Override
	@Deprecated
	public default V compute(Float key, BiFunction<? super Float, ? super V, ? extends V> mappingFunction) {
		return Float2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfAbsent(Float key, Function<? super Float, ? extends V> mappingFunction) {
		return Float2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfPresent(Float key, BiFunction<? super Float, ? super V, ? extends V> mappingFunction) {
		return Float2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super V> action) {
		Float2ObjectMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default V merge(Float key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Float2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default V getOrDefault(Object key, V defaultValue) {
		return Float2ObjectMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Float key, V value) {
		return Float2ObjectMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2ObjectMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, V oldValue, V newValue) {
		return Float2ObjectMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Float key, V value) {
		return Float2ObjectMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super V, ? extends V> mappingFunction) {
		Float2ObjectMap.super.replaceAll(mappingFunction);
	}
}