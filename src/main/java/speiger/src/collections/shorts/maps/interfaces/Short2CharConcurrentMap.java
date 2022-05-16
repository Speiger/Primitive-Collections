package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2CharConcurrentMap extends ConcurrentMap<Short, Character>, Short2CharMap
{
	@Override
	@Deprecated
	public default Character compute(Short key, BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) {
		return Short2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfAbsent(Short key, Function<? super Short, ? extends Character> mappingFunction) {
		return Short2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfPresent(Short key, BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) {
		return Short2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Character> action) {
		Short2CharMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Character merge(Short key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Short2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Short2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Short key, Character value) {
		return Short2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Character oldValue, Character newValue) {
		return Short2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(Short key, Character value) {
		return Short2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Character, ? extends Character> mappingFunction) {
		Short2CharMap.super.replaceAll(mappingFunction);
	}
}