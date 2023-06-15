package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2CharConcurrentMap extends ConcurrentMap<Long, Character>, Long2CharMap
{
	@Override
	@Deprecated
	public default Character compute(Long key, BiFunction<? super Long, ? super Character, ? extends Character> mappingFunction) {
		return Long2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfAbsent(Long key, Function<? super Long, ? extends Character> mappingFunction) {
		return Long2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Character computeIfPresent(Long key, BiFunction<? super Long, ? super Character, ? extends Character> mappingFunction) {
		return Long2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Character> action) {
		Long2CharMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Character merge(Long key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Long2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Long2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Character putIfAbsent(Long key, Character value) {
		return Long2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Character oldValue, Character newValue) {
		return Long2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(Long key, Character value) {
		return Long2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Character, ? extends Character> mappingFunction) {
		Long2CharMap.super.replaceAll(mappingFunction);
	}
}