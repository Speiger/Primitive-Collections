package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2ShortConcurrentMap extends ConcurrentMap<Character, Short>, Char2ShortMap
{
	@Override
	@Deprecated
	public default Short compute(Character key, BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) {
		return Char2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfAbsent(Character key, Function<? super Character, ? extends Short> mappingFunction) {
		return Char2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfPresent(Character key, BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) {
		return Char2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Short> action) {
		Char2ShortMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Short merge(Character key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Char2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Char2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Character key, Short value) {
		return Char2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Short oldValue, Short newValue) {
		return Char2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(Character key, Short value) {
		return Char2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Short, ? extends Short> mappingFunction) {
		Char2ShortMap.super.replaceAll(mappingFunction);
	}
}