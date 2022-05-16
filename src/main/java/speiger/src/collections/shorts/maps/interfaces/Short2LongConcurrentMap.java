package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2LongConcurrentMap extends ConcurrentMap<Short, Long>, Short2LongMap
{
	@Override
	@Deprecated
	public default Long compute(Short key, BiFunction<? super Short, ? super Long, ? extends Long> mappingFunction) {
		return Short2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfAbsent(Short key, Function<? super Short, ? extends Long> mappingFunction) {
		return Short2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfPresent(Short key, BiFunction<? super Short, ? super Long, ? extends Long> mappingFunction) {
		return Short2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Long> action) {
		Short2LongMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Long merge(Short key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Short2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Short2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Short key, Long value) {
		return Short2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Long oldValue, Long newValue) {
		return Short2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(Short key, Long value) {
		return Short2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Long, ? extends Long> mappingFunction) {
		Short2LongMap.super.replaceAll(mappingFunction);
	}
}