package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2DoubleConcurrentMap extends ConcurrentMap<Short, Double>, Short2DoubleMap
{
	@Override
	@Deprecated
	public default Double compute(Short key, BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) {
		return Short2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfAbsent(Short key, Function<? super Short, ? extends Double> mappingFunction) {
		return Short2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfPresent(Short key, BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) {
		return Short2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Double> action) {
		Short2DoubleMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Double merge(Short key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Short2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Short2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Short key, Double value) {
		return Short2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Double oldValue, Double newValue) {
		return Short2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(Short key, Double value) {
		return Short2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) {
		Short2DoubleMap.super.replaceAll(mappingFunction);
	}
}