package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2LongConcurrentMap extends ConcurrentMap<Integer, Long>, Int2LongMap
{
	@Override
	@Deprecated
	public default Long compute(Integer key, BiFunction<? super Integer, ? super Long, ? extends Long> mappingFunction) {
		return Int2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfAbsent(Integer key, Function<? super Integer, ? extends Long> mappingFunction) {
		return Int2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfPresent(Integer key, BiFunction<? super Integer, ? super Long, ? extends Long> mappingFunction) {
		return Int2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Long> action) {
		Int2LongMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Long merge(Integer key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Int2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Int2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Integer key, Long value) {
		return Int2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Long oldValue, Long newValue) {
		return Int2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(Integer key, Long value) {
		return Int2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Long, ? extends Long> mappingFunction) {
		Int2LongMap.super.replaceAll(mappingFunction);
	}
}