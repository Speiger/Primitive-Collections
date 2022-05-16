package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2CharConcurrentMap extends ConcurrentMap<Integer, Character>, Int2CharMap
{
	@Override
	@Deprecated
	public default Character compute(Integer key, BiFunction<? super Integer, ? super Character, ? extends Character> mappingFunction) {
		return Int2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfAbsent(Integer key, Function<? super Integer, ? extends Character> mappingFunction) {
		return Int2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfPresent(Integer key, BiFunction<? super Integer, ? super Character, ? extends Character> mappingFunction) {
		return Int2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Character> action) {
		Int2CharMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Character merge(Integer key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Int2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Int2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Integer key, Character value) {
		return Int2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Character oldValue, Character newValue) {
		return Int2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(Integer key, Character value) {
		return Int2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Character, ? extends Character> mappingFunction) {
		Int2CharMap.super.replaceAll(mappingFunction);
	}
}