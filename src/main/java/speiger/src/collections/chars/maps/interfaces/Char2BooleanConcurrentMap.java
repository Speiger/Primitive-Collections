package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2BooleanConcurrentMap extends ConcurrentMap<Character, Boolean>, Char2BooleanMap
{
	@Override
	@Deprecated
	public default Boolean compute(Character key, BiFunction<? super Character, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Char2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Character key, Function<? super Character, ? extends Boolean> mappingFunction) {
		return Char2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfPresent(Character key, BiFunction<? super Character, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Char2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Boolean> action) {
		Char2BooleanMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Boolean merge(Character key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Char2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Char2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Character key, Boolean value) {
		return Char2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Boolean oldValue, Boolean newValue) {
		return Char2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Character key, Boolean value) {
		return Char2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Boolean, ? extends Boolean> mappingFunction) {
		Char2BooleanMap.super.replaceAll(mappingFunction);
	}
}