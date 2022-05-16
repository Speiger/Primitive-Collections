package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2ShortConcurrentMap extends ConcurrentMap<Float, Short>, Float2ShortMap
{
	@Override
	@Deprecated
	public default Short compute(Float key, BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) {
		return Float2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfAbsent(Float key, Function<? super Float, ? extends Short> mappingFunction) {
		return Float2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfPresent(Float key, BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) {
		return Float2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Short> action) {
		Float2ShortMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Short merge(Float key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Float2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Float2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Float key, Short value) {
		return Float2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Short oldValue, Short newValue) {
		return Float2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(Float key, Short value) {
		return Float2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Short, ? extends Short> mappingFunction) {
		Float2ShortMap.super.replaceAll(mappingFunction);
	}
}