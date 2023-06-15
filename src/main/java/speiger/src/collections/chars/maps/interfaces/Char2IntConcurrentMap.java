package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2IntConcurrentMap extends ConcurrentMap<Character, Integer>, Char2IntMap
{
	@Override
	@Deprecated
	public default Integer compute(Character key, BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) {
		return Char2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfAbsent(Character key, Function<? super Character, ? extends Integer> mappingFunction) {
		return Char2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfPresent(Character key, BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) {
		return Char2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Integer> action) {
		Char2IntMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Integer merge(Character key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Char2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Char2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Character key, Integer value) {
		return Char2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Integer oldValue, Integer newValue) {
		return Char2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(Character key, Integer value) {
		return Char2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Integer, ? extends Integer> mappingFunction) {
		Char2IntMap.super.replaceAll(mappingFunction);
	}
}