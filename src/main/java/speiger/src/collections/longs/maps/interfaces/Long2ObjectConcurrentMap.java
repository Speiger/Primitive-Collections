package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface Long2ObjectConcurrentMap<V> extends ConcurrentMap<Long, V>, Long2ObjectMap<V>
{
	@Override
	@Deprecated
	public default V compute(Long key, BiFunction<? super Long, ? super V, ? extends V> mappingFunction) {
		return Long2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfAbsent(Long key, Function<? super Long, ? extends V> mappingFunction) {
		return Long2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfPresent(Long key, BiFunction<? super Long, ? super V, ? extends V> mappingFunction) {
		return Long2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super V> action) {
		Long2ObjectMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default V merge(Long key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Long2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default V getOrDefault(Object key, V defaultValue) {
		return Long2ObjectMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Long key, V value) {
		return Long2ObjectMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2ObjectMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, V oldValue, V newValue) {
		return Long2ObjectMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Long key, V value) {
		return Long2ObjectMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super V, ? extends V> mappingFunction) {
		Long2ObjectMap.super.replaceAll(mappingFunction);
	}
}