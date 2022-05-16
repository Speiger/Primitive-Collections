package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2FloatConcurrentMap extends ConcurrentMap<Character, Float>, Char2FloatMap
{
	@Override
	@Deprecated
	public default Float compute(Character key, BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) {
		return Char2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfAbsent(Character key, Function<? super Character, ? extends Float> mappingFunction) {
		return Char2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfPresent(Character key, BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) {
		return Char2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Float> action) {
		Char2FloatMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Float merge(Character key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Char2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Char2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Character key, Float value) {
		return Char2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Float oldValue, Float newValue) {
		return Char2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(Character key, Float value) {
		return Char2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Float, ? extends Float> mappingFunction) {
		Char2FloatMap.super.replaceAll(mappingFunction);
	}
}