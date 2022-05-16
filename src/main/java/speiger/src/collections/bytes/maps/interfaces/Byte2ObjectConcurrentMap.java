package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <V> the type of elements maintained by this Collection
 */
public interface Byte2ObjectConcurrentMap<V> extends ConcurrentMap<Byte, V>, Byte2ObjectMap<V>
{
	@Override
	@Deprecated
	public default V compute(Byte key, BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) {
		return Byte2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfAbsent(Byte key, Function<? super Byte, ? extends V> mappingFunction) {
		return Byte2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfPresent(Byte key, BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) {
		return Byte2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super V> action) {
		Byte2ObjectMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default V merge(Byte key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Byte2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default V getOrDefault(Object key, V defaultValue) {
		return Byte2ObjectMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Byte key, V value) {
		return Byte2ObjectMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2ObjectMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, V oldValue, V newValue) {
		return Byte2ObjectMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Byte key, V value) {
		return Byte2ObjectMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super V, ? extends V> mappingFunction) {
		Byte2ObjectMap.super.replaceAll(mappingFunction);
	}
}