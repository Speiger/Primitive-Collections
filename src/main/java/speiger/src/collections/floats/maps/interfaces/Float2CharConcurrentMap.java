package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2CharConcurrentMap extends ConcurrentMap<Float, Character>, Float2CharMap
{
	@Override
	@Deprecated
	public default Character compute(Float key, BiFunction<? super Float, ? super Character, ? extends Character> mappingFunction) {
		return Float2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfAbsent(Float key, Function<? super Float, ? extends Character> mappingFunction) {
		return Float2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfPresent(Float key, BiFunction<? super Float, ? super Character, ? extends Character> mappingFunction) {
		return Float2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Character> action) {
		Float2CharMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Character merge(Float key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Float2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Float2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Float key, Character value) {
		return Float2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Character oldValue, Character newValue) {
		return Float2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(Float key, Character value) {
		return Float2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Character, ? extends Character> mappingFunction) {
		Float2CharMap.super.replaceAll(mappingFunction);
	}
}