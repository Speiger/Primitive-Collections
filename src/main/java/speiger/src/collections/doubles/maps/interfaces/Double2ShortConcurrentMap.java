package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2ShortConcurrentMap extends ConcurrentMap<Double, Short>, Double2ShortMap
{
	@Override
	@Deprecated
	public default Short compute(Double key, BiFunction<? super Double, ? super Short, ? extends Short> mappingFunction) {
		return Double2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfAbsent(Double key, Function<? super Double, ? extends Short> mappingFunction) {
		return Double2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfPresent(Double key, BiFunction<? super Double, ? super Short, ? extends Short> mappingFunction) {
		return Double2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Short> action) {
		Double2ShortMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Short merge(Double key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Double2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Double2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Double key, Short value) {
		return Double2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Short oldValue, Short newValue) {
		return Double2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(Double key, Short value) {
		return Double2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Short, ? extends Short> mappingFunction) {
		Double2ShortMap.super.replaceAll(mappingFunction);
	}
}