package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2DoubleConcurrentMap extends ConcurrentMap<Float, Double>, Float2DoubleMap
{
	@Override
	@Deprecated
	public default Double compute(Float key, BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) {
		return Float2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfAbsent(Float key, Function<? super Float, ? extends Double> mappingFunction) {
		return Float2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfPresent(Float key, BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) {
		return Float2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Double> action) {
		Float2DoubleMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Double merge(Float key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Float2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Float2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Float key, Double value) {
		return Float2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Double oldValue, Double newValue) {
		return Float2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(Float key, Double value) {
		return Float2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Double, ? extends Double> mappingFunction) {
		Float2DoubleMap.super.replaceAll(mappingFunction);
	}
}