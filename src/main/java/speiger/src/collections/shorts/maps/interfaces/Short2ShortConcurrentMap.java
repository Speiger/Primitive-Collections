package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2ShortConcurrentMap extends ConcurrentMap<Short, Short>, Short2ShortMap
{
	@Override
	@Deprecated
	public default Short compute(Short key, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Short2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfAbsent(Short key, Function<? super Short, ? extends Short> mappingFunction) {
		return Short2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfPresent(Short key, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Short2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Short> action) {
		Short2ShortMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Short merge(Short key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Short2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Short2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Short key, Short value) {
		return Short2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Short oldValue, Short newValue) {
		return Short2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(Short key, Short value) {
		return Short2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		Short2ShortMap.super.replaceAll(mappingFunction);
	}
}