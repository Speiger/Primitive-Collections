package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <V> the type of elements maintained by this Collection
 */
public interface Char2ObjectConcurrentMap<V> extends ConcurrentMap<Character, V>, Char2ObjectMap<V>
{
	@Override
	@Deprecated
	public default V compute(Character key, BiFunction<? super Character, ? super V, ? extends V> mappingFunction) {
		return Char2ObjectMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfAbsent(Character key, Function<? super Character, ? extends V> mappingFunction) {
		return Char2ObjectMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default V computeIfPresent(Character key, BiFunction<? super Character, ? super V, ? extends V> mappingFunction) {
		return Char2ObjectMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super V> action) {
		Char2ObjectMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default V merge(Character key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) {
		return Char2ObjectMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default V getOrDefault(Object key, V defaultValue) {
		return Char2ObjectMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default V putIfAbsent(Character key, V value) {
		return Char2ObjectMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2ObjectMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, V oldValue, V newValue) {
		return Char2ObjectMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default V replace(Character key, V value) {
		return Char2ObjectMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super V, ? extends V> mappingFunction) {
		Char2ObjectMap.super.replaceAll(mappingFunction);
	}
}