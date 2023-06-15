package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2ShortConcurrentMap extends ConcurrentMap<Long, Short>, Long2ShortMap
{
	@Override
	@Deprecated
	public default Short compute(Long key, BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) {
		return Long2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfAbsent(Long key, Function<? super Long, ? extends Short> mappingFunction) {
		return Long2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfPresent(Long key, BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) {
		return Long2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Short> action) {
		Long2ShortMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Short merge(Long key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Long2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Long2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Long key, Short value) {
		return Long2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Short oldValue, Short newValue) {
		return Long2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(Long key, Short value) {
		return Long2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Short, ? extends Short> mappingFunction) {
		Long2ShortMap.super.replaceAll(mappingFunction);
	}
}