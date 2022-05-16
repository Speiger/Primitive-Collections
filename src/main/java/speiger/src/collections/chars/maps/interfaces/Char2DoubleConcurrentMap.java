package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2DoubleConcurrentMap extends ConcurrentMap<Character, Double>, Char2DoubleMap
{
	@Override
	@Deprecated
	public default Double compute(Character key, BiFunction<? super Character, ? super Double, ? extends Double> mappingFunction) {
		return Char2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfAbsent(Character key, Function<? super Character, ? extends Double> mappingFunction) {
		return Char2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfPresent(Character key, BiFunction<? super Character, ? super Double, ? extends Double> mappingFunction) {
		return Char2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Double> action) {
		Char2DoubleMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Double merge(Character key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Char2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Char2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Character key, Double value) {
		return Char2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Double oldValue, Double newValue) {
		return Char2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(Character key, Double value) {
		return Char2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Double, ? extends Double> mappingFunction) {
		Char2DoubleMap.super.replaceAll(mappingFunction);
	}
}