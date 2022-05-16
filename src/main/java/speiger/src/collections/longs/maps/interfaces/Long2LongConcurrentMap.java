package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2LongConcurrentMap extends ConcurrentMap<Long, Long>, Long2LongMap
{
	@Override
	@Deprecated
	public default Long compute(Long key, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Long2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfAbsent(Long key, Function<? super Long, ? extends Long> mappingFunction) {
		return Long2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfPresent(Long key, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Long2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Long> action) {
		Long2LongMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Long merge(Long key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Long2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Long2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Long key, Long value) {
		return Long2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Long oldValue, Long newValue) {
		return Long2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(Long key, Long value) {
		return Long2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		Long2LongMap.super.replaceAll(mappingFunction);
	}
}