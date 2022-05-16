package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2LongConcurrentMap extends ConcurrentMap<Character, Long>, Char2LongMap
{
	@Override
	@Deprecated
	public default Long compute(Character key, BiFunction<? super Character, ? super Long, ? extends Long> mappingFunction) {
		return Char2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfAbsent(Character key, Function<? super Character, ? extends Long> mappingFunction) {
		return Char2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfPresent(Character key, BiFunction<? super Character, ? super Long, ? extends Long> mappingFunction) {
		return Char2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Long> action) {
		Char2LongMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Long merge(Character key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Char2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Char2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Character key, Long value) {
		return Char2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Long oldValue, Long newValue) {
		return Char2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(Character key, Long value) {
		return Char2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Long, ? extends Long> mappingFunction) {
		Char2LongMap.super.replaceAll(mappingFunction);
	}
}