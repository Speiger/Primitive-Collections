package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface Int2ObjectConcurrentMap<V> extends ConcurrentMap<Integer, V>, Int2ObjectMap<V>
{
	@Override
	@Deprecated
	public default V compute(Integer key, BiFunction<? super Integer, ? super V, ? extends V> mappingFunction) {
		return Int2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfAbsent(Integer key, Function<? super Integer, ? extends V> mappingFunction) {
		return Int2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfPresent(Integer key, BiFunction<? super Integer, ? super V, ? extends V> mappingFunction) {
		return Int2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super V> action) {
		Int2ObjectMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default V merge(Integer key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Int2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default V getOrDefault(Object key, V defaultValue) {
		return Int2ObjectMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Integer key, V value) {
		return Int2ObjectMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2ObjectMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, V oldValue, V newValue) {
		return Int2ObjectMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Integer key, V value) {
		return Int2ObjectMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> mappingFunction) {
		Int2ObjectMap.super.replaceAll(mappingFunction);
	}
}