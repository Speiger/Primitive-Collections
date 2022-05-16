package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2BooleanConcurrentMap extends ConcurrentMap<Long, Boolean>, Long2BooleanMap
{
	@Override
	@Deprecated
	public default Boolean compute(Long key, BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Long2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Long key, Function<? super Long, ? extends Boolean> mappingFunction) {
		return Long2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfPresent(Long key, BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Long2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Boolean> action) {
		Long2BooleanMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Boolean merge(Long key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Long2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Long2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Long key, Boolean value) {
		return Long2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Boolean oldValue, Boolean newValue) {
		return Long2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Long key, Boolean value) {
		return Long2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Boolean, ? extends Boolean> mappingFunction) {
		Long2BooleanMap.super.replaceAll(mappingFunction);
	}
}