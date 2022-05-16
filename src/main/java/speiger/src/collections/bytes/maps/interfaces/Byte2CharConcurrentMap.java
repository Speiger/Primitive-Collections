package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2CharConcurrentMap extends ConcurrentMap<Byte, Character>, Byte2CharMap
{
	@Override
	@Deprecated
	public default Character compute(Byte key, BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) {
		return Byte2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfAbsent(Byte key, Function<? super Byte, ? extends Character> mappingFunction) {
		return Byte2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfPresent(Byte key, BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) {
		return Byte2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Character> action) {
		Byte2CharMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Character merge(Byte key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Byte2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Byte2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Byte key, Character value) {
		return Byte2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Character oldValue, Character newValue) {
		return Byte2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(Byte key, Character value) {
		return Byte2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Character, ? extends Character> mappingFunction) {
		Byte2CharMap.super.replaceAll(mappingFunction);
	}
}