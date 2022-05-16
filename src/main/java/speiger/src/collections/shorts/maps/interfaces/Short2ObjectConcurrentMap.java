package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <V> the type of elements maintained by this Collection
 */
public interface Short2ObjectConcurrentMap<V> extends ConcurrentMap<Short, V>, Short2ObjectMap<V>
{
	@Override
	@Deprecated
	public default V compute(Short key, BiFunction<? super Short, ? super V, ? extends V> mappingFunction) {
		return Short2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfAbsent(Short key, Function<? super Short, ? extends V> mappingFunction) {
		return Short2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfPresent(Short key, BiFunction<? super Short, ? super V, ? extends V> mappingFunction) {
		return Short2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super V> action) {
		Short2ObjectMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default V merge(Short key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Short2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default V getOrDefault(Object key, V defaultValue) {
		return Short2ObjectMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Short key, V value) {
		return Short2ObjectMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2ObjectMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, V oldValue, V newValue) {
		return Short2ObjectMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Short key, V value) {
		return Short2ObjectMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super V, ? extends V> mappingFunction) {
		Short2ObjectMap.super.replaceAll(mappingFunction);
	}
}