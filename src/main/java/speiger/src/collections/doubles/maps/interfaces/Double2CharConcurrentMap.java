package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2CharConcurrentMap extends ConcurrentMap<Double, Character>, Double2CharMap
{
	@Override
	@Deprecated
	public default Character compute(Double key, BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) {
		return Double2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfAbsent(Double key, Function<? super Double, ? extends Character> mappingFunction) {
		return Double2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfPresent(Double key, BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) {
		return Double2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Character> action) {
		Double2CharMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Character merge(Double key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Double2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Double2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Double key, Character value) {
		return Double2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Character oldValue, Character newValue) {
		return Double2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(Double key, Character value) {
		return Double2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Character, ? extends Character> mappingFunction) {
		Double2CharMap.super.replaceAll(mappingFunction);
	}
}