package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2CharConcurrentMap extends ConcurrentMap<Character, Character>, Char2CharMap
{
	@Override
	@Deprecated
	public default Character compute(Character key, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Char2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfAbsent(Character key, Function<? super Character, ? extends Character> mappingFunction) {
		return Char2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfPresent(Character key, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Char2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Character> action) {
		Char2CharMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Character merge(Character key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Char2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Char2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Character key, Character value) {
		return Char2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Character oldValue, Character newValue) {
		return Char2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(Character key, Character value) {
		return Char2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		Char2CharMap.super.replaceAll(mappingFunction);
	}
}