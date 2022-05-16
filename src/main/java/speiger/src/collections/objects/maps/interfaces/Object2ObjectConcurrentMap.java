package speiger.src.collections.objects.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <T> the type of elements maintained by this Collection
 * @param <V> the type of elements maintained by this Collection
 */
public interface Object2ObjectConcurrentMap<T, V> extends ConcurrentMap<T, V>, Object2ObjectMap<T, V>
{
	@Override
	public default V compute(T key, BiFunction<? super T, ? super V, ? extends V> mappingFunction) {
		return Object2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	public default V computeIfAbsent(T key, Function<? super T, ? extends V> mappingFunction) {
		return Object2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default V computeIfPresent(T key, BiFunction<? super T, ? super V, ? extends V> mappingFunction) {
		return Object2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super V> action) {
		Object2ObjectMap.super.forEach(action);
	}

	@Override
	public default V merge(T key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Object2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public V getOrDefault(Object key, V defaultValue);
	@Override
	public V putIfAbsent(T key, V value);
	@Override
	public boolean remove(Object key, Object value);
	@Override
	public boolean replace(T key, V oldValue, V newValue);
	@Override
	public V replace(T key, V value);
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super V, ? extends V> mappingFunction) {
		Object2ObjectMap.super.replaceAll(mappingFunction);
	}
}